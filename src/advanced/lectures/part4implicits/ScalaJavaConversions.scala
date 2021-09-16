package advanced.lectures.part4implicits

import java.{util => ju}
import scala.jdk.FunctionConverters.{enrichAsJavaFunction, enrichAsJavaIntFunction, enrichAsJavaIntUnaryOperator}
object ScalaJavaConversions extends App {
  import collection.JavaConverters._

  import scala.jdk.CollectionConverters._

  val javaSet : ju.Set[Int] = new ju.HashSet[Int]()
  (1 to 5).foreach(javaSet.add)
  println(javaSet)

//  val scalaSet = javaSet.asScala

  /*
  Iterator
  Iterable
  ju.List - collection.scala.mutable.Buffer
  ju.Set - collection.scala.mutable.Set
   */

  import collection.mutable._
  val numbersBuffer = ArrayBuffer[Int](1, 2, 3)
//  val juNumbersBuffer = numbersBuffer.asJava
}
