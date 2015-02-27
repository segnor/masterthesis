#! /bin/bash

###############################################################################
# Masterthesis Big Data Processing with Apache Spark
# Sascha P. Lorenz - Hochschule Emden-Leer, Beuth Hochschule Berlin
# Start Script for WordCount in Apache Flink for Performance Comparison Reasons	
# OUTPUTDIR = Destination for performance logs
# INPUT_PATH = Location of the application 
# APPLICATION = Name of the application
# CLASS = Full qualified classname of the main method
###############################################################################

OUTPUTDIR=/Users/contexagon-SL01/Documents/masterthesis/thesis_lorenz/apache_spark/prototypes/logs
OUTPUT_LOG=$OUTPUTDIR/simpleapp.log
INPUT_PATH=/Users/contexagon-SL01/Documents/masterthesis/thesis_lorenz/apache_spark/prototypes
APPLICATION=/WordcountSparkPrototype/target/scala-2.10/sparkwordcountprototype_2.10-1.0.jar
CLASS="com.contexagon.thesis.prototype.SparkWordCountPrototype"


COMMAND_TOP=top > $OUTPUT_LOG
COMMAND_PID="ps -ef | grep -v 'grep' | grep 'top'"



# uncomment this block for usage at multinode clusters
#for i in `cat hosts`;do
#  ssh $i $COMMAND_TOP
#  ssh $i "$COMMAND_PID | awk '{print \$2}' > $PID"
#done



./bin/flink run -p 4 ./Users/contexagon-SL01/Documents/masterthesis/thesis_lorenz/apache_spark/prototypes/flink_tests/WordCountPrototypeFlink/target/WordCountFlinkPrototype-0.1-flink-fat-jar.jar \

kill COMMAND_PID
