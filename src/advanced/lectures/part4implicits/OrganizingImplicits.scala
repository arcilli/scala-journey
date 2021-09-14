package advanced.lectures.part4implicits

object OrganizingImplicits extends App {
  implicit val reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  //  implicit val normalOrdering: Ordering[Int] = Ordering.fromLessThan(_ < _)
  println(List(1, 4, 5, 3, 2).sorted)

  // scala.Predef

  /*
    Implicits (used as implicit parameters):
      - val/var
      - objects
      - accessor methods = defs with no parentheses
   */
  val persons = List(
    Person("Steve", 30),
    Person("Amy", 22),
    Person("John", 66)
  )

  // Exercise
  case class Person(name: String, age: Int)

  //  object Person {
  //    implicit val alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  //  }

  /*
    Implicit scope
    - normal scope = LOCAL SCOPE
    - imported scope
    - companion of all types involved in the method signatures
      - List
      - Ordering
      - all the types involved = A or any supertype
   */

  // def sorted[B >: A](implicit ord: Ordering[B]): List[B]

  /*
    Exercise.

    - totalPrice = most used (50%)
    - by unit count = 25%
    - by unit price = 25%

   */
  case class Purchase(nUnits: Int, unitPrice: Double)

  object AlphabeticNameOrdering {
    implicit val alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  }

  import AlphabeticNameOrdering._

  println(persons.sorted)

  object AgeOrdering {
    implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.age < b.age)
  }

  object Purchase {
    implicit val totalPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((a, b) => a.unitPrice * a.nUnits < b.unitPrice * b.nUnits)
  }

  object ByUnitCountOrdering {
    implicit val sorting: Ordering[Purchase] = Ordering.fromLessThan((a, b) => a.nUnits < b.nUnits)
  }

  object ByUnitPriceOrdering {
    implicit val sorting: Ordering[Purchase] = Ordering.fromLessThan((a, b) => a.unitPrice < b.unitPrice)
  }
}
