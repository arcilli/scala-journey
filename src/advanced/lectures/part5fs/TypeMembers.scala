package advanced.lectures.part5fs

object TypeMembers extends App {

  class Animal

  class Dog extends Animal

  class Cat extends Animal

  class AnimalCollection {
    type AnimalType // abstract type member
    type BoundedAnimal <: Animal
    type SuperBoundedAnimal >: Dog <: Animal

    // just another name for a class/Type
    type AnimalC = Cat
  }

  val ac = new AnimalCollection
  val dog: ac.AnimalType = ???

//  val cat: ac.BoundedAnimal = new Cat

  val pup: ac.SuperBoundedAnimal = new Dog
  val cat: ac.AnimalC = new Cat

  // type alias
  type CatAlias = Cat
  val anotherCat: CatAlias = new Cat

  // this is an alternative to generics
  trait MyList {
    type T
    def add(element: T): MyList
  }

  class NonEmptyList(value: Int) extends MyList{
    override type T = Int

    override def add(element: Int): MyList = ???
  }

  // .type
  type CatsType = cat.type
  val newCat: CatsType = cat

//  new CatsType - this will not compile

  /*
    Exercise - enforce a type to be applicable to SOME TYPES only
   */
  // LOCKED
  trait MList {
    type A
    def head: A
    def tail: MList
  }

  trait ApplicableToNumbers {
    type A <: Number
  }

  // this should not be OK
  class CustomList(hd: String, tl: CustomList) extends MList {
    type A = String
    def head = hd
    def tail = tl
  }

  // this should not be OK
//  class CustomList2(hd: String, tl: CustomList) extends ApplicableToNumbers {
//    type A = String
//    def head = head
//    def tail = tl
//  }

  // this should be ok
  class IntList2(hd: Integer, tl: IntList2) extends ApplicableToNumbers {
    type A = Integer
    def head = hd
    def tail = tl
  }

  // hint: all the numbers are under Number type
  // use type members & type member constraints (bounds)
}
