package advanced.lectures.part2advancedFunctionalProgramming

object Monads extends App {

  // our own Try monad
  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }

  object Attempt {
    def apply[A](a: => A): Attempt[A] =
      try {
        Success(a)
      } catch {
        case e: Throwable => Fail(e)
      }

  }

  case class Success[+A](value: A) extends Attempt[A]{
    override def flatMap[B](f: A => Attempt[B]): Attempt[B] =
      try{
        f(value)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Fail(e: Throwable) extends Attempt[Nothing] {
    override def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

  /*
  * monad laws
  * 1. left- identity
  *
  * unit.flatMap(f) = f(x)
  * Attempt(x).flatMap(f) = f(x) // Success case!
  * Success(x).flatMap(f) = f(x) // Proved
  *
  * 2. right-identity
  * attempt.flatMap(unit) = attempt
  * Success(x).flatMap(x => Attempt(x)) = Attempt(x) = Success(x)
  * Fail(e).flatMap(...) = Fail(_)
  *
  * 3. associativity
  * attempt.flatMap(f).flatMap(g) == attempt.flatMap(x => f(x).flatMap(g))
  * Fail(e).flatMap(f).flatMap(g) == Fail(e)
  * Fail(e).flatMap(x => f(x).flatMap(g)) = Fail(e)
  *
  * Success(v).flatMap(f).flatMap(g) = f(v).flatMap(g) OR Fail(e)
  *
  * Success(v).flatMap(x => f(x).flatMap(g)) = f(v).flatMap(g) OR Fail(e)
   */

  val attempt = Attempt {
    throw new RuntimeException("My own monad")
  }

  println(attempt)

  /*
    EXERCISE:
    1) implement a Lazy[T] monad = computation which will only be executed when it's needed
    - unit/apply in a companion object for Lazy trait
    - flatMap

    2) Monads = unit + flatMap
       Monads = unit + map + flatten

       Transform a monad (unit + flatMap) into a Monad (unit + map +flatten)

       Monad[T] {
        def flatMap[B](f: T=> Monad[B]): Monad[B] = ... (implemented)

        def map[B](f: T => B): Monad[B] = ???
        def flatten(m: Monad[Monad[T]]): Monad[T] = ???

        (have List in mind)
       }
   */

  // 1 - Lazy monad

  // by name prevents the evaluation
  class Lazy[+A](internalValue: => A) {
    def flatMap[B](f: (=>A) => Lazy[B]): Lazy[B] = f(internalValue)
    def use: A = internalValue
  }

  object Lazy {
    def apply[A](value: => A): Lazy[A] = new Lazy(value) // unit
  }

  val lazyInstance = Lazy {
    println("Lazy person Bruno Mars")
    42
  }
  println(lazyInstance.use)

  val flatMappedInstance = lazyInstance.flatMap(x => Lazy{
    10 *x
  })

  val flatMappedInstance2 = lazyInstance.flatMap(x => Lazy{
    10 *x
  })

  flatMappedInstance.use
  flatMappedInstance2.use

  /*
    left-identity
    unit.flatMap(f) = f(v)
    Lazy(v).flatMap(f) = f(v)

    right-identity
    1.flatMap(unit) = 1
    Lazy(v).flatMap(x => Lazy(x)) = Lazy(v)

    associativity law: 1. flatMap(f).flatMap(g) = 1.flatMap(x => f(x).flatMap(g))

    Lazy(v).flatMap(f).flatMap(g) = f(v).flatMap(g)
    Lazy(v).flatMap(x => f(x).flatMap(g)) = f(v).flatMap(g)
   */

  /*
    Exercise 2: map & flatten in terms of flatMap

    Monad[T] {
      def flatMap[B](f: T => Monad[B]): Monad[B] = ... (implemneted)

      def map[B](f: T => B): Monad[B] = flatMap(x => unit(f(x))) // Monad[B]
      def flatten(m: Monad[Monad[T]]): Monad[T] = m.flatMap((x: Monad[T]) =>x)
    }

    List(1, 2, 3).map(_ * 2) = List(1, 2, 3).flatMap(x => List(x *2))

    flattening: List(List(1, 2), List(3, 4)).flatten = List(List(1, 2), List(3, 4)).flatMap(x => x) = List(1, 2, 3, 4)
   */

}
