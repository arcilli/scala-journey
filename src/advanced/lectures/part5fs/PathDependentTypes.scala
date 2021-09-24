package advanced.lectures.part5fs

object PathDependentTypes extends App {

  class Outer {
    class Inner
    object InnerObject
    type InnerType

    def print(i: Inner) = println(i)
    def printGeneral(i: Outer#Inner) = println(i)
  }

  def aMethod: Int = {
    class HelperClass
    type HelperType = String
    2
  }

  // per instance
  val o = new Outer
  val inner = new o.InnerType // o.Inner is a TYPE

  val oo = new Outer
//  val otherInner: oo.Inner = new o.Inner -- this does not compile

//  o.print(inner)
//  oo.print(inner)

  // Called "path-dependent" type

  // Outer#Inner
  // Why those 2 are not working?

//  o.printGeneral(inner)
//  oo.printGeneral(inner)

  /*
  Exercise
  DB keyed by INT or String
   */

  /*
  Hints:
    1. Use path-dependent types
    2. Abstract type members and/or type aliases
   */

  trait ItemLike {
    type Key
  }

  trait Item[K] extends ItemLike {
    type Key = K
  }

  trait Item[Key]
  trait IntItem extends Item[Int]
  trait StringItem extends Item[String]

  def get[ItemType <: ItemLike](key: ItemLike#Key): ItemType = ???

  get[IntItem](42) = ??? // this should compile
  get[StringItem]("home") = ??? //ok
  get[IntItem]("scala") // not ok
}
