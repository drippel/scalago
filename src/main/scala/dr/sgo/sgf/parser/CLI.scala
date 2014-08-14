package dr.sgo.sgf.parser

import org.apache.logging.log4j.LogManager

object CLI {

  val logger = LogManager.getLogger("dr.sgo")

  def main( args : Array[String] ) : Unit = {

    logger.info("sgo sgf parser...")

    if( args.size > 0 ){

      val fileName = args(0)
    }

  }

}