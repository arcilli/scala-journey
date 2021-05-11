package lectures.part3fp

object TuplesAndMaps extends App {
  // tuples = finite ordered "lists"
  val aTuple = new Tuple2(2, "hello, Scala") // Tuple2[Int, String] = (Int, String)

  println(aTuple._1)
  println(aTuple._2)

  println(aTuple.copy(_2 = "goodBye Java"))
  println(aTuple.swap) // => ("hello, Scala", 2)

  // Maps - keys -> values
  val aMap: Map[String, Int] = Map()

  val phoneBook = Map(("Jim", 555), ("Gigel", 789), "Bulică" -> 1).withDefaultValue(-1)
  // a -> b is a sintactic sugar for (a, b)

  println(phoneBook)
  println(phoneBook.contains("Jim"))
  println(phoneBook("Jim")) // actually, .apply() method
  println(phoneBook("Mary"))

  // TODO: check #withDefault() method in Map
  val newPairing = "Mary" -> 666
  // maps are immutable
  val newPhoneBook = phoneBook + newPairing
  println(newPhoneBook)

  // functions on maps
  // map, flatMap, filter

  println(phoneBook.map(pair => pair._1.toLowerCase -> pair._2))

  // filterKeys and mapValues
  println(phoneBook.view.filterKeys(_.startsWith("J"))) // TODO: check this
  println(phoneBook.mapValues(number => number *10))

  // conversions to other collections
  println(phoneBook.toList)
  println(List(("Daniel", 555)).toMap)

  val names = List("Bob", "James", "Angela", "Mary", "Daniel", "Jim")
  println(names.groupBy(name => name.charAt(0)))
}
