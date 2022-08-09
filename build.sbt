name := "scala-influxdb-client"

organization := "io.razem"

scalaVersion := "2.12.16"
crossScalaVersions := Seq(scalaVersion.value, "2.13.8", "2.11.12")

publishTo := sonatypePublishToBundle.value

Test / testOptions += Tests.Argument("-oDF")

releaseCrossBuild := true

libraryDependencies += "org.asynchttpclient" % "async-http-client" % "2.12.3"
libraryDependencies += "io.spray" %%  "spray-json" % "1.3.6"
libraryDependencies += "com.github.tomakehurst" % "wiremock" % "2.27.2" % "test"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.12" % "test"
libraryDependencies += "com.dimafeng" %% "testcontainers-scala" % "0.38.9" % "test"

import ReleaseTransformations._

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  ReleaseStep(action = Command.process("publishSigned", _), enableCrossBuild = true),
  setNextVersion,
  commitNextVersion,
  ReleaseStep(action = Command.process("sonatypeReleaseAll", _), enableCrossBuild = true),
  pushChanges
)
