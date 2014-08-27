package dr.sgo.model

import org.junit.Test
import org.junit.Assert._
import dr.sgo.model.play.Place

class GameTest {


  @Test
  def test_initialize_game() : Unit = {

    val game = Game.initializeGame( 19, new Player("white"), new Player("black") )

    val gs = game.currentState

    assertNotNull( gs )

    assertNotNull( gs.board )

    assertTrue( gs.board.size == 19 )
  }


  @Test
  def test_play() : Unit = {

    val white = new Player("white")
    val black = new Player("black")
    val game = Game.initializeGame( 19, white, black )

    val gs = game.currentState

    assertNotNull( gs )

    assertNotNull( gs.board )

    assertTrue( gs.board.size == 19 )

    Game.execute( game, Some(black), new Place( Black(), new Position(3,3) ) )
  }
}