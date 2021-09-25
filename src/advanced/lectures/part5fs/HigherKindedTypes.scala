package advanced.lectures.part5fs

import scala.concurrent.Future

object HigherKindedTypes extends App {

  def multiply[A, B](listA: List[A], listB: List[B]): List[(A, B)] =
    for {
      a <- listA
      b <- listB
    } yield (a, b)

  def multiply[A, B](listA: Option[A], listB: Option[B]): Option[(A, B)] =
    for {
      a <- listA
      b <- listB
    } yield (a, b)

  trait AHigherKindedType[F[_]]

  trait MyList[T] {
    def flatMap[B](f: T => B): MyList[B]
  }

  // combine/multiply List(1,2) x List("a", "b") => List("1a", "1b", "2a", "2b")

  trait MyOption[T] {
    def flatMap[B](f: T => B): MyOption[B]
  }

  trait MyFuture[T] {
    def flatMap[B](f: T => B): MyFuture[B]
  }



  //  def multiply[A, B](listA: Future[A], listB: Future[B]): Future[(A, B)] =
  //    for {
  //      a <- listA
  //      b <- listB
  //    } yield (a, b)

  // use HKT
  // TO remember: the fundamental operation of a Monad is #flatMap
  trait Monad[F[_], A] {
    def flatMap[B](f: A => F[B]): F[B]
    def map[B](f: A => B): F[B]
  }

  class MonadList[A](list: List[A]) extends Monad[List, A] {
    override def flatMap[B](f: A => List[B]): List[B] = list.flatMap(f)
    override def map[B](f: A => B): List[B] = list.map(f)
  }

  def multiply[F[_], A, B](ma: Monad[F, A], mb: Monad[F, B]): F[(A,B)] =
    for {
      a <- ma
      b <- mb
    } yield (a,b)


  val monadList = new MonadList(List(1,2,3))
  monadList.flatMap(x => List(x, x+1))
  // Monad[List, Int] => List[Int]
  monadList.map(_ * 2)
  // Monad[List, Int] => List[Int]



  multiply(new MonadList(List(1,2,3)), new MonadList("a", "b"))
}
