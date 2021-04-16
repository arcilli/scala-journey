package lectures.part1basics

object CBNcsCBV extends App {
  def calledByValue(x: Long): Unit = {
    println("by value: " + x)
    println("by value: " + x)
  }

  def calledByName(x: => Long): Unit = {
    println("by name: " + x)
    println("by name: " + x)
  }

  calledByValue(System.nanoTime())

  calledByName(System.nanoTime())

  def infinite(): Int = 1 + infinite()

  // The "by name" parameter delays the evaluation of the expression passed to when it's used.
  def printFirst(x:  Int, y: => Int) = println(x)

  // This will crash.
//  printFirst(infinite(), 34)

  // This will work fine, it will not crash, since the _infinite()_ will never be evaluated.
  printFirst(34, infinite())
}
