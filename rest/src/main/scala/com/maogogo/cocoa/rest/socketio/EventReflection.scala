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

package com.maogogo.cocoa.rest.socketio

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration.Duration
import scala.reflect.runtime.universe._

object EventReflection {

  lazy val currentMirror = runtimeMirror(getClass.getClassLoader)

  def lookupEventMethods[T]: Seq[ProviderEventMethod] = {

    typeOf[AA].decls.map(_.asMethod).map { mth ⇒
      (mth, filterAnnotation(mth.annotations))
    }.filter(_._2.nonEmpty).map {
      case (mth, anno) ⇒

        val event = toEventAnnotation(anno.head)

        val paramType = mth.paramLists.flatMap {
          _.map { x ⇒
            x.typeSignature.typeSymbol.asClass
          }
        }.head

        val paramSymbol = currentMirror.staticClass(paramType.fullName)

        val paramClazz = currentMirror.runtimeClass(paramSymbol)

        val futureType = if (mth.returnType.typeConstructor =:= typeOf[scala.concurrent.Future[_]].typeConstructor) {
          mth.returnType.typeArgs.head
        } else mth.returnType

        ProviderEventMethod(event = event, method = mth, paramClazz = paramClazz, futureType = futureType)

    } toSeq
  }

  def getMethodMirror[T: Manifest](i: T, m: MethodSymbol): MethodMirror = {
    currentMirror.reflect(i).reflectMethod(m)
  }

  private def toEventAnnotation: PartialFunction[Annotation, event] = {
    case anno: Annotation ⇒
      // 这里是按照顺序获取的 顺序不对会报错
      val annoVals = anno.tree.children.tail.collect {
        case Literal(Constant(e)) ⇒ e
      }

      event(annoVals(0).toString, annoVals(1).asInstanceOf[Int],
        annoVals(2).asInstanceOf[Long], annoVals(3).asInstanceOf[String])

  }

  private def filterAnnotation: PartialFunction[Seq[Annotation], Seq[Annotation]] = {
    case annos: Seq[Annotation] ⇒
      annos.filter(anno ⇒ anno.tree.tpe =:= typeOf[event])
  }

  def lookupEventMethod[T](event: String): Option[ProviderEventMethod] = {
    lookupEventMethods.find(_.event.event == event)
  }

  def invokeMethod[T](instance: T, method: ProviderEventMethod, json: String): AnyRef = {
    import com.maogogo.cocoa.rest.socketio.SocketIOServerLocal.mapper

    val params = SocketIOServerLocal.mapper.readValue(json, method.paramClazz)
    val methodMirror = getMethodMirror(instance, method.method)

    methodMirror(params) match {
      case f: Future[_] ⇒
        val resp = Await.result(f, Duration.Inf)
        mapper.convertValue(resp, classOf[java.util.Map[String, Any]])
      case s: String ⇒ s
    }

  }

}

