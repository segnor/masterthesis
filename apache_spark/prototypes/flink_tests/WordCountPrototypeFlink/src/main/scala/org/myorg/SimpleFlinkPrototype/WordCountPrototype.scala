package org.myorg.SimpleFlinkPrototype

import org.apache.flink.api.scala._

/**
 * Created by contexagon-SL01 on 25/02/15.
 */
object WordCountPrototype {

  def main(args: Array[String]) {
    if (!parseParameters(args)) {
      sourcePath = "/Applications/spark-1.1.0/wikilogs_oct07/wikiall"
      destinationPath = "/Users/contexagon-SL01/Documents/masterthesis/thesis_lorenz/apache_spark/prototypes/logs/flinkOutput.out"
      return
    }


    val t1 = System.currentTimeMillis
    val env = ExecutionEnvironment.getExecutionEnvironment
    //val text = getTextDataSet(env)

    val text = env.readTextFile(sourcePath)

    val t2 = System.currentTimeMillis
    val counts = text.flatMap {
      _.toLowerCase.split("\\W+") filter {
        _.nonEmpty
      }
    }
      .map { word => (word, 1)}
      .groupBy(0)
      .sum(1)


    val t3 = System.currentTimeMillis
    counts.writeAsCsv(destinationPath, "\n", " ")


    env.execute("WordCountPrototype")
    val t4 = System.currentTimeMillis


    println("Creating Dataset took " + (t2-t1) + " ms.")
    println("Wordcount took " + (t3-t2) + " ms.")
    println("Writing files took " + (t4-t3) + " ms.")
    println("Total time consumption: " + (t4-t1) + " ms.")

  }

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
  private var destinationPath: String = "/Users/contexagon-SL01/Documents/masterthesis/thesis_lorenz/apache_spark/prototypes/logs/flinkOutput.out"

}

