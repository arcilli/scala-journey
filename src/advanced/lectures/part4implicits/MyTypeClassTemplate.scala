package advanced.lectures.part4implicits

// type class
trait MyTypeClassTemplate[T] {
  def action(value: T): String
}

object MyTypeClassTemplate {
  def apply[T](implicit instance: MyTypeClassTemplate[T]) = instance
}