package lectures.part1basics

object Expressions extends App {
  val x = 1 + 2 // expression
  println(x)

  println(2 + 3 * 4)

  println(1 == x)

  println(!(1 == x))
  // if expression, not an if instruction
  val aCondition = true
  aVariable += 3 // this produces side effects
  println(aVariable)

  // instructions (DO) something; something that is executed vs expressions (value), which are evaluated
  val aConditionedValue = if (aCondition) 8 else 3
  // everything in scala should be an expression
  val aWeirdValue = (aVariable = 3) // Unit ==== void
  println(aConditionedValue)


  val aWhile = while (i < 10) {
    println("$i")
    i += 1
  }
  // code blocks, that is an expression, like everything in scala. The value of the codeBlock is the value from the last
  // line in the block
  val aCodeBlock = {
    val y = 2
    val z = y + 1

    if (z > 2) "hello" else "bye"
  }

  // side effects: println(), whiles, reassigning

  // looping is more specific to imperative programming
  while (i < 10) {
    println(i)
    i += 1
  }
  // 1. diff between "hello world" vs println("hello world"): the string is a value of type string, println is an
  // expression, which is an side effects, which returns a unit
  // 2.
  val someValue = {
    2 < 3
  }
  println(aWeirdValue) // () is the only value of an Unit
  val someOtherValue = {
    if (someValue) 239 else 986
    42
  }

  // z is defined inside the codeBlock, it's not visible anymore
  //  val anotherValue = z+1
  var i = 0
  // side effects: println(), whiles, reassigning
  var aVariable = 2
}
