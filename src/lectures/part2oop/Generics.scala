package lectures.part2oop

object Generics extends App {
  val listOfIntegers = new MyList[Int]
  val listOfStrings = new MyList[String]

  /*
   * traits can also be parameterized
   */
  val emptyListOfIntegers = MyList.empty[Int]
  val animal: Animal = new Cat

  // 1. yes, List[Cat] extends List[Animal] = COVARIANCE
  // _+A_ means that this is a covariant list
  class CovariantList[+A]{
    // The following will not work, as it is in this moment.
    //    def add(element: A): MyList[A] = ???
    def add[B >: A](element: B): MyList[B] = ???
    /*
      A = Cat
      B = Dog = Animal
      Add an animal to a list of cats -> the list "transform" to a list of animalss
     */
  }
  val animalList: CovariantList[Animal] = new CovariantList[Cat]
  //  animalList.add(new Dog)??? HARD question => we return a list of Animal


  // 2. NO = INVARIANCE
  // elements from invariantList cannot substitute one for another
  class InvariantList[A]

  // the following is not working
  //  val invariantAnimalList: invariantAnimalList[Animal] = new InvariantList[Cat]


  // 3. Hell, no! CONTRAVARIANCE
  class ContraVariantList[-A]
  val contravariantList: ContraVariantList[Cat] = new ContraVariantList[Animal]

  // continue of 3
  class Trainer[-A]
  // the trainer can train also a cat, an animal, a dog etc & in particular it trains a cat, which is a particular case
  val trainer: Trainer[Cat] = new Trainer[Animal]

  // bounded types: allow you to use your generic types only for certain types
  // class Cage accepts as parameter only classes which are subtypes of Animal
  // an example of higher bound
  class Cage[A <:Animal](animal: A)
  val cage = new Cage(new Dog)

  // an example of lower bound
  // accepts only a class that's a supertype of the Animal
  class Cage2[A >: Animal](animal: A)

  class Car
  // the following will not work because of the bounded types
  //  val newCage = new Cage(new Car)

  // a generic class
  class MyList[A] {

  }

  // multiple generic types
  class MyMap[Key, Value]

  // variance problem
  class Animal

  class Cat extends Animal

  class Dog extends Animal
  // objects can't be type parameterized
  object MyList {
    // generic methods
    def empty[A]: MyList[A] = ???
  }
}