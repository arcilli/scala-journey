package beginners.lectures.part1basics

object Functions extends App {
  def aFunction(a: String, b: Int): String = {
    a + " " + b
  }

  def aParameterlessFunction(): Int = 42

  println(aParameterlessFunction())
  println(aParameterlessFunction)

  def aRepeatedFunction(aString: String, n: Int): String = {
    if (n == 1) aString
    else aString + " " + aRepeatedFunction(aString, n - 1)
  }

  println(aRepeatedFunction("hello", 3))

  // WHEN YOU NEED LOOPS, USE RECURSION. :-)

  def aFunctionWithSideEffects(aString: String): Unit = println(aString)

  def aBigFunction(n: Int): Int = {
    def aSmallerFunction(a: Int, b: Int): Int = a + b

    aSmallerFunction(n, n - 1)
  }

  /*
    1. A greeting function (name, age) => "Hi, my name is $name and I am $age yrs old"
    2. Factorial function
    3. A fibonacci function
    4. Tests if a number is prime
   */

  def greetingFunction(name: String, age: Int): String = ("Hi, my name is $name and I am $age yrs old.")

  println(greetingFunction("Gigel", 32))

  def factorialFunction(n: Int): Int = {
    if (n <= 1) 1
    else n * factorialFunction(n - 1)
  }

  println(factorialFunction(5))

  def fibonacciFunction(n: Int): Int = {
    if (n <= 2) 1
    else fibonacciFunction(n - 1) + fibonacciFunction(n - 2)
  }

  println(fibonacciFunction(8))


  def isPrimeOrNot(n: Int): Boolean = {
    def isPrimeUntil(t: Int): Boolean = {
      if (t <= 1) true
      else n % t != 0 && isPrimeUntil(t - 1)
    }
    isPrimeUntil(n/2)
  }

  println(isPrimeOrNot(13))
  println(isPrimeOrNot(1024))
}
