name := "jeju-api"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  "mysql" % "mysql-connector-java" % "5.1.18"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)