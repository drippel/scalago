package dr.sgo.model

import dr.sgo.model.play.Play
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.HashMap

class GameState( val prev : Option[GameState], val player : Option[Player], val play : Play ) {

  var board : Board = prev match {
    case Some(p) => { p.board.clone }
    case _ => { new Board(19) }
  }

  val next = ListBuffer[GameState]()

  var comment = ""

  val territory = HashMap[Color,List[Position]]()

  var toPlay = ""
  var blackTimeLeft = ""
  var whiteTimeLeft = ""
  var blackMovesLeft = ""
  var whiteMovesLeft = ""
  var id = ""
  var name = ""

  val circle = ListBuffer[Position]()
  val labels = ListBuffer[Label]()
  val illegals = ListBuffer[Position]()

  val groups = ListBuffer[Group]()
  val captures = ListBuffer[Group]()

  play.execute(this)
}