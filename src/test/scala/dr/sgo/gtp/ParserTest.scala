package dr.sgo.gtp

import org.junit.Test
import org.junit.Assert._

class ParserTest {


  @Test
  def test_parse_comment() : Unit = {

    val line = "# this is a comment\n"

    GTPParser.parseLine(line) match {
      case Some(c) => { fail() }
      case None => {
        // ok
      }
    }
  }

  @Test
  def test_parse_blank() : Unit = {

    val line = "\n"

    GTPParser.parseLine(line) match {
      case Some(c) => { fail() }
      case None => {
        // ok
      }
    }
  }

}