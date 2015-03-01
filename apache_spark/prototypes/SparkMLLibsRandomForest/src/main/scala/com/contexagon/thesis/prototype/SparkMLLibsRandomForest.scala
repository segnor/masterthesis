package com.contexagon.thesis.prototype

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.mllib.tree.RandomForest
import org.apache.spark.mllib.util.MLUtils

/** ******************************************************
  * Spark MLLib Test for Random Forest regressions
  * Master's Thesis Big Data Processing with Apache Spark
  * Apache Software Foundation and Sascha P. Lorenz
  *
  * ******************************************************
  */

object SparkMLLibsRandomForest {

  def main(args: Array[String]) {



      val source = "../testdata/a9a"

      val source_test = "../testdata/a9a.t"



      // for usage within the IDE
      val conf = new SparkConf().setAppName("SparkMLLibsRandomForest").setMaster("local")

      // for usage as standalone or on cluster
      //val conf = new SparkConf().setAppName("Simple Application")

      val sc = new SparkContext(conf)
      // Load and parse the data file.
      //val data = MLUtils.loadLibSVMFile(sc, source)
      // Split the data into training and test sets (30% held out for testing)
      //val splits = data.randomSplit(Array(0.7, 0.3))
      //val (trainingData, testData) = (splits(0), splits(1))

      val trainingData = MLUtils.loadLibSVMFile(sc, source)
      val testData = MLUtils.loadLibSVMFile(sc, source_test)

    // Train a RandomForest model.
    //  Empty categoricalFeaturesInfo indicates all features are continuous.
    val numClasses = 2
    val categoricalFeaturesInfo = Map[Int, Int]()
    val numTrees = 32
    // Use more in practice.
    val featureSubsetStrategy = "auto"
    // Let the algorithm choose.
    val impurity = "variance"
    val maxDepth = 4
    val maxBins = 32

    val model = RandomForest.trainRegressor(trainingData, categoricalFeaturesInfo,
      numTrees, featureSubsetStrategy, impurity, maxDepth, maxBins)

    // Evaluate model on test instances and compute test error
    val labelsAndPredictions = testData.map { point =>
      val prediction = model.predict(point.features)
      (point.label, prediction)
    }
    val testMSE = labelsAndPredictions.map { case (v, p) => math.pow((v - p), 2)}.mean()
    println("Test Mean Squared Error = " + testMSE)
    println("Learned regression forest model:\n" + model.toDebugString)
  }
}