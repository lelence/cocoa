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

package com.maogogo.cocoa.common.utils

import scala.util.Random

object MathUtil extends App {

  val k: Int = 6

  val size = 20

  val dd = Seq.range(0, k).flatMap {i ⇒
    Seq.fill(5)(i).zip(Seq.range(0, k))
  }

  val ee = dd.filter(x ⇒ x._1 + x._2 < k).foldLeft(Seq.empty[AA]) {(ss, ij) ⇒
    val (i, j) = ij
    ss :+ AA(i, j, ( i + j ), "+")
  }

  val ff = dd.filter(x ⇒ x._1 - x._2 >= 0).foldLeft(ee) {(ss, ij) ⇒
    val (i, j) = ij
    ss :+ AA(i, j, ( i - j ), "-")
  }

  scala.util.Random.shuffle(ff).take(20).foreach {
    case aa: AA ⇒
      Random.nextInt(3) match {
        case 0 ⇒ println(s"__ ${aa.symbol} ${aa.j} = ${aa.r}")
        case 1 ⇒ println(s"${aa.i} ${aa.symbol} __ = ${aa.r}")
        case 2 ⇒ println(s"${aa.i} ${aa.symbol} ${aa.j} = __")
      }
  }

}

case class AA(i: Int, j: Int, r: Int, symbol: String)