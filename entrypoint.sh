#!/bin/bash
set -e

if  [ $# -ne 1 ]; then
        echo "Usage: $0 testDataPath"
        exit 1
fi
testDataPath= "$1"

$SPARK_HOME/bin/spark-submit --class org.cloudcomputing.WineQualityPrediction Homework2Pred-1.0-SNAPSHOT.jar "$testDataPath"
