package advanced.lectures.part5fs

object Variance extends App {

  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  // what is variance?
  // "inheritance" - type substitution of generics

  class Cage[T]
  // yes - covariance

  class CCage[+T]
  val ccage: CCage[Animal] = new CCage[Cat]

  // no - invariance
  class ICage[T]
//  val iCage: InvariantCage[Animal] = new InvariantCage[Cat]

  // hell no, even opposite = contravariance
  class XCage[-T]
  val contraCage: XCage[Cat] = new XCage[Animal]

  class InvariantCage[T](val animal: T) // invariant

  // covariant positions
  class CovariantCage[+T](val animal: T) // covariant position

//  class ContravariantCage[-T](val animal: T) // this will result in a compiling error
  /*
    val catCage: XCage[Cat] = new XCage[Animal](new Crocodile)
   */

//  class CovariantVariableCage[+T](var animal: T) // this will also not compile because of the "var"
  // types of vars are in CONTRAVARIANT position
  /*
    val ccage: CCage[Animal] = new CCage[Cat](new Cat)
    ccage.animal = new Crocodile
   */

//  class ContravariantVariableCage[-T](var animal: T) // this will also will not compile
//     also, the variable is in COVARIANT position

  class InvariantVariableCage[T](var animal: T) // ok

  //  trait AnotherCovariantCage[+T] {
//    def addAnimal(animal: T) // CONTRAVARIANT POSITION
//  }
  /*
    val ccage: CCage[Anima] = new CCage[Dog]
    ccage.add(new Cat)
   */

  class AnotherContravariantCage[-T] {
    def addAnimal(animal: T) = true
  }

  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  acc.addAnimal(new Cat)

  class Kitty extends Cat
  acc.addAnimal(new Kitty)

  class MyList[+A] {
    def add[B >: A](element:B): MyList[B] = new MyList[B] // widening the type
  }

  val emptyList = new MyList[Kitty]
  val animals = emptyList.add(new Kitty)
  val moreAnimals = animals.add(new Cat)
  val evenMoreAnimals = moreAnimals.add(new Dog)

  // Method arguments are in contravariant position.

  // return types
  class PetShop[-T] {
    // def get(isItAPuppy: Boolean): T // method return types are in covariant position
    /*
      val catShop = new Petshop[Animal] {
        def get(...): Animal = new Cat
      }
        val dogShop: PetShop[Dog] = catshop
        dogShop.get(true) // EVIL CAT!
     */

    // a potential solution:
     def get[S<:T](isItAPuppy: Boolean, defaultAnimal: S): S = defaultAnimal
  }

  val shop: PetShop[Dog] = new PetShop[Animal]
//  val evilCat = shop.get(true, new Cat)

  class TerraNova extends Dog
  val bigFurry = shop.get(true, new TerraNova)

  /*
  BIG rule:
    - method arguments are in CONTRAVARIANT position
    - return types are in COVARIANT position
   */
}
