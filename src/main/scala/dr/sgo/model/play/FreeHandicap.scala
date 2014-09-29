package dr.sgo.model.play

import dr.sgo.model.{Black, GameState}

class FreeHandicap( ps : List[(Int,Int)] ) extends Handicap {

  positions ++= ps

  override def execute(state : GameState) : Unit = {
    for( p <- positions ) {
      state.board.setStone(Black(), p)
    }
  }

  def undoable() = false
}
