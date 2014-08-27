package dr.sgo.model

import org.junit.Test
import org.junit.Assert._
import dr.sgo.model.play.Place

class GroupTest {

  def testGame() = {
    val white = new Player("white")
    val black = new Player("black")
    Game.initializeGame( 19, white, black )
  }

  @Test
  def test_find_chains_1() : Unit = {

    val game = testGame

    Game.execute( game, Some(game.black), new Place( Black(), new Position(3,3) ) )
    Game.execute( game, Some(game.white), new Place( White(), new Position(16,16) ) )

    val whites = Groups.whites(game.currentState.board )
    assertTrue( whites != null )
    assertTrue( whites.size == 1 )

    val blacks = Groups.blacks(game.currentState.board )
    assertTrue( blacks != null )
    assertTrue( blacks.size == 1 )

    // check for chains
    val chains = Groups.findChains( game.currentState.board )
    chains.foreach( Console.println(_) )

    assertTrue( chains != null )
    assertTrue( chains.size == 2 )
  }

  @Test
  def test_find_chains_2() : Unit = {

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

  @Test
  def test_find_chains_3() : Unit = {

    val game = testGame

    Game.execute( game, Some(game.black), new Place( Black(), new Position(3,3) ) )
    Game.execute( game, Some(game.white), new Place( White(), new Position(16,16) ) )
    Game.execute( game, Some(game.black), new Place( Black(), new Position(3,4) ) )
    Game.execute( game, Some(game.white), new Place( White(), new Position(15,16) ) )
    Game.execute( game, Some(game.black), new Place( Black(), new Position(3,5) ) )
    Game.execute( game, Some(game.white), new Place( White(), new Position(2,16) ) )
    Game.execute( game, Some(game.black), new Place( Black(), new Position(4,5) ) )
    Game.execute( game, Some(game.white), new Place( White(), new Position(3,16) ) )

    // check for chains
    val chains = Groups.findChains( game.currentState.board )
    chains.foreach( Console.println(_) )

    assertTrue( chains != null )
    assertTrue( chains.size == 2 )
  }
}