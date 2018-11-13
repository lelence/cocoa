package com.maogogo.cocoa.jsonrpc

import com.google.inject.Injector
import com.google.inject.name.Names

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

object JsonRpcSettings {

  def apply(): JsonRpcSettings = new JsonRpcSettings(Map.empty)
}

class JsonRpcSettings(ps: Map[String, MethodMirror]) {

  private[jsonrpc] lazy val m = runtimeMirror(getClass.getClassLoader)

  def register[T: ClassTag](instance: T): JsonRpcSettings = {

    val iMr = m.reflect(instance)

    val providers = iMr.symbol.typeSignature.decls
      .map(_.asMethod)
      .filter(filterMethod)
      .map {
        case symbol: MethodSymbol ⇒
          val mMr = iMr.reflectMethod(symbol)
          (symbol.name.encodedName.toString, mMr)
      }

    new JsonRpcSettings(providers.foldLeft(ps) { (_0, _tupl) ⇒
      _0 + (_tupl._1 → _tupl._2)
    })
  }

  def register[T: Manifest](
    implicit
    injector: Injector): JsonRpcSettings = {
    import net.codingwell.scalaguice.InjectorExtensions._
    register(injector.instance[T])
  }

  def register[T: Manifest](named: String)(
    implicit
    injector: Injector): JsonRpcSettings = {
    import net.codingwell.scalaguice.InjectorExtensions._
    register(injector.instance[T](Names.named(named)))
  }

  private[jsonrpc] def findProvider(method: String): Option[MethodMirror] = ps.get(method)

  private[jsonrpc] def filterMethod: PartialFunction[MethodSymbol, Boolean] = {
    case m: MethodSymbol ⇒ m.isPublic && !m.isConstructor
  }

}
