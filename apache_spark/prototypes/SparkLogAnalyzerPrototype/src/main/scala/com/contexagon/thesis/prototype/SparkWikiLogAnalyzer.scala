package com.contexagon.thesis.prototype
import com.contexagon.thesis.prototype.WikiLog
import com.contexagon.thesis.prototype.SortHelper
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext._

/**
 * Created by Sascha Lorenz
 * To run locally:
 * ./bin/spark-submit --class com.contexagon.thesis.prototype.SparkWikiLogAnalyzer
 * ~/Documents/masterthesis/thesis_lorenz/apache_spark/prototypes/SparkLogAnalyzerPrototype/target/
 *  scala-2.10/sparkwikiloganalyzer_2.10-1.0.jar logs.log
 */
object SparkWikiLogAnalyzer {
  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setAppName("SparkWikiLogAnalyzer")

    // create spark context as representer of the cluster
    val sc = new SparkContext(sparkConf)

    // take file name as argument
    val wikiFile = args(0)


    // Reads wikilogs line per line, maps it into a RDD and tries to cache it
    val wikiLog = sc.textFile(wikiFile).map(WikiLog.parseLogLine).cache()

    // calculate statistics based on the logfile size
    val contentSizes = wikiLog.map(log => log.id).cache()
    println("Content Size Avg: %s, Min: %s, Max: %s".format(
      contentSizes.reduce(_ + _) / contentSizes.count,
      contentSizes.min,
      contentSizes.max))

    // compute Response Code to Count
    val responseCodeToCount = wikiLog
      .map(log => (log.timeStamp, 1))
      .reduceByKey(_ + _)
      .take(100)

    /// """ --> multi line String literal
    println(s"""Response code counts: ${responseCodeToCount.mkString("[", ",", "]")}""")

    // Any IPAddress that has accessed the server more than 10 times.
   val urls = wikiLog
      .map(log => (log.url, 1))
      .reduceByKey(_ + _)
      .filter(_._2 > 10)
      .map(_._1)
      .take(100)
    println(s"""IPAddresses > 10 times: ${urls.mkString("[", ",", "]")}""")

    // Top Endpoints.
    val topEndpoints = wikiLog
      .map(log => (log.url, 1))
      .reduceByKey(_ + _)
      .top(10)(SortHelper.IntegerSort)
    println(s"""Top Endpoints: ${topEndpoints.mkString("[", ",", "]")}""")

    sc.stop()
  }
}