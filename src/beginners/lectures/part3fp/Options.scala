package beginners.lectures.part3fp

import scala.util.Random

object Options extends App {
  val myFirstOption: Option[Int] = Some(4)
  val noOption: Option[Int] = None

  println(myFirstOption)
  println(noOption)
  //  val result = Some(null) // WRONG
  val result = Option(unsafeMethod()) // will build a Some or None, depending if the unsafeMethod() will return a null or not
  val chainedResult = Option(unsafeMethod()).orElse(Option(backupMethod()))
  println(result)
  val aBetterChainedResult = betterUnsafeMethod() orElse betterBackupMethod()
  /*
    Exercises
   */
  val config: Map[String, String] = Map(
    "host" -> "127.0.0.1",
    "port" -> "1234"
  )
  val host = config.get("host")
  val port = config.get("port")
  /*
    The equivalent to connection = host.flatMap ..

    if (h is not null)
      if p!= null
      return connection.Apply(h, p)
    return null
   */
  val connection = host.flatMap(h => port.flatMap(p => Connection.apply(h, p)))

  // functions on Options
  println(myFirstOption.isEmpty)
  println(myFirstOption.get) // this one is UNSAFE - do not use this

  // map, flatMap, filter
  println(myFirstOption.map(_ * 2))
  println(myFirstOption.filter(x => x > 10))
  println(myFirstOption.flatMap(x => Option(x * 10)))

  // for-comprehensions
  /*
    if c!= null
      return c.connect
    return null
   */
  val connectionStatus = connection.map(c => c.connect())
  // for-comprehensions
  val forConnectionStatus = for {
    host <- config.get("host")
    port <- config.get("port")
    connection <- Connection(host, port)
  } yield connection.connect
  print("For Connection status")
  forConnectionStatus.foreach(println)

  // unsafe API
  def unsafeMethod(): String = null

  // try to establish a connection. If so, print the connect method

  // chained methods
  def backupMethod(): String = "A valid result"

  // DESIGN unsafe APIs:
  def betterUnsafeMethod(): Option[String] = None

  def betterBackupMethod(): Option[String] = Some("A result")

  class Connection {
    def connect() = "Connected" // connect to some server, in the reality
  }

  // if (connectionStatus == null) println(None) else print(Some(connectionstatus.get))
  println(connectionStatus)

  // if(status != null) println(status)
  connectionStatus.foreach(println)

  // chained calls
  config.get("host")
    .flatMap(host => config.get("port")
      .flatMap(port => Connection(host, port))
      .map(connection => connection.connect())
    ).foreach(println)

  object Connection {
    val random = new Random(System.nanoTime())

    def apply(host: String, port: String): Option[Connection] =
      if (random.nextBoolean()) Some(new Connection)
      else None
  }
}
