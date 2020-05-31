package gtfsproject

import scala.io.{BufferedSource, Source}
import java.io.{File, BufferedWriter, FileWriter}

object Main extends App {

  val tripSource: BufferedSource = Source.fromFile("C:\\Users\\iurie\\OneDrive\\A_MCIT\\04.20 - 420-MD2-UM\\STM PROJECT\\gtfs_stm\\trips.txt")
  val tripList = tripSource
    .getLines() // a collection of lines
    .toList
    .tail
    .map(_.split(",", -1)) // a collection of collection of fields
    .map(t => Trip(t(0).toInt, t(1), t(2), t(3), t(4).toInt, t(5).toInt, t(6).toInt, if (t(7).isEmpty) None else Some(t(7)), if (t(8).isEmpty) None else Some(t(8))))
//    .foreach(println)
  tripSource.close()

  val routeSource: BufferedSource = Source.fromFile("C:\\Users\\iurie\\OneDrive\\A_MCIT\\04.20 - 420-MD2-UM\\STM PROJECT\\gtfs_stm\\routes.txt")
  val routeList = routeSource
    .getLines() // a collection of lines
    .toList
    .tail
    .map(_.split(",", -1)) // a collection of collection of fields
    .map(r => Route(r(0).toInt, r(1), r(2).toInt, r(3), r(4).toInt, r(5), r(6), if (r(7).isEmpty) None else Some(r(7))))
//    .foreach(println)
  routeSource.close()

  val calendarSource: BufferedSource = Source.fromFile("C:\\Users\\iurie\\OneDrive\\A_MCIT\\04.20 - 420-MD2-UM\\STM PROJECT\\gtfs_stm\\calendar.txt")
  val calendarList = calendarSource
    .getLines() // a collection of lines
    .toList
    .tail
    .map(_.split(",", -1)) // a collection of collection of fields
    .map(c => Calendar(c(0), c(1).toInt, c(2).toInt, c(3).toInt, c(4).toInt, c(5).toInt, c(6).toInt, c(7).toInt, c(8).toInt, c(9).toInt))
//    .foreach(println)
  calendarSource.close()

//  println(routeList)
//  println(tripList)
//  println(calendarList)
  println(tripList.length)
  println(routeList.length)
  println(calendarList.length)

  val tripRouteList = new TripRoute[Trip, Route](trip => trip.route_id.toString)(route => route.route_id.toString).join(tripList, routeList)

//  println(tripRouteList)
  println(tripRouteList.length)

  val enrichedTripList = new EnrichedTrip[Calendar, JoinClass]((calendar, routetrip) => calendar.service_id == routetrip.left.asInstanceOf[Trip].service_id).join(calendarList, tripRouteList)

//  println(enrichedTripList)
  println(enrichedTripList.length)

  val outputFile = new BufferedWriter(new FileWriter("C:\\Users\\iurie\\OneDrive\\A_MCIT\\04.20 - 420-MD2-UM\\STM PROJECT\\EnrichedTrip2.csv"))
  val header: String = "route_id,service_id,trip_id,trip_headsign,direction_id,shape_id,wheelchair_accessible,note_fr,note_en,route_id,agency_id,route_short_name,route_long_name,route_type,route_url,route_color,route_text_color,service_id,monday,tuesday,wednesday,thursday,friday,saturday,sunday,start_date,end_date"
  val output = enrichedTripList
    .map(print => {
      val t = Trip.toCsv(print.right.getOrElse(" ").asInstanceOf[JoinClass].left.asInstanceOf[Trip])
      val r = Route.toCsv(print.right.getOrElse(" ").asInstanceOf[JoinClass].right.getOrElse(" ").asInstanceOf[Route])
      val c = Calendar.toCsv(print.left.asInstanceOf[Calendar])
      t + "," + r + "," + c
    })

  outputFile.write(header)
  for (line <- output) {
    outputFile.newLine()
    outputFile.write(line)
  }
  outputFile.close()

}
