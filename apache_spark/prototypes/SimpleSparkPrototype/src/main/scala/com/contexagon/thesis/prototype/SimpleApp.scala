package com.contexagon.thesis.prototype
/* SimpleApp.scala */
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

object SimpleApp {
  def main(args: Array[String]) {
    val logFile = "/Applications/spark-1.1.0/wikilogs_oct07/*" // Should be some file on your system
    // this line is for local use within IDE
    //val conf = new SparkConf().setAppName("Simple Application").setMaster("local[8]")

    // this line is for local use as standalone application
    val conf = new SparkConf().setAppName("Simple Application")

    val sc = new SparkContext(conf)

    val t1 = System.currentTimeMillis
    val logData = sc.textFile(logFile, 2).cache()
    val t2 = System.currentTimeMillis

    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()

    val t3 = System.currentTimeMillis
    println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))

    println("Creating RDD took " + (t2-t1) + " ms.")
    println("Counting the chars took " + (t3-t2) + " ms.")
  }


}