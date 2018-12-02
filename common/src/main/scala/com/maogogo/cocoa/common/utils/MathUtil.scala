package com.maogogo.cocoa.common.utils

import scala.util.Random

object MathUtil extends App {

  val k: Int = 6

  val size = 20

  val dd = Seq.range(0, k).flatMap { i ⇒
    Seq.fill(5)(i).zip(Seq.range(0, k))
  }

  val ee = dd.filter(x ⇒ x._1 + x._2 < k).foldLeft(Seq.empty[AA]) { (ss, ij) ⇒
    val (i, j) = ij
    ss :+ AA(i, j, (i + j), "+")
  }

  val ff = dd.filter(x ⇒ x._1 - x._2 >= 0).foldLeft(ee) { (ss, ij) ⇒
    val (i, j) = ij
    ss :+ AA(i, j, (i - j), "-")
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