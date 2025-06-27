package app

import org.apache.spark.sql.SparkSession;

object FlightPricesAnalysisApp {
  val path_dataset_itineraries = "/datasets/itineraries.csv"
  val path_output = "/output/"

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Flight Prices Analysis")
      .getOrCreate()

    // Load the flight prices dataset
    val rddItineraries = spark.sparkContext.textFile(path_dataset_itineraries)

    // Parse the CSV data
    val rddParsedItineraries = ItinerariesParser.parseRDD(rddItineraries)
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
