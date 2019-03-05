name := "analyze-novel"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.4" % "test",
  "org.apache.lucene" % "lucene-core" % "7.1.0",
  "org.apache.lucene" % "lucene-analyzers-common" % "7.1.0",
  "org.apache.lucene" % "lucene-analyzers-kuromoji" % "7.1.0",
  "org.apache.lucene" % "lucene-queryparser" % "7.1.0"
)
        