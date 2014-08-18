package dr.sgo.model.play

import dr.sgo.model.Color
import dr.sgo.model.Position
import dr.sgo.model.GameState
import dr.sgo.model.Stone

class Add( val color : Color, val positions : List[Position] ) extends Play {

  def execute( state : GameState ) : Unit = {
    for ( p <- positions ) {
      state.board.setStone( new Stone(color), p )
    }
  }
}