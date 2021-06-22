package beginners.lectures.part2oop

import beginners.playground.{PrinceCharming => Prince}

object PackagingAndImports extends App {

  // package members are accessible by their simple name
  val writer = new Writer("Daniel", "RockTheJvm", 2018)

  // package objects
  sayHello
  println(SPEED_OF_LIGHT)

  // imports
  val prince = new Prince
}
