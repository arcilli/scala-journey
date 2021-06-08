package lectures.part4pm

import exercises.{Cons, Empty}
import lectures.part2oop.Generics.MyList
import lectures.part3fp.Sequences.aList

object AllThePatterns extends App {
  // 1 - constants
  val x: Any = "Scala"
  val constants = x match {
    case 1 => "a number"
    case "Scala" => "The SCALA"
    case true => "The Truth"
    case AllThePatterns => "A singleton object"
  }

  // 2 - match anything
  // 2.1 wildcard
  val matchAnything = x match {
    case _ =>
  }

  // 2.2 variable
  val matchAVariable = x match {
    case something => s"I've found $something"
  }

  // 3 - tuples
  val aTuple = (1, 2)
  val matchATuple = aTuple match {
    case (1, 1) =>
    case (something, 2) => s"I've found $something"
  }

  val nestedTuple = (1, (2, 3))
  val matchANestedTUple = nestedTuple match {
    case (_, (2, v)) =>
  }

  // PatternMatchers can be NESTED
  // PatternMatchers can be nested with case classes as well
  // 4 - case classes - constructor pattern
  // TODO: this should work but packages...
//  val aList: MyList[Int] = Cons(1, Cons(2, Empty))
//  val matchAlist = aList match {
//    case Empty =>
//    case Cons(head, tail) =>
//  }

  // 5 - list patterns
  val aStandardList = List(1,2,3,42)
  val standardListMatching = aStandardList match {
    case List(1, _, _, _ ) => // extractor for list - advanced
    case List(1, _*) => // list of arbitrary length - advanced
    case 1 :: List(_) => // infix pattern
    case List(1, 2, 3) :+ 42  =>//infix pattern also
  }

  // 6 - type specifiers
  val unknown: Any = 2
  val unknownMatch = unknown match {
    case list: List[Int] => // explicit type specifier
    case _ =>
  }

  // 7 - name binding
//  val nameBindingMatch = aList match {
//    case notEmptyList @ Cons(_, _) => //name binding => use the name later(here)
//    case Cons(1, rest @ Cons(2, _)) => //name binding inside nested patterns
//  }

  // 8 - multi patterns
  // TODO: fix with Empty thing
//  val multipattern = aList match {
//    case Empty | Cons(0, _) => // compound pattern (multi-pattern)
//  }

  // 9 - if guards
//  val secondElementSpecial = aList match {
//    case Cons(_, Cons(specialElement, _)) if specialElement % 2 == 0 =>
//  }

  /*
    Question
   */

  val numbers = List(1, 2, 3)
  val numbersMatch = numbers match {
    case listOfStrings: List[String] => "a List of strings"
    case listOfNumbers: List[Int] => "a list of numbers"
    case _ => ""
  }
  // JVM trick question, does not support generics at this point (type erasure)
  println(numbersMatch)
}
