package advanced.lectures.part3concurrency

import java.util.concurrent.Executors

object Intro extends App {
  /*
    interface Runnable {
      public void run()
    }
   */
  // JVM threads
  val runnable = new Runnable {
    override def run(): Unit = println("Running in parallel")
  }


  val aThread = new Thread(runnable)

  aThread.start() // this only gives the signal to the JVM to start a JVM thread
  // create a JVM thread => OS thread

  aThread.join() // blocks until aThread finishes running

  val threadHello = new Thread(() => (1 to 5).foreach(
    _ => println("hello")
  ))

  val threadGoodbye = new Thread(() => (1 to 5).foreach(
    _ => println("goodbye")
  ))

  threadHello.start()
  threadGoodbye.start()
  // different runs produce different results!
  // thread scheduling depends on a number of factors


  // executors
  val pool = Executors.newFixedThreadPool(10)
//  pool.execute(() => println("Something in the thread pool"))
//
//  pool.execute( () => {
//    Thread.sleep(1000)
//    println("Done after 1 sec")
//  })
//
//  pool.execute( () => {
//    Thread.sleep(1000)
//    println("almost done")
//    Thread.sleep(1000)
//    println("done after 2 sec")
//  })

  // No more actions can be submitted to this pool.
   pool.shutdown()
  // pool.execute(() => println("Should not appear")) // this throws an exception in the calling thread

//  pool.shutdownNow()

  println(pool.isShutdown) // true

//  def runInParallel: Unit = {
//    var x = 0
//
//    val thread1 = new Thread(() => {
//      x = 1
//    })
//
//    val thread2 = new Thread(() => {
//      x = 2
//    })
//
//    thread1.start()
//    thread2.start()
//    println(x)
//  }

  // race condition
//  for (_ <- 1 to 10000) runInParallel

  class BankAccount (var amount: Int) {
    override def toString: String = "" + amount
  }

  def buy(account: BankAccount, thing: String, price: Int): Unit = {
    account.amount -= price
//    println("I've bought" + thing)
//    println("my account is now " + account)
  }

//  for (_ <- 1 to 1000) {
//    val account = new BankAccount(50000)
//    val thread1 = new Thread(() => buy(account, "shoes", 3000))
//    val thread2 = new Thread(() => buy(account, "iphone 12", 4000))
//
//    thread1.start()
//    thread2.start()
//
//    Thread.sleep(10)
//    if (account.amount != 43000) println("AHA: " + account.amount)
////    println()
//
//  }
  /*
    thread1 (shoes): 50000
      - account = 50000 -3000 = 47000
    thread2 (iphone): 50000
      - accunt = 50000 - 4000 = 46000 -> overwrites the memory of account.amoutn
   */

  // option #1: use synchronized()
  def buySafe(account: BankAccount, thing: String, price: Int): Unit = {
    account.synchronized {
      // no 2 threads can evaluate this at the same time
      account.amount -= price
      println("I've bought" + thing)
      println("my account is now " + account)
    }
  }

  // option #2: use @volatile -> when added to a variable, all reads & writes to that variable are synchronized
//  class BankAccount (@volatile var amount: Int) {

  /**
   * Exercises
   *
   * 1) Construct 50 "inception" threads:
   *    Thread1 -> thread2 -> threa3 -> ...
   *    each thread println("hello from thread $myNumber htread")
   *  IN REVERSE ORDER
   *
   *
   */

    def inceptionThreads(maxThreads: Int, i: Int = 1): Thread = new Thread(() => {
      if (i < maxThreads) {
        val newThread = inceptionThreads(maxThreads, i+1)
        newThread.start()

        newThread.join()
      }
      println(s"Hello from thread $i")
    })
    inceptionThreads(50).start()
  /*
   * 2)
   */
  var x =0
  val threads = (1 to 100).map(_ => new Thread(() => x+1))
  threads.foreach(_.start())

  /*
  1. What's the biggest value possible for x? 100?
  2. What's the smallest value possible for x? 1?
   */

  /*
    3. sleep fallacy
   */
  var message = ""
  val awesomeThread = new Thread(() => {
    Thread.sleep(1000)
    message = "Scala is awesome"
  })

  message = "Scala sucks"
  awesomeThread.start()
  Thread.sleep(2000)
  println(message)

  // What's the value of message?
  // Is it guaranteed?
  // Why & why not?
}