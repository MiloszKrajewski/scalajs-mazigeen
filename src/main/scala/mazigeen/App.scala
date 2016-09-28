package mazigeen

import org.scalajs.dom.{window, document, CanvasRenderingContext2D}
import org.scalajs.dom.html.Canvas
import org.scalajs.jquery.jQuery

import scala.scalajs.js.annotation.JSExport
import scala.util.Random

object UI {
    val WORLD_SIZE = Point(100, 100)
    val ROOM_SIZE = 6
    val DOOR_SIZE = 2

    val ROOM_COLOR = "#fff"
    val DOOR_COLOR = "#eee"

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
        context.fillStyle = ROOM_COLOR
        context.strokeStyle = DOOR_COLOR
        context.lineWidth = ROOM_SIZE * 0.9

        handle = Some(window.setInterval(() => step(algorithm, context), 0))
    }

    def drawRoom(context: CanvasRenderingContext2D, room: Room) = {
        val p = toPixel(room.position)
        context.fillRect(p.x, p.y, ROOM_SIZE, ROOM_SIZE)
    }

    def drawExit(context: CanvasRenderingContext2D, exit: Exit) = {
        val a = toPixel(exit.A.position)
        val b = toPixel(exit.B.position)
        val o = ROOM_SIZE / 2
        context.beginPath()
        context.moveTo(a.x + o, a.y + o)
        context.lineTo(b.x + o, b.y + o)
        context.closePath()
        context.stroke()
    }

    def step(algorithm: Prim[Room, Exit], context: CanvasRenderingContext2D) = {
        algorithm.next() match {
            case None => shutdown()
            case Some(exit) => 
                drawExit(context, exit)
                drawRoom(context, exit.A)
                drawRoom(context, exit.B)
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
