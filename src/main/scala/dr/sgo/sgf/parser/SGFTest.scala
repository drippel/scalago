package dr.sgo.sgf.parser

import scala.collection.JavaConversions
import org.apache.commons.io.FileUtils
import java.io.File
import org.apache.logging.log4j.LogManager

object SGFTest {
  val logger = LogManager.getLogger("dr.sgo")
  def main( args : Array[String] ) {
    logger.info( "Test" )
    var p = new SGFParser()
    var exts = Array( "sgf" )
    var iter = JavaConversions.asScalaIterator( FileUtils.iterateFiles( new File( "c:/dev/projects/go/sgo/git/sgo/src/test/resources" ), exts, true ) )
    for ( f <- iter ) {
      logger.info( f.getCanonicalPath() )
      var gt = p.loadSGF( f.getCanonicalPath() )
      logger.info( gt._1 )

      // var g = GameTree.convertToGame( gt );
      // println( g.board.showBoard() )
    }
  }

}