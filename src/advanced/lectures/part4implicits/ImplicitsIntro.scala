package advanced.lectures.part4implicits

object ImplicitsIntro extends App {
  val pair = "Daniel" -> "555"
  val intPar = 1 -> 2

  case class Person(name: String) {
    def greet = s"Hi, my name is $name"
  }

  implicit def fromStringToPerson(str: String): Person = Person(str)

  println("Peter".greet) // println(fromStrinToPerson("Peter").greet)

//  class A {
//    def greet: Int = 2
//  }
//
//  implicit def fromStringToA(str: String): A = new A // This will not work since the compiler is confused on which implicit to use

  // implicit parameters
  def increment(x: Int)(implicit amount: Int): Unit = x + amount

  implicit val defaultAmount = 10

  increment(2)
  // NOT default args

}
