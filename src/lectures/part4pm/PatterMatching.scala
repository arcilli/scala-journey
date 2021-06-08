package lectures.part4pm

import scala.util.Random

object PatterMatching extends App {
  // switch on steroids
  val random = new Random
  val x = random.nextInt(10)

  val description = x match {
    case 1 => "the ONE"
    case 2 => "double or nothing"
    case 3 => "third time is the charm"
    case _ => "something else" // _ = WILDCARD
  }

  println(x)
  println(description)
  val bob = Person("Bob", 20)
  val greeting = bob match {
    case Person(n, a) if a < 21 => s"Hi, my name is $n and I can't legally drink in the US."
    case Person(n, a) => s"Hi, my name is $n and I am $a yrs old."
    case _ => "I don't know wtf I am"
  }

  // 1. Decompose values
  case class Person(name: String, age: Int)
  print(greeting)

  /*
    1. cases are matched in order
    2. what if no catch: scala.MatchError
    3. what's the type of the pattern match? => the return type of the cases (or the unified type of all the types
    in all the cases)
    4. PM works really well with case classes
   */
  // PM on sealed hierarchies
  sealed class Animal
  case class Dog(breed: String) extends Animal
  case class Parrot(greeting: String) extends Animal

  val animal: Animal = Dog("Terra nova")
  animal match {
    case Dog(someBreed) => println(s"Matched a dof of the $someBreed breed")
  }

  // match everything
  val isEven = x match {
    case n if n%2 == 0 => true
    case _ => false
  }
  // Why would you do this?

  val isEvenCond = if (x%2 ==0) true else false //??
  val isEvenNormal = x%2 == 0

  /*
    Exercise
    simple function uses PM
      takes an Expr => human readable form of it

      Sum(Number(2), Number(3)) => 2+3
      Sum(Number(2), Number(3), NUmber(4)) => 2+3 + 4
      Prod(Sum(Number(2), Number(1)), Number(3)) = (2+1) *3
      Sum(Prod(Number(2), Number(1)), Number(3)) = (2*1)+3


   */
  trait Expr
  case class Number(n: Int) extends Expr
  case class Sum(e1: Expr, e2: Expr) extends Expr
  case class Product(e1: Expr, e2: Expr) extends Expr

  def get_readable_form(expr: Expr): String = expr match {
      case Number(n) => s"$n"
      case Sum(e1, e2) => get_readable_form(e1) + " + " +get_readable_form(e2)
      case Product(e1, e2) => {

        def maybeShowParentheses(e: Expr): String = e match {
          case Product(_, _) => get_readable_form(e)
          case Number(_) => get_readable_form(e)
          case _ => "(" + get_readable_form(e) + ")"
        }
        maybeShowParentheses(e1) + " * " + maybeShowParentheses(e2)
      }
    }
  println(get_readable_form(Sum(Number(2), Number(3))))
  println(get_readable_form(Sum(Sum(Number(2), Number(3)), Number(4))))
  println(get_readable_form(Product(Sum(Number(2), Number(1)), Number(3))))
  println(get_readable_form(Sum(Product(Number(2), Number(1)), Number(3))))
}
