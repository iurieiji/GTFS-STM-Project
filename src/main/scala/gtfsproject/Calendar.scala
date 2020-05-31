package gtfsproject

case class Calendar(
                     service_id: String,
                     monday: Int,
                     tuesday: Int,
                     wednesday: Int,
                     thursday: Int,
                     friday: Int,
                     saturday: Int,
                     sunday: Int,
                     start_date: Int,
                     end_date: Int
                   )

object Calendar {
  def apply(csvLine: String): Calendar = {
    val c: Array[String] = csvLine.split(",",-1)
    new Calendar(c(0), c(1).toInt, c(2).toInt, c(3).toInt, c(4).toInt, c(5).toInt, c(6).toInt, c(7).toInt, c(8).toInt, c(9).toInt)
  }

  def toCsv(calendar: Calendar): String = {
    calendar.service_id + "," +
    calendar.monday + "," +
    calendar.tuesday + "," +
    calendar.wednesday + "," +
    calendar.thursday + "," +
    calendar.friday + "," +
    calendar.saturday + "," +
    calendar.sunday + "," +
    calendar.start_date + "," +
    calendar.end_date
  }

}