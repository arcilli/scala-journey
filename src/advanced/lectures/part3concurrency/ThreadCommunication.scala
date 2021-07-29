package advanced.lectures.part3concurrency

import scala.collection.mutable
import scala.util.Random

object ThreadCommunication extends App{

  class SimpleContainer {
    private var value: Int =0

    def isEmpty: Boolean = value == 0

    def get = {
      val result = value
      value = 0
      result
    }

    def set(newValue: Int) = value = newValue
  }
  def naiveProdCons(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting...")
      while (container.isEmpty) {
        println("[consumer] actively waiting")
      }

      println("[consumer] I have consumed " + container.get)
    })

    val producer = new Thread(new Runnable {
      override def run(): Unit = {
        println("[producer] computing...")
        val value = 42
        println("[producer] I have produced, after long work, the value " + value)
      }
    })

    consumer.start()
    producer.start()
    // object's monitor?
  }
  // wait and notify!! <3
  def smartProducerConsumer(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[Consumer] waiting")
      container.synchronized {
        container.wait()
      }
      // container must have some value
      println("[Consumer] I consumed " + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] Hard @ work...")
      Thread.sleep(2000)
      val value = 42

      container.synchronized {
        println("[producer] I'm producing " + value)
        container.set(value)
        container.notify()
      }
    })
    consumer.start()
    producer.start()
  }
  smartProducerConsumer()

  /*
    producer -> [ ? ? ?] -> consumer extract any value that's new
   */

  def prodConsLargeBuffer(): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    val consumer = new Thread( () => {
      val random = new Random()

      while(true) {
        buffer.synchronized{
          if (buffer.isEmpty) {
            println("[consumer] buffer empty... waiting")
            buffer.wait()
          }

          // there must be at LEAST ONE value in the buffer
          val x = buffer.dequeue()
          println(s"[consumer] consumed $x")
        }
      })
    })
  }
}
