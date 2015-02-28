package com.contexagon.thesis.prototype


import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.mllib.tree.RandomForest
import org.apache.spark.mllib.util.MLUtils



object SparkMLLibsRandomForest {

  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("SparkMLLibsRandomForest").setMaster("local[4]")

    //val conf = new SparkConf().setAppName("Simple Application")

    val sc = new SparkContext(conf)
    // Load and parse the data file.
    val data = MLUtils.loadLibSVMFile(sc, "/Users/contexagon-SL01/Documents/masterthesis/thesis_lorenz/apache_spark/prototypes/testdata/a1a.txt")
    // Split the data into training and test sets (30% held out for testing)
    val splits = data.randomSplit(Array(0.7, 0.3))
    val (trainingData, testData) = (splits(0), splits(1))

    // Train a RandomForest model.
    //  Empty categoricalFeaturesInfo indicates all features are continuous.
    val numClasses = 2
    val categoricalFeaturesInfo = Map[Int, Int]()
    val numTrees = 3
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
    val testMSE = labelsAndPredictions.map { case (v, p) => math.pow((v - p), 2)} //.mean()
    println("Test Mean Squared Error = " + testMSE)
    println("Learned regression forest model:\n" + model.toDebugString)
  }
}