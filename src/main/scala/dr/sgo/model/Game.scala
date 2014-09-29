package dr.sgo.model

import dr.sgo.model.play.{Handicap, Setup, Play}
import scala.collection.mutable.ListBuffer

class Game {
  var white : Player = null
  var black : Player = null
  var size : Int = 19
  var headState : GameState = null

  var fileFormat : String = null
  var gameHandicap : String = null
  var komi : Option[Komi] = None
  var event : String = null
  var gameLocation : String = null
  var rules : String = null
  var timeLimit : String = null
  var gameDate : String = null
  var round : String = null
  var result : String = null
  var gameType : String = null
  var charSet : String = null
  var gameComment : String = null
  var app : String = null
  var appVersion : String = null
  var boardSize : String = null
  var gameSource : String = null
  var gameName : String = null
  var showVariations : String = null
  var synthesis : String = null
  var toPlay : String = null
  var user : String = null
  var whiteTeam : String = null
  var blackTeam : String = null
  var overtimePeriod : String = null
  var overtimeMoves : String = null
  var whiteTimeLeft : String = null
  var blackTimeLeft : String = null
  var copyright : String = null
  var overTime : String = null
  var annotation : String = null
  var blackSpecies : String = null
  var whiteSpecies : String = null
  var id : String = null
  var name : String = null

  val labels = ListBuffer[Label]()
  val illegals = ListBuffer[Position]()

  def currentState() : GameState = {

    var state = headState
    while ( !state.next.isEmpty ) {
      state = state.next.head
    }

    state

  }

  def inProgress() : Boolean = {
    !currentState().play.isInstanceOf[Setup]
  }

  def canUndo() : Boolean = {
    currentState().play.undoable()
  }

  def undo() = {
    val state = currentState()
    state.prev match {
      case Some(prev) => {
        prev.next.clear()
      }
      case _ => {}
    }
  }

  def getPlayer( color : Color ) : Player = {
    color match {
      case White() => white
      case Black() => black
    }
  }
}

object Game {

  def initializeGame( s : Int, w : Player, b : Player ) : Game = {

    val game = new Game()
    game.size = s
    game.white = w
    game.black = b

    game.headState = new GameState( None, None, new Setup( game.size ) )

    game
  }

  def execute( game : Game, player : Option[Player], play : Play ) = {

    val current = game.currentState
    current.next += new GameState( Some(current), player, play )

  }
}