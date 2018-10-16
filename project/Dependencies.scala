import sbt._

object Dependencies {
  val slf4jVersion = "1.7.25"
  val logbackVersion = "1.2.3"
	lazy val akkaVersion = "2.5.17"

  lazy val commonDependency = Seq(
    "org.slf4j" % "slf4j-api" % slf4jVersion,
    "ch.qos.logback" % "logback-core" % logbackVersion,
    "ch.qos.logback" % "logback-classic" % logbackVersion,
    "ch.qos.logback" % "logback-access" % logbackVersion,

    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",
    "com.google.inject" % "guice" % "4.2.0",
    "net.codingwell" %% "scala-guice" % "4.2.1",
    "com.github.scopt" %% "scopt" % "3.7.0",
    "com.thesamet.scalapb" %% "scalapb-json4s" % "0.7.1",
    "org.json4s" %% "json4s-native" % "3.6.1"
  )

  lazy val actorDependency = Seq(
  	"com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-remote" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion
  )

  lazy val httpAndSocketDependency = Seq(
  	"com.typesafe.akka" %% "akka-http" % "10.1.5",
    "de.heikoseeberger" %% "akka-http-json4s" % "1.22.0",
    "com.corundumstudio.socketio" % "netty-socketio" % "1.7.16"
  )

  lazy val driverDependency = Seq(
  	"com.github.etaty" %% "rediscala" % "1.8.0",
    "com.lightbend.akka" %% "akka-stream-alpakka-slick" % "0.20",
    "mysql" % "mysql-connector-java" % "5.1.47"
  )

  lazy val scalapbDependency = Seq(
  	"com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion,
    "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf"
  )

}