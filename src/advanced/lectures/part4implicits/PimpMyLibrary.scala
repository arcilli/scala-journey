package advanced.lectures.part4implicits

import scala.concurrent.duration.DurationInt

object PimpMyLibrary extends App {

  // 2.isPrime

  implicit class RichInt(val value: Int) extends AnyVal {
    def isEven: Boolean = value %2 == 0
    def sqrt: Double = Math.sqrt(value)

    def times(function: () => Unit): Unit = {
      def timesAux(n: Int): Unit = {
        if (n <= 0) ()
        else {
          function()
          timesAux(n-1)
        }
      }

      timesAux(value)
    }

    def *[T](list: List[T]): List[T] = {
      def concatenate(n: Int): List[T] = {
        if (n<=0) List()
        else concatenate(n-1) ++ list
      }
      concatenate(value)
    }
  }

  implicit class RicherInt(richInt: RichInt) {
    def isOdd: Boolean = richInt.value %2 != 0
  }
  new RichInt(42).sqrt

  // type enrichment = pimping
  42.isEven // new RichInt(42).isEven

  // example: implicit conversion
  1 to 10

  import scala.concurrent.duration._
  3.seconds

  // compiles doesn't do multiple implicit searches.
//  42.isOdd

  /*
  Exercises
  1. Enrich the String class
    - asInt
    - encrpyt
      John -> Lnjp

      Keep enriching the Int class
      - times(function)
      3.times(() => ...)
      - multiply
        3 * List(1, 2) => List(1,2, 1,2, 1,2)
   */

  implicit class RichString(string: String) {

    def asInt: Int = Integer.valueOf(string) // java.lang.Integer -> Int
    def encrypt(cypherDistance: Int): String = string.map(c => (c + cypherDistance).asInstanceOf[Char])
  }

  println("3".asInt + 4)
  println("John".encrypt(2))

  3.times(() => println("Scala rocks"))

  println(4 * List(1, 2))

  // "3"/4

  implicit def stringToInt(string: String): Int = Integer.valueOf(string)
  println("6"/2)

  // equivalent: implicit class RichAltInt(value: Int)
  class RichAlternativeInt(value: Int)
  implicit def enrich(value: Int): RichAlternativeInt = new RichAlternativeInt(value)

  // danger zone
  implicit def intToBoolean(i: Int): Boolean = (i == 1)

  /*
    if (n) do something
    else do something else
   */

  val aConditionValue = if(3) "Ok" else "Something wrong"
  println(aConditionValue)
}
