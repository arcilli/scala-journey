package advanced.lectures.part4implicits

object TypeClasses extends App {

  val john = User("John", 32, "john@outlook.com")

  trait HTMLWritable {
    def toHtml: String
  }

  /*
    1. This only works for the types WE write
    2. One implementation out of quite a number
   */

  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  /*
    1 - lost type safety
    2 - need to modify the code every time
    3 - still ONE implementation for each given time
   */

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHtml: String = s"<div>$name ($age yo) <a href=$email/>"
  }

  implicit object UserSerializer extends HTMLSerializer[User] {
    override def serialize(value: User): String = s"<div>${value.name} (${value.age} yo) <a href=${value.email}/>"
  }

  println(UserSerializer.serialize(User("John", 32, "john@outlook.com")))

  // 1 - we can define serializers for other types

  import java.util.Date

  // option 2: use pattern matching
  object HTMLSerializerPM {
    def serializeToHTML(value: Any): Unit = value match {
      case User(n, a, e) =>
      //      case java.util.Date =>
      case _ =>
    }
  }

  object DateSerializer extends HTMLSerializer[Date] {
    override def serialize(date: Date): String = s"<div>${date.toString}</div>"
  }

  // 2 - we can define MULTIPLE serializers
  object PartialUserSerializer extends HTMLSerializer[User] {
    override def serialize(value: User): String = s"<div>${value.name} </div>"
  }

  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div>$value</div>"
  }

  println(HTMLSerializer.serialize(42)(IntSerializer))
  println(HTMLSerializer.serialize(42))

  // part 2
  object HTMLSerializer {
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String = {
      serializer.serialize(value)
    }

    def apply[T](implicit serializer: HTMLSerializer[T]): Unit = serializer
  }

  //  println(HTMLSerializer.serialize(john))

  // part 3
  implicit class HTMLEnrichment[T](value: T) {
    def toHTML(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)
  }

  //  println(john.toHtml(UserSerializer)) // println(new HTMLEnrichment[User](john).toHTML(UserSerialize))
  println(john.toHtml)
  // COOL!

  /*
    - extend to new types
    - choose implementation
    - super expressive
   */

  println("2.toHTML" + 2.toHTML)

  // TODO: why is this not working as expected?
  //  println(john.toHtml(PartialUserSerializer))


  /*
  - type class itself ---- HTMLSerializer[T] {...}
  - type class instances (some of which are implicit) ---- UserSerializer, IntSerializer
  - conversion with implicit classes ---- HTMLEncrichment
 */

  // context bounds
  def htmlBoilerplate[T](content: T)(implicit serializer: HTMLSerializer[T]): String =
    s"<html><body> ${content.toHTML(serializer)}</body></html>"

  def htmlSugar[T: HTMLSerializer](content: T): String = {
    val serializer = implicitly[HTMLSerializer[T]]
    // use serializer
    s"<html><body> ${content.toHTML(serializer)}</body></html>"
  }

  // implicitly; it's a method
  case class Permissions(mask: String)
  implicit val defaultPermissions: Permissions = Permissions("0744")

  // in some other part of the code
  val standardPerms = implicitly[Permissions]

}