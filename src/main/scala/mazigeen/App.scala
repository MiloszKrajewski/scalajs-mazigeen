package mazigeen

import org.scalajs.dom.{window, document, CanvasRenderingContext2D}
import org.scalajs.dom.html.Canvas
import org.scalajs.jquery.jQuery

import scala.scalajs.js.annotation.JSExport
import scala.util.Random

object UI {
    val WORLD_SIZE = Point(100, 100)
    val ROOM_SIZE = 6
    val DOOR_SIZE = 1
    var handle: Option[Int] = None

    def toPixel(v: Int): Int = v * ROOM_SIZE + (v + 1) * DOOR_SIZE

    def toPixel(p: Point): Point = Point(toPixel(p.x), toPixel(p.y))

    def shutdown() = {
        handle.foreach(window.clearInterval)
        handle = None
    }

    def restart(context: CanvasRenderingContext2D, width: Int, height: Int) = {
        shutdown()

        val model = new Model(WORLD_SIZE, Random.nextDouble)
        val algorithm = new Prim[Room, Exit](model.room00)

        context.clearRect(0, 0, width, height)
        context.lineWidth = ROOM_SIZE / 2
        context.strokeStyle = "#fff"

        handle = Some(window.setInterval(() => step(algorithm, context), 0))
    }

    def step(algorithm: Prim[Room, Exit], context: CanvasRenderingContext2D) = {
        algorithm.next() match {
            case None => shutdown()
            case Some(exit) =>
                val a = toPixel(exit.A.position)
                val b = toPixel(exit.B.position)
                val o = ROOM_SIZE / 2
                context.beginPath()
                context.moveTo(a.x + o, a.y + o)
                context.lineTo(b.x + o, b.y + o)
                context.closePath()
                context.stroke()
        }
    }

    def initialize() = {
        val size = toPixel(WORLD_SIZE)
        val (width, height) = (size.x, size.y)
        val view = s"0 0 $width $height"

        jQuery("#canvas")
                .attr("width", width).attr("height", height)
                .attr("viewbox", view).attr("viewport", view)

        val context =
            document.getElementById("canvas").asInstanceOf[Canvas]
                    .getContext("2d").asInstanceOf[CanvasRenderingContext2D]

        jQuery("#restart").click((e: Any) => restart(context, width, height))
    }
}

@JSExport
object App {
    @JSExport
    def run() = UI.initialize()
}
