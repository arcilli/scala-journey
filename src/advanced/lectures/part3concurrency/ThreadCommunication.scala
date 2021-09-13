package advanced.lectures.part3concurrency

import scala.collection.mutable
import scala.util.Random

object ThreadCommunication extends App {
  /**
   * The producer-consumer problem
   *
   * producer -> [ x ] -> consumer
   *
   */

  //  def naiveProducerCons(): Unit = {
  //    val container = new SimpleContainer
  //
  //    val consumer = new Thread(() => {
  //      println("[consumer] waiting... ")
  //      while (container.isEmpty) {
  //        println("[consumer] actively waiting...")
  //      }
  //
  //      println("[consumer] I have consumed " + container.get)
  //    })
  //
  //    val producer = new Thread(() => {
  //      println("[producer] computing....")
  //      Thread.sleep(500)
  //      val value = 42
  //      println(s"[producer] I have produced the value $value")
  //      container.set(value)
  //    })
  //
  //    consumer.start()
  //    producer.start()
  //  }

  // wait and notify
  def smartProdCons(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting...")
      container.synchronized {
        container.wait()
      }

      // at this point, the container must have some value
      println(s"[consumer] I have consumed " + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] Hard at work...")
      Thread.sleep(2000)
      val value = 42

      container.synchronized {
        println(s"[producer] I'm producing " + value)
        container.set(value)
        container.notify()
      }
    })

    consumer.start()
    producer.start()
  }

  //  smartProdCons()

  /**
   * producer -> [ ? ? ? ] -> consumer
   *
   *
   */
  def prodConsLargeBuffer(): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    val consumer = new Thread(() => {
      val random = new Random()
      while (true) {
        buffer.synchronized {
          if (buffer.isEmpty) {
            println("[consumer] buffer empty, waiting...")
            buffer.wait()
          }

          // there must be at least ONE value in the buffer.
          val x = buffer.dequeue()
          println("[consumer] consumed " + x)

          // hey producer, there's an empty space available, are you lazy or wut?
          buffer.notify()

          Thread.sleep(random.nextInt(500))
        }
      }
    })

    val producer = new Thread(() => {
      val random = new Random()
      var i = 0

      while (true) {
        buffer.synchronized {
          if (buffer.size == capacity) {
            println("[producer] buffer is full, waiting...")
            buffer.wait()
          }

          // there must be at least ONE empty space in the buffer
          println("[producer] producing " + i)
          buffer.enqueue(i)

          // Hey consumer, new food for you
          buffer.notify()

          i = i + 1
        }
        Thread.sleep(random.nextInt(500))
      }
    })

    consumer.start()
    producer.start()
  }

  //  prodConsLargeBuffer()
  /*
    Prod-cons, level 3: multiple producers & consumers on the same buffer
      producer 1 -> [ ? ? ?] -> consumer 1
      producer 2 -----^   ^---- consumer 2
   */

  class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
    override def run(): Unit = {
      val random = new Random()
      while (true) {
        buffer.synchronized {
          /*
            producer produces a value, 2 cons are waiting
            notifies ONE consumer, notifies the buffer
            notifies the other consumer
           */
          while (buffer.isEmpty) {
            println(s"[consumer $id] buffer empty, waiting...")
            buffer.wait()
          }

          // there must be at least ONE value in the buffer.
          val x = buffer.dequeue() // OOps!
          println(s"[consumer $id] consumed " + x)

          // hey producer, there's an empty space available, are you lazy or wut?
          // notify signals only one notifiers
          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
      }
    }
  }

  class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int) extends Thread {
    override def run(): Unit = {
      val random = new Random()
      var i = 0

      while (true) {
        buffer.synchronized {
          while (buffer.size == capacity) {
            println("[producer] buffer is full, waiting...")
            buffer.wait()
          }

          // there must be at least ONE empty space in the buffer
          println("[producer] producing " + i)
          buffer.enqueue(i)

          // This will notify somebody
          buffer.notify()

          i = i + 1
        }
        Thread.sleep(random.nextInt(500))
      }
    }
  }

  def multiProdCons(nConsumers: Int, nProducers: Int): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    (1 to nConsumers).foreach{
      i => new Consumer(i, buffer).start()
    }

    (1 to nProducers).foreach{
      i => new Producer(i, buffer, capacity).start()
    }
  }
  multiProdCons(3, 3)

  class SimpleContainer {
    private var value: Int = 0

    def isEmpty: Boolean = value == 0

    def set(newValue: Int) = value = newValue

    def get: Unit = {
      val result = value
      value = 0
      result
    }
  }
}

