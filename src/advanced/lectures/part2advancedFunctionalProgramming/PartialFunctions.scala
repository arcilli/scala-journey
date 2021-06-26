package advanced.lectures.part2advancedFunctionalProgramming

object PartialFunctions extends App{
  val aFunction = (x: Int) => x+1 //Function1[Int, Int] === Int => Int

  val aFussyFunction = (x:Int) =>
  if (x==1) 42
  else if (x==2) 56
  else if (x==5) 999
  else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  val aNicerFussyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }

  // {1, 2, 5} => Int
  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  } // partial function value

//  println(aPartialFunction(52112))

  // PF utilities
  println(aPartialFunction.isDefinedAt(67))

  // lift
  val lifted = aPartialFunction.lift // converts the function to a one Int => Option[Int]
  println(lifted(2))
  println(lifted(98))
  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 45 => 67
  }

  println(pfChain(2))
  println(pfChain(45))

  // PF = partial function extend normal functions
  val aTotalFunction: Int => Int = {
    case 1 => 99
  }

  // HOFs accept partial functions as well
  val aMappedList = List(1, 2, 3).map {
    case 1 => 42
    case 2 => 78
    case 3 => 1000
  }

  println(aMappedList)
  /*
    Note: PF can only have ONE parameter type
   */


  /**
   * Exercises
   *
   * 1 - construct a PF instance yourself (anonymous class)
   * 2 - dumb chat as PF
   */

  val pfInstance = new PartialFunction[String, Unit] {
    override def isDefinedAt(x: String): Boolean = x match {
      case "hello" => true
      case "it it me" => true
      case _ => false
    }

    override def apply(v1: String): Unit = println(s"you said $v1")
  }

  val chatBot: PartialFunction[String, String] = {
    case "hello" => "Hi, my name si HALL9000"
    case "goodbye" => "Bye sucka"
  }

  scala.io.Source.stdin.getLines().map(chatBot).foreach(println)
}
