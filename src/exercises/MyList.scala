package exercises

//trait MyPredicate[-T] { // it's actually a function type from T to Boolean: T => Boolean
//  def test(value: T): Boolean
//}
//
//trait MyTransformer[-A, B] { // a function type: from A to B: A => B
//  def transform(value: A): B
//}

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

  def map[B](transformer: A => B): MyList[B]

  def flatMap[B](transformer: A => MyList[B]): MyList[B]

  def filter(predicate: A => Boolean): MyList[A]

  def ++[B >: A](list: MyList[B]): MyList[B]

  // hofs
  def forEach(f: A => Unit): Unit

  def sort(compare: (A, A) => Int): MyList[A]

  def zipWith[B, C](list: MyList[B], zip: (A, B) => C): MyList[C]

  def fold[B](start: B)(operator: (B, A) => B): B
}

case object Empty extends MyList[Nothing] {
  // ?? returns nothing, when called, it will thrown an _not implemented error_
  override def head(): Nothing = throw new NoSuchElementException

  override def tail(): Nothing = throw new NoSuchElementException

  override def isEmpty(): Boolean = true

  override def add[B >: Nothing](valueToAdd: B): MyList[B] = new Cons(valueToAdd, Empty)

  override def printElements: String = ""

  override def map[B](transformer: Nothing => B): MyList[B] = Empty

  override def flatMap[B](transformer: Nothing => MyList[B]): MyList[B] = Empty

  override def filter(predicate: Nothing => Boolean): MyList[Nothing] = Empty

  def ++[B >: Nothing](list: MyList[B]): MyList[B] = list

  // hofs
  def forEach(f: Nothing => Unit): Unit = ()

  def sort(compare: (Nothing, Nothing) => Int) = Empty

  def zipWith[B, C](list: MyList[B], zip: (Nothing, B) => C): MyList[C] =
    if (!list.isEmpty()) throw new RuntimeException("The lists do not have the same length")
    else Empty

  override def fold[B](start: B)(operator: (B, Nothing) => B): B = start
}

case class Cons[+A](head: A, tail: MyList[A]) extends MyList[A] {
  override def isEmpty(): Boolean = false

  override def add[B >: A](valueToAdd: B): MyList[B] = new Cons(valueToAdd, this)

  override def printElements: String = {
    if (tail.isEmpty()) "" + head
    else head + " " + tail.printElements
  }

  def map[B](transformer: A => B): MyList[B] = {
    new Cons(transformer(head), tail.map(transformer))
  }

  //  def flatMap[B](transformer: MyTransformer[A, MyList[B]]): MyList[B]

  def filter(predicate: A => Boolean): MyList[A] =
    if (predicate(head)) new Cons(head, tail.filter(predicate))
    else tail.filter(predicate)

  /*
    [1, 2] || [3, 4, 5]
    = new Cons (1, [2], ++ [3, 4, 5])
    = new Cons(1, new Const(2, Empty ++ [3, 4, 5])
    = new Cons(1, new Const(2, new Cons(3, new Cons(4, new Cons(5))))
   */
  def ++[B >: A](list: MyList[B]): MyList[B] = new Cons(head, tail ++ list)


  /*
    [1, 2].flatMap(n=> [n, n+1])
    = [1, 2] ++ [2].flatMap(n => [n, n+1])
    = [1, 2] || [2, 3] || Empty.flatMap(n => [n, n+1])
    = [1, 2] ++ [2, 3, ] ++ Empty
    = [1, 2, 2, 3]
   */
  override def flatMap[B](transformer: Function1[A, MyList[B]]): MyList[B] =
    transformer(head) ++ tail.flatMap(transformer)

  // hofs
  def forEach(f: A => Unit): Unit = {
    f(head)
    tail.forEach(f)
  }

  def sort(compare: (A, A) => Int): MyList[A] = {
    def insert(x: A, sortedList: MyList[A]): MyList[A] =
      if (sortedList.isEmpty()) Cons(x, Empty)
      else if (compare(x, sortedList.head()) < 0) Cons(x, sortedList)
      else Cons(sortedList.head(), insert(x, sortedList.tail()))

    val sortedTail = tail.sort(compare)
    insert(head, sortedTail)
  }

  override def zipWith[B, C](list: MyList[B], zip: (A, B) => C): MyList[C] = {
    if (list.isEmpty()) throw new RuntimeException("The lists do not have the same length.")
    else Cons(zip(this.head, list.head()), tail.zipWith(list.tail(), zip))
  }

  /*
    [1, 2, 3].fold(0)(+)
    = [2, 3].fold(0+1)(+)=
    = [3].fold(2+1)(+) =
    = [].fold(3+3)(+) = 6
   */
  def fold[B](start: B)(operator: (B, A) => B): B = {
    tail.fold(operator(start, head))(operator)
  }
}

object ListTest extends App {
  val listOfIntegers: MyList[Int] = new Cons(1, new Cons(2, new Cons(3, Empty)))
  val cloneOfListOfIntegers: MyList[Int] = new Cons(1, new Cons(2, new Cons(3, Empty)))
  val listOfStrings: MyList[String] = new Cons("Hello", new Cons("Scala", Empty))

  println("List with transformer: ")
  println(listOfIntegers.map(elem => elem * 2).toString)
  // or:
  println(listOfIntegers.map(_ * 2).toString)

  println("List with filter: ")
  println(listOfIntegers.filter(x => x % 2 == 0).toString)

  val anotherListOfIntegers = new Cons(1, new Cons(4, new Cons(5, Empty)))
  println(listOfIntegers ++ anotherListOfIntegers)

  println(s"List of integers before flatMapping: ${listOfIntegers.toString}")
  println("Flat mapping")

  // here the underscore does not work, since it's not possible to use the _ multiple times
  println(listOfIntegers.flatMap(x => Cons(x, Cons(x + 1, Empty))).toString)

  println(listOfIntegers)
  println(listOfStrings)

  println(cloneOfListOfIntegers == listOfIntegers)

  val superAdd = (x: Int) => (y: Int) => x + y
  println(superAdd(4)(5))

  // hofs
  println("Hofs: ")
  listOfIntegers.forEach(println)

  println(listOfIntegers.sort((x, y) => y - x))

  val simpleListOfIntegers = Cons(1, new Cons(2, Empty))
  println(simpleListOfIntegers.zipWith[String, String](listOfStrings, _ + "-" + _))


  println(listOfIntegers.fold(0)(_ + _))

  // for comprehensions
  println(for {
    n <- listOfIntegers
    string <- listOfStrings
  } yield n + "-" + string
  )
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