package com.contexagon.thesis.FlinkWikiLogAnalyzer

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.flink.api.scala._
import org.apache.flink.api.scala.operators._

import org.apache.flink.client.LocalExecutor._

import org.apache.log4j.Logger._
import org.apache.log4j.BasicConfigurator._



/**
 * Implements the "WordCount" program that computes a simple word occurrence histogram
 * over some sample data
 *
 * This example shows how to:
 *
 *   - write a simple Flink program.
 *   - use Tuple data types.
 *   - write and use user-defined functions.
 */
object WordCount {
  def main(args: Array[String]) {

    // set up the execution environment
    val env = ExecutionEnvironment.getExecutionEnvironment

    

    val text = env.readTextFile("/Users/contexagon-SL01/Documents/masterthesis/thesis_lorenz/apache_spark/prototypes/testdata/access_log/accesslog_big.log")

    for (i <- 0 to 10) {
      val counts = text.flatMap {
        _.toLowerCase.split("\\W+")
      }
        .map {
        (_, 1)
      }
        .groupBy(0)
        .sum(1)

      // emit result
      counts.print()

      // execute program
      env.execute("WordCount Example")
    }
  }
}
