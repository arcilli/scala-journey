package advanced.lectures.part4implicits

import java.util.Date

object JSONSerialization extends App {
  /*
    Users, posts, feeds
    Serialize to JSON
   */
  case class User(name: String, age: Int, email: String)
  case class Post(content: String, createdAt: Date)
  case class Feed(user: User, posts: List[Post])

  /*
  1 - create intermediate data types: Int, String, List, Date
  2 - type classes for conversion to intermediate data types
  3 - serialize to JSON
  */

  sealed trait JSONValue { // intermediate data type
    def stringify: String
  }

  final case class JSONString(value: String) extends JSONValue {
    override def stringify: String =
      "\"" + value + "\""
  }

  final case class JSONNumber(value: Int) extends JSONValue {
    override def stringify: String = value.toString
  }

  final case class JSONArray(values: List[JSONValue]) extends JSONValue {
    override def stringify: String = values.map(_.stringify).mkString("[", ",", "]")
  }

  final case class JSONObject(values: Map[String, JSONValue]) extends JSONValue {
    /*
      {
        name: "John",
        age: 22,
        friends: [...],
        latestPosts: {
          "content: "Scala rocks",
          "date": ...
        }
      }
     */
    override def stringify: String = values.map {
      case (key, value) => "\"" + key + "\":" + value.stringify
    }
      .mkString("{", ",", "}")
  }

  val data = JSONObject(Map(
    "user" -> JSONString("Gigel"),
    "posts" -> JSONArray(List(
      JSONString("Scala"),
      JSONNumber(453)
    ))
  ))

  println(data.stringify)

  // type class
  /*
    for type classes we basically need 3 fundamental things:
      1) the typeclass itslef
      2) typeclass instances (implicit)
      3) method to use: pimp library to use type class instances
   */

  // step 2.1, the typeclass itself
  trait JSONConverter[T]{
    def convert(value: T): JSONValue
  }

  // 2. 3rd step: conversion
  implicit class JSONOps[T](value: T) {
    def toJSON(implicit converter: JSONConverter[T]): JSONValue = {
      converter.convert(value)
    }
  }

  // step 2.2, our typeclass instances

  // for existing data types
  implicit object StringConverter extends JSONConverter[String] {
    override def convert(value: String): JSONValue = JSONString(value)
  }

  implicit object NumberConverter extends JSONConverter[Int] {
    override def convert(value: Int): JSONValue = JSONNumber(value)
  }

  // custom data types
  implicit object UserConverter extends JSONConverter[User] {
    override def convert(user: User): JSONValue = JSONObject(Map(
      "name" -> JSONString(user.name),
      "age" -> JSONNumber(user.age),
      "email" -> JSONString(user.email)
    ))
  }

  implicit object PostConverter extends JSONConverter[Post] {
    override def convert(post: Post): JSONValue = JSONObject(Map(
      "content" -> JSONString(post.content),
      "created" -> JSONString(post.createdAt.toString)
    ))
  }

  implicit object FeedConverter extends JSONConverter[Feed] {
    override def convert(feed: Feed): JSONValue = JSONObject(Map(
      "user" -> feed.user.toJSON, // TODO
      "posts" -> JSONArray(feed.posts.map(_.toJSON)) // TODO
    ))
  }

  // call stringify on result
  val now = new Date(System.currentTimeMillis())
  val john = User("John", 34, "john@outlook.ooo")
  val feed = Feed(john, List(
    Post("hello", now),
    Post("goodby", now)
  ))

  println(feed.toJSON.stringify)
}
