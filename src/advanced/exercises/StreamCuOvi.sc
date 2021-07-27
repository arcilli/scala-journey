import advanced.exercises.{Cons, EmptyStream}

lazy val a = {
  println("Gigel")
  42
}

lazy val b = {
  println("B")
  22
}
val myStream = new Cons(a, new Cons(b, EmptyStream))
myStream.map(_+1)