package rwalerow.cursera.curse2

import scala.collection.mutable.ListBuffer

object Graph {
  case class Graph[K](vertices: ListBuffer[Vertex[K]])
  case class Edge[K](start: Vertex[K], end: Vertex[K])
  class Vertex[K](var key: K, var edges: ListBuffer[Edge[K]], var visited: Boolean = false)

  def extendVertices[K, E <: Vertex[K]](inputGraph: Graph[K], extendFunction: Vertex[K] => E): Graph[K] = {
    Graph(inputGraph.vertices.map(extendFunction))
  }

  def createSpecialGraph[K](verticesList: Iterable[K], edges: Iterable[(K, K)]): Graph[K] = {
    val allVertices = verticesList.to[ListBuffer].map(e => new Vertex(e, ListBuffer[Edge[K]]()))
    for(edge <- edges) {
      val startVertex = allVertices.find(_.key equals edge._1)
      val endVertex = allVertices.find(_.key equals edge._2)

      for {
        start <- startVertex
        end <- endVertex
      } start.edges.append(Edge(start, end))
    }
    Graph(allVertices)
  }

  def createReversedGraph[K](graph: Graph[K]): Graph[K] = {
    val reversedEdges = graph.vertices
      .flatMap(_.edges)
      .map(e => (e.end.key, e.start.key))
    createSpecialGraph[K](graph.vertices.map(_.key), reversedEdges)
  }
}