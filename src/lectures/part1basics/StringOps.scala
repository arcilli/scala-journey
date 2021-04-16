package lectures.part1basics

object StringOps extends App {
  val str: String = "Hello, I'm learning Scala"
  println(str.charAt(2))

  println(str.split(" ").toList)
  println(str.startsWith("Hello"))
  println(str.replace(" ", "-"))
  println(str.toLowerCase())
  println(str.toUpperCase())
  println(str.length)

  val aNumberString = "2"
  val aNumber = aNumberString.toInt
  println('a' +: aNumberString :+ 'z')

  println(str.reverse)
  println(str.take(2))

  // Scala-specific: String interpolators

  // S-interpolators
  val name= "David"
  val age = 12
  val greeting = s"Hello, my name is $name and I have ${age} years old."
  println(greeting)

  // F-interpolators
  val speed = 1.2f
  val myth = f"$name can eat $speed%2.2f burgers per minute"
  println(myth)

  // raw interpolator
  println(raw"This is a \n newline")

  val escaped = "This is a \n newline"
  println(escaped)
  println(raw"$escaped")
}
