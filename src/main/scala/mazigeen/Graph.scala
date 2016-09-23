package mazigeen

import scala.collection.{mutable => c}

trait Node[E] {
    def edges: Seq[E]
}

trait Edge[N] {
    def A: N
    def B: N
    def weight: Double
}

class Prim[N <: Node[E], E <: Edge[N]](val node: N) {
    private val ordering: Ordering[E] = Ordering.by(e => -e.weight)
    private val queue = c.PriorityQueue.empty(ordering)
    private val visited = c.HashSet.empty[N]

    visit(node)

    private def visit(node: N) = {
        visited.add(node)
        queue.enqueue(node.edges:_*)
    }

    def next(): Option[E] = {
        if (queue.isEmpty) {
            None
        } else {
            val edge = queue.dequeue()
            (visited.contains(edge.A), visited.contains(edge.B)) match {
                case (true, true) => next()
                case (false, true) => visit(edge.A); Some(edge)
                case (true, false) => visit(edge.B); Some(edge)
                case _ => throw new IllegalArgumentException("Not a valid graph")
            }
        }
    }
}
