package advanced.lectures.part4implicits

import sun.nio.cs.ext.Johab

object TypeClasses extends App {
  trait HTMLWritable {
    def toHtml: String
  }

  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  val john = User("John", 32, "john@outlook.com")
  john.toHtml
  /*
    1. This only works for the types WE write
    2. One implementation out of quite a number
   */

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHtml: String = s"<div>$name ($age yo) <a href=$email/>"
  }

  /*
    1 - lost type safety
    2 - need to modify the code every time
    3 - still ONE implementation for each given time
   */

  // option 2: use pattern matching
  object HTMLSerializerPM {
    def serializeToHTML(value: Any): Unit = value match {
      case User(n, a, e) =>
      //      case java.util.Date =>
      case _ =>
    }
  }

  implicit object UserSerializer extends HTMLSerializer[User] {
    override def serialize(value: User): String = s"<div>${value.name} (${value.age} yo) <a href=${value.email}/>"
  }

  println(UserSerializer.serialize(User("John", 32, "john@outlook.com")))

  // 1 - we can define serializers for other types

  import java.util.Date

  object DateSerializer extends HTMLSerializer[Date] {
    override def serialize(date: Date): String = s"<div>${date.toString}</div>"
  }

  // 2 - we can define MULTIPLE serializers
  object PartialUserSerializer extends HTMLSerializer[User] {
    override def serialize(value: User): String = s"<div>${value.name} </div>"
  }

  // type class
  trait MyTypeClassTemplate[T] {
    def action(value: T): String
  }

  object MyTypeClassTemplate{
    def apply[T](implicit instance: MyTypeClassTemplate[T]) = instance
  }

  /**
   * Equality
   */
  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }

  implicit object NameEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = (a.name == b.name)
  }

  object FullEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = (a.name == b.name && a.email == b.email)
  }

  // part 2
  object HTMLSerializer {
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String = {
      serializer.serialize(value)
    }

    def apply[T](implicit serializer: HTMLSerializer[T]): Unit = serializer
  }

  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div>$value</div>"
  }

  println(HTMLSerializer.serialize(42)(IntSerializer))
  println(HTMLSerializer.serialize(42))
  println(HTMLSerializer.serialize(john))

  // access to the entire type class interface
//  println(HTMLSerializer[User].serialize(john))

  /*
  Exercise: implement the Type class pattern for the equality type class
   */
  object Equal {
    def apply[T](a:T, b: T)(implicit equalizer: Equal[T]): Boolean =
      equalizer.apply(a, b)
  }

  val anotherJohn = User("John", 45, "another-john@outlook.com")
  println(Equal(john, anotherJohn))

  // AD-HOC polymorphism

}