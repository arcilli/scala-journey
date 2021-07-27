package advanced.lectures.part3concurrency

object Intro extends App {

  /*
    interface Runnable {
      public void run()
    }
   */
  // JVM threads
  /*  val aThread = new Thread(new Runnable {
      override def run(): Unit = println("Running in parallel")
    })
    aThread.start() // create a JVM thread => OS.thread, only gives the signal to the JVM to start a JVM thread


    val runnable = new Runnable {
      override def run(): Unit = println("Call from runnable")
    }
    runnable.run() // this doesn't do anything in parallel

    aThread.join() // blocks until aThread finishes running

    val threadHello = new Thread(
      () => (1 to 5).foreach(_ => println("Hello"))
    )

    val threadGoodbie = new Thread(
      () => (1 to 5).foreach(_ => println("goodby"))
    )

    threadHello.start()
    threadGoodbie.start()*/
  // different runs produce different results, thread scheduling depends on a no parameters

  // executors
  /*  val pool = Executors.newFixedThreadPool(10)
    pool.execute(
      () => println("some thing in the thread pool")
    )

    pool.execute(
      () => {
        Thread.sleep(1000)
        println("done after 1 sec")
      }
    )

    pool.execute(
      () => {
        Thread.sleep(1000)
        println("almost done")
        Thread.sleep(4000)
        println("done after 2 sec")
      }
    )*/

  //  pool.shutdown() // no more actions can be submitted

  // The following will throw an exception in the calling thread
  /*
  pool.execute(
    () => println("should not appear")
  )
  */

  /*
  pool.shutdownNow() // interrupts the sleeping threads
  */

  //  println(s"Pool is shutdown: ${pool.isShutdown}")

  /*  def runInParallel = {
      var x = 0

      val thread1 = new Thread(
        () => {
          x=1
        }
      )

      val thread2 = new Thread (
        () => {
          x=2
        }
      )
      thread1.start()
      thread2.start()
      println(x)
    }*/

  //for (_ <- 1 to 100) runInParallel // race condition

  def buy(account: BankAccount, thing: String, price: Int) = {
    account.amount -= price // account.amount = account.amount-price
    //    println(s"I've bought $thing")
    //    println(s"my account is now $account")
  }

  // option #1: use synchronized()
  def buySafe(account: BankAccount, thing: String, price: Int) =
    account.synchronized {
      // no 2 threads can evaluate this at the same time
      account.amount -= price
      println(s"I've bought $thing")
      println(s"my account is now $account")
    }

  // option #2: use @volatile

  /*  for (_ <- 1 to 10000) {
      val account = new BankAccount(50000)
      val thread1 = new Thread(() => {
        buy(account, "shoes", 3000)
      })
      val thread2 = new Thread(() => {
        buy(account, "iphone 12", 4000)
      })

      thread1.start()
      thread2.start()
      Thread.sleep(10)
      if (account.amount != 43000) println(s"AHA: ${account.amount}")*/
  //    println()

  /*
    thread1 (shoes): 50000
    thread2 (
   */



  class BankAccount(@volatile var amount: Int) {
    override def toString: String = "" + amount
  }

  /*
    Exercises:
    1) construct 50 "inception" threads
      Thread 1 -> thread2 -> threads3 -> ...
      every thread should println("hello from thread #MyNumber")

      IN REVERSE ORDER
   */

  /*
  * 2)
  **/
  var x = 0
  val threads = (1 to 100).map(_ => new Thread( () => x+=1))
  threads.foreach(_.start())
  /*
    1. What's the biggest value possible for x?
    2. what is the SMALLEST value possible for x?
   */

  /**
   * 3. sleep fallacy
   */
  var message = ""
  val awesomeThread = new Thread(() => {
    Thread.sleep(1000)
    message = "Scala is nice"
  })

  message = "Scala sucks"
  awesomeThread.start()
  Thread.sleep(2000)
  println(message)
  /*
  what's the value for message?
  is it guaranteed?
  why + why not?
   */


  /*
  Exercises:
  1) construct 50 "inception" threads
    Thread 1 -> thread2 -> threads3 -> ...
    every thread should println("hello from thread #MyNumber")

    IN REVERSE ORDER
 */
  def inceptionThreads(maxThreads: Int, i: Int = 1): Thread = new Thread(() => {
    if (i < maxThreads){
      val newThread = inceptionThreads(maxThreads, i+1)
      newThread.start()
      newThread.join()
    }
    println(s"I am thread $i")
  })
  inceptionThreads(50).start()

  // exercise #2
  threads.foreach(_.join())
  println(x)

  message = "Scala sucks"
  awesomeThread.start()
  Thread.sleep(2000)
  println(message)
  /*
  what's the value of meesage? almost always 'Scala is awesome'
  is it guarangteed? no

  (main thread)
    message = "Scala sucks"
    aweSomeThread.start(0
    sleep() - relieves execution
  (awesome thread)
    sleep() - relieves execution
  (OS gives the CPU to some important thread - takes CPU for more thatn 2 seconds)
  (OS gives the CPU back to the MAIN thread)
    println("Scala sucks")
  (OS gives the cpu to awesomeThread)
    message = "Scala is awesome"
   */

  // how do we fix this?
  // synchronizing is not helping us
}
