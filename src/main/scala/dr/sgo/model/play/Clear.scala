package dr.sgo.model.play

import dr.sgo.model.Position
import dr.sgo.model.GameState
import dr.sgo.model.Open
import dr.sgo.model.Stone

class Clear( val pos : List[Position] ) extends Play {

  def execute( state : GameState ) : Unit = {
    for ( p <- pos ) {
      state.board.setStone( new Stone( Open() ), p )
    }
  }
}