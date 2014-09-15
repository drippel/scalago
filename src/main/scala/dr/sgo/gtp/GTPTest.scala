package dr.sgo.gtp

import org.apache.logging.log4j.LogManager
import scala.collection.JavaConversions
import org.apache.commons.io.FileUtils
import java.io.File

object GTPTest {
 val logger = LogManager.getLogger("dr.sgo")
  def main( args : Array[String] ) {
    logger.info( "Test" )

    var p = new GTPParser()
    var exts = Array( "tst", "gtp" )
    var iter = JavaConversions.asScalaIterator( FileUtils.iterateFiles( new File( "c:/dev/projects/go/sgo/git/sgo/src/test/resources/gtp" ), exts, true ) )
    for ( f <- iter ) {

      logger.info( f.getCanonicalPath() )

      var lines = GTPParser.readFile( f.getCanonicalPath() )

      var cmds = GTPParser.parseLines(lines)

      cmds.foreach( (c) => { Console.println(c) } )

    }
  }
}