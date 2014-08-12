package dr.sgo.ui.console

import org.apache.logging.log4j.LogManager


object SGoConsole {

  val logger = LogManager.getLogger("dr.sgo")

  def main( args : Array[String] ) : Unit = {

    logger.info("SGo 0.1")

  }

}