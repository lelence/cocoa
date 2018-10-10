package com.maogogo.cocoa.common.inject

import akka.actor.{ Actor, ActorRef, ActorSystem, Props }
import com.google.inject.{ Injector, Provider }
import net.codingwell.scalaguice.InjectorExtensions._
import net.codingwell.scalaguice.KeyExtensions._
import net.codingwell.scalaguice._

import scala.util.Random

trait Builder[I, O] {

  protected type BuilderT

  def named(anno: String): BuilderT

  def required: O

  def optional: Option[O]

  protected def thisBuilder(): BuilderT

}

trait InjectionBuilder[T] extends Builder[T, T] {
  override protected type BuilderT = InjectionBuilder[T]
}

trait ActorInjectionBuilder[T <: Actor] extends Builder[T, ActorRef] {
  override protected def thisBuilder(): BuilderT = this.asInstanceOf[BuilderT]
}

private[inject] object Internals {

  abstract class AbstractInjectionBuilder[I, O] extends Builder[I, O] {
    protected val namedOpt: ThreadLocal[Option[String]] = ThreadLocal.withInitial(() ⇒ None)

    override protected def thisBuilder(): BuilderT = this.asInstanceOf[BuilderT]

    override def named(anno: String): BuilderT = {
      namedOpt.set(Some(anno))
      thisBuilder()
    }
  }

  class InjectionBuilderImpl[T: Manifest](implicit ip: InjectorProvider)
    extends AbstractInjectionBuilder[T, T] with InjectionBuilder[T] {

    implicit val injector = ip()

    override def required: T = checkopt(optional)

    override def optional: Option[T] = provider[T](namedOpt.get).map(_.get())

  }

  class ActorInjectionBuilderImpl[T <: Actor : Manifest](implicit system: ActorSystem)
    extends AbstractInjectionBuilder[T, ActorRef] with ActorInjectionBuilder[T] {

    implicit lazy val injector = InjectExt(system).actorInjector

    override def required: ActorRef = checkopt(optional)

    override def optional: Option[ActorRef] = (provider[T] _ andThen toActorRef) (namedOpt.get)

    protected def toActorRef(implicit system: ActorSystem): PartialFunction[Option[Provider[T]], Option[ActorRef]] = {
      case opt: Option[Provider[T]] ⇒ opt.map { p ⇒
        val named = namedOpt.get.getOrElse(s"actor_${Random.nextInt(Int.MaxValue)}")
        system.actorOf(Props(p.get()), name = named)
      }
    }

  }

  class ClusterInjectionBuilderImpl[T <: Actor : Manifest](implicit system: ActorSystem)
    extends ActorInjectionBuilderImpl[T] {

  }

  private def provider[T: Manifest](annotated: Option[String] = None)(
    implicit
    inj: Injector): Option[Provider[T]] = {

    val keyFn = annotated match {
      case Some(anno) ⇒ typeLiteral[T].annotatedWithName(anno)
      case _ ⇒ typeLiteral[T].toKey
    }

    inj.existingBinding(keyFn).map(_.getProvider)
  }

  private def checkopt[T]: PartialFunction[Option[T], T] = {
    case opt: Option[T] ⇒ opt match {
      case Some(t) ⇒ t
      case _ ⇒ throw new Exception("hahfehfhef")
    }
  }

  //  private def toT[T: Manifest]: PartialFunction[Try[T], T] = {
  //    case Success(t) ⇒ t
  //    case Failure(ex) ⇒ throw ex
  //  }

  case class InjectionNotAvailable[T: Manifest](s: String) extends IllegalStateException {
    override def getMessage: String = {
      s"Injection of [${manifest[T].runtimeClass.getName}] is not available $s"
    }
  }

}

