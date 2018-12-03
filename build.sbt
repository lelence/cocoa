import Settings._
import Dependencies._

lazy val common = (project in file("common"))
  .enablePlugins(AutomateHeaderPlugin)
  .dependsOn(proto)
  .settings(
    basicSettings,
    libraryDependencies ++= dependency4Common
  )

lazy val rest = (project in file("rest"))
  .enablePlugins(AutomateHeaderPlugin)
  .dependsOn(common)
  .enablePlugins(JavaServerAppPackaging)
  .settings(
    basicSettings,
    libraryDependencies ++= dependency4Rest
  )

lazy val rpc = (project in file("rpc"))
  .enablePlugins(AutomateHeaderPlugin)
  .dependsOn(common)
  .enablePlugins(JavaAppPackaging)
  .settings(
    basicSettings,
    libraryDependencies ++= dependency4Rpc
  )

lazy val node = (project in file("node"))
  .enablePlugins(AutomateHeaderPlugin)
  .dependsOn(common)
  .enablePlugins(JavaAppPackaging)
  .settings(
    basicSettings,
    libraryDependencies ++= dependency4Rpc
  )

//lazy val jsonrpc = (project in file("jsonrpc"))
//  .enablePlugins(AutomateHeaderPlugin)
//  .enablePlugins(JavaAppPackaging)
//  .settings(
//    basicSettings,
//
//    libraryDependencies ++= commonDependency ++ json4sDependency ++ testDependency
//  )

lazy val proto = (project in file("proto"))
  .settings(
    libraryDependencies ++= scalapbDependency,
    PB.targets in Compile := Seq(
      scalapb.gen() -> (sourceManaged in Compile).value
    )
  )

lazy val all = (project in file("."))
  .aggregate(common, rest, rpc, proto, node)
  .withId("cocoa")

