package beginners.exercises.own.playground

object AbstractClassCaseOverriding extends App {
  val obj = ActualImplementation("gigel", 22, 32.5)

  abstract class SomeSortOfThing(
                                  val name: String,
                                  age: Int
                                ) {
    def print(): Unit = println(s"Name: $name, age: $age yrs old.")
  }

  case class ActualImplementation(
                                   override val name: String,
                                   age: Int,
                                   anotherProp: Double
                                 ) extends SomeSortOfThing(age.toString, age) {
    override def print(): Unit = println(s"Name: $name, age: $age, anotherProp: $anotherProp")
  }
  obj.print()

}
