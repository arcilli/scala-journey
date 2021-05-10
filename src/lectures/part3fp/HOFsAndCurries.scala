package lectures.part3fp

object HOFsAndCurries extends App {
  val superFunction: (Int, (String, (Int => Boolean)) => Int) => (Int => Int) = null
  // higher order function (HOF): functions that either takes functions as parameter, either returns function

  // eg. of hof: map, flatMap, filter in MyList

  // function that applies a function n times over a value x
  // n'times(function-f, n-number-of-applications, x)
  //ntimes(f, 3, x) = f(f(f(x)))
  val plusOne = (x: Int) => x + 1

  def nTimes(f: Int => Int, n: Int, x: Int): Int =
    if (n <= 0) x
    else
      nTimes(f, n - 1, f(x))

  println(nTimes(plusOne, 10, 1))

  // ntimesBetter(f, n) = x => f(f(f...(x)))
  // increment10 = nTimesBetter(plusOne, 10) = x => plusOne(plusOne...(x))
  // val y = increment10(1)
  def nTimesBetter(f: Int => Int, n: Int): (Int => Int) =
    if (n<=0) (x:Int) => x
    else (x: Int) => nTimesBetter(f, n-1)(f(x))

  val plus10 = nTimesBetter(plusOne, 10)
  println(plus10(1))

  // curried functions
  // this function receives an Int and returns another function (the last one is receiving an Int and returns an Int)
  val superAdder : Int => (Int => Int) = (x: Int) => (y: Int) => x+y
  val add3 = superAdder(3) // y = > 3+Y
  println(add3(10))

  // functions with multiple parameter lists
  def curriedFormatter(c: String)(x: Double): String = c.format(x)

  val standardFormat: (Double => String) = curriedFormatter("%4.2f")

  val preciseFormat: (Double => String) = curriedFormatter("%10.8f")

  println(standardFormat(Math.PI))
  println(preciseFormat(Math.PI))
}
