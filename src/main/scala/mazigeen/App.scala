package mazigeen

import org.scalajs.dom
import org.scalajs.jquery.jQuery

import scala.scalajs.js.annotation.JSExport

case class Point(x: Int, y: Int)

class XN() extends Node[XN, XE] {
    override def edges: Seq[XE] = Seq.empty
}
class XE extends Edge[XN, XE] {
    override def A: XN = ???
    override def B: XN = ???
    override def weight: Double = 0.0
}

object UI {
    val WORLD_SIZE = Point(10, 10)
    val ROOM_SIZE = 10
    val DOOR_SIZE = 2
    var handle: Option[Int] = None

    def toPixel(v: Int): Int = v * ROOM_SIZE + (v + 1) * DOOR_SIZE
    def toPixel(p: Point): Point = Point(toPixel(p.x), toPixel(p.y))

    def restart() = {
        handle.foreach(dom.window.clearInterval)
        handle = Some(dom.window.setInterval(() => step(), 1000/60))
    }

    def step(): Unit = {
        println("step")
    }

    def initialize(): Unit = {
        val size = toPixel(WORLD_SIZE)
        val (width, height) = (size.x, size.y)
        val view = s"0 0 $width $height"

        jQuery("#canvas")
                .attr("width", width).attr("height", height)
                .attr("viewbox", view).attr("viewport", view)

        jQuery("#restart").click((e: Any) => restart())
    }
}

@JSExport
object App {
    @JSExport
    def run(): Unit = {
        UI.initialize()
    }
}
