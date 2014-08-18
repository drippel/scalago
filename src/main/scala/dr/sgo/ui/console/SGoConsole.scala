package dr.sgo.ui.console

import org.apache.logging.log4j.LogManager
import dr.sgo.model.Game
import dr.sgo.model.Player
import dr.sgo.model.Board


object SGoConsole {

  // val logger = LogManager.getLogger("dr.sgo")

  def main( args : Array[String] ) : Unit = {

    val game = Game.initializeGame(19, new Player("computer" ), new Player( "you" ) )

    renderGame( game )

  }


  def renderGame( game : Game ) : Unit = {

    val state = game.currentState


    Console.println( "sgo 0.1" )
    Console.println( "\u2554\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2557" )
    Console.println( "\u2551 sgo 0.1                                                                      \u2551" )
    Console.println( "\u2560\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2563" )
    Console.println( "\u2551                                                                              \u2551" )
    Console.println( "\u2551                                                                              \u2551" )
    Console.println( "\u2551                                                                              \u2551" )
    Console.println( "\u2551                                                                              \u2551" )
    Console.println( "\u2551                                                                              \u2551" )
    Console.println( "\u2551                                                                              \u2551" )
    Console.println( "\u2551                                                                              \u2551" )
    Console.println( "\u2551                                                                              \u2551" )
    Console.println( "\u2551                                                                              \u2551" )
    Console.println( "\u2551                                                                              \u2551" )
    Console.println( "\u2551                                                                              \u2551" )
    Console.println( "\u2551                                                                              \u2551" )
    Console.println( "\u2551                                                                              \u2551" )
    Console.println( "\u2551                                                                              \u2551" )
    Console.println( "\u2551                                                                              \u2551" )
    Console.println( "\u2551                                                                              \u2551" )
    Console.println( "\u2551                                                                              \u2551" )
    Console.println( "\u2551                                                                              \u2551" )
    Console.println( "\u2551                                                                              \u2551" )
    Console.println( "\u2551                                                                              \u2551" )
    Console.println( "\u2551                                                                              \u2551" )
    Console.println( "\u2551                                                                              \u2551" )
    Console.println( "\u255A\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u255D" )
  }


  def renderBoard( board : Board ) : List[String] = {

    var lines = ListBuffer[String]()

    lines.toList
  }
}