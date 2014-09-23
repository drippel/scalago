package dr.sgo.engine

import dr.sgo.model.{Color, Position, Board}

abstract class Engine {

  def generateMove( board : Board, color : Color ) : Move

}
