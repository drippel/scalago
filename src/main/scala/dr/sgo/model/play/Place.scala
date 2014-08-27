package dr.sgo.model.play

import dr.sgo.model.GameState
import dr.sgo.model.Color
import dr.sgo.model.Position
import dr.sgo.model.Stone

class Place( val color : Color, val pos : Position ) extends Play {

  def execute( state : GameState  ) : Unit = {
    state.board.setStone( new Stone( color ), pos )

    // eval for captures
  }
}