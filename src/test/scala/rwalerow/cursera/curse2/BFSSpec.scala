package rwalerow.cursera.curse2

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by robert on 11.11.16.
  */
class BFSSpec extends FlatSpec with Matchers {

  trait Graph {
    val edges = List((1, 2), (2, 1), (1, 4), (4, 1), (2, 4), (4 ,2),
      (4, 7), (7, 4), (7, 8), (8, 7), (5, 8), (8, 5), (2, 5), (5, 2),
      (2, 3), (3, 2), (3, 6), (6, 3)
    )

    val graph = Graph.createSpecialGraph((1 to 8).toList, edges)

    val edgesEmpty = List((1, 2), (2, 1), (1, 4), (4, 1), (2, 4), (4 ,2),
      (4, 7), (7, 4), (7, 8), (8, 7), (5, 8), (8, 5), (2, 5), (5, 2),
      (2, 3), (3, 2)
    )
    val graphWithEmpty = Graph.createSpecialGraph((1 to 8).toList, edgesEmpty)
  }

  "Depth" should "find rode from 2 to 7" in new Graph {
    val start = graph.vertices.filter(_.key == 2).head
    val end = graph.vertices.filter(_.key == 7).head
    val depth = BFS.depth(start)(end)

    depth shouldBe 2
  }

  it should "find longer path" in new Graph {
    val start = graph.vertices.filter(_.key == 8).head
    val end = graph.vertices.filter(_.key == 6).head
    val depth = BFS.depth(start)(end)

    depth shouldBe 4
  }

  "Conectivity" should "find 2 groups" in new Graph {
    val groups = BFS.conectivity(graphWithEmpty)

    groups.size shouldBe 2
    groups.filter(_.size == 1).head should contain theSameElementsAs List(6)
    groups.filterNot(_.size == 1).head should contain theSameElementsAs List(1,2,3,4,5,7,8)
  }
}
