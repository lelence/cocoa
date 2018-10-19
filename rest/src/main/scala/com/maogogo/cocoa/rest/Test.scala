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

///*
// * Copyright 2018 Maogogo Workshop
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.maogogo.cocoa.rest
//
//import com.maogogo.cocoa.rest.socketio.{ AA, event }
//
//import scala.reflect.runtime.universe._
//
//object Test extends App {
//
//  case class haha(name: String)
//
//  val currentMirror = runtimeMirror(getClass.getClassLoader)
//
//  typeOf[AA].decls.map(_.asMethod).map { mth ⇒
//    filterAnnotation(mth.annotations)
//    // println("=" * 50)
//  }.filter(_.nonEmpty).map(_.head).map { anno ⇒
//
//    //
//
//    anno.tree.children.tail.map { anno ⇒
//      anno match {
//        // case s: Literal ⇒ println(s.value.value)
//        case a: Ident ⇒
//          println()
//
//          println(a.tpe)
//
//        // case Ident(name) ⇒ println(name.encodedName.toString)
//        case x ⇒
//
//          println("==>>" + x.getClass)
//      }
//    }
//
//    anno.tree.children.tail.map { abc ⇒
//      // println(x.getClass)
//
//      //      x match {
//      //        case  ⇒ println(s)
//      //        case _ ⇒ println("==>>")
//      //      }
//
//    }
//
//    //    val ee = anno.tree.children.tail.foldLeft(event(event = "", replyTo = "", broadcat = false, interval = -1)) { (e, a) ⇒
//    //      a match {
//    //        case s @ Ident(TermName("x$1")) ⇒ println(s)
//    //          e.copy(event = "kkk")
//    //        case Literal(Constant(replyTo: String)) ⇒ e.copy(replyTo = replyTo)
//    //        case Literal(Constant(broadcat: Boolean)) ⇒ e.copy(broadcat = broadcat)
//    //        case Literal(Constant(interval: Int)) ⇒ e.copy(interval = interval)
//    //      }
//    //    }
//
//    //  println(ee.event + "###" + ee.replyTo)
//
//  }
//
//  private def filterAnnotation: PartialFunction[Seq[Annotation], Seq[Annotation]] = {
//    case annos: Seq[Annotation] ⇒
//      annos.filter(anno ⇒ anno.tree.tpe =:= typeOf[event])
//  }
//
//  // val dd = Reflection.lookupEventMethods[AA]
//
//}