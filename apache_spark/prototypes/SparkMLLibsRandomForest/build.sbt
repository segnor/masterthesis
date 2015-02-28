name := "SparkMLLibTest"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
    "org.apache.spark" % "spark-core_2.10" % "1.2.0",
    "org.apache.spark" % "spark-mllib_2.10" % "1.2.0",
    "com.github.scopt" % "scopt_2.9.3" % "3.3.0"
)