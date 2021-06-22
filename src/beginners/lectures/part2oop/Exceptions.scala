package beginners.lectures.part2oop

object Exceptions extends App {
  val x: String = null
  //  println(x.length)

  // throwing & catching exceptions

//  val aWeirdValue: String = throw new NullPointerException

  // 1. throwable classes extend the Throwable class
  // Exception & Error are the major Throwable subtypes

  // Diff between exceptions & errors:
  // Both exceptions & errors will crash the JVM
  // Exception: denotes something that went wrong with the program (eg. a npe)
  // Error: something that went wrong with the system (ex. a stackoverflow)

  // 2. catching exceptions
  def getInt(withExceptions: Boolean): Int =
    if (withExceptions) throw new RuntimeException("No int for u")
    else 42

  val potentialFail = try {
    // code that might fail
    getInt(false)
  } catch {
    case e: NullPointerException  => println("caught a Runtime exception")
  } finally {
    // finally block is optional
    // & does not influence the return type of this expression
    // use finally only for side effects

    // code that will get executed NO MATTER WHAT
    println("finally")
  }

  // 3. how to define your own exceptions

  class MyException extends Exception
  val exception = new MyException

//  throw exception

  /*
    1. Crash the program with an OutOfMemoryError (allocate more memory than the JVM has)
    2. Crash with a SOError
    3. PocketCalculator
        - add(x,y)
        - multiply(x, y)
        - subtract(x, y)
        - divide(x,y)

        Throw a custom exception if something wrong is happening.
          - OverflowException if add(x, y) exceeds Int.MAX_VALUE
          - UnderFlowException if subtract(x,y) exceeds INT.MIN_VALUE
          - MathCalculationException for divisiong by 0
   */

//  throw new OutOfMemoryError("here")

//  throw new StackOverflowError("stack overflow")

//  val array = Array.ofDim(Int.MaxValue)

  def infinite: Int = 1 + infinite
  infinite
  class OverflowException extends RuntimeException
  class UnderflowException extends RuntimeException
  class MathCalculationException extends RuntimeException("Division by 0")

  def add(x:Int, y: Int) = {
    val result = x+y
    if (x>0 && y >0 && result < 0) throw new OverflowException
    else if (x<0 && y<0 && result >0) throw new UnderflowException
    result
  }

  def divide(x: Int, y:Int) = {
    if (y == 0) throw new MathCalculationException
    else x/y
  }
}
