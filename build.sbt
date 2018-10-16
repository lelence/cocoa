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
    libraryDependencies ++= httpAndSocketDependency
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
  .withId("cocoa")

