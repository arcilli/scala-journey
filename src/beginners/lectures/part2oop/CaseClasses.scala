package beginners.lectures.part2oop

object CaseClasses extends App {


  case class Person(name: String, age: Int)
  // 1. class parameters are promoted to fields
  val jim = new Person("Jim", 34)
  println(jim.name)

  // 2. sensible toString
  println(jim.toString)

  // 3. equals are hashcode implemented OOTB
  val jum2 = new Person("Jim", 34)
  println(jim == jum2)

  // 4. Case classes have handy copy method
  val jim3 = jim.copy(age = 45)
  println(jim3)
  println(jim3 == jim)

  // 5. Case classes (CCs) have companion objects
  val thePerson = Person
  println(thePerson)

  // The following can also be used as Person("Marry", 23), as a constructor, 'cause we can remove .apply
  val mary = Person.apply("Marry", 23)

  // 6. CCs are serializable
  // Akka framework for example

  // 7. CCs have extractor patterns => CCs can be used in PATTERN MATCHING


  // Case objects have the same properties as the case classes
  case object UnitedKingdom{
    def name: String = "The UK of GB and NI"
  }

  /*
    Expand MyList - use case classes and case objects
   */
}
