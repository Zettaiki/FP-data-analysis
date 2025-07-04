package app

import java.time.LocalDate

/**
 * Class representing a flight itinerary
 *
 * @param legId                             Leg ID
 * @param searchDate                        Search date
 * @param flightDate                        Flight date
 * @param startingAirport                   Departure airport
 * @param destinationAirport                Destination airport
 * @param fareBasisCode                     Fare basis code
 * @param travelDuration                    Travel duration
 * @param elapsedDays                       Elapsed days
 * @param isBasicEconomy                    Indicates if it is basic economy
 * @param isRefundable                      Indicates if it is refundable
 * @param isNonStop                         Indicates if it is non-stop
 * @param baseFare                          Base fare
 * @param totalFare                         Total fare
 * @param seatsRemaining                    Remaining seats
 * @param totalTravelDistance               Total travel distance
 * @param segmentsDepartureTimeEpochSeconds Segments departure time (epoch seconds)
 * @param segmentsDepartureTimeRaw          Segments departure time (raw)
 * @param segmentsArrivalTimeEpochSeconds   Segments arrival time (epoch seconds)
 * @param segmentsArrivalTimeRaw            Segments arrival time (raw)
 * @param segmentsArrivalAirportCode        Segments arrival airport code
 * @param segmentsDepartureAirportCode      Segments departure airport code
 * @param segmentsAirlineName               Segments airline name
 * @param segmentsAirlineCode               Segments airline code
 * @param segmentsEquipmentDescription      Segments equipment description
 * @param segmentsDurationInSeconds         Segments duration in seconds
 * @param segmentsDistance                  Segments distance
 * @param segmentsCabinCode                 Segments cabin code
 */

case class Flight(
                   legId: String,
                   searchDate: LocalDate,
                   flightDate: LocalDate,
                   startingAirport: String,
                   destinationAirport: String,
                   fareBasisCode: String,
                   travelDuration: Int,
                   elapsedDays: Int,
                   isBasicEconomy: Boolean,
                   isRefundable: Boolean,
                   isNonStop: Boolean,
                   baseFare: Double,
                   totalFare: Double,
                   seatsRemaining: Int,
                   totalTravelDistance: Double,
                   segmentsDepartureTimeEpochSeconds: String,
                   segmentsDepartureTimeRaw: String,
                   segmentsArrivalTimeEpochSeconds: String,
                   segmentsArrivalTimeRaw: String,
                   segmentsArrivalAirportCode: String,
                   segmentsDepartureAirportCode: String,
                   segmentsAirlineName: String,
                   segmentsAirlineCode: String,
                   segmentsEquipmentDescription: String,
                   segmentsDurationInSeconds: String,
                   segmentsDistance: String,
                   segmentsCabinCode: String
                 )