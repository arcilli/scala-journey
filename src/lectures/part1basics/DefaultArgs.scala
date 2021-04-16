package lectures.part1basics

import scala.annotation.tailrec

object DefaultArgs extends App {

  val fact10 = trFactorial(10)

  @tailrec
  def trFactorial(n: Int, acc: Int = 1): Int = {
    if (n <= 1) acc
    else trFactorial(n - 1, n * acc)
  }

  def savePicture(format: String = "jpg", width: Int = 1920, height: Int = 1080): Unit = println("saving picture")

  savePicture("bmp")

  /*
    1. pass in every leading argument
    2. name the arguemnts
   */
  savePicture(width = 800)
  // or pass the argument in different order
}
