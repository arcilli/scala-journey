package exercises


// This list should be parametrized
abstract class MyList {
  /*
    head = the first element of the list
    tail = remainder of the list
    isEmpty => boolean
    add(int) => new list with this new element added
    override toString => a string representation of the list
   */
  def head(): Int

  def tail(): MyList

  def isEmpty(): Boolean

  def add(valueToAdd: Int): MyList

  override def toString: String = "[" + printElements + "]"

  def printElements: String
  //  override protected def toString: String = super.toString
}

object Empty extends MyList {
  // ?? returns nothing, when called, it will thrown an _not implemented error_
  override def head(): Int = throw new NoSuchElementException

  override def tail(): MyList = throw new NoSuchElementException

  override def isEmpty(): Boolean = true

  override def add(valueToAdd: Int): MyList = new Cons(valueToAdd, Empty)

  override def printElements: String = ""
}

class Cons(head: Int, tail: MyList) extends MyList {
  override def head(): Int = head

  override def tail(): MyList = tail

  override def isEmpty(): Boolean = false

  override def add(valueToAdd: Int): MyList = new Cons(valueToAdd, this)

  override def printElements: String = {
    if (tail.isEmpty()) "" + head
    else head + " " + tail.printElements
  }
}

object ListTest extends App {
  val list = new Cons(1, new Cons(2, new Cons(3, Empty)))
  println(list.tail().head())
  println(list.add(4).head())
  println(list.isEmpty())

  println(list.toString)
}


//class MyListImp extends MyList {
//  val someList = new util.ArrayList[Int]()
//
//  override protected def head(): Int = someList.get(0)
//
//  override protected def tail(): Int = someList.get(someList.size()-1)
//
//  override protected def isEmpty(): Boolean = someList.isEmpty
//
//  override protected def add(valueToAdd: Int): MyList = someList.add(valueToAdd)
//}