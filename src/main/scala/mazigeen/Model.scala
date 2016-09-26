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

    private def at(x: Int, y: Int): Room = rooms(x)(y)
    private def connect(room: Room, other: Room, weight: Double) = room.add(other, weight)

    for (x <- 0 until size.x; y <- 0 until size.y) {
        if (x > 0) connect(at(x, y), at(x - 1, y), random())
        if (y > 0) connect(at(x, y), at(x, y - 1), random())
    }

    def room00 = rooms(0)(0)
}