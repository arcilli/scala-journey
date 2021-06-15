package filesystem.commands

import filesystem.State

trait Command {
  def apply(state: State): State
}

object Command {
  // a factory method inside this

  def from(input: String): Command =
    new UnknownCommand
}
