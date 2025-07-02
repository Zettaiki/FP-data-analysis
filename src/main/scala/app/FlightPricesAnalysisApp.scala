package app

import org.apache.spark.sql.SparkSession;

object FlightPricesAnalysisApp {
  val path_dataset_itineraries = "/datasets/itineraries.csv"
  val path_output = "/output/"

  def main(args: Array[String]): Unit = {
    if (args.length < 1) {
      println("Usage: FlightPricesAnalysisApp <non-opt|opt|both>")
      return
    }

    val spark = SparkSession.builder()
      .appName("Flight Prices Analysis")
      .getOrCreate()

    // Load and parse the dataset.
    val rddRaw = spark.sparkContext.textFile(path_dataset_itineraries)
    val rddParsed = ItinerariesParser.parseRDD(rddRaw)

    if (args(0) == "non-opt") {
      println("Running non-optimized pipeline...\n")
      nonOptimizedPipeline(rddParsed)
    } else if (args(0) == "opt") {
      println("Running optimized pipeline...\n")
      optimizedPipeline(rddParsed)
    } else if (args(0) == "both") {
      println("Running both pipelines...\n")
      optimizedPipeline(rddParsed)
      nonOptimizedPipeline(rddParsed)
    }
  }

  def nonOptimizedPipeline(): Unit = {
    // This method is a placeholder for the non-optimized pipeline implementation
    // It will contain the logic to process the parsed itineraries RDD
    // and perform the necessary transformations and actions.
  }

  def optimizedPipeline(): Unit = {
    // This method is a placeholder for the optimized pipeline implementation
    // It will contain the logic to process the parsed itineraries RDD
    // and perform the necessary transformations and actions.
  }

}
