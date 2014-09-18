package dr.sgo.ui.console

import org.apache.logging.log4j.LogManager
import dr.sgo.model.Game
import dr.sgo.model.Player
import dr.sgo.model.Board
import scala.collection.mutable.ListBuffer
import dr.sgo.model.Stone
import org.apache.commons.lang3.StringUtils
import dr.sgo.model.White
import dr.sgo.model.Black
import dr.sgo.model.Position
import dr.sgo.sgf.parser.SGFParser
import dr.sgo.sgf.parser.GameTree


object SGoConsole {

  // val logger = LogManager.getLogger("dr.sgo")

  def main( args : Array[String] ) : Unit = {

    var p = new SGFParser()
    var gt = p.loadSGF( "c:/dev/projects/go/sgo/git/sgo/src/test/resources/heikki01.sgf" )
    var g = GameTree.convert( gt._2.get )
    renderGame( g )

  }

  def renderGame( game : Game ) : Unit = {
    Console.print( toString( game ) )
  }


  def toString( game : Game ) : String = {

    val state = game.currentState
    var out = ""

    // Console.println( "sgo 0.1" )
    out += "\u2554\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2557\n"
    out += "\u2551 sgo 0.1                                                                      \u2551\n"
    out += "\u2560\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2563\n"
    out += "\u2551                                                                              \u2551\n"
    for( line <- toString( state.board ) ){
      out += line +"\n"
    }
    out += "\u2551                                                                              \u2551\n"
    out += "\u255A\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u255D\n"

    out
  }


  def toString( board : Board ) : List[String] = {

    var lines = ListBuffer[String]()

    var header = renderColumnHeader(board)
    lines += header

    for( i <- 0 until board.size ){

      lines += renderRow( board, i )
    }

    lines += header
    lines.toList
  }

  def renderRow( board : Board, row : Int ) : String = {
    var line = "\u2551 " + StringUtils.leftPad( ( row + 1 ).toString, 2, ' ' ) +" "

    val stones = board.getRow(row)
    val dots = for( i <- 0 until stones.size ) yield { renderCell( board.size, i, row, stones(i) ) }

    line += dots.mkString(" ")

    line += " " + StringUtils.leftPad( ( row + 1 ).toString, 2, ' ' )

    line = StringUtils.rightPad( line, 79, " " )

    line += "\u2551 "

    line
  }

  def renderCell( size : Int, col : Int, row : Int, stone : Stone ) : String = {
    stone.color match {
      case White() => { "\u25CF" }
      case Black() => { "\u25CB" }
      case _ => { renderEmptyCell( size, col, row ) }
    }
  }

  val crossHairs = List(
      (9,2,2), (9,4,2), (9,6,2),
      (9,2,4), (9,4,4), (9,6,4),
      (9,2,6), (9,4,6), (9,6,6),
      (11,2,2), (11,5,2), (11,8,2),
      (11,2,5), (11,5,5), (11,8,5),
      (11,2,8), (11,5,8), (11,8,8),
      (13,3,3), (13,6,3), (13,9,3),
      (13,3,6), (13,6,6), (13,9,6),
      (13,3,9), (13,6,9), (13,9,9),
      (15,3,3), (15,7,3), (15,11,3),
      (15,3,7), (15,7,7), (15,11,7),
      (15,3,11), (15,7,11), (15,11,11),
      (17,3,3), (17,8,3), (17,13,3),
      (17,3,8), (17,8,8), (17,13,8),
      (17,3,13), (17,8,13), (17,13,13),
      (19,3,3),  (19,9,3), (19,15,3),
      (19,3,9),  (19,9,9), (19,15,9),
      (19,3,15),  (19,9,15), (19,15,15)
      )

  def renderEmptyCell( size : Int, col : Int, row : Int ) : String = {
    val t = ( size, col, row )
    if( crossHairs.contains(t) ){ "\u002B" }
    else { "\u00B7" }
  }

  def renderColumnHeader( board : Board ) : String = {

    var line = "\u2551    "
    val size = board.size
    val cols = Cols.take(size)
    line += cols.mkString( " " )

    line = StringUtils.rightPad( line, 79, " " )

    line += "\u2551 "

    line
  }

  val Cols = List( 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T' )
}