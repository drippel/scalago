package dr.sgo.model

import dr.sgo.model.play.Play

class GameState( val play : Play ) {

  var player : Player = null
  var board : Board = null

  var prev : GameState = null
  var next = List[GameState]()

  play.excecute(this)
}