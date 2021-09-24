package advanced.lectures.part5fs

object Variance extends App {

  val ccage: CCage[Animal] = new CCage[Cat]
  val contraCage: XCage[Cat] = new XCage[Animal]
  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  val emptyList = new MyList[Kitty]

  // what is variance?
  // "inheritance" - type substitution of generics
  val animals = emptyList.add(new Kitty)
  // yes - covariance
  val moreAnimals = animals.add(new Cat)
  val evenMoreAnimals = moreAnimals.add(new Dog)
  val shop: PetShop[Dog] = new PetShop[Animal]
  //  val iCage: InvariantCage[Animal] = new InvariantCage[Cat]
  val bigFurry = shop.get(true, new TerraNova)

  trait Animal

  class Dog extends Animal

  class Cat extends Animal

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

  class Crocodile extends Animal

  //  trait AnotherCovariantCage[+T] {
  //    def addAnimal(animal: T) // CONTRAVARIANT POSITION
  //  }
  /*
    val ccage: CCage[Anima] = new CCage[Dog]
    ccage.add(new Cat)
   */

  class Cage[T]

  class CCage[+T]
  acc.addAnimal(new Cat)

  // no - invariance
  class ICage[T]

  acc.addAnimal(new Kitty)

  // hell no, even opposite = contravariance
  class XCage[-T]

  class InvariantCage[T](val animal: T) // invariant

  // covariant positions
  class CovariantCage[+T](val animal: T) // covariant position

  class InvariantVariableCage[T](var animal: T) // ok

  class AnotherContravariantCage[-T] {
    def addAnimal(animal: T) = true
  }

  // Method arguments are in contravariant position.

  class Kitty extends Cat

  class MyList[+A] {
    def add[B >: A](element: B): MyList[B] = new MyList[B] // widening the type
  }
  //  val evilCat = shop.get(true, new Cat)

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
    def get[S <: T](isItAPuppy: Boolean, defaultAnimal: S): S = defaultAnimal
  }

  class TerraNova extends Dog

  /*
  BIG rule:
    - method arguments are in CONTRAVARIANT position
    - return types are in COVARIANT position
   */

  /**
   * Invariant, covariant, contravariant
   * Parking[T](things: List[T]) {
   * park(vehicle: T)
   * impound(vehicles: List[T])
   * checkVehicles(conditions: String): List[T]
   * }
   *
   * 2. use someone else's API: Ilist[T]
   *
   * 3. Parking = monad!
   *    - add a flatMap
   */

  class Vehicle

  class Bike extends Vehicle

  class Car extends Vehicle

  class IList[T]

  class IParking[T](vehicles: List[T]) {
    def park(vehicle: T): IParking[T] = ???

    def impound(vehicles: List[T]): IParking[T] = ???

    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => IParking[S]): IParking[S] = ???
  }

  class CParking[+T](vehicles: List[T]) {
    // called "widening the type"
    def park[S >: T](vehicle: S): CParking[S] = ???

    def impound[S >: T](vehicles: List[S]): CParking[S] = ???

    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => CParking[S]): CParking[S] = ???
  }

  // contravariant
  class XParking[-T](vehicles: List[T]) {
    def park(vehicle: T): XParking[T] = ???
    def impound(vehicles: List[T]): XParking[T] = ???
    def checkVehicles[S <:T](conditions: String): List[S] = ???

    // Method arguments should be in a contravariant position.
    def flatMap[R <: T, S](f: T => Function1[R, XParking[S]]): XParking[S] = ???
  }

  /*
    Rule of thumb:
      - use covariance = COLLECTION of things
      - use contravariance = GROUP of actions you want to apply
   */


  // someone's else list
  class CParking2[+T](vehicles: List[T]) {
    // called "widening the type"
    def park[S >: T](vehicle: S): CParking[S] = ???
    def impound[S >: T](vehicles: IList[S]): CParking2[S] = ???
    def checkVehicles[S >:T](conditions: String): IList[S] = ???
  }

  // contravariant
  class XParking2[-T](vehicles: IList[T]) {
    def park(vehicle: T): XParking[T] = ???
    def impound[S <: T](vehicles: IList[S]): XParking[S] = ???
    def checkVehicles[S <:T](conditions: String): IList[S] = ???
  }

  // flatMap
}
