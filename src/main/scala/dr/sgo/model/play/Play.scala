package dr.sgo.model.play

import dr.sgo.model.GameState

abstract class Play {

  def execute( state : GameState ) : Unit

}