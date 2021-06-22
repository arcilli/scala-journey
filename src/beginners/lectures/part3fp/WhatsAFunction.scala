package beginners.lectures.part3fp

object WhatsAFunction extends App {
  //Dream: use functions as first class elements -> work with functions as we would work with values
  //Problem: OOP

  val doubler = new MyFunction[Int, Int] {
    override def apply(element: Int): Int = element * 2;
  }

  // doubler acts as a function
  println(doubler(2))

  // function types = Function1[A, B]
  // Scala supports this kind of functions up to 22 parameters
  val stringToIntConverter = new Function1[String, Int] {
    override def apply(v1: String): Int = v1.toInt
  }

  println(stringToIntConverter("3") + 4)

  val adder: ((Int, Int) => Int) = new Function2[Int, Int, Int] {
    override def apply(v1: Int, v2: Int): Int = v1 + v2
  }

  // Function types Function2[A, B, R] === (A,B) => R

  // All Scala functions are objects or instances of classes deriving from function1, function2....

  /*
    1. a function which takes 2 strings and concatenates them
    2. transform the MyPredicate & MyTransformer into function types
    3. define a function  whciih takes an int arg and returns another function which takes an int and returns an int
        - what's the type of this function
        - how to actually implement this special function
   */

  val concatenate = new Function2[String, String, String] {
    override def apply(v1: String, v2: String): String = v1 + v2
  }

  println(concatenate("Gigel", "Costel"))
  val adder3 = specialFunction(3)

  //3.
  def specialFunction: Function1[Int, Function1[Int, Int]] = new Function1[Int, Function1[Int, Int]] {
    override def apply(x: Int): Function1[Int, Int] = new Function1[Int, Int] {
      override def apply(y: Int): Int = x + y
    }
  }
  println(adder3)
  println(adder3(4))
  println(specialFunction(4)(25)) // curried function

  // a curried function receive some kind of a parameter and returns a function
}

trait MyFunction[A, B] {
  def apply(element: A): B = ???
}