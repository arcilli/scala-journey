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

      while(true) {
        buffer.synchronized{
          if (buffer.size == capacity) {
            println("[producer] buffer is full, waiting...")
            buffer.wait()
          }

          // there must be at least ONE empty space in the buffer
          println("[producer] producing " + i)
          buffer.enqueue(i)

          // Hey consumer, new food for you
          buffer.notify()

          i = i+1
        }
        Thread.sleep(random.nextInt(500))
      }
    })

    consumer.start()
    producer.start()
  }

  prodConsLargeBuffer()

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

