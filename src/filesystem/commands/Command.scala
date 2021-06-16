package filesystem.commands

import filesystem.State

trait Command {
  def apply(state: State): State
}

object Command {
  val MKDIR = "mkdir"

  // a factory method inside this
  def from(input: String): Command = {
    val tokens: Array[String] = input.split(" ")
    if (input.isEmpty || tokens.isEmpty) emptyCommand
    else if (MKDIR.equals(tokens(0))) {
      if (tokens.length < 2) incompleteCommand(MKDIR)
      else new Mkdir(tokens(1))
    } else new UnknownCommand
  }

  def emptyCommand: Command =
    new Command { // anonymous class
      override def apply(state: State): State = state
    }

  def incompleteCommand(name: String): Command =
    new Command {
      override def apply(state: State): State =
        state.setMessage(name + " incomplete command!")
    }
}
