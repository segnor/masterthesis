package com.contexagon.FlinkWikiLogAnalyzer

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.api.scala._
import org.apache.flink.api.scala.operators._
import org.apache.flink.api.java.operators.AggregateOperator._
import org.apache.flink.client.LocalExecutor._

import org.apache.log4j.Logger._
import org.apache.log4j.BasicConfigurator._

/**
 * Created by contexagon-SL01 on 24/02/15.
 */

  object SimpleApp {
    def main(args: Array[String]) {
      val logFile = "/Applications/spark-1.1.0/wikilogs_oct07/*" // Should be some file on your system
      // this line is for local use within IDE
      //val conf = new SparkConf().setAppName("Simple Application").setMaster("local[8]")

      // this line is for local use as standalone application
      //val conf = new SparkConf().setAppName("Simple Application")

      val env = ExecutionEnvironment.getExecutionEnvironment

      //val sc = new SparkContext(conf)

      val t1 = System.currentTimeMillis
      val logData  = env.readTextFile(logFile)
      val t2 = System.currentTimeMillis

      val numAs = logData.filter(line => line.contains("a"))
      val numBs = logData.filter(line => line.contains("b"))
      var count = 1L
      val numVerticesA =
        (numAs).reduceGroup { iter =>

                  while (iter.hasNext) {
                    count += 1
                    iter.next
                  }
                  count
                  }


//      if (logData.filter(line => line.contains("a")) != null){
//        ScalaObject.a = ScalaObject.a + 1
//      }
//      if (logData.filter(line => line.contains("b")) != null){
//        ScalaObject.b = ScalaObject.b + 1
//      }

//      val numAs = ScalaObject.a
//      val numBs = ScalaObject.b

      val t3 = System.currentTimeMillis
      println("Lines with a: %s, Lines with b: %s".format(count, numBs))

      println("Creating RDD took " + (t2-t1) + " ms.")
      println("Counting the chars took " + (t3-t2) + " ms.")
    }

//  object ScalaObject {
//
//    var a: Int = 0
//    var b: Int = 0
//  }

}
