package advanced.exercises

import scala.annotation.tailrec

trait MySet[A] extends (A => Boolean) {

  def apply(elem: A): Boolean = contains(elem)
  /**
    * Implement a functional set
    */
  def contains(elem: A): Boolean
  def +(elem: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A] // Union

  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]
  def foreach(f: A => Unit): Unit

  def -(v: A): MySet[A] // removing an Element
  def &(anotherSet: MySet[A]): MySet[A] //intersection
  def --(anotherSet: MySet[A]): MySet[A] // diference

  // Exercice #3 - implement a unary_! = NEGATION of a set
  // set[1, 2, 3] => set[everything, but not 1, 2, 3]
  def unary_! : MySet[A]
}

class EmptySet[A] extends MySet[A]{
  def contains(elem: A): Boolean = false
  def +(elem: A): MySet[A] = new NonEmptySet[A](head = elem, tail=this)
  def ++(anotherSet: MySet[A]): MySet[A] = anotherSet

  def map[B](f: A => B): MySet[B] = new EmptySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]
  def filter(predicate: A => Boolean): MySet[A] = this
  def foreach(f: A => Unit): Unit = () // the <<Unit value>>

  // part 2
  def -(v: A): MySet[A] = this
  def &(anotherSet: MySet[A]): MySet[A] = this
  def --(anotherSet: MySet[A]): MySet[A] = this
//  def unary_! : MySet[A]

  // part 3
  def unary_! : MySet[A] = new PropertyBasedSet[A](_ => true)
}

/*
class AllInclusiveSet[A] extends MySet[A] {

  /**
   * Implement a functional set
   */
  override def contains(elem: A): Boolean = true

  override def +(elem: A): MySet[A] = this

  override def ++(anotherSet: MySet[A]): MySet[A] = this

  // naturals = allInclusiveSet[Int] = all the natural numbers
  // naturals.map(x => x%3) => ???
  // [0 1 2]
  override def map[B](f: A => B): MySet[B] = ???
  override def flatMap[B](f: A => MySet[B]): MySet[B] = ???
  override def filter(predicate: A => Boolean): MySet[A] = ??? // property-based set
  override def foreach(f: A => Unit): Unit = ???
  override def -(v: A): MySet[A] = ???

  override def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)
  override def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)

  override def unary_! : MySet[A] = new EmptySet[A]
}

 */

// all elements of type A which satisfy a property
// {x in A | property(x)}
class PropertyBasedSet[A](property: A => Boolean) extends MySet[A]{
  /**
   * Implement a functional set
   */
  override def contains(elem: A): Boolean = property(elem)

  /**
   *
    {x in A| property(x) } + element = {x in A| property(x) || x == element}
   */
  override def +(elem: A): MySet[A] =
    new PropertyBasedSet[A](x => property(x) || x==elem)

  /**
   * {x in A | property(x)} ++ set => {x in A | property(x) || set contains x}
   */
  override def ++(anotherSet: MySet[A]): MySet[A] =
    new PropertyBasedSet[A](x => property(x) || anotherSet(x))

  override def map[B](f: A => B): MySet[B] = politelyFail

  override def flatMap[B](f: A => MySet[B]): MySet[B] = politelyFail

  override def filter(predicate: A => Boolean): MySet[A] = new PropertyBasedSet[A](x => property(x) && predicate(x))

  override def foreach(f: A => Unit): Unit = politelyFail

  /**
   * Removing an element
   */
  override def -(v: A): MySet[A] = filter(x => x!=v)

  override def &(anotherSet: MySet[A]): MySet[A] =
    filter(anotherSet)

  override def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)

  override def unary_! : MySet[A] = new PropertyBasedSet[A](x => !property(x))

  def politelyFail = throw new IllegalArgumentException("Really deep rabbit hole!")
}

class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {
  def contains(elem: A): Boolean =
    head == elem || tail.contains(elem)

  def +(elem: A): MySet[A] =
    if (this.contains(elem)) this
    else new NonEmptySet[A](head = elem, tail = this)

  /**
   * [1 2 3] ++ [4 5] =
   * [2 3] ++ [4 5] + 1 =
   * [3] ++ [4 5] + 1 + 2 =
   * [] ++ [4 5] + 1 + 2 +3 =
   * [4 5] + 1 +2 +3 = [4 5 1 2 3]
   */
  def ++(anotherSet: MySet[A]): MySet[A] =
    tail ++ anotherSet + head

  def map[B](f: A => B): MySet[B] =( tail map f)+ f(head)

  def flatMap[B](f: A => MySet[B]): MySet[B] = (tail flatMap f) ++ f(head)

  def filter(predicate: A => Boolean): MySet[A] = {
    val filteredTail = tail filter predicate
    if (predicate(head)) filteredTail + head
    else filteredTail
  }

  def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }

  /**
   * Exercise
   * - removing an element
   * - intersection with another set
   * - difference with another set
   */

  // part 2
  def -(element: A): MySet[A] = { // removing an element
    if (head == element) tail
    else tail - element + head
  }

  // intersecting
  def &(anotherSet: MySet[A]): MySet[A] = {
    filter(anotherSet) // anotherSet is actually anotherSet.apply(), which can be reduced to .filter(x => anotherSet(x),
    // which can be reduced to filter(anotherSet(_)) = filter(anotherSet)
  }

  def --(anotherSet: MySet[A]): MySet[A] = {
    this.filter(!anotherSet.contains(_))
  }

  // new operator
  def unary_! : MySet[A] = new PropertyBasedSet[A](x => !this.contains(x))
}

object MySet{
  /*
    val s = MySet(1, 2, 3) = buildSet(seq(1,2,3), [])
    = buildSet(seq(2, 3), [] +1) =
    = buildSet(seq(3), [1] + 2) =
    = buildSet(seq(), [1, 2] + 3)
    = [1, 2, 3]
   */
  def apply[A](values: A*): MySet[A] = {
    @tailrec
    def buildSet(valSeq: Seq[A], acc: MySet[A]): MySet[A] = {
      if (valSeq.isEmpty) acc
      else buildSet(valSeq.tail, acc + valSeq.head)
    }
    // TODO: inspect this one
    buildSet(values.toSeq, new EmptySet[A])
  }
}

object MySetPlayground extends App {
  val s = MySet(1, 2, 3, 4)
  println("Foreach")
  s.foreach(println)
  println("S+5 foreach")
  s +5 foreach println

  println("s+5 ++ MySet(-1, -2) foreach println")
  s+5 ++ MySet(-1, -2) foreach println

  println("s+5+5 ++ MySet(-1, -2) foreach println")
  s+5 +5 ++ MySet(-1, -2) foreach println

  s+5 ++ MySet(-1, -2) + 3 map(x => x*10) foreach println


  s+5 ++ MySet(-1, -2) + 3 flatMap (x => MySet(x, 10*x)) foreach println

  s+5 ++ MySet(-1, -2) + 3 flatMap (x => MySet(x, 10*x)) filter (_ % 2 ==0) foreach println

  val negative = !s // s.unary_! = all the natural not equal to 1, 2, 3, 4
  println(negative(2))
  println(negative(5))

  val negativeEven = negative.filter(_ %2 ==0)
  println(negativeEven(5))

  val negativeEven5 = negativeEven + 5 //all the even numbers >4 +5
  println(negativeEven5(5))
}