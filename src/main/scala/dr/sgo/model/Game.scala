package dr.sgo.model

import dr.sgo.model.play.Setup


class Game {
  var white : Player = null
  var black : Player = null
  var size : Int = 19
  var headState : GameState = null


  def currentState() : GameState = {

    var state = headState
    while( !state.next.isEmpty ){
      state = state.next.head
    }

    state

  }
}

object Game {


  def initializeGame( s : Int, w : Player, b : Player ) : Game = {

    val game = new Game()
    game.size = s
    game.white = w
    game.black = b

    game.headState = new GameState( new Setup( game.size ) )

    game
  }
}