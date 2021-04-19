package lectures.part2oop

object OOBasics extends App {
  val person = new Person("Gigel", 26)
  println(person.age)
  println(person.x)

  person.greet("Daniel")
}

// constructor
class Person(name: String, val age: Int = 0) {
  // body
  val x = 2

  // All side effects will be executed.
  println(1 + 3)

  // a method
  def greet(name: String): Unit = println(s"${this.name} says: Hi, $name")

  // overloading
  def greet(): Unit = println(s"Hi, I am $name")


  // multiple constructor
  def this(name: String) = this(name)

  // A secondary constructor can only cann others constructors.
  def this() = this("John Doe")

}

// class parameters ARE NOT FIELDS