package lectures.part2oop

import scala.language.postfixOps

object MethodNotations extends App {

  val mary = new Person("Mary", "Inception")
  // "operators" in Scala
  val tom = new Person("Tom", "Fight Club")
  // prefix notation
  // the following 2 are the same
  val x = -1 // _minus_ is an example of unary operator

  // Those both expressions are the same
  println(mary.likes("Inception"))

  // an example of syntactic sugar
  println(mary likes "Inception") // infix notation = operator notation; works only with methods with only one parameter
  val y = 1.unary_-
  println(mary + tom)
  println(mary.+(tom))

  // + operators between numbers are functions as well
  // ALL OPERATORS ARE METHODS.

  println(1 + 2)
  println(1.+(2))

  // by specifying _name_ as a val, we're transforming it in a field
  class Person(val name: String, favoriteMovie: String) {
    def likes(movie: String): Boolean = movie == favoriteMovie

    def +(person: Person): String = s"${this.name} is hanging out with ${person.name}."

    // the unary_ prefix only works with -, +, ~, ! (the last one is bang operator)


    // the space between _! and the : is in order to make the difference between the function name and the returned type
    // Otherwise, it would be unary_!: which can be interpreted as a new method
    def unary_! : String = s"$name, what the heck??!"


    // postfix notation
    def isAlive: Boolean = true

    // The method signature is important.
    def apply(): String = s"Hi, my name is $name and i like $favoriteMovie"
  }

  // the following 2 are equivalent:
  println(!mary)
  println(mary.unary_!)


  // the following 2 are equivalent:
  // postfix notation
  println(mary.isAlive)
  println(mary isAlive)

  // apply
  // the following 3 are equivalent
  println(mary.apply())
  println(mary.apply)
  // Whenever a class is called with (), automatically the _apply_ function will be called.
  println(mary())
}
