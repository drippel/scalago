package dr.sgo.model.play

import dr.sgo.model.GameState

abstract class Play {

  def excecute( state : GameState ) : Unit

}