package advanced.lectures.part4implicits

import sun.nio.cs.ext.Johab

object TypeClasses extends App {
  trait HTMLWritable {
    def toHtml: String
  }

  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  User("John", 32, "john@outlook.com").toHtml
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

  object UserSerializer extends HTMLSerializer[User] {
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

  /**
   * Equality
   */
  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }

  object NameEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = (a.name == b.name)
  }

  object FullEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = (a.name == b.name && a.email == b.email)
  }
}
