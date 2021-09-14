package advanced.lectures.part3concurrency

import java.util.concurrent.ForkJoinPool
import java.util.concurrent.atomic.AtomicReference


object ParallelUtils extends App {
  // 1 - parallel collections

  //  val parList = List(1, 2, 3).par

  val aParVector = ParVector[Int](1, 2, 3)

  /*
  Seq
  Array
  Vector
  Map - Hash, Trie
  Set - Hash, Trie
   */
  val list = (1 to 10000000).toList
  val serialTime = measure(list.map(_ + 1))
  val parallelTime = measure {
    list.par.map(_ + 1)
  }
  val atomic = new AtomicReference[Int](2)

  println("serial time: " + serialTime)
  println("parallel time: " + parallelTime)

  /*
    Map-reduce model
    - split the elemnts into chunks - Splitter
    - operation
    - recombine - Combine
   */

  // map, flatMap, filter, foreach, reduce, fold

  // FOLD and Reduce might not be associative, in order to safety use with `.par`
  println(List(1, 2, 3).reduce(_ - _))
  println(List(1, 2, 3).par.reduce(_ - _))
  val currentValue = atomic.get() // thread-safe, no other thread can read/write to this atomic reference
  List(1, 2, 3).par.foreach(sum += _)

  println(sum) // race conditions!

  // configuring

  // TODO: ForkJoinPool?
  aParVector.tasksupport = new ForkJoinTaskSupport(new ForkJoinPool(2)) // the #parallelism parameter is the number of threads that will manage the parallel
  // operation over the collection

  // tasksupport is a member in the parallel collection
  /*
    alternatives:
    - ThreadPoolTaskSupport - deprecated
    - ExecutionContextTaskSupport(ExecutionContext)
   */

  aParVector.taskSupport = new TaskSupport {
    override def execute[R, Tp](fjtask: Task[R, Tp]): () => R = ???

    override def executeAndWaitResult[R, Tp](task: Task[R, Tp]): R = ???

    override def parallelismLevel: Int = ???

    override val environment: AnyRef = ???
  }

  // 2 - atomic ops & references
  // synchronization
  var sum = 0

  def measure[T](operation: => T): Long = {
    val time = System.currentTimeMillis()
    operation
    System.currentTimeMillis() - time
  }
  atomic.set(4) // thread-safe write

  atomic.getAndSet(5) // thread-safe combo

  //  if (x == 42) x = 45
  atomic.compareAndSet(42, 45)
  // if the value is 42, set the value to 45, otherwise do nothing
  // it does reference equality

  // functions running on atomic references
  atomic.updateAndGet(_ + 1) // a thread-safe function run

  atomic.getAndUpdate(_ + 1) // get the old value, increment + return new value

  atomic.accumulateAndGet(12, _ + _) // thread-safe accumulation

  // reverse version
  atomic.getAndAccumulate(12, _ + _)
}
