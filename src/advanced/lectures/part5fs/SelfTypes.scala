package advanced.lectures.part5fs

import com.sun.org.glassfish.external.statistics.Stats

// a way of requiring a Type to be mixed in
object SelfTypes extends App {

  trait Instrumentalist {
    def play(): Unit
  }

  trait Singer {
    self: Instrumentalist => // SELF TYPE whoever implements Singer to implement Instrumentalist

    def sing(): Unit
  }

  class LeadSinger extends Singer with Instrumentalist {
    override def play(): Unit = ???

    override def sing(): Unit = ???
  }

  // This is illegal.
  //  class Vocalist extends Singer {
  //    override def sing(): Unit = ???
  //  }

  val jamesHetfield = new Singer with Instrumentalist {
    override def sing(): Unit = ???
    override def play(): Unit = ???
  }

  class Guitarist extends Instrumentalist {
    override def play(): Unit = println("(guitar solo)")
  }

  val ericClaptop =  new Guitarist with Singer {
    override def sing(): Unit = ???
  }

  // self-types are compared to inheritance
  class A
  class B extends A // "B is an A"

  trait T

  // "S requires a T"
  trait S {self : T=> }

  // CAKE pattern Scala version for => "dependency injection" from the Java world
  class Component {
    // API
  }
  class ComponentA extends Component
  class ComponentB extends Component
  class DependentComponent(val component: Component)

  // in Scala we have the Cake pattern as:
  trait ScalaComponent {
    // API
    def action(x: Int): String
  }

  // Interpretation: whoever implements the ScalaDependentComponent trait must implement also some form of ScalaComponent.
  trait ScalaDependentComponent {self: ScalaComponent =>
    def dependentAction(x: Int): String = action(x) + " this rocks"
  }

  // layer 1 - small components
  trait Picture extends ScalaComponent
  trait Status extends ScalaComponent

  // layer2 - compose
  trait Profile extends ScalaDependentComponent with Picture
//  trait Analytics extends ScalaDependentComponent with Stats

  trait ScalaApplication{ self: ScalaDependentComponent => }
  // layer 3 - app
//  trait AnalyticsApp extends ScalaApplication with Analytics

  // self types allows us to define cyclical dependencies

  // This will not compile
//  class X extends Y
//  class Y extends X

  trait X{ self: Y =>}
  trait Y{ self: X =>}
}
