package exercises


// This list should be parametrized
abstract class MyList[+A] {
  /*
    head = the first element of the list
    tail = remainder of the list
    isEmpty => boolean
    add(int) => new list with this new element added
    override toString => a string representation of the list
   */
  def head(): A

  def tail(): MyList[A]

  def isEmpty(): Boolean

  def add[B >: A](element: B): MyList[B]

  override def toString: String = "[" + printElements + "]"

  def printElements: String
  //  override protected def toString: String = super.toString
}

object Empty extends MyList[Nothing] {
  // ?? returns nothing, when called, it will thrown an _not implemented error_
  override def head(): Nothing = throw new NoSuchElementException

  override def tail(): Nothing = throw new NoSuchElementException

  override def isEmpty(): Boolean = true

  override def add[B >: Nothing](valueToAdd: B): MyList[B] = new Cons(valueToAdd, Empty)

  override def printElements: String = ""
}

class Cons[+A](head: A, tail: MyList[A]) extends MyList[A] {
  override def head(): A = head

  override def tail(): MyList[A] = tail

  override def isEmpty(): Boolean = false

  override def add[B >: A](valueToAdd: B): MyList[B] = new Cons(valueToAdd, this)

  override def printElements: String = {
    if (tail.isEmpty()) "" + head
    else head + " " + tail.printElements
  }
}

object ListTest extends App {
  val listOfIntegers: MyList[Int] = new Cons(1, new Cons(2, new Cons(3, Empty)))
  val listOfStrings: MyList[String] = new Cons("Hello", new Cons("Scala", Empty))

  println(listOfIntegers)
  println(listOfStrings)
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