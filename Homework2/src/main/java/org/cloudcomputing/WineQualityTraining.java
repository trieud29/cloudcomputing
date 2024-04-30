package org.cloudcomputing;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import org.apache.spark.SparkConf;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.io.IOException;

public class WineQualityTraining {
    public static void main(String[] args) throws IOException {
        SparkConf conf = new SparkConf().setAppName("WineQualityTraining").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SparkSession spark = SparkSession.builder().appName("WineQualityTraining").getOrCreate();

        String localFilePath = "TrainingDataset.csv";
        Dataset<Row> trainingData = spark.read().format("csv")
                .option("header", "true")
                .option("sep", ";")
                .option("inferSchema", "true")
                .load(localFilePath);

        // Prepare the feature columns
        String[] featureColumns = {"fixed acidity", "volatile acidity", "citric acid", "residual sugar",
                "chlorides", "free sulfur dioxide", "total sulfur dioxide", "density",
                "pH", "sulphates", "alcohol"};
        VectorAssembler assembler = new VectorAssembler()
                .setInputCols(featureColumns)
                .setOutputCol("features");

        // Split the data into training and validation sets

        String validationFilePath = "ValidationDataset.csv";
        Dataset<Row> validationData = spark.read().format("csv")
                .option("header", "true")
                .option("sep", ";")
                .option("inferSchema", "true")
                .load(validationFilePath);

        // Create a LogisticRegression model
        LogisticRegression lr = new LogisticRegression()
                .setMaxIter(10)
                .setRegParam(0.3)
                .setElasticNetParam(0.8)
                .setFamily("multinomial")
                .setLabelCol("quality");

        // Create a pipeline with feature transformation and model
        Pipeline pipeline = new Pipeline().setStages(new PipelineStage[]{assembler, lr});

        // Train the model
        PipelineModel model = pipeline.fit(trainingData);

        // Evaluate the model on the validation dataset
        Dataset<Row> predictions = model.transform(validationData);
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setLabelCol("quality")
                .setPredictionCol("prediction")
                .setMetricName("f1");
        double f1Score = evaluator.evaluate(predictions);
        System.out.println("F1 score on validation data: " + f1Score);




        model.write().overwrite().save("winequalitymodel");

        jsc.close();
    }
}



