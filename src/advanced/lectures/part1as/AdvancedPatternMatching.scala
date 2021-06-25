package advanced.lectures.part1as

object AdvancedPatternMatching extends App{
  val numbers = List(1)
  val description: Unit = numbers match {
    case head :: Nil => println(s"The only element is $head")
    case _ =>
  }
  /*
  Pattern matching for:
  - constants
  - wildcards
  - case classes
  - tuples
  - some special magic like above :D
   */

  class Person(val name: String, val age: Int)
  object Person{
    def unapply(person: Person): Option[(String, Int)] = {
      if (person.age < 21) None
      else Some((person.name, person.age))
    }

    def unapply(age: Int): Option[(String)] = {
      Some(if (age <21) "minor" else "major")
    }
  }
  val bob = new Person("Bob", 33)
  val greeting = bob match {
    case Person(n, a) => s"Hi, I am $n & I am $a yrs old."
  }

  println(greeting)
  val legalStatus = bob.age match {
    case Person(status) => s"My legal status is $status."
  }
  println(legalStatus)

  /*
    Exercise.
   */
  // When used for pattern matching, usually we're using it with lowercase
  object even{
    def unapply(arg: Int): Boolean = arg %2 ==0
  }

  object singleDigit{
    def unapply(arg: Int): Boolean =(arg > -10 && arg < 10)
  }

  val n: Int = 45
  /*
  val mathProperty = n match {
    case x if x<10 => "single digit"
    case x if x%2 ==0 => "an even number"
  }
  */

  val mathPropertyFancy = n match {
    case singleDigit() => "single digit"
    case even() => "an even number"
    case _ => "no property"
  }

  println(mathPropertyFancy)

}
