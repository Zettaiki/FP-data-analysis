package app

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import utils.Config.path_dataset_itineraries

object FlightPricesAnalysisApp {

  /**
   * Main entry point for the Flight Prices Analysis application.
   * @param args Command line arguments: "non-opt", "opt", or "both" to run respective pipelines.
   */
  def main(args: Array[String]): Unit = {
    if (args.length < 1) {
      println("Usage: FlightPricesAnalysisApp <non-opt|opt|both>")
      return
    }

    val spark = SparkSession.builder()
      .appName("Flight Prices Analysis")
      .getOrCreate()

    // Load and parse the dataset.
    // NOTE: The dataset path should be set in the "Config" object, inside the "utils" directory.
    val rddRaw = spark.sparkContext.textFile(path_dataset_itineraries)
    // val rddRaw = spark.sparkContext.textFile("src/main/resources/itineraries.csv") // For local testing
    val rddParsed = ItinerariesParser.parseRDD(rddRaw)

    if (args(0) == "non-opt") {
      println("Running non-optimized pipeline...\n")
      nonOptimizedFlightPriceAnalysis(rddParsed)
    } else if (args(0) == "opt") {
      println("Running optimized pipeline...\n")
      optimizedFlightPriceAnalysis(rddParsed)
    } else if (args(0) == "both") {
      println("Running both pipelines...\n")
      optimizedFlightPriceAnalysis(rddParsed)
      nonOptimizedFlightPriceAnalysis(rddParsed)
    }
  }

  private def nonOptimizedFlightPriceAnalysis(flights: RDD[Flight]): (RDD[String], RDD[String]) = {

    // Job 1: Average Price per Route and classification
    val routeAvg = flights.map(f => ((f.startingAirport, f.destinationAirport), f.totalFare))
      .groupByKey()
      .mapValues { fares =>
        val (sum, count) = fares.foldLeft((0.0, 0)) { case ((s, c), fare) => (s + fare, c + 1) }
        sum / count
      }

    val routeClass = routeAvg.map { case ((origin, dest), avgFare) =>
      val category = if (avgFare < 200) "Cheap"
      else if (avgFare <= 500) "Moderate"
      else "Expensive"
      ((origin, dest), category)
    }

    val joinedFlights = flights
      .map(f => ((f.startingAirport, f.destinationAirport), f))
      .join(routeClass)

    val flightsByClass = joinedFlights.map { case (_, (flight, routeClass)) =>
        (routeClass, flight.totalFare)
      }
      .groupByKey()
      .mapValues { fares =>
        val (sum, count) = fares.foldLeft((0.0, 0)) { case ((s, c), fare) => (s + fare, c + 1) }
        (sum / count, count)
      }

    // Print results for Job 1
    val resultFirstJob = flightsByClass.map { case (routeClass, (avgFare, totalCount)) =>
      s"Class: $routeClass | Avg Fare: $$${"%.2f".format(avgFare)} | Flights: $totalCount"
    }

    // Job 2: Average Price by Direct vs. Connecting Flights
    val routeNonStopPairs = flights.map { flight =>
      ((flight.startingAirport, flight.destinationAirport, flight.isNonStop), flight.totalFare)
    }

    val routeNonStopAverage = routeNonStopPairs
      .groupByKey()
      .mapValues { fares =>
        val (sum, count) = fares.foldLeft((0.0, 0)) { case ((s, c), fare) => (s + fare, c + 1) }
        sum / count
      }

    val routeGrouped = routeNonStopAverage.map { case ((origin, dest, isNonStop), avgFare) =>
        ((origin, dest), (isNonStop, avgFare))
      }
      .groupByKey()

    // Print results for Job 2
    val resultSecondJob = routeGrouped.map { case ((origin, dest), faresIterable) =>
      val fares = faresIterable.toMap
      val directFare = fares.getOrElse(true, Double.NaN)
      val connectingFare = fares.getOrElse(false, Double.NaN)
      s"$origin -> $dest | Direct: $$${"%.2f".format(directFare)} | Connecting: $$${"%.2f".format(connectingFare)}"
    }

    (resultFirstJob, resultSecondJob)
  }

  private def optimizedFlightPriceAnalysis(flights: RDD[Flight]): (RDD[String], RDD[String]) = {

    val routeAggregates = flights
      .map { flight =>
        val routeKey = (flight.startingAirport, flight.destinationAirport)
        val fare = flight.totalFare
        val isDirect = flight.isNonStop
        (routeKey, (fare, 1, Map(isDirect -> (fare, 1))))
      }
      .reduceByKey { case ((fareSum1, count1, map1), (fareSum2, count2, map2)) =>
        val mergedMap = map1 ++ map2.map { case (k, v) =>
          k -> (v._1 + map1.getOrElse(k, (0.0, 0))._1, v._2 + map1.getOrElse(k, (0.0, 0))._2)
        }
        (fareSum1 + fareSum2, count1 + count2, mergedMap)
      }
      .cache()

    // Extract results per route (Job 2) + classify (Job 1)
    val perRouteResults = routeAggregates.map { case ((origin, dest), (totalFare, totalCount, directMap)) =>
      val routeAvg = totalFare / totalCount
      val routeClass = if (routeAvg < 200) "Cheap"
      else if (routeAvg <= 500) "Moderate"
      else "Expensive"

      val directAvg = directMap.get(true).map { case (sum, cnt) => sum / cnt }.getOrElse(Double.NaN)
      val connectingAvg = directMap.get(false).map { case (sum, cnt) => sum / cnt }.getOrElse(Double.NaN)

      ((origin, dest), routeClass, routeAvg, directAvg, connectingAvg, totalCount)
    }

    // Prepare Job 1 aggregates (class averages and counts)
    val classAggregates = perRouteResults
      .map { case (_, routeClass, routeAvg, _, _, totalCount) =>
        (routeClass, (routeAvg * totalCount, totalCount))
      }
      .reduceByKey { case ((sum1, count1), (sum2, count2)) =>
        (sum1 + sum2, count1 + count2)
      }
      .mapValues { case (totalSum, totalCount) =>
        (totalSum / totalCount, totalCount)
      }

    val resultFirstJOb = classAggregates.map { case (routeClass, (avgFare, count)) =>
      s"Class: $routeClass | Avg Fare: $$${"%.2f".format(avgFare)} | Flights: $count"
    }

    val resultSecondJob = perRouteResults.map { case ((origin, dest), _, _, directAvg, connectingAvg, _) =>
      s"$origin -> $dest | Direct: $$${"%.2f".format(directAvg)} | Connecting: $$${"%.2f".format(connectingAvg)}"
    }

    routeAggregates.unpersist() // Clean up cached RDD

    (resultFirstJOb, resultSecondJob)
  }

}
