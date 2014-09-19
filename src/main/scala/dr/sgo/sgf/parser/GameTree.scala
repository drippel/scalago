package dr.sgo.sgf.parser

import dr.sgo.model.Game
import dr.sgo.model.Player
import dr.sgo.model.Komi
import dr.sgo.model.Board
import org.apache.commons.lang3.math.NumberUtils
import dr.sgo.model.Stone
import dr.sgo.model.Black
import dr.sgo.model.White
import dr.sgo.model.Color
import dr.sgo.model.play.Add
import dr.sgo.model.play.Place
import dr.sgo.model.play.Clear
import dr.sgo.model.Position
import dr.sgo.model.Label
import dr.sgo.model.GameState
import org.apache.commons.lang3.StringUtils
import dr.sgo.model.play.Pass
import org.apache.logging.log4j.LogManager

class GameTree( val nodes : List[GameNode], val tree : List[GameTree] ) {

}

object GameTree {

  val logger = LogManager.getLogger( GameTree.getClass() )

  def convert( tree : GameTree ) : Game = {

    var gameNode = tree.nodes.head
    val game = convertGameNode( gameNode )

    for ( node <- tree.nodes.tail ) {
      convertNode( game, node )
    }

    game
  }

  def convertGameNode( gameNode : GameNode ) : Game = {

    val game = Game.initializeGame( 19, new Player("white", "" ), new Player("black", "" ) )

    for ( prop <- gameNode.properties ) {
      // logger.debug(prop.id)
      prop.id match {
        case "FF" => game.fileFormat = prop.values.head
        case "HA" => game.gameHandicap = prop.values.head
        case "KM" => game.komi = Some(new Komi( prop.values.head ) )
        case "EV" => game.event = prop.values.head
        case "PC" => game.gameLocation = prop.values.head
        case "RU" => game.rules = prop.values.head
        case "TM" => game.timeLimit = prop.values.head
        case "DT" => game.gameDate = prop.values.head
        case "RO" => game.round = prop.values.head
        case "PB" => game.black.name = prop.values.head
        case "PW" => game.white.name = prop.values.head
        case "RE" => game.result = prop.values.head
        case "GM" => game.gameType = prop.values.head
        case "CA" => game.charSet = prop.values.head
        case "GC" => game.gameComment = prop.values.head
        case "BR" => game.black.rank = prop.values.head
        case "WR" => game.white.rank = prop.values.head
        case "AP" => {
          if ( prop.values.head.contains( ':' ) ) {
            game.app = prop.values.head.split( ':' ).head;
            game.appVersion = prop.values.head.split( ':' ).tail.head;
          } else {
            game.app = prop.values.head
          }
        }
        case "SZ" => {
          game.boardSize = prop.values.head
          game.currentState.board = new Board( prop.values.head.toInt )
        }
        case "SO" => game.gameSource = prop.values.head
        case "GN" => game.gameName = prop.values.head
        case "ST" => game.showVariations = prop.values.head
        case "AB" => addStones( Black(), game, prop.values )
        case "AW" => addStones( White(), game, prop.values )
        case "SY" => game.synthesis = prop.values.head
        case "PL" => game.toPlay = prop.values.head
        case "LB" => addLabels( game, prop.values )
        case "C" => game.gameComment = prop.values.head
        case "IL" => addIllegal( game, prop.values )
        case "US" => game.user = prop.values.head
        case "WT" => game.whiteTeam = prop.values.head
        case "BT" => game.blackTeam = prop.values.head
        case "OP" => game.overtimePeriod = prop.values.head
        case "OM" => game.overtimeMoves = prop.values.head
        case "BL" => game.whiteTimeLeft = prop.values.head
        case "WL" => game.blackTimeLeft = prop.values.head
        case "CP" => game.copyright = prop.values.head
        case "OT" => game.overTime = prop.values.head
        case "AN" => game.annotation = prop.values.head
        case "BS" => game.blackSpecies = prop.values.head
        case "WS" => game.whiteSpecies = prop.values.head
        case "ID" => game.id = prop.values.head
        case "N" => game.name = prop.values.head
        case _ => logger.info( prop.id + ":unknown" )
      }
    }

    game

  }

  def addStones( color : Color, game : Game, positions : List[String] ) : Unit = {
    var ps = positions.map( ( p ) => { convertCoords( p.toUpperCase(), game.currentState().board.size ) } )
    Game.execute( game, None, new Add( color, ps ) )
  }

  def convertNode( game : Game, gameNode : GameNode ) : Unit = {

    for ( prop <- gameNode.properties ) {
      // logger.debug(prop.id)
      prop.id match {
        case "B" => {
          if( isPass(prop.values.head) ){
            Game.execute( game, Some(game.black), new Pass( Black() ) )
          }
          else {
            Game.execute( game, Some(game.black), new Place( Black(), convertCoords( prop.values.head.toUpperCase(), game.currentState().board.size ) ) )
          }
        }
        case "W" => {
          if( isPass( prop.values.head )){
            Game.execute( game, Some(game.white), new Pass( White() ) )
          }
          else {
            Game.execute( game, Some(game.white), new Place( White(), convertCoords( prop.values.head.toUpperCase(), game.currentState().board.size ) ) )
          }
        }
        case "C" => game.currentState.comment = prop.values.head
        case "AB" => {
          Game.execute( game, Some(game.black), new Add( Black(), convertCoords( prop.values, game.currentState().board.size ) ) )
        }
        case "AW" => {
          Game.execute( game, Some(game.white), new Add( White(), convertCoords( prop.values, game.currentState().board.size ) ) )
        }
        case "AE" => {
          Game.execute(game, None, new Clear( convertCoords( prop.values, game.currentState().board.size ) ) )
        }
        case "TB" => {
          game.currentState.territory += Black() -> convertCoords( prop.values, game.currentState().board.size )
        }
        case "TW" => {
          game.currentState.territory += White() -> convertCoords( prop.values, game.currentState().board.size )
        }
        case "PL" => game.currentState.toPlay = prop.values.head
        case "LB" => addLabels( game.currentState, prop.values )
        case "IL" => addIllegal( game, prop.values )
        case "BL" => game.currentState.blackTimeLeft = prop.values.head
        case "WL" => game.currentState.whiteTimeLeft = prop.values.head
        case "OB" => game.currentState.blackMovesLeft = prop.values.head
        case "OW" => game.currentState.whiteMovesLeft = prop.values.head
        case "CR" => game.currentState.circle ++= convertCoords( prop.values, game.currentState().board.size )
        case "ID" => game.currentState.id = prop.values.head
        case "N" => game.currentState.name = prop.values.head
        case _ => logger.info( prop.id + ":unknown" )
      }
    }
    // println(game.board.showBoard())
  }

  def addLabels( game : Game, props : List[String] ) : Unit = {
    for ( p <- props ) {
      var pos = convertCoords( p.split( ':' ).head.toUpperCase(), game.currentState().board.size )
      var txt = p.split( ':' ).tail.head
      game.labels += new Label( pos, txt )
    }
  }

  def addLabels( state : GameState, props : List[String] ) : Unit = {
    for ( p <- props ) {
      var pos = convertCoords( p.split( ':' ).head.toUpperCase(), state.board.size )
      var txt = p.split( ':' ).tail.head
      state.labels += new Label( pos, txt )
    }
  }

  def addIllegal( game : Game, props : List[String] ) : Unit = {
    for ( p <- props ) {
      game.illegals += convertCoords( p.toUpperCase(), game.currentState().board.size )
    }
  }

  def addIllegal( state : GameState, props : List[String] ) : Unit = {
    for ( p <- props ) {
      state.illegals += convertCoords( p.toUpperCase(), state.board.size )
    }
  }

  def isPass( coords : String ) : Boolean = {
    if ( StringUtils.isEmpty( coords ) ) {
      true
    } else if ( StringUtils.equalsIgnoreCase( "TT", coords ) ) {
      true
    } else {
      false
    }
  }

  def convertCoords( coords : String, size : Int ) : Position = {
    var col = coords.head
    var row = coords.tail.head
    new Position( Cols.indexOf(col), ((size - 1) - Cols.indexOf(row) ) )
  }

  def convertCoords( coords : List[String], size : Int ) : List[Position] = {
    coords.map( ( c ) => { convertCoords( c.toUpperCase(), size ) } )
  }

  val Cols = List( 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S' )
}

