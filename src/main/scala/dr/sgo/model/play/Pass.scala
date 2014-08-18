package dr.sgo.model.play

import dr.sgo.model.GameState
import dr.sgo.model.Color


class Pass( val color : Color ) extends Play {

  def execute( state : GameState ) : Unit = {
    return
  }
}