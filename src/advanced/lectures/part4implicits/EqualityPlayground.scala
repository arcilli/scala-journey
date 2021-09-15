package advanced.lectures.part4implicits

import advanced.lectures.part4implicits.TypeClasses.{User, john}

object EqualityPlayground extends App{

  val anotherJohn = User("John", 45, "another-john@outlook.com")

  implicit object NameEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = (a.name == b.name)
  }

  /**
   * Equality, this is a type class
   */
  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }

  object FullEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = (a.name == b.name && a.email == b.email)
  }

  /*
Exercise: implement the Type class pattern for the equality type class
 */
  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean =
      equalizer.apply(a, b)
  }

  val john = User("John", 32, "john@outlook.com")
//  john.toHtml
  println(Equal(john, anotherJohn))
  // AD-HOC polymorphism

  /*
    Exercise - improve the Equal TC with an implicit conversion class
    ===(another value: T)
    !==(another value: T)
   */

  implicit class TypeSafeEqual[T](value: T) {
    def ===(other: T)(implicit equalizer: Equal[T]): Boolean = equalizer.apply(value, other)
    def !==(other: T)(implicit equalizer: Equal[T]): Boolean = !equalizer.apply(value, other)
  }

  println(john === anotherJohn)
  /*
    the compiler rewrites the expression as the following: john.===(anotherJohn)
    john.===(anotherJohn)
    new TypeSafeEqual[User](john).===(anotherJohn)(NameEquality)
   */

  /*
    TYPE SAFE
   */
  println(john == 42)
//  println(john === 43) // this will not compile, the compiler will force me so the 2 variables will be of the same type
}
