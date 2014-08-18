package dr.sgo.model.play

import dr.sgo.model.GameState
import dr.sgo.model.Color
import dr.sgo.model.Position

class Place( val color : Color, val pos : Position ) extends Play {

  def execute( state : GameState  ) : Unit = {
    return
  }
}