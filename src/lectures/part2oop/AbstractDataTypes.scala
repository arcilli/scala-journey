package lectures.part2oop

object AbstractDataTypes extends App {
  // abstract: classes/methods

  val dog = new Dog
  val croc = new Crocodile()

  // traits
  // What's special about traits is that unlike abstract classes, they can be inherited along classes.
  trait Carnivore {
    val preferredMeal: String = "fresh meat"

    def eat(animal: Animal): Unit
  }

  trait ColdBlooded

  abstract class Animal {
    val creatureType: String

    def eat: Unit
  }

  class Crocodile extends Animal with Carnivore with ColdBlooded {
    // inherit members both from Animal and Carnivore
    override val creatureType: String = "croc"

    override def eat: Unit = "nomnomnom"

    override def eat(animal: Animal): Unit = println(s"I'm a croc and I'm eating: ${animal.creatureType}")

  }

  class Dog extends Animal {
    // override keyword is not mandatory
    override val creatureType: String = "Canine"

    def eat: Unit = println("crunch crunch")
  }

  croc.eat(dog)

  // traits VS abstract classes
  // both traits & abstract classes can have abstract & non-abstract classes
  // 1 - traits do not have constructor parameters
  // 2 - multiple traits may be inherited by the same class, but you can inherit only 1 class
  // 3 - traits are behavior, abstract class = "thing"

}
