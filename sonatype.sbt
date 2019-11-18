pomExtra in Global := {
  <url>https://github.com/razem-io/scala-influxdb-client</url>
  <licenses>
    <license>
      <name>MIT</name>
      <url>https://opensource.org/licenses/MIT</url>
    </license>
  </licenses>
  <scm>
    <connection>scm:git:github.com/razem-io/scala-influxdb-client</connection>
    <developerConnection>scm:git:git@github.com:razem-io/scala-influxdb-client.git</developerConnection>
    <url>github.com/razem-io/scala-influxdb-client</url>
  </scm>
  <developers>
    <developer>
      <id>razem-io</id>
      <name>Julian Pieles</name>
      <url>https://razem-io</url>
    </developer>
  </developers>
}

// Your profile name of the sonatype account. The default is the same with the organization value
sonatypeProfileName := "io.razem"

isSnapshot := true

// To sync with Maven central, you need to supply the following information:
publishMavenStyle := true

// Open-source license of your choice
licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT"))

import xerial.sbt.Sonatype._
sonatypeProjectHosting := Some(GitHubHosting("razem-io", "scala-influxdb-client", "scala-influxdb-client@pieles.digital"))

// or if you want to set these fields manually
homepage := Some(url("https://github.com/razem-io/scala-influxdb-client"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/razem-io/scala-influxdb-client"),
    "scm:git@github.com:razem-io/scala-influxdb-client.git"
  )
)
developers := List(
  Developer(id="razem-io", name="Julian Pieles", email="scala-influxdb-client@pieles.digital", url=url("https://razem.io"))
)
