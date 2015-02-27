#! /bin/bash

#############################################
# This is a series of running scripts
# 1. start_top.sh
# 2. run user application (such as wordcount)
# 3. stop_stop.sh
#   3-1. kill top process
#   3-2. move outputs of top to specified dir
#############################################

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



./bin/spark-submit --class $CLASS --master local[4] $INPUT_PATH$APPLICATION

kill COMMAND_PID
