package app

import org.apache.spark.rdd.RDD
import app.Flight

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object ItinerariesParser {
  /**
   * Parse a single CSV row from the flight dataset
   *
   * @param row the raw CSV string
   * @return an Option of the parsed tuple or None if the row is invalid
   */

  private def parseLine(line: String): Option[Flight] = {
    try {
      val cols = line.split(",", -1).map(_.trim)
      val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

      def parseDurationToMinutes(duration: String): Int = {
        val hourPattern = "PT(\\d+)H".r
        val minutePattern = "PT(?:\\d+H)?(\\d+)M".r

        val hours = hourPattern.findFirstMatchIn(duration).map(_.group(1).toInt).getOrElse(0)
        val minutes = minutePattern.findFirstMatchIn(duration).map(_.group(1).toInt).getOrElse(0)

        hours * 60 + minutes
      }

      def toIntSafe(s: String): Int = try {
        s.toInt
      } catch {
        case _: Exception => 0
      }

      def toDoubleSafe(s: String): Double = try {
        s.toDouble
      } catch {
        case _: Exception => 0.0
      }

      def toBooleanSafe(s: String): Boolean = try {
        s.toBoolean
      } catch {
        case _: Exception => false
      }

      val flight = Flight(
        legId = cols(0),
        searchDate = LocalDate.parse(cols(1), dateFormatter),
        flightDate = LocalDate.parse(cols(2), dateFormatter),
        startingAirport = cols(3),
        destinationAirport = cols(4),
        fareBasisCode = cols(5),
        travelDuration = parseDurationToMinutes(cols(6)),
        elapsedDays = toIntSafe(cols(7)),
        isBasicEconomy = toBooleanSafe(cols(8)),
        isRefundable = toBooleanSafe(cols(9)),
        isNonStop = toBooleanSafe(cols(10)),
        baseFare = toDoubleSafe(cols(11)),
        totalFare = toDoubleSafe(cols(12)),
        seatsRemaining = toIntSafe(cols(13)),
        totalTravelDistance = toDoubleSafe(cols(14)),
        segmentsDepartureTimeEpochSeconds = cols(15),
        segmentsDepartureTimeRaw = cols(16),
        segmentsArrivalTimeEpochSeconds = cols(17),
        segmentsArrivalTimeRaw = cols(18),
        segmentsArrivalAirportCode = cols(19),
        segmentsDepartureAirportCode = cols(20),
        segmentsAirlineName = cols(21),
        segmentsAirlineCode = cols(22),
        segmentsEquipmentDescription = cols(23),
        segmentsDurationInSeconds = cols(24),
        segmentsDistance = cols(25),
        segmentsCabinCode = cols(26)
      )

      Some(flight)
    } catch {
      case e: Exception =>
        println(s"[PARSE ERROR] Line: $line\nError: ${e.getMessage}")
        None
    }
  }

  /**
   * Parse an RDD of raw flight data lines
   *
   * @param rdd input RDD of raw CSV lines
   * @return RDD of valid parsed tuples
   */
  def parseRDD(rdd: RDD[String]): RDD[Flight] = {
    rdd.flatMap(parseLine)
  }
}
