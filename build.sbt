// name := "cocoa"

// version := "0.1"

// scalaVersion := "2.12.7"

// PB.targets in Compile := Seq(
//   scalapb.gen() -> (sourceManaged in Compile).value
// )

// resolvers += Resolver.bintrayRepo("jw3", "maven")

// libraryDependencies ++= {
//   val akkaVersion = "2.5.17"

//   Seq(
//     "com.google.inject" % "guice" % "4.2.0",
//     "net.codingwell" %% "scala-guice" % "4.2.1",
//     // "com.rxthings" %% "akka-injects" % "0.8",

//     "com.typesafe.akka" %% "akka-actor" % akkaVersion,
//     "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
//     "com.typesafe.akka" %% "akka-remote" % akkaVersion,
//     "com.typesafe.akka" %% "akka-stream" % akkaVersion,
//     "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
//     "com.typesafe.akka" %% "akka-http" % "10.1.5",
//     "com.thesamet.scalapb" %% "scalapb-json4s" % "0.7.1",
//     "org.json4s" %% "json4s-native" % "3.6.1",
//     "de.heikoseeberger" %% "akka-http-json4s" % "1.22.0",
//     "com.github.etaty" %% "rediscala" % "1.8.0",
//     "com.lightbend.akka" %% "akka-stream-alpakka-slick" % "0.20",
//     "mysql" % "mysql-connector-java" % "5.1.47",

//     "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion,
//     "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",

//     "com.github.scopt" %% "scopt" % "3.7.0"
//   )
// }
// //.map(_ withSources() withJavadoc())

import Settings._
import Dependencies._

lazy val common = (project in file("common"))
  .dependsOn(proto)
  .settings(
    basicSettings,
    libraryDependencies ++= commonDependency ++ actorDependency ++ driverDependency
  )

lazy val rest = (project in file("rest"))
  .dependsOn(common)
  .enablePlugins(JavaServerAppPackaging)
  .settings(
    basicSettings,
    libraryDependencies ++= httpDependency
  )

lazy val rpc = (project in file("rpc"))
  .dependsOn(common)
  .enablePlugins(JavaAppPackaging)
  .settings(
    basicSettings
  )

lazy val proto = (project in file("proto"))
  .settings(
    libraryDependencies ++= scalapbDependency,
    PB.targets in Compile := Seq(
      scalapb.gen() -> (sourceManaged in Compile).value
    )
  )

lazy val all = (project in file("."))
  .aggregate(common, rest, rpc, proto)

