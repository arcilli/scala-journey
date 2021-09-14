package advanced.lectures.part3concurrency

import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Random, Success, Try}

object FuturesAndPromises extends App {
  val aFuture = Future {
    calculateMeaningOfLife // calculates the meaning of life on another thread
  } // (global) - passed by the compiler
  // client: mark to poke bill
  val mark = SocialNetwork.fetchProfile("fb.id.1-zuck")

  println(aFuture.value) // Option[Try[Int]]

  println("Waiting on the future")
  aFuture.onComplete {
    case Success(meaningOfLife) => println(s"The meaning of life is $meaningOfLife")
    case Failure(exception) => println(s"I have failed with $exception")
  } // SOME THREAD will run this, we make no assumption

  Thread.sleep(3000)
  // functional composition of futures
  // map, flatMap & filter
  val nameOnTheWall = mark.map(profile => profile.name)
  val marksBestFriend = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))
  val zucksBestFriendRestricted = marksBestFriend.filter(profile => profile.name.startsWith("Z"))

  mark.onComplete {
    case Success(markProfile) => {
      val bill = SocialNetwork.fetchBestFriend(markProfile)
      bill.onComplete {
        case Success(billProfile) => markProfile.poke(billProfile)
        case Failure(e) => e.printStackTrace()
      }
    }
    case Failure(exception) => exception.printStackTrace()
  }
  // fallbacks
  val aProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recover {
    case e: Throwable => Profile("fb.id.0-dummy", "4ever alone")
  }
  val aFetchedProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recoverWith {
    case e: Throwable => SocialNetwork.fetchProfile("fb.id.0-dummy")
  }
  val fallbackResult = SocialNetwork.fetchProfile("unknown id").fallbackTo(SocialNetwork.fetchProfile("fb.id.0-dummy"))

  // for-comprehensions
  for {
    mark <- SocialNetwork.fetchProfile("fb.id.1-zuck")
    bill <- SocialNetwork.fetchBestFriend(mark)
  } mark.poke(bill)
  Thread.sleep(1000)
  // promises
  val promise = Promise[Int]() // "controller" over a future
  val future = promise.future
  // thread 2 - "producer"
  val producer = new Thread(() => {
    println("[producer] crunching numbers... ")
    Thread.sleep(500)

    // "fulfilling" the promise
    promise.success(42)
    println("[producer] done")
  })

  def calculateMeaningOfLife: Int = {
    Thread.sleep(2000)
    42
  }

  /**
   *
   * 1) fulfill a future IMMEDIATELY with a value
   * 2) inSequence(fa, fb), runs fb after making sure fa is completed
   * 3) first(fa, fb) => new future with the first value of the 2 futures
   * 4) last(fa, fb) => new future with the last value of the 2 futures
   * 5) retryUntil[T](action: () => Future[T], condition: T => Boolean): Future[T]
   */

  // 1 - fulfill immediately
  def fulfillImmediately[T](value: T): Future[T] = Future(value)

  // 2 - insequence
  def inSequence[A, B](first: Future[A], second: Future[B]): Future[B] = first.flatMap(_ => second)

  println(BankingApp.purchase("Daniel", "iPhone 12", "JVM store", 3000))

  // 3 - first out of 2 futures
  def first[A](fa: Future[A], fb: Future[A]): Future[A] = {
    val promise = Promise[A]

    def tryComplete(promise: Promise[A], result: Try[A]) = result match {
      case Success(value) => try {
        promise.success(value)
      } catch {
        case _ =>
      }
      case Failure(throwable) => try {
        promise.failure(throwable)
      } catch {
        case _ =>
      }
    }

    fa.onComplete(promise.tryComplete)

    fb.onComplete(promise.tryComplete)
    promise.future
  }

  // 4 - last out of 2 futures
  def last[A](fa: Future[A], fb: Future[A]): Future[A] = {
    // 1 promise which both futures will try to complete
    // 2nd promise which the LAST future will complete
    val bothPromise = Promise[A]
    val lastPromise = Promise[A]
    val checkAndComplete= (result: Try[A]) =>
    if (!bothPromise.tryComplete(result))
      lastPromise.complete(result)

    fa.onComplete(checkAndComplete)

    fb.onComplete(checkAndComplete)
    lastPromise.future
  }

  val fast = Future {
    Thread.sleep(100)
    42
  }

  val slow = Future {
    Thread.sleep(200)
    45
  }
  first(fast, slow).foreach(println)
  last(fast, slow).foreach(println)
  Thread.sleep(1000)

  // mini social network
  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile): Unit = {
      println(s"${this.name} poking ${anotherProfile.name}")
    }
  }

  // thread 1 - "consumer"
  future.onComplete {
    case Success(r) => println("[consumer] I've received " + r)
  }

  // online banking app
  case class User(name: String)

  producer.start()
  Thread.sleep(1000)

  // retry until
  def retryUntil[A](action: () => Future[A], condition: A => Boolean): Future[A] =
    action()
      .filter(condition)
      .recoverWith {
        case _ => retryUntil(action, condition)
      }

  val random = new Random()
  val action = () => Future {
    Thread.sleep(100)
    val nextValue = random.nextInt(100)
    println("generated: " +nextValue)
    nextValue
  }

  retryUntil(action, (x: Int) => x<50).foreach(result => println("settled at " + result))

  case class Transaction(sender: String, receiver: String, amount: Double, status: String)

  object SocialNetwork {
    // "database"
    val names = Map(
      "fb.id.1-zuck" -> "Mark",
      "fb.id.2-bill" -> "Bill",
      "fb.id.0-dummy" -> "Dummy"
    )

    val friends = Map(
      "fb.id.1-zuck" -> "fb.id.2-bill"
    )

    val random = new Random()

    // API
    def fetchProfile(id: String): Future[Profile] = Future {
      // fetching from the DB
      Thread.sleep(random.nextInt(300))
      Profile(id, names(id))
    }

    def fetchBestFriend(profile: Profile): Future[Profile] = Future {
      Thread.sleep(random.nextInt(400))
      val bfId = friends(profile.id)
      Profile(bfId, names(bfId))
    }
  }

  object BankingApp {
    val name = "jvm banking"

    def purchase(username: String, item: String, merchantName: String, cost: Double): String = {
      // fetch the user from the db
      // create a transaction from the username to the merchantName
      // wait 'til the transaction is finished
      val transactionStatusFuture = for {
        user <- fetchUser(username)
        transaction <- createTransaction(user, merchantName, cost)
      } yield transaction.status

      import scala.concurrent.duration._
      Await.result(transactionStatusFuture, 2.seconds) // -> pimp my library

    }

    def fetchUser(name: String): Future[User] = Future {
      // simulate a long computation/fetching from DB
      Thread.sleep(500)
      User(name)
    }

    def createTransaction(user: User, merchantName: String, amount: Double): Future[Transaction] = Future {
      // simulate some processes
      Thread.sleep(1000)
      Transaction(user.name, merchantName, amount, "success")
    }
  }
}
