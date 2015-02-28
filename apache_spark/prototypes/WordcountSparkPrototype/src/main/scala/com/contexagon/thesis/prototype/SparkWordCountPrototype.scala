package com.contexagon.thesis.prototype
/* SparkWordCountPrototype.scala */
import org.apache.spark._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

/**
 * Simple WordCountPrototype for Apache Spark
 * Apache Foundation, Sascha P. Lorenz
 * Purpose: performance comparisons between Apache Spark and Apache Flink
 * Date: 23.12.2014
 * Version: 1.0
 */

object SparkWordCountPrototype {

    def main(args: Array[String]) {
      if (!parseParameters(args)) {
        // if application is calles without any parms
        sourcePath = "/Applications/spark-1.1.0/wikilogs_oct07/wikiall"
        destinationPath = "/Users/contexagon-SL01/Documents/masterthesis/thesis_lorenz/apache_spark/prototypes/logs/sparkOutput.out"
        return
      }


    val logFile = "/Applications/spark-1.1.0/wikilogs_oct07/wikiall" // Should be some file on your system
    // this line is for local use within IDE
    val conf = new SparkConf().setAppName("Simple Application").setMaster("local-cluster[8]")

    // this line is for local use as standalone application
    //val conf = new SparkConf().setAppName("Simple Application")

    // creates the SparkContext sc
    val sc = new SparkContext(conf)

    val t1 = System.currentTimeMillis

    // reads the textfile and transforms it into a RDD as soon as it will be called
    val text = sc.textFile(logFile)

    val t2 = System.currentTimeMillis

    // The actual word count algorithm as MapReduce implementation
    val counts = text.flatMap(line => line.toLowerCase().split("\\W+")).filter(_.nonEmpty)
      .map(word => (word, 1))
      .reduceByKey(_ + _)
      .sortByKey(true)
    //counts.sortByKey()

    val t3 = System.currentTimeMillis
      //counts.foreach(println)

      counts.saveAsTextFile(destinationPath)
    val t4 = System.currentTimeMillis

      //file.saveAsTextFile(args(2))
    sc.stop()

    // print some performance data
    println("Creating RDD took " + (t2-t1) + " ms.")
    println("Wordcount took " + (t3-t2) + " ms.")
    println("Writing files took " + (t4-t3) + " ms.")
    println("Total time consumption: " + (t4-t1) + " ms.")

  }

  // parse call parameters
  private def parseParameters(args: Array[String]): Boolean = {
    if (args.length > 0) {
      if (args.length == 2) {
        sourcePath = args(0)
        destinationPath = args(1)
        true
      } else {
        System.err.println("Usage: WordCount <text path> <result path>")
        false
      }
    } else {
      System.out.println("Executing WordCount example with built-in default data.")
      System.out.println("  Provide parameters to read input data from a file.")
      System.out.println("  Usage: WordCount <text path> <result path>")
      true
    }
  }


  private var sourcePath: String = "/Applications/spark-1.1.0/wikilogs_oct07/wikiall"
  private var destinationPath: String = "/Users/contexagon-SL01/Documents/masterthesis/thesis_lorenz/apache_spark/prototypes/logs/sparkOutput.out"

}

