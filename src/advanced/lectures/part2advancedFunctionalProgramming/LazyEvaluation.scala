package advanced.lectures.part2advancedFunctionalProgramming

object LazyEvaluation extends App {

  // lazy DELAYS the evaluation of values
  lazy val x: Int = {
    println("hello")
    42
  }

  println(x)
  println(x)

  // examples of implications:
  // 1. side effects
  def sideEffectCondition: Boolean = {
    println("Boo")
    true
  }

  def simpleCondition: Boolean = false

  // this is only evaluated if needed
  lazy val lazyCondition = sideEffectCondition

  // the println("Boo") from sideEffectCondition is not printed, since its evaluation is no more needed
  println(if (simpleCondition && lazyCondition) "yes" else "no")

  // 2. in conjunction with call by name
  def byNameMethod(n: => Int): Int = {
    // technique named "CALL BY NEED"
    lazy val t = n
    t+t+t+1
  }

  def retrieveMagicValue: Int = {
    // side effect or a long computation
    println("waiting")
    Thread.sleep(1000)
    42
  }
  println(byNameMethod(retrieveMagicValue))
  // use lazy vals

  // filtering with lazy vals
  def lessThan30(i: Int): Boolean = {
    println(s"$i is less than 30?")
    i < 30
  }

  def greaterThan20(i: Int): Boolean = {
    println(s"$i is greater than 20?")
    i > 20
  }

  val numbers = List(1, 25, 40, 5, 23)
  val lt30 = numbers.filter(lessThan30) // List(1, 25, 5, 23)
  val gt20 = lt30.filter(greaterThan20)
  println(gt20)

  val lt30Lazy = numbers.withFilter(lessThan30) // lazy vals under the hood
  val gt20Lazy = numbers.withFilter(greaterThan20)
  println("-------")
  println(gt20Lazy)
  println("-------")
  gt20Lazy.foreach(println)

  // for-comprehensions use withFilter with guards
  // if guards use lazy guards in for-comprehensions
  for {
    a <- List(1, 2, 3) if a%2 ==0
  } yield a+1
  List(1,2,3).withFilter(_ %2 == 0).map(_ +1) // List[Int]

  /**
   * Exercise: implement a lazily evaluated, singly linked STREAM of elements.
   *
   * naturals = MyStream.from(1)(x => x+1) = stream of natural numbers (potentially infinite!)
   * naturals.take(100) // lazily evaluated stream of the first 100 naturals (finite stream)
   * naturals.take(100).foreach(println) -> this will print the first 100
   * naturals.foreach(println) -> will crash - infinite!
   * naturals.map( _ * 2) // stream of all even numbers (potentially infinite)
   */

  abstract class MyStream[+A] {
    def isEmpty: Boolean
    def head: A
    def tail: MyStream[A]

    // TODO: check basic course
    def #::[B >:A](element:B): MyStream[B] // prepands operator
    def ++[B >:A](anotherStream: MyStream[B]): MyStream[B] // concatenate 2 streams

    def foreach(f: A => Unit): Unit
    def map[B](f: A => B): MyStream[B]
    def flatMap[B](f: A=> MyStream[B]): MyStream[B]
    def filter(predicate: A => Boolean): MyStream[A]

    def take(n: Int): MyStream[A] // takes the first n elements out of the stream
    def takeAsList(n: Int): List[A]
  }


  object MyStream {
    def from[A](start: A)(generator: A => A): MyStream[A] = ???
  }
}
