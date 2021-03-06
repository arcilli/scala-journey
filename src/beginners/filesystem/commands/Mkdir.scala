package beginners.filesystem.commands
import beginners.filesystem.State
import beginners.filesystem.files.Directory

class Mkdir(name: String) extends Command {

  def doMkdir(state: State, name: String): State = {
    val wd = state.wd
    val fullPath = wd
    // TODO: complete this
    ???
  }

  override def apply(state: State): State = {
    val wd = state.wd
    if (wd.hasEntry(name)) {
      state.setMessage("Entry " + name + " already exists")
    } else if(name.contains(Directory.SEPARATOR)) {
      state.setMessage(name + " must not contain separators!")
    } else if (checkIllegal(name)) {
      state.setMessage(name + ": illegal entry name")
    } else {
      doMkdir(state, name)
    }
  }

  def checkIllegal(str: String): Boolean = {
    name.contains(".")
  }
}
