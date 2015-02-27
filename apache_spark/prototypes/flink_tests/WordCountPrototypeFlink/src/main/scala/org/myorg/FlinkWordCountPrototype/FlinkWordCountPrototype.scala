package com.contexagon.thesis.prototype

import org.apache.flink.api.scala._

/**
 * Simple WordCountPrototype for Apache Flink
 * Apache Foundation, Sascha P. Lorenz
 * Purpose: performance comparisons between Apache Spark and Apache Flink
 * Date: 03.01.2015
 * Version: 1.0
 */
object FlinkWordCountPrototype {

  def main(args: Array[String]) {
    if (!parseParameters(args)) {
      // if application is calles without any parms
      sourcePath = "/Applications/spark-1.1.0/wikilogs_oct07/wikiall"
      destinationPath = "/Users/contexagon-SL01/Documents/masterthesis/thesis_lorenz/apache_spark/prototypes/logs/flinkOutput.out"
      return
    }


    val t1 = System.currentTimeMillis
    val env = ExecutionEnvironment.getExecutionEnvironment
    //val text = getTextDataSet(env)

    val text = env.readTextFile(sourcePath)

    val t2 = System.currentTimeMillis

    // The actual word count algorithm as MapReduce implementation
    val counts = text.flatMap {
      _.toLowerCase.split("\\W+") filter {
        _.nonEmpty
      }
    }
      .map { word => (word, 1)}
      .groupBy(0)
      .sum(1)


    val t3 = System.currentTimeMillis
    // write results as file
    counts.writeAsCsv(destinationPath, "\n", " ")


    env.execute("WordCountPrototype")
    val t4 = System.currentTimeMillis

    // print some performance data
    println("Creating Dataset took " + (t2-t1) + " ms.")
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
        System.err.println("Wrong call: FlinkWordCountPrototype <text path> <result path>")
        false
      }
    } else {
      System.out.println("  Reading default input file on local file system (wikilogs_all).")
      true
    }
  }


  private var sourcePath: String = "/Applications/spark-1.1.0/wikilogs_oct07/wikiall"
  private var destinationPath: String = "/Users/contexagon-SL01/Documents/masterthesis/thesis_lorenz/apache_spark/prototypes/logs/flinkOutput.out"

}

