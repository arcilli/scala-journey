package lectures.part3fp

import scala.annotation.tailrec

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
  println(phoneBook.mapValues(number => number * 10))

  // conversions to other collections
  println(phoneBook.toList)
  println(List(("Daniel", 555)).toMap)

  val names = List("Bob", "James", "Angela", "Mary", "Daniel", "Jim")
  println(names.groupBy(name => name.charAt(0)))

  /*
    1. What would happen if I had 2 original entries: "Jim" -> 555 & "JIM" -> 900
    2. Overly simplified sn based on maps
      Person - string
        - add a person
        - remove a pers
        - friend  (mutual)
        - unfriend

        - noFriends of a perrs
        - the person wth most firneds
        - how many people have NO friends
        - if there is a social connection between 2 people (direct or not)
   */
  val empty: Map[String, Set[String]] = Map()
  val network = add(add(empty, "Bob"), "Mary")
  // a small net with Jim, Bob, Mary. Bob and Mary are friends, bob and mary are friends
  val people = add(add(add(empty, "Bob"), "Mary"), "Jim")
  val jimBob = friend(people, "Bob", "Jim")
  val testNet = friend(people, "Bob", "Mary")

  def add(network: Map[String, Set[String]], person: String): Map[String, Set[String]] =
    network + (person -> Set())

  println(network)
  println(friend(network, "Bob", "Mary"))
  println(unfriend(friend(network, "Bob", "Mary"), "Bob", "Mary"))
  println(remove(friend(network, "Bob", "Mary"), "Bob"))
  println(testNet)

  def nFriends(network: Map[String, Set[String]], person: String): Int =
    if (!network.contains(person)) 0
    else network(person).size

  println(nFriends(testNet, "Bob"))

  def mostFriends(network: Map[String, Set[String]]): String =
    network.maxBy(pair => pair._2.size)._1

  println(mostFriends(network))

  def nPeopleWithNoFriends(network: Map[String, Set[String]]): Int =
  //    network.view.filterKeys(k => network(k).isEmpty).size
    network.count(_._2.isEmpty)

  println(nPeopleWithNoFriends(testNet))
  println(nPeopleWithNoFriends(network))

  def friend(network: Map[String, Set[String]], a: String, b: String): Map[String, Set[String]] = {
    val friendsA = network(a)
    val friendsB = network(b)

    network + (a -> (friendsA + b)) + (b -> (friendsB + a))
  }

  def remove(network: Map[String, Set[String]], person: String): Map[String, Set[String]] = {
    @tailrec
    def removeAux(friends: Set[String], networkAcc: Map[String, Set[String]]): Map[String, Set[String]] =
      if (friends.isEmpty) networkAcc
      else
        removeAux(friends.tail, unfriend(networkAcc, person, friends.head))

    val unfriended = removeAux(network(person), network)
    unfriended - person // - method just removes the person key entirely
  }

  def unfriend(network: Map[String, Set[String]], a: String, b: String): Map[String, Set[String]] = {
    val friendsA = network(a)
    val friendB = network(b)

    network + (a -> (friendsA - b)) + (b -> (friendB - a)) // the + actually creates a new pairing
  }

  def socialConnection(network: Map[String, Set[String]], a: String, b: String): Boolean = {
    @tailrec
    def bfs(target: String, alreadyConsideredPeople: Set[String], discoveredPeople: Set[String]): Boolean = {
      // can I find the target in the discoveredPeople, having consideredPeople
      if (discoveredPeople.isEmpty) false
      else {
        val person = discoveredPeople.head
        if (person == target) true
        else if (alreadyConsideredPeople.contains(person)) bfs(target, alreadyConsideredPeople, discoveredPeople.tail)
        else bfs(target, alreadyConsideredPeople + person, discoveredPeople.tail ++ network(person))
      }
    }

    bfs(b, Set(), network(a) + a)
  }

  println(socialConnection(testNet, "Mary", "Jim"))
  println(socialConnection(testNet, "Bob", "Mary"))
}
