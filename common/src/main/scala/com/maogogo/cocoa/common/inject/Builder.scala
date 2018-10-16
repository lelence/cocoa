/*
 * Copyright 2018 Maogogo Workshop
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.maogogo.cocoa.common.inject

import akka.actor.{ Actor, ActorRef, ActorSystem, Props }
import com.google.inject.{ Injector, Provider }
import net.codingwell.scalaguice.InjectorExtensions._
import net.codingwell.scalaguice.KeyExtensions._
import net.codingwell.scalaguice._

import scala.util.Random

trait Builder[I, O] {

  protected type BuilderType

  def named(anno: String): BuilderType

  def required: O

  def optional: Option[O]

}

trait InjectionBuilder[T] extends Builder[T, T] {
  protected type BuilderType = InjectionBuilder[T]
}

trait ActorInjectionBuilder[T <: Actor] extends Builder[T, ActorRef] {
  protected type BuilderType = ActorInjectionBuilder[T]
}

private[inject] object Internals {

  //  abstract class AbstractBuilder[I, O] extends Builder[I, O] {
  //    protected val annotationNamed: ThreadLocal[Option[String]] = ThreadLocal.withInitial(() ⇒ None)
  //
  //    override def named(anno: String): BuilderType = {
  //      annotationNamed.set(Some(anno))
  //      this.asInstanceOf[BuilderType]
  //    }
  //  }

  //  class InjectionBuilderImpl[T: Manifest](implicit ip: InjectorProvider)
  //    extends AbstractBuilder[T, T] with InjectionBuilder[T] {
  //
  //    implicit val injector = ip()
  //
  //    override def required: T = checkopt(optional)
  //
  //    override def optional: Option[T] = provider[T](annotationNamed.get()).map(_.get())
  //
  //  }
  //
  //  class ActorInjectionBuilderImpl[T <: Actor: Manifest](implicit system: ActorSystem)
  //    extends AbstractBuilder[T, ActorRef] with ActorInjectionBuilder[T] {
  //
  //    implicit lazy val injector = InjectExt(system).actorInjector
  //
  //    override def required: ActorRef = checkopt(optional)
  //
  //    override def optional: Option[ActorRef] = ??? //(provider[T] _ andThen toActorRef) (name)
  //
  //    //    protected def toActorRef(implicit system: ActorSystem): PartialFunction[Option[Provider[T]], Option[ActorRef]] = {
  //    //      case opt: Option[Provider[T]] ⇒ opt.map { p ⇒
  //    //        val named = namedOpt.get.getOrElse(s"actor_${Random.nextInt(Int.MaxValue)}")
  //    //        system.actorOf(Props(p.get()), name = named)
  //    //      }
  //    //    }
  //  }

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

  //  case class InjectionNotAvailable[T: Manifest](s: String) extends IllegalStateException {
  //    override def getMessage: String = {
  //      s"Injection of [${manifest[T].runtimeClass.getName}] is not available $s"
  //    }
  //  }

}

