package com.contexagon.thesis.prototype

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.mllib.tree.{DecisionTree, RandomForest}
import org.apache.spark.mllib.util.MLUtils

/** ******************************************************
  * Spark MLLib Test for Random Forest classification
  * Master's Thesis Big Data Processing with Apache Spark
  * Apache Software Foundation and Sascha P. Lorenz
  *
  * ******************************************************
  */

object SparkMLLibsRandomFClass {

  def main(args: Array[String]) {

    //These are libSVMFiles for prediction of salaries. These files are causing an IndexOutOfBoundException
    //in gini and entropy impurity. This is a known Issue (Spark JIRA 5119 and should be fixed in 1.3.0
    //val source = "/Users/contexagon-SL01/Documents/masterthesis/thesis_lorenz/apache_spark/prototypes/testdata/a9a"
    //val source_test = "/Users/contexagon-SL01/Documents/masterthesis/thesis_lorenz/apache_spark/prototypes/testdata/a9a.t"


    // this file contains test data to classify forests
    val source = "../testdata/covtype.txt"




    // for usage within the IDE
    val conf = new SparkConf().setAppName("SparkMLLibsRandomFClass").setMaster("local")

    // for usage as standalone or on cluster
    //val conf = new SparkConf().setAppName("Simple Application")

    val sc = new SparkContext(conf)
    // parse the data into RDD
    val data = MLUtils.loadLibSVMFile(sc, source)
    // split data sets into training (0.7) and test (0.3)
    val splits = data.randomSplit(Array(0.7, 0.3))
    val (trainingData, testData) = (splits(0), splits(1))

    //val trainingData = MLUtils.loadLibSVMFile(sc, source)
    //val testData = MLUtils.loadLibSVMFile(sc, source_test)

    // Train a RandomForest model.
    //  Empty categoricalFeaturesInfo indicates all features are continuous.
    val numClasses = 3
    val categoricalFeaturesInfo = Map[Int, Int](12->7) // indicates that feature 13 has 7 possibilities
    val numTrees = 12
    val featureSubsetStrategy = "auto" // let the algorithm choose the strategy
    val impurity = "gini"
    val maxDepth = 6
    val maxBins = 32

    val model = RandomForest.trainClassifier(trainingData, numClasses, categoricalFeaturesInfo,
      numTrees, featureSubsetStrategy, impurity, maxDepth, maxBins)

    // evaluate model on test instances and compute test error
    val labelAndPreds = testData.map { point =>
      val prediction = model.predict(point.features)
      (point.label, prediction)
    }
    val testErr = labelAndPreds.filter(r => r._1 != r._2).count.toDouble / testData.count()
    println("Test Error = " + testErr)
    println("Learned classification forest model:\n" + model.toDebugString)
  }
}