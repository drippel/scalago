package dr.sgo.sgf.parser

import scala.util.parsing.combinator.JavaTokenParsers
import scala.util.parsing.combinator.Parsers
import java.io.FileReader
import scala.util.parsing.combinator.Parsers
import org.apache.logging.log4j.LogManager

class SGFParser extends JavaTokenParsers {

  def gameTree : Parser[GameTree] = "(" ~ rep( node ) ~ rep( gameTree ) ~ ")" ^^ {
    case "(" ~ nodes ~ trees ~ ")" => new GameTree( nodes, trees )
  }
  def node : Parser[GameNode] = ";" ~ rep( property ) ^^ { case ";" ~ props => new GameNode( props ) }
  def property : Parser[GameProperty] =
    propIdent ~ rep( propValue ) ^^
      {
        case pi ~ pv => new GameProperty( pi, pv )
      }
  /*def propIdent : Parser[String] =
letter~opt(letter) ^^
{
case l1~Some(l2) => l1 + l2
case l1~None => l1
}*/
  def propIdent : Parser[String] = """[a-zA-Z]+""".r
  def propValue : Parser[String] =
    "[" ~ opt( propContent ) ~ "]" ^^ {
      case "[" ~ Some( pc ) ~ "]" => pc
      case "[" ~ None ~ "]" => ""
    }
  def propContent : Parser[String] = text ^^ ( _.toString )
  def letter : Parser[String] = """[a-zA-Z]""".r

  def text : Parser[String] = """[^\]]*""".r

  def parseFile( fileName : String ) = {
    val reader = new FileReader( fileName )
    parseAll( gameTree, reader )
  }

  def loadSGF( fileName : String ) : ( String, Option[GameTree] ) = {
    parseFile( fileName ) match {

      case Success( r, _ ) => { ( "OK", Some(r) ) }
      case e @ Error( msg, _ ) => { ( e.toString, None ) }
      case f @ Failure( msg, _ ) => { ( f.toString, None ) }

    }
  }


}

object SGFParser {

  val logger = LogManager.getLogger("dr.sgo")

  def main( args : Array[String] ) : Unit = {

    val p = new SGFParser()
    var r = p.loadSGF("c:/dev/projects/go/sgo/git/sgo/src/test/resources/2004-04-10-GnuGoCVS-inuyasha34.sgf")

    logger.info(r._1)

   /* p.parseFile("c:/dev/projects/go/sgo/git/sgo/src/test/resources/2004-04-10-GnuGoCVS-inuyasha34.sgf") match {

      case SGFParser.Success( tree, _ ) => {}
      case Error( msg, next ) => {}
      case Failure( msg, next ) => {}

    }*/

  }

}