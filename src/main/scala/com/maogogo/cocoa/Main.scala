package com.maogogo.cocoa

import com.google.inject.Guice
import com.maogogo.cocoa.data.CommonSettings

object Main extends App {

  new scopt.OptionParser[CommonSettings]("cocoa") {
    head("cocoa", "0.0.1")

    opt[Int]('p', "port").action((x, c) => {
      c.copy(port = x)
    }).text("server port")

  }.parse(args, CommonSettings()) match {
    case Some(config) ⇒
      val injector = Guice.createInjector(new ServicesModule)

      println("sss =>>" + config)
    // println("xx =>>" + config.port)

    case None ⇒
      println("xxx =>>11111")
  }

  //    val parser = new scopt.OptionParser[Config]("cocoa") {
  //      head("cocoa", "0.0.1")
  //
  //      opt[Int]('f', "foo").action((x, c) => {
  //        println("fff =>>" + x)
  //        c.copy(foo = x)
  //      }).text("foo is an integer property")
  //    }
  //
  //    parser.parse(args, Config()) match {
  //      case Some(config) ⇒
  //
  //      // println("sss ==>>>" + s)
  //      case None ⇒
  //    }

  lazy val logo =
    """
      |   _____
      |  / ____|
      | | |     ___   ___ ___   __ _
      | | |    / _ \ / __/ _ \ / _` |
      | | |___| (_) | (_| (_) | (_| |
      |  \_____\___/ \___\___/ \__,_|
      |
      |
    """.stripMargin
}
