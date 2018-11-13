import Settings._
import Dependencies._

lazy val common = (project in file("common"))
  .enablePlugins(AutomateHeaderPlugin)
  .dependsOn(proto)
  .settings(
    basicSettings,
    libraryDependencies ++= commonDependency ++ actorDependency ++ driverDependency
  )

lazy val rest = (project in file("rest"))
  .enablePlugins(AutomateHeaderPlugin)
  .dependsOn(common)
  .enablePlugins(JavaServerAppPackaging)
  .settings(
    basicSettings,
    libraryDependencies ++= httpAndSocketDependency ++ testDependency,
    libraryDependencies += "com.typesafe.akka" %% "akka-http-testkit" % "10.1.5" % Test
  )

lazy val rpc = (project in file("rpc"))
  .enablePlugins(AutomateHeaderPlugin)
  .dependsOn(common)
  .enablePlugins(JavaAppPackaging)
  .settings(
    basicSettings,
    libraryDependencies ++= testDependency
  )

lazy val jsonrpc = (project in file("jsonrpc"))
  .enablePlugins(AutomateHeaderPlugin)
  .enablePlugins(JavaAppPackaging)
  .settings(
    basicSettings,

    libraryDependencies ++= commonDependency ++ testDependency
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
  .withId("cocoa")

