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
//import com.maogogo.cocoa.rest.socketio.Reflection.currentMirror
//import com.maogogo.cocoa.rest.socketio.{ AA, ReflectMethod, Reflection }
//
//import scala.collection.immutable
//import scala.concurrent.Future
//import scala.reflect.runtime.universe
//import scala.reflect.runtime.universe._
//
//object Test2 extends App {
//
//
//  //  def getTypeTag[T: TypeTag](obj: T) = typeTag[T]
//  //
//  //  def getInnerType[T](future: Future[T])(implicit tag: TypeTag[T]) = tag.tpe.toString
//  //
//  //
//  //  //  classOf[AA]
//  //
//  //  //  typeOf[AA].typeParams
//  //  //  val a = new AA
//  //  // val run = runtimeMirror(getClass.getClassLoader)
//  //  //  typeOf[AA].typeSymbol.asClass.asMethod.annotations.foreach(println)
//  //
//
//  val a = new AA
//  val dd: Seq[ReflectMethod] = Reflection.lookupEventMethod[AA] //.toSeq
//
//  val uu = currentMirror.reflect(a).reflectMethod(dd.head.method)("fefwfew")
//
//  println(uu)
//
//  //  println(dd)
//
//  // val qq: immutable.Seq[List[universe.Symbol]] = ee.typeSignature.paramLists
//
//  //  println(getInnerType(ee))
//
//  // println(ee.returnType)
//
//
//  //
//  //  run.reflect(a).reflectMethod(dd).apply(9, "")
//
//  //  class TestReflect(init: Int) {
//  //    val field = init
//  //
//  //    def showField = {
//  //      println("Call method 'showField'")
//  //      2333
//  //    }
//  //  }
//  //
//  //  val run = runtimeMirror(getClass.getClassLoader)
//  //
//  //  // 反射获取构造器MethodSymbol，构造器名称为 <init>
//  //  val constructor = typeOf[TestReflect].decl(TermName("<init>")).asMethod
//  //  val instance = run //通过构造器的MethodSymbol反射构建实例
//  //    .reflectClass(symbolOf[TestReflect].asClass)
//  //    .reflectConstructor(constructor).apply(2333)
//  //
//  //  // 遍历筛选特定成员
//  //  typeOf[TestReflect].decls foreach {
//  //    case term: TermSymbol if term.name == TermName("field") =>
//  //      println(s"Field name: ${term.name}, value: ${run.reflect(instance).reflectField(term).get}")
//  //    case method: MethodSymbol if method.name == TermName("showField") =>
//  //      println(s"Method name: ${method.name}, value: ${run.reflect(instance).reflectMethod(method).apply()}")
//  //    case _ =>
//  //  }
//
//  //  import scala.reflect.runtime.universe._
//  //
//  //  val cm = runtimeMirror(getClass.getClassLoader)
//  //  val moduleA = cm.classSymbol(classOf[AA]).typeSignature
//  //  val methodY = moduleA.decls.filter(_.name.encodedName.toString == "aa")
//
//
//  // val methodY = moduleA.decls.filter(_.name.encodedName.toString == "aa") // .head.asMethod
//  // val m = moduleA.reflectMethod(methodY)
//
//  //  println(methodY)
//
//  //  m(8, "")
//
//  //  import org.portablescala.reflect._
//
//  //  Reflect
//
//  //  import scala.reflect.runtime.universe._
//  //
//  //  val rm = runtimeMirror(getClass.getClassLoader)
//  //
//  //  val aa = rm.classSymbol(classOf[AA]).typeSignature
//  //
//  //  val m = aa.members.filter(_.name.encodedName.toString == "aa").head.asMethod
//  //
//  //  val dd = m.paramLists.foreach(_.foreach(x ⇒ println(x.getClass)))
//  //
//  //  println(dd)
//
//  //  classOf[AA].getAnnotation()
//
//  //  val rm = runtimeMirror(getClass.getClassLoader)
//  //
//  //  val dd = rm.classSymbol(classOf[AA]).typeSignature
//  //
//  //  dd.members.filter { x ⇒
//  //
//  //    // encodedName(x) == "a"
//  //
//  //    false
//  //
//  //  }
//
//  // val ee = dd.decl(TermName("aa")).asMethod
//  //  val dd = typeOf[AA]
//
//  //  rm.
//
//  //  println(ee)
//
//  //  val ru = scala.reflect.runtime.universe
//  //
//  //  // get runtime mirror
//  //
//  //
//  //  // define class and companion object
//  //  class Boo {
//  //    def hey(x: Int) = x * x
//  //  }
//  //
//  //  import scala.reflect.runtime.universe._
//  //
//  //  //  val rm = runtimeMirror(getClass.getClassLoader)
//  //  //
//  //  //  val value = rm.classSymbol(classOf[Boo]).typeSignature
//  //  //
//  //  //  val dd = value.decl(TermName("hey")).asMethod
//  //  //
//  //  //  val ee = rm.reflect(classOf[Boo])
//  //  //
//  //  //  val qq = ee.reflectMethod(dd)
//  //
//  //  case class Test(a: String, b: List[Int])
//  //
//  //  import scala.reflect.runtime.{ currentMirror => m }
//  //  import scala.reflect.runtime.{ universe => u }
//  //
//  //  val t = Test("abca", List(5))
//  //
//  //  val b = new Boo
//  //
//  //
//  //  val dd = ru.runtimeMirror(getClass.getClassLoader)
//  //  val classPerson = ru.typeOf[Boo].typeSymbol.asClass
//  //  val cm = dd.reflectClass(classPerson)
//  //
//  //  val ctor = typeOf[Boo].decl(TermName("")).asMethod // .decls(TermName("hey")).asMethod
//
//  // val ctorm = cm.reflectConstructor(ctor)
//
//  //  val lookAtMe = m.reflect(b)
//  //    val value = u.typeOf[Test]
//  //  val value = m.classSymbol(classOf[Test]).typeSignature
//  //  val members = value.members
//  //  val member = value.members.filter(_.name.encoded == "hey")
//  //
//  //  val aAccessor = lookAtMe.reflectMethod(member.head.asMethod)
//  //  val thisShouldBeA = aAccessor.apply()
//  //  println(thisShouldBeA)
//
//  // println(qq(3))
//
//  //  println(dd)
//
//  //  object Boo {
//  //    def hi(x: Int) = x * x
//  //  }
//
//  // get instance mirror for companion object Boo
//  //  val instanceMirror = rm.reflect(classOf[Boo])
//  //
//  //  // get method symbol for the "hi" method in companion object
//  //  val methodSymbolHi = ru.typeOf[Boo].decl(ru.TermName("hi")).asMethod
//  //
//  //  // get method mirror for "hi" method
//  //  val methodHi = instanceMirror.reflectMethod(methodSymbolHi)
//  //
//  //  // invoke the method "hi"
//  //  methodHi(4)
//
//}
