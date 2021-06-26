package advanced.exercises

import scala.annotation.tailrec

trait MySet[A] extends (A => Boolean) {

  def apply(elem: A): Boolean = contains(elem)
  /**
    * Implement a functional set
    */
  def contains(elem: A): Boolean
  def +(elem: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A]

  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]
  def foreach(f: A => Unit): Unit
}

class EmptySet[A] extends MySet[A]{
  def contains(elem: A): Boolean = false
  def +(elem: A): MySet[A] = new NonEmptySet[A](head = elem, tail=this)
  def ++(anotherSet: MySet[A]): MySet[A] = anotherSet

  def map[B](f: A => B): MySet[B] = new EmptySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]
  def filter(predicate: A => Boolean): MySet[A] = this
  def foreach(f: A => Unit): Unit = () // the <<Unit value>>
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
}