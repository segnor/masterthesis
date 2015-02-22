package com.contexagon.FlinkWikiLogAnalyzer

import com.contexagon.FlinkWikiLogAnalyzer.WikiLog._
import com.contexagon.FlinkWikiLogAnalyzer.SortHelper._
import org.apache.flink.api.scala._
import org.apache.flink.api.scala.operators._

import org.apache.flink.client.LocalExecutor._

import org.apache.log4j.Logger._
import org.apache.log4j.BasicConfigurator._


/**
 * Created by contexagon-SL01 on 18/02/15.
 */
class FlinkWikiLogAnalyzer {
  def main(args: Array[String]) {
    val env = ExecutionEnvironment.getExecutionEnvironment


    // take file name as argument
    val wikiFile = args(0)


    // Reads wikilogs line per line, maps it into a Flink Dataset and tries to cache it
    val wikiLogcompl = env.readTextFile(wikiFile).map(WikiLog.parseLogLine())
    val wikiLog: DataSet[String] = WikiLog.parseLogLine(wikiLogcompl.toString).toString


    val wlog = wikiLogcompl.map(log => WikiLog.parseLogLine)


    // calculate statistics based on the logfile size
    val contentSizes = wikiLog.map(log => log.id)
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

