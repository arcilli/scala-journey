package advanced.lectures.part5fs

object FBoundedPolymorphism extends App {

  //  trait Animal {
  //    def breed: List[Animal]
  //  }
  //
  //  class Cat extends Animal {
  //    override def breed: List[Animal] = ??? // List[Cat], not List[Animal]
  //  }
  //
  //  class Dog extends Animal {
  //    override def breed: List[Animal] = ??? // List[Dog], not List[Animal]
  //  }

  // Solution 1 - naive
  //  trait Animal {
  //    def breed: List[Animal]
  //  }
  //
  //  class Cat extends Animal {
  //    override def breed: List[Cat] = ???
  //  }
  //
  //  class Dog extends Animal {
  //    override def breed: List[Dog] = ???
  //  }

  // solution #2: F-Bounded polymorphism
  //  trait Animal[A <: Animal[A]] { // recursive type: F-Bounded Polymorphism
  //    def breed: List[Animal[A]]
  //  }
  //
  //  class Cat extends Animal[Cat] {
  //    override def breed: List[Animal[Cat]] = ???
  //  }
  //
  //  class Dog extends Animal[Dog] {
  //    override def breed: List[Animal[Dog]] = ???
  //  }
  //
  //  trait Entity[E <: Entity[E]] // ORM
  //
  //  class Person extends Comparable[Person]{ // another example of F-Bounded polymorphism
  //    override def compareTo(o: Person): Int = ???
  //  }
  //
  //  // i can make mistakes:
  //  class Crocodile extends Animal[Dog] {
  //    override def breed: List[Animal[Dog]] = ???
  //  }

  // how do I enforce the compiler that the class I'm defining and the one I'm annotating with is the same?
  // Solution #3 - use F-Bounded polymorphism with self types


  //  trait Animal[A <: Animal[A]] { self: A =>
  //    def breed: List[Animal[A]]
  //  }
  //
  //  class Cat extends Animal[Cat]{
  //    override def breed: List[Animal[Cat]] = ???
  //  }
  //
  //  class Dog extends Animal[Dog] {
  //    override def breed: List[Animal[Dog]] = ???
  //  }

  //  class Crocodile extends Animal[Dog] { // this is not compiling in any way
  //    override def breed: List[Animal[Dog]] = ???
  //  }

  //  trait Fish extends Animal[Fish]
  //  class Shark extends Fish {
  //    override def breed: List[Animal[Fish]] = List(new Cod) // wrong
  //  }
  //
  //  class Cod extends Fish {
  //    override def breed: List[Animal[Fish]] = ???
  //  }

  // Exercise

  // Solution #4: type classes!
  //  trait Animal
  //  trait CanBreed[A] {
  //    def breed(a: A): List[A]
  //  }
  //
  //  class Dog extends Animal
  //  object Dog {
  //    implicit object DogsCanBreed extends CanBreed[Dog] {
  //      override def breed(a: Dog): List[Dog] = List()
  //    }
  //  }
  //
  //  implicit class CanBreedOps[A](animal: A) {
  //    def breed(implicit canBreed: CanBreed[A]): List[A] =
  //      canBreed.breed(animal)
  //  }
  //
  //  val dog = new Dog
  //
  //  // the compiler does: new CanBreedOps[Dog](dog).breed
  //  // implicit value to pass to breed: Dog.DogsCanBreed
  //  dog.breed
  //
  //  class Cat extends Animal
  //  object Cat {
  //    implicit object CatsCanBreed extends CanBreed[Dog] {
  //      override def breed(a: Dog): List[Dog] = List()
  //    }
  //  }

  val dog = new Dog

  // Solution #5
  trait Animal[A] { // pure type classes
    def breed(a: A): List[A]
  }

  class Dog

  implicit class AnimalOps[A](animal: A) {
    def breed(implicit animalTypeClassInstance: Animal[A]): List[A] =
      animalTypeClassInstance.breed(animal)
  }

  object Dog {
    implicit object DogAnimal extends Animal[Dog] {
      override def breed(a: Dog): List[Dog] = List()
    }
  }
  dog.breed
}