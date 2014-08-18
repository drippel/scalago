package dr.sgo.model.play

import dr.sgo.model.GameState
import dr.sgo.model.Board


class Setup( val size : Int ) extends Play {

  def execute( state : GameState ) : Unit = {

    state.board = new Board(size)

    return
  }

}