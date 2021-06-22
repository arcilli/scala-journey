package beginners.lectures.part3fp

object HOFsAndCurries extends App {
  val superFunction: (Int, (String, (Int => Boolean)) => Int) => (Int => Int) = null
  // higher order function (HOF): functions that either takes functions as parameter, either returns function

  // eg. of hof: map, flatMap, filter in MyList

  // function that applies a function n times over a value x
  // n'times(function-f, n-number-of-applications, x)
  //ntimes(f, 3, x) = f(f(f(x)))
  val plusOne = (x: Int) => x + 1
  val plus10 = nTimesBetter(plusOne, 10)

  println(nTimes(plusOne, 10, 1))
  // curried functions
  // this function receives an Int and returns another function (the last one is receiving an Int and returns an Int)
  val superAdder: Int => (Int => Int) = (x: Int) => (y: Int) => x + y
  val add3 = superAdder(3) // y = > 3+Y
  println(plus10(1))
  val standardFormat: (Double => String) = curriedFormatter("%4.2f")
  val preciseFormat: (Double => String) = curriedFormatter("%10.8f")
  println(add3(10))

  def nTimes(f: Int => Int, n: Int, x: Int): Int =
    if (n <= 0) x
    else
      nTimes(f, n - 1, f(x))

  // ntimesBetter(f, n) = x => f(f(f...(x)))
  // increment10 = nTimesBetter(plusOne, 10) = x => plusOne(plusOne...(x))
  // val y = increment10(1)
  def nTimesBetter(f: Int => Int, n: Int): (Int => Int) =
    if (n <= 0) (x: Int) => x
    else (x: Int) => nTimesBetter(f, n - 1)(f(x))

  // functions with multiple parameter lists
  def curriedFormatter(c: String)(x: Double): String = c.format(x)

  println(standardFormat(Math.PI))
  println(preciseFormat(Math.PI))

  /*
    1. Expand MyList with:
      - foreach method: receive a function from A => Unit
      - sort function: receive a sorting function ((A, A) => Int)
      - zipWith (list, (A, A) => B) => MyList[B]
        [1, 2, 3].zipWith([4,5,6], x*y) => [1*4, 2*5, 3*6]

      - fold(start)(function) => a value
        [1, 2, 3].fold(0)(x +y) = 6
        -- use 0 as an initial accumulator

    2.  toCurry(f: (Int, Int) => Int) => (Int => Int => Int)
        fromCurry(f: (Int => Int => Int)) => (Int, Int) => Int

    3.  compose(f, g) => x => f(g(x))
        andThen(f, g) => x => g(f(x))
   */

  def toCurry(f: (Int, Int) => Int): (Int => Int => Int) =
    x => y => f(x, y)

  def fromCurry(f: (Int => Int => Int)): (Int, Int) => Int =
    (x, y) => f(x)(y)

  def compose[A, B, T](f: A => B, g: T => A): T => B =
    x => f(g(x))

  def andThen[A, B, T](f: A => B, g: B => T): A => T =
    x => g(f(x))

  def supperAdder2: (Int => Int => Int) = toCurry(_ + _)

  def add4 = supperAdder2(4)

  println(add4(17))

  def simpleAdder = fromCurry(superAdder)
  println(simpleAdder(4, 17))

  val add2 = (x: Int) => x+2

  val times3 = (x: Int) => x *3

  val composed = compose(add2, times3)
  val ordered = andThen(add2, times3)

  println(composed(4))
  println(ordered(4))
}
