/* SimpleApp.scala */
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import com.codahale.metrics.Meter

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