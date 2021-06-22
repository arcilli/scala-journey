package beginners.lectures.part1basics

import scala.annotation.tailrec

object Recursion extends App {
  val concatenatedString = concatenateStringMultipleTimes("bye ", 3, "")

  println(factorial(5))

  def factorial(n: Int): BigInt = {
    if (n <= 1) 1
    else {
      println("Computing factorial of " + n + " - I need first factorial of " + (n - 1))
      val result = n * factorial(n - 1)
      println("Computed factorial of: " + n)
      result
    }
  }

  // This will not work, unless the factorial is made to be tailrecursive
  //println(factorial(5000))

  println(anotherFactorial(5000))
  /*
    anotherFactorial(10) = factorialHelper(10, 1)
    = factorialHelper(9, 10*1)
    = factorialHelper(8, 9*10)
    = factorialHelper(7, 8*9*10*1)
    ...
    = factorialHelper(2, 3*4*..*10*1)
    = factorialHelper(1, 2*3*...*10*1) = factorialHelper(1, 1*2*3*...*10)
    = accumulator = 1*2*3*..*10
   */

  // When you need loops, use _TAIL_ recursion.

  /*
    1. Concatenate a string n times using tail recursion
    2. IsPrime function tail recursive
    3. Fibonacci function, tail recursive
   */

  def anotherFactorial(n: Int): BigInt = {
    @tailrec // this function should be tailrecursion
    def factorialHelper(x: Int, accumulator: BigInt): BigInt = {
      if (x <= 1) accumulator
      else factorialHelper(x - 1, x * accumulator) // Tail recursion = recursive call as the LAST expression
    }

    factorialHelper(n, 1)
  }

  @tailrec
  def concatenateStringMultipleTimes(toConcatenate: String, nTimes: Int, accumulator: String): String = {
    if (nTimes >= 1) concatenateStringMultipleTimes(toConcatenate, nTimes - 1, accumulator + toConcatenate)
    else accumulator
  }

  println(concatenatedString)

  @tailrec
  def fibonacciTailRecursive(n: Int, acc1: Int, acc2: Int): Int = {
    if (n <= 2) acc1
    else fibonacciTailRecursive(n - 1, acc1 + acc2, acc1)
  }

  println(fibonacciTailRecursive(8, 1, 1))

  @tailrec
  def isPrimeOrNot(n: Int, divisor: Int, partialResult: Boolean): Boolean = {
    if (!partialResult) false
    else if (divisor <= 1) true
    else {
      val local = (n % divisor != 0) && partialResult
      isPrimeOrNot(n, divisor - 1, local)
    }
    /*
    Not really the best solution.
    if (divisor <= 1) partialResult
    else {
      val local = (n % divisor != 0) && partialResult
      isPrimeOrNot(n, divisor - 1, local)
    }
     */
  }

  println(isPrimeOrNot(13, 7, partialResult = true))
  println(isPrimeOrNot(24, 13, partialResult = true))
  println(isPrimeOrNot(2003, 1024, partialResult = true))
}
