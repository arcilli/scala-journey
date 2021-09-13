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
  pool.execute(() => println("Something in the thread pool"))

  pool.execute( () => {
    Thread.sleep(1000)
    println("Done after 1 sec")
  })

  pool.execute( () => {
    Thread.sleep(1000)
    println("almost done")
    Thread.sleep(1000)
    println("done after 2 sec")
  })

  // No more actions can be submitted to this pool.
   pool.shutdown()
  // pool.execute(() => println("Should not appear")) // this throws an exception in the calling thread

//  pool.shutdownNow()

  println(pool.isShutdown)
}
