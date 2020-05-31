package gtfsproject

class TripRoute[Left,Right] (val join1: Left => String) (val join2: Right => String) extends Join[Left, Right, JoinClass] {
  override def join(a: List[Left], b: List[Right]): List[JoinClass] = {
    val l: Map[String, Right] = b.map(b => join2(b) -> b).toMap
    a.filter(a => l.contains(join1(a))).map(a => JoinClass(a, Some(l(join1(a)))))
  }
}
