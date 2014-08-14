package dr.sgo.model

import org.junit.Test
import org.junit.Assert._

class GameTest {


  @Test
  def test_initialize_game() : Unit = {

    val game = Game.initializeGame( 19, new Player("white"), new Player("black") )

    val gs = game.currentState

    assertNotNull( gs )

    assertNotNull( gs.board )

    assertTrue( gs.board.size == 19 )
  }
}