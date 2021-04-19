package playground

object Objects {
  def main(args: Array[String]): Unit = {
    // Scala object is a SINGLETON INSTANCE.
    val mary = Person
    val john = Person

    // SCALA DOES NOT HAVE CLASS-LEVEL FUNCTIONALITY ("static")

    // An object can have this "static-like" functionality
    val person1 = Person
    val person2 = Person

    // Those 2 point to the same instance.
    println(mary == john)
    // COMPANIONS: writing objects and classes with the same name: both Person are companions because they are
    // residing in the same scope; have the same
    // name
    val mary2 = new Person("Mary")
    val john2 = new Person("John")
    println(person1 == person2)


    println(Person.N_EYES)
    println(Person.canFly)
    // this is actually _apply_ method from the singleton object
    val bobbie = Person(mary2, john2)

    // Scala application = Scala object with def main(args: Array[String]): Unit
  }

  class Person(val name: String) {
    // instance-level functionality
  }

  object Person { // the type + its only instance
    // "static"/"class" - level functionality
    val N_EYES = 2

    def canFly: Boolean = false

    // factory
    def apply(mother: Person, father: Person): Person = new Person("Bobbie")
  }
}
