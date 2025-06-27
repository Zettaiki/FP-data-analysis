package app

import org.apache.spark.rdd.RDD

object ItinerariesParser {

  /**
   * Parse a single CSV row from the flight dataset
   *
   * @param row the raw CSV string
   * @return an Option of the parsed tuple or None if the row is invalid
   */
  private def parseRow(row: String): Option[
    (String, String, String, String, String, String, String, Int, Boolean, Boolean, Boolean,
      Double, Double, Int, Option[Double],
      String, String, String, String, String, String, String, String, String, String)
  ] = {
    try {
      val columns = row.split(",", -1).map(_.trim)

      val legId = columns(0)
      val searchDate = columns(1)
      val flightDate = columns(2)
      val startingAirport = columns(3)
      val destinationAirport = columns(4)
      val fareBasisCode = columns(5)
      val travelDuration = columns(6)
      val elapsedDays = columns(7).toInt
      val isBasicEconomy = columns(8).toBoolean
      val isRefundable = columns(9).toBoolean
      val isNonStop = columns(10).toBoolean
      val baseFare = columns(11).toDouble
      val totalFare = columns(12).toDouble
      val seatsRemaining = columns(13).toInt
      val totalTravelDistance =
        if (columns(14).isEmpty) None else Some(columns(14).toDouble)

      val segmentsDepartureTimeEpochSeconds = columns(15)
      val segmentsDepartureTimeRaw = columns(16)
      val segmentsArrivalTimeEpochSeconds = columns(17)
      val segmentsArrivalTimeRaw = columns(18)
      val segmentsArrivalAirportCode = columns(19)
      val segmentsDepartureAirportCode = columns(20)
      val segmentsAirlineName = columns(21)
      val segmentsAirlineCode = columns(22)
      val segmentsEquipmentDescription = columns(23)
      val segmentsDurationInSeconds = columns(24)
      val segmentsDistance = columns(25)
      val segmentsCabinCode = columns(26)

      Some((
        legId, searchDate, flightDate, startingAirport, destinationAirport, fareBasisCode,
        travelDuration, elapsedDays, isBasicEconomy, isRefundable, isNonStop,
        baseFare, totalFare, seatsRemaining, totalTravelDistance,
        segmentsDepartureTimeEpochSeconds, segmentsDepartureTimeRaw,
        segmentsArrivalTimeEpochSeconds, segmentsArrivalTimeRaw,
        segmentsArrivalAirportCode, segmentsDepartureAirportCode,
        segmentsAirlineName, segmentsAirlineCode, segmentsEquipmentDescription,
        segmentsDurationInSeconds, segmentsDistance, segmentsCabinCode
      ))
    } catch {
      case _: Exception =>
        println(s"Error parsing row: $row")
        None
    }
  }

  /**
   * Parse an RDD of raw flight data lines
   *
   * @param rdd input RDD of raw CSV lines
   * @return RDD of valid parsed tuples
   */
  def parseRDD(
                rdd: RDD[String]
              ): RDD[
    (String, String, String, String, String, String, String, Int, Boolean, Boolean, Boolean,
      Double, Double, Int, Option[Double],
      String, String, String, String, String, String, String, String, String, String)
  ] = {
    rdd.flatMap(parseRow)
  }
}
