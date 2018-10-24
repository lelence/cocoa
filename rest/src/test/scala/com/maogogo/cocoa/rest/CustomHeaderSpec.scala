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

package com.maogogo.cocoa.rest

import akka.http.scaladsl.model.headers.{ ModeledCustomHeader, ModeledCustomHeaderCompanion, RawHeader }

import scala.util.Try

class CustomHeaderSpec extends RestSpec {

  "test1" in {
    //    val ApiTokenHeader(t1) = ApiTokenHeader("token")
    //    t1 should ===("token")
  }

  "test2" in {
    Get() ~> RawHeader("X-User-Id", "Joe42") ~> root ~> check {
      responseAs[String] shouldEqual "hello:Joe42"
    }
  }

  "test3" in {
    Get() ~> root ~> check {
      responseAs[String] shouldEqual "hello:haha"
    }
  }

  "test4" in {
    Get("/hh") ~> root ~> check {
      responseAs[String] shouldEqual "hello:dudu"
    }
  }

  "test5" in {
    Get("/hh") ~> RawHeader("apiKey", "Joe42") ~> root ~> check {
      responseAs[String] shouldEqual "hello:Joe42"
    }
  }

}
