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

package com.maogogo.cocoa.rpc.services

import org.scalamock.scalatest.MockFactory
import org.scalatest.{ AsyncWordSpec, Matchers, WordSpec }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class HelloServiceSpec extends WordSpec with Matchers with MockFactory {

  "test1" in {

    val hello = stub[HelloService]

    (hello.sayHi _).when(*).returns(Future.successful("hello"))

    val future = hello.sayHi("Ttutut")

    future.map { result ⇒

      println("result ===>>" + result)
      result should be("hello")

      // assert(result == "hello")
    }

  }

  "test2" in {

    val m = mockFunction[Int, String]

    m expects (*) onCall { arg: Int ⇒
      s"hello : ${arg}"
    }

    // m.when(10).returns("dudud")

    m(10) shouldBe ("hello : 10")

  }

}
