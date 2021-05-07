package lectures.part3fp

object AnonymousFunctions extends App {
  val doubler = new Function1[Int, Int] {
    override def apply(v1: Int): Int = v1 * 2
  }

  // anonymous function (LAMBDA), from lambda calculation
  val differentDoubler = (x: Int) => x * 2

  // multiple params in a lambda
  val adder: (Int, Int) => Int = (a: Int, b: Int) => a + b

  // no params
  val justDoSomething = () => 3

  // careful: the function itself VS the actual call
  println(justDoSomething)
  println(justDoSomething())

  // curly braces with lambdas
  val stringToInt = { (str: String) =>
    str.toInt
  }

  // more syntactic sugar
  val niceIncrementer: Int => Int = _ + 1 // equivalent to x => x+1

  //  val niceAdder: (Int, Int) => Int = (a, b) => a+b
  val niceAdder: (Int, Int) => Int = _ + _ // equivalent to (a, b) => a+b,

  /*
    1. MyList: replace all functionX calls with lambda
    2. Rewrite the "special" adder (the curried one) as an anonymous function
   */
}
