package dr.sgo.ui.console

import org.apache.logging.log4j.LogManager
import dr.sgo.model.Game
import dr.sgo.model.Player


object SGoConsole {

  val logger = LogManager.getLogger("dr.sgo")

  def main( args : Array[String] ) : Unit = {

    logger.info("SGo 0.1")

    val game = new Game()

    game.white = new Player("computer")
    game.black = new Player( "you" )

  }

}