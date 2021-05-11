package lectures.part3fp

object MapFlatmapFilterFor extends App {
  val list = List(1, 2, 3)
  println(list)
  println(list.head)
  println(list.tail)

  // map
  println(list.map(_ + 1))
  println(list.map(_ + " is a number"))

  // filter
  println(list.filter(_ % 2 == 0))

  // flatMap
  val toPair = (x: Int) => List(x, x + 1)
  println(list.flatMap(toPair))

  // print all combination between two lists
  val numbers = List(1, 2, 3, 4)
  val chars = List('a', 'b', 'c', 'd')
  val colors = List("black", "white")

  val combinations = numbers.flatMap(n => chars.map(c => n + "" + c))
  println(combinations)

  val combinationsWithColors = numbers.flatMap(n => chars.flatMap(c => colors.map(color => color + "-" + c + "-" + n)))
  println(combinationsWithColors)

  // foreach
  list.foreach(println)

  // for-comprehensions
  val forCombinations = for {
    n <- numbers if n % 2 == 0
    c <- chars
    color <- colors
  } yield "" + c + n + "-" + color

  println(forCombinations)

  for {
    n <- numbers
  } println(n)

  // syntax overload
  list.map {
    x => x * 2
  }

  /*
    1. MyList supports for comprehensions?
      map (f: A => B) => MyList[B]
      filter (p: A => Boolean) => MyList[A]
      flatMap (f: A => MyList[B]) => MyList[B]
    2. Implement a small collection of at most ONE element - Maybe[+T]
        - map, flatMap, filter
   */

}