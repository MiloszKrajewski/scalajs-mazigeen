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

        context.clearRect(0, 0, width, height)

        val model = new Model(WORLD_SIZE, Random.nextDouble)
        val algorithm = new Prim[Room, Door](model.room00)

        handle = Some(window.setInterval(() => step(algorithm, context), 0))
    }

    def drawRoom(context: CanvasRenderingContext2D, room: Room) = {
        val p = toPixel(room.position)
        context.fillStyle = ROOM_COLOR
        context.fillRect(p.x, p.y, ROOM_SIZE, ROOM_SIZE)
    }

    def drawDoor(context: CanvasRenderingContext2D, door: Door) = {
        val a = toPixel(door.A.position)
        val b = toPixel(door.B.position)
        val o = ROOM_SIZE / 2
        context.strokeStyle = DOOR_COLOR
        context.lineWidth = ROOM_SIZE
        context.beginPath()
        context.moveTo(a.x + o, a.y + o)
        context.lineTo(b.x + o, b.y + o)
        context.closePath()
        context.stroke()
    }

    def step(algorithm: Prim[Room, Door], context: CanvasRenderingContext2D) = {
        algorithm.next() match {
            case None => shutdown()
            case Some(door) => 
                drawDoor(context, door)
                drawRoom(context, door.A)
                drawRoom(context, door.B)
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
