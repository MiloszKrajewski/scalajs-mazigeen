package mazigeen

import scala.collection.{mutable => c}

trait Node[N, E] {
    def edges: Seq[E]
}

trait Edge[N, E] {
    def A: N
    def B: N
    def weight: Double
}

class Prim[N <: Node[N, E], E <: Edge[N, E]](val node: N) {
    val ordering: Ordering[E] = Ordering.by(e => -e.weight)
    val queue = c.PriorityQueue.empty(ordering)
    val visited = c.HashSet.empty[N]

    queue.enqueue(node.edges:_*)
    visited.add(node)

    def next(): Option[E] = {
        if (queue.isEmpty) {
            None
        } else {
            val edge = queue.dequeue()
            val valid = visited.contains(edge.A) != visited.contains(edge.B)
            if (!valid) {
                next()
            } else {
                visited.add(edge.A)
                visited.add(edge.B)
                Some(edge)
            }
        }
    }
}
