name := "cv-system-consider"
version := "0.1.0"
scalaVersion := "2.13.15"
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.17" % Test,
  "org.typelevel" %% "cats-core" % "2.12.0",
  "org.typelevel" %% "cats-effect" % "3.5.2"
)
