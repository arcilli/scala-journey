package advanced.lectures.part1as

import scala.annotation.tailrec

object Recap extends App {
  val aCondition: Boolean = false
  // if expression
  val aConditionedVal = if (aCondition) 42 else 65

  // instructions VS expressions

  // compiler infers types for us
  val aCodeBlock = {
    if (aCondition) 54
    56
  }

  // Unit = void, but do some side effects
  val theUnit = println("Hello, Scala") // the type of this will be Unit

  // functions
  def aFunction(x: Int): Int = x+1

  // recursion: stack and tail
  @tailrec def factorial(n: Int, acc: Int): Int =
    if (n<0) acc
    else factorial(n-1, n*acc)

  // OO
  class Animal
  class Dog extends Animal
  val aDog: Animal = new Dog // subtyping polymorphism

  trait Carnivore {
    def eat(a: Animal): Unit
  }

  class Crocodile extends Animal with Carnivore{
    override def eat(a: Animal): Unit = println("crunch!")
  }

  // method notations
  val aCroc = new Crocodile
  aCroc.eat(aDog)

  // infix, postfix, prefix notations
  aCroc eat aDog // natural language

  1+2
  // is converted to: 1.+(2)

  // anonymous classes
  val aCarnivore = new Carnivore {
    override def eat(a: Animal): Unit = println("Roooar")
  }

  // generics
  abstract class MyList[+A] //variance and variance problems

  // singletons and companions
  object MyList

  // case classes
  case class Person(name: String, age: Int)

  // exceptions & try/catch/finally
  val throwsException = throw new RuntimeException //Nothing type
  val aPotentialFailure = try {
    throw new RuntimeException
  } catch {
    case e: Exception => "I caught one"
  } finally {
    println("some logs")
  }

  // functional programming
  val incrementer = new Function1[Int, Int] {
    override def apply(v1: Int): Int = v1+1
  }

  incrementer(1)
  val anonymousIncrementer = (x: Int) => x+1

  List(1, 2, 3).map(anonymousIncrementer) //HOF
  // map, flatMap, filter

  // for-comprehension
  val pairs = for {
    num <- List(1, 2, 3) // if condition
    char <- List('a', 'b', 'c')
  } yield num + "-" +char

  // Scala collections: Seq, Arrays, Lists, Vectors, Maps, Tuples
  val aMap = Map(
    "Daniel" -> 789,
    "Jess" -> 555
  )

  // "collections": Options, Try
   val anOption = Some(2)

  // pattern matching
  val x = 2
  val order = x match {
    case 1 => "first"
    case 2 => "second"
    case 3 => "third"
    case _ => x + "th"
  }

  val bob = Person("Bob", 22)
  val greeting = bob match {
    case Person(n, _) => s"Hi, my name is $n"
  }

  // all the patterns
}
