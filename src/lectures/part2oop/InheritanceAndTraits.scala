package lectures.part2oop

object InheritanceAndTraits extends App {
  val cat = new Cat
  cat.eat
  cat.crunch
  //  val dog = new Dog
  val dog2 = new Dog("K9")
  // type substitution (broad: polymorphism)
  val unknownAnimal: Animal = new Dog("K9")

  // No access modifier = public
  class Animal {
    val creatureType = "wild"

    def eat = println("num num num")

    protected def someProtected() = "Protected"

    private def privateFunction() = "Smth"
  }

  // single class inheritance
  // you can only inherits non-private members of the superclass
  class Cat extends Animal {
    def crunch = {
      someProtected()
      println("crunch crunch")
    }
  }

  // constructors
  class Person(name: String, age: Int) {
    def this(name: String) = this(name, 0)
  }


  //  dog.eat
  //  println(dog.creatureType)

  // the right way of extending a class with parameters
  class Adult(name: String, age: Int, idCard: String) extends Person(name, age)

  // overriding
  class Dog(override val creatureType: String) extends Animal {

    final override def eat = {
      super.eat
      println("crunch, crunch dog")
    }
  }

  unknownAnimal.eat

  // ! overRIDING vs overLOADING

  // super, super

  // preventing overrides
  // 1 - use final keyword on member
  // 2 - use final keyword on the class itself
  // 3 - seal the class; you can extend classes in THIS FILE, prevent extending in other files
  // for what wtf should we would need this?
}
