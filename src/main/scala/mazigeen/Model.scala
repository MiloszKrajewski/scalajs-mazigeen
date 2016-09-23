package mazigeen

import scala.collection.{mutable => c}

case class Point(x: Int, y: Int)

class Room(val position: Point) extends Node[Exit] {
    val edges = c.ArrayBuffer.empty[Exit]

    def add(other: Room, weight: Double) = {
        val exit = new Exit(this, other, weight)
        edges += exit
        other.edges += exit
    }
}

class Exit(val A: Room, val B: Room, val weight: Double) extends Edge[Room]

class Model(size: Point, random: () => Double) {
    private val rooms = Array.tabulate(size.x, size.y) { (x, y) => new Room(Point(x, y)) }

    private def getAt(x: Int, y: Int): Room = rooms(x)(y)
    private def tryAt(x: Int, y: Int): Option[Room] =
        if (x < 0 || y < 0 || x >= size.x || y >= size.y) None else Some(getAt(x, y))

    private def connect(room: Room, other: Option[Room], weight: Double) =
        other.foreach { room.add(_, weight) }

    for (x <- 0 until size.x; y <- 0 until size.y) {
        connect(getAt(x, y), tryAt(x - 1, y), random())
        connect(getAt(x, y), tryAt(x, y - 1), random())
    }

    def room00 = rooms(0)(0)
}