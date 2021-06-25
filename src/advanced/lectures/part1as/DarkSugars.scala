package advanced.lectures.part1as

import scala.util.Try

class DarkSugars extends App {
  val description = singleArgMethod {
    // write some code
    42
  }
  // the apply method from Try
  val aTryInstance = Try { // java's try {}
    throw new RuntimeException
  }

  // syntax sugar #1: methods with single param
  def singleArgMethod(arg: Int): String = s"$arg little ducks..."

  List(1, 2, 3).map { x =>
    x + 1
  }

  // syntax sugar #2: single abstract method pattern
  trait Action{
    def act(x: Int): Int
  }

  val anInstance: Action = new Action{
    override def act(x: Int): Int = x+1
  }

  // another way:
  val aFunkyInstance:Action = (x: Int) => x+1 // it does all the magic

  // example: Runnables
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Hello, Scala, my darling")
  })

  val aSweeterThread = new Thread(() => println("Sweeeet Scala"))

  abstract class AnAbstractType {
    def implemented: Int = 23
    def f(a: Int): Unit
  }

  val anAbstractInstace: AnAbstractType = (a: Int) => println("sweeet")

  // syntax sugar #3: the :: and #:: methods are special
  val prependedList = 2 :: List(3, 4) // 2.::(List(3,4))
  // the compiler refactor it to: List(3,4).::(2) ??!?WTF

  // scala specification: last character decides the associativity of a method
  val smth = 1 ::2 :: 3 :: List(4,5) // transforms to:
  List(4,5).::(3).::(2).::(1) //equivalent

  // if the method is ending with a `:`, it means that it's right associative
  class MyStream[T] {
    def -->:(value: T): MyStream[T] = this //actual implementation here
  }
  val myStream = 1 -->:2 -->: 3 -->: new MyStream[Int]

}
