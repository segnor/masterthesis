/* SimpleApp.scala */

import org.apache.flink.api.scala._

object SimpleApp {
  def main(args: Array[String]) {

    val logFile = "acc.log" // Should be some file on your system
    val conf = new SparkConf().setAppName("Simple Application")
    val sc = new SparkContext(conf)
    val logData = sc.textFile(logFile, 2).cache()
    val numAs = logData.filter(line => line.contains("e")).count()
    val numBs = logData.filter(line => line.contains("s")).count()
    println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))
  }
}