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
APPLICATION=/SimpleSparkPrototype/target/scala-2.10/simple-project_2.10-1.0.jar
CLASS="com.contexagon.thesis.prototype.SimpleApp"

#rm $OUTPUT_LOG > /dev/null
COMMAND_TOP="top -d 5 -b > $OUTPUT_LOG &"
COMMAND_PID="ps -ef | grep -v 'grep' | grep 'top -d 5'"


# uncomment this block for usage at multinode clusters
#for i in `cat hosts`;do
#  ssh $i $COMMAND_TOP
#  ssh $i "$COMMAND_PID | awk '{print \$2}' > $PID"
#done

$COMMAND_TOP

./bin/spark-submit --class $CLASS --master local[4] $INPUT_PATH$APPLICATION

kill COMMAND_PID