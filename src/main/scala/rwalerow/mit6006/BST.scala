package rwalerow.mit6006

/**
  * Created by robert on 03.11.16.
  */
object BST {

  sealed trait BSTNode {

    var parent: Option[BSTNode] = None

    def key: Int
    def left: BSTNode
    def right: BSTNode
    def isEmpty: Boolean
    def setLeft(bSTNode: BSTNode): Unit = ()
    def setRight(bSTNode: BSTNode): Unit = ()
    def setKey(k: Int): Unit
  }

  case class Node(var left: BSTNode, var right: BSTNode, var key: Int) extends BSTNode {
    override def isEmpty: Boolean = false
    override def setLeft(bSTNode: BSTNode): Unit = left = bSTNode
    override def setRight(bSTNode: BSTNode): Unit = right = bSTNode
    override def setKey(k: Int): Unit = key = k
  }

  case class Empty() extends BSTNode {

    override def left: BSTNode = this
    override def right: BSTNode = this
    override def key: Int = Int.MaxValue
    override def isEmpty: Boolean = true
    override def setKey(k: Int): Unit = ()
  }

  def newTree(): BSTNode = Empty()

  def min(root: BSTNode): BSTNode = {
    var curr: BSTNode = root
    while(!curr.left.isEmpty){
      curr = curr.left
    }
    curr
  }

  def max(root: BSTNode): BSTNode = {
    var curr: BSTNode = root
    while(!curr.left.isEmpty){
      curr = curr.right
    }
    curr
  }

  def find(root: BSTNode, key: Int): Option[BSTNode] = {
    if(root.isEmpty) None
    else if(root.key == key) Some(root)
    else if(root.key > key) find(root.left, key)
    else find(root.right, key)
  }

  def insert(root: BSTNode, key: Int): BSTNode = {
    var candidate = root
    var returnValue = root

    while(!candidate.isEmpty && !(candidate.key == key)){
      if(candidate.key < key) candidate = candidate.right
      else candidate = candidate.left
    }

    if(candidate.isEmpty){
      if(candidate.parent.isEmpty){
        returnValue = Node(Empty(), Empty(), key)

        returnValue.left.parent = Some(returnValue)
        returnValue.right.parent = Some(returnValue)
      } else {
        val candidateParent = candidate.parent.get
        val newInsert = Node(Empty(), Empty(), key)

        newInsert.left.parent = Some(newInsert)
        newInsert.right.parent = Some(newInsert)
        newInsert.parent = candidate.parent

        if (candidateParent.left eq candidate) {
          candidateParent.setLeft(newInsert)
        } else {
          candidateParent.setRight(newInsert)
        }
      }
    }

    returnValue
  }

  def delete(node: BSTNode): Unit = {
    if(node.left.isEmpty && node.right.isEmpty){
      node.parent.foreach(p => {
        val newEmpty = Empty()
        newEmpty.parent = Some(p)

        if(p.left eq node) p.setLeft(newEmpty)
        else p.setRight(newEmpty)
      })
    } else if(node.left.isEmpty) {
      node.parent.foreach { p =>
        if (p.left eq node) p.setLeft(node.right)
        else p.setRight(node.right)
      }
      node.right.parent = node.parent
    } else if(node.right.isEmpty) {
      node.parent.foreach(p =>
        if (p.left eq node) p.setLeft(node.left)
        else p.setRight(node.left)
      )
    } else {
      val successor = findNextLarger(node, node.key)
      node.setKey(successor.getOrElse(Empty()).key)
      delete(successor.get)
    }
  }

  def findNextLarger(root: BSTNode, key: Int): Option[BSTNode] = findNextGeneral(_.right, min)(root, key)
  def findNextSmaller(root: BSTNode, key: Int): Option[BSTNode] = findNextGeneral(_.left, max)(root, key)

  def findNextGeneral(moveUp: BSTNode => BSTNode, moveDown: BSTNode => BSTNode)(root: BSTNode, key: Int): Option[BSTNode] = {
    val startO = find(root, key)

    if(startO.isEmpty) return None

    if(startO.exists(!moveUp(_).isEmpty)) {
      startO.map(moveDown compose moveUp)
    } else {
      var current = startO

      while(current.isDefined && current.flatMap(_.parent).isDefined && current.flatMap(_.parent).exists(moveUp(_) eq current.get)){
        current = current.flatMap(_.parent)
      }

      if(current.isEmpty || current.flatMap(_.parent).isEmpty) None
      else current.flatMap(_.parent)
    }
  }
}
