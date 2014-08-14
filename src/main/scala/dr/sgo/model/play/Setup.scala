package dr.sgo.model.play

import dr.sgo.model.GameState
import dr.sgo.model.Board


class Setup( val size : Int ) extends Play {

  def excecute( state : GameState ) : Unit = {

    state.board = new Board(size)

    return
  }

}