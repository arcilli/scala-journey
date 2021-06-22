package beginners.lectures.part1basics

object ValuesVariablesTypes extends App {

  // vals are immutable
  val x = 42
  println(x)

  val aString: String = "hello"

  println(aString)

  val aShort: Short = 4613

  val aLong: Long = 52739851532L

  val aFloat: Float = 2.0f
  val aDouble: Double = 3.14

  // mutable
  var aVariable: Int = 4

  aVariable = 5 // side effects
}
