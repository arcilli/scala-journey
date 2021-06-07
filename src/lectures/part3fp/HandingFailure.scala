package lectures.part3fp

import scala.util.{Failure, Random, Success, Try}

object HandingFailure extends App {
  // create success and failure
  val aSuccess = Success(3)
  val aFailure = Failure(new RuntimeException("Super failure"))

  println(aSuccess)
  println(aFailure)
  // Try will wrap the throw in a Failure, so the program will not crash
  // Try objects via the apply method
  val potentialFailure = Try(unsafeMethod())
  // syntax sugar
  val anotherPotentialFailure = Try {
    // code that may throw
  }
  println(potentialFailure)

  def unsafeMethod(): String = throw new RuntimeException("NO STRING FOR u")

  // utilities
  println(potentialFailure.isSuccess)

  // orElse
  def backupMethod(): String = "A valid result"
  val fallbackTry = Try(unsafeMethod()).orElse(Try(backupMethod()))
  print(fallbackTry)

  // If you design the API
  // Write your computation in a try
  def betterUnsafeMethod(): Try[String] = Failure(new RuntimeException)
  def betterBackupMethod(): Try[String] = Success("A valid result")
  val betterFallback = betterUnsafeMethod().orElse(betterBackupMethod())

  // Try has also map, flatMap, filter
  println(aSuccess.map(_ *2))
  println(aSuccess.flatMap(x => Success(x * 10)))
  println(aSuccess.filter(_ > 10)) // This success wil turn into a failure
  // => for-comprehensions

  /*
    Exercise
   */
  val hostname = "localhost"
  val port = "8080"
  def renderHTML(page: String) = println(page)

  class Connection {
    def get(url: String): String = {
      val random = new Random(System.nanoTime())
      if (random.nextBoolean()) "<html>...</html>"
      else throw new RuntimeException("Connection interrupted")
    }
    def getSafe(url: String): Try[String] = Try{get(url)}
  }

  object HttpService {
    val random = new Random(System.nanoTime())

    def getConnection(host: String, port: String): Connection = {
      if (random.nextBoolean()) new Connection
      else throw new RuntimeException("Someone else took the port")
    }
    def getSafeConnection(host:String, port: String): Try[Connection] = Try(getConnection(host, port))
  }

  // try to print random.nextBoolean if you get the html page from connection, print it to the console
  // ie. call randomHtml
  val possibleConnection = HttpService.getSafeConnection(hostname, port)
  val possibleHtml = possibleConnection.flatMap(connection => connection.getSafe("/home"))

  possibleHtml.foreach(renderHTML)

  // shorthand version
  HttpService.getSafeConnection(hostname, port)
    .flatMap(connection => connection.getSafe("/home"))
    .foreach(renderHTML)

  // for-comprehension version
  // TODO: check for-comprehension version
  for {
    connection <- HttpService.getSafeConnection(hostname, port)
    htmlPage <- connection.getSafe("/home")
  } renderHTML(htmlPage)

  /*
    imperative way:

    try {
      connection = HttpService.getConnection(host, port)
      try{
        page = connection.getSafe("/home")
        renderHTML(page)
      }
      catch()
    } catch(Exception)
   */
}
