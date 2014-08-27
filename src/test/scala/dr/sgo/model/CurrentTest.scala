package dr.sgo.model

import org.junit.Test
import org.junit.Assert._
import dr.sgo.model.play.Place

class CurrentTest {

  def testGame() = {
    val white = new Player("white")
    val black = new Player("black")
    Game.initializeGame( 19, white, black )
  }

  @Test
  def test_find_group_2() : Unit = {

    val game = testGame

    Game.execute( game, Some(game.black), new Place( Black(), new Position(3,3) ) )
    Game.execute( game, Some(game.white), new Place( White(), new Position(16,16) ) )
    Game.execute( game, Some(game.black), new Place( Black(), new Position(3,4) ) )

    val whites = Groups.whites(game.currentState.board )
    assertTrue( whites != null )
    assertTrue( whites.size == 1 )

    val blacks = Groups.blacks(game.currentState.board )
    assertTrue( blacks != null )
    assertTrue( blacks.size == 2 )

    // check for chains
    val chains = Groups.findChains( game.currentState.board )
    chains.foreach( Console.println(_) )

    assertTrue( chains != null )
    assertTrue( chains.size == 2 )
  }
}