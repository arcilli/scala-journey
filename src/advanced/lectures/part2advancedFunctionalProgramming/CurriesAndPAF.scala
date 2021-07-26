package advanced.lectures.part2advancedFunctionalProgramming

object CurriesAndPAF extends App{
  // curried functions
  val superAdder: Int => Int => Int =
    x => y => x+y

  val add3 = superAdder(3) // Int => Int = y => 3+y
  println(add3(5))

  println(superAdder(3)(5)) // curried function

  // curried method
  // Method
  def curriedAdder(x: Int)(y: Int): Int = x+y

  // We converted a method into a function value of type Int => Int
  val add4: Int => Int = curriedAdder(4)

  // lifting = transforming a method to a function
  // lifting = ETA-EXPANSION

  // functions != methods (JVM limitation)

  def inc(x: Int) = x+1
  List(1, 2, 3).map(inc) // the compiler does ETA-EXPANSION

  // the compiler rewrite the line above as:
  List(1, 2, 3).map(x => inc(x))

  // Partial function applications
  val add5 = curriedAdder(5) _ // Hey compiler, do an ETA-Expansion and convert this to a function Int => Int

  // Exercise
  val simpleAddFunction = (x: Int, y: Int) => x+y
  def simpleAddMethod(x: Int, y: Int) = x+y
  def curriedAddMethod(x: Int)(y: Int) = x+y

  // define an add7 : Int => Int = y => 7+y
  // as many different implementations of add7 using the above

  val add7_v1 = (x: Int) => simpleAddFunction(7, x)
  val add7_v2 = (x: Int) => simpleAddMethod(7, x)
  val add7_v3 = (x: Int) => curriedAddMethod(7)(x)

  val add7_v1_2 = simpleAddFunction.curried(7)
  val add7_v4 = curriedAddMethod(7) _ // Partially applied function, forces the compiler to do ETA
  val add7_5 = curriedAddMethod(7)(_) // PAF = alternative syntax to the previous one

  val add7_6 = simpleAddMethod(7, _: Int)  // Alternative syntax for turning methods into function values
              // the compiler rewrites it as y => simpleAddMethod(7, y)

  val add7_7  = simpleAddFunction(7, _ : Int)

  // underscores are powerful
  def concatenator(a: String, b: String, c: String) = a + b + c
  val insertName = concatenator("Hello, I'm ", _: String, ", how are u?") // ETA-Expansion will convert it to x: String => concatenator(hello, x, howareu)
  println(insertName("Sile"))

  val fillInTheBlanks = concatenator("Hello, ", _: String, _: String) // (x, y) => concatenator(hello, x, y)

  println(fillInTheBlanks("Sile ", "dă banii"))

  /**
   * Exercises
   * 1. Process a lsit of numbers and return their string representations with diff formats
   * Use the %4.2f, %8.6f and %14.12f with a curried formatter function
   */
  println("%4.2f".format(Math.PI))
  def curriedFormatter(s: String)(number: Double): String = s.format(number)
  val numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)

  val simpleFormat = curriedFormatter("%4.2f") _ // lift this method into a function value
  val seriousFormat = curriedFormatter("%8.6f") _
  val preciseFormat = curriedFormatter("%14.12f") _

  println(numbers.map(simpleFormat))
  println(numbers.map(preciseFormat))
  println(numbers.map(seriousFormat))

  println(numbers.map(curriedFormatter("%12.4f"))) // compiler does sweet ETA-expansion for us
  /**
   * 2. diffeence between:
   *  - functions VS methods
   *  - parameters: by-name VS 0-lambda
   *
   */
  def byName(n: => Int) = n+1
  def byFunction(f: () => Int) = f() + 1

  def method: Int = 42
  def parenMethod(): Int = 42

  /**
   * calling byName and byFunction with the following:
   * - a int
   * - a method (method)
   * - parenMethod
   * -lambda
   * -PartiallyAppliedFunction
   */
  byName(23)  // ok
  byName(method)  //ok
  byName(parenMethod())
  byName(parenMethod) // ok but be aware ===> byName(parenMethod()), the parenMethod is called

//  byName(() => 42) // not ok
    byName((() => 42)()) // ok, the lambda is called
//  byName(parenMethod _) // not ok, this is a function value

//  byFunction(45) // not ok
//  byFunction(method) // not ok!!!!!, method here is evaluated to its value, 42, the method `method` is actually called
                      // the compiler does not do ETA-expansion
  byFunction(parenMethod)
  byFunction(() => 46) // works
  byFunction(parenMethod _) // also works
}
