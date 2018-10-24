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
    libraryDependencies ++= httpAndSocketDependency ++ testDependency
  )

lazy val rpc = (project in file("rpc"))
  .enablePlugins(AutomateHeaderPlugin)
  .dependsOn(common)
  .enablePlugins(JavaAppPackaging)
  .settings(
    basicSettings,
    libraryDependencies ++= testDependency
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

