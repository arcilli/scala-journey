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
  /*
    Novel and a Writer

    Writer: first name, surName, year
      - method fullName

    Novel: name, year of release, author
      - authorAge
      - isWrittenBy(author)
      - copy(new year of release) = new instance of Novel

   */
  val author = new Writer("Charles", "Dickens", 1812)
  val impostor = new Writer("Charles", "Dickens", 1812)
  val novel = new Novel("Great expectations", 1861, author)


  // multiple constructor
  //  def this(name: String) = this(name)
  val counter = new Counter

  // a method
  def greet(name: String): Unit = println(s"${this.name} says: Hi, $name")

  println("Novel: ")
  println(novel.authorAge)
  println(novel.isWrittenBy(author))
  println(novel.isWrittenBy(impostor))

  // class parameters ARE NOT FIELDS

  // overloading
  def greet(): Unit = println(s"Hi, I am $name")

  // A secondary constructor can only cann others constructors.
  def this() = this("John Doe")

  print("Constructor!!!")
  counter.increment(10)
}

class Novel(name: String, yearOfRelease: Int, author: Writer) {
  def authorAge = yearOfRelease - author.year

  def isWrittenBy(author: Writer) = author == this.author

  def copy(newYear: Int): Novel = new Novel(name, newYear, author)
}

class Writer(firstName: String, surName: String, val year: Int) {
  def fullName() = s"$firstName $surName"
}

//Counter class:
//  - receives an int value
//  - method current count
//  - method to increment/decrement => return a new Counter
//  - overload inc/dec to receive an amount => new Counter
class Counter(val count: Int = 0) {

  def increment = {
    // Immutability, the same principle with declaring immutable objects
    println("incrementing")
    new Counter(count + 1)
  }

  def decrement = {
    println("decrementing")
    new Counter(count - 1)
  }

  def increment(toIncrementWith: Int): Counter = {
    if (toIncrementWith <= 0) this
    else increment.increment(toIncrementWith - 1)
  }

  def decrement(toDecrementWith: Int): Counter = {
    if (toDecrementWith <= 0) this
    else decrement.decrement(toDecrementWith - 1)
  }

}