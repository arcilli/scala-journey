package lectures.part2oop

object AnonymousClasses extends App {

  // anonymous class, the same as the one in comment from ANonymousClasses$$anon$1
  val funnyAnimal = new Animal {
    override def eat: Unit = println("ahahahah")
  }
  val funnyAnimal2 = new AnonymousClasses$$anon$1

  abstract class Animal {
    def eat: Unit
  }

  //  class AnonymousClasses$$anon$1 extends Animal {
  //    override def eat: Unit = println("ahahahah from anon class but not so anonymous")
  //  }
  //  println(funnyAnimal2.getClass)
  println(funnyAnimal.getClass)

  class Person(name: String){
    def sayHi: Unit = println($"Hi, my name is $name, how can I help?")
  }

  val jim = new Person("Jim") {
    override def sayHi: Unit = println($"Hi, I'm jim, how can i be of service?")
  }
}
