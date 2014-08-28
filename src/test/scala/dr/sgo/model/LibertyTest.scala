package dr.sgo.model

import org.junit.Test
import org.junit.Assert._
import dr.sgo.model.play.Place

class LibertyTest {

  def testGame() = {
    val white = new Player("white")
    val black = new Player("black")
    Game.initializeGame( 19, white, black )
  }

  @Test
  def test_liberties_1 = {

    val game = testGame

    val p = new Position(3,3)
    Game.execute( game, Some(game.black), new Place( Black(), p ) )

    val chains = Groups.findChains(game.currentState.board)

    assertTrue( chains != null )
    assertTrue( chains.size == 1 )

    Liberties.calculateLiberties(game.currentState.board, chains)

    val c = chains.head

    assertTrue( c.liberties != null )
    assertTrue( c.liberties.size == 4 )
    assertTrue( c.liberties.contains( p.n ) )
    assertTrue( c.liberties.contains( p.s ) )
    assertTrue( c.liberties.contains( p.e ) )
    assertTrue( c.liberties.contains( p.w ) )

  }

  @Test
  def test_liberties_2 = {

    val game = testGame

    val p1 = new Position(3,3)
    Game.execute( game, Some(game.black), new Place( Black(), p1 ) )
    val p2 = new Position(16,16)
    Game.execute( game, Some(game.white), new Place( White(), p2 ) )

    val chains = Groups.findChains(game.currentState.board)

    assertTrue( chains != null )
    assertTrue( chains.size == 2 )

    Liberties.calculateLiberties(game.currentState.board, chains)

    val c1 = chains.find( (c) => { c.color == Black() } ).get

    assertTrue( c1.liberties != null )
    assertTrue( c1.liberties.size == 4 )
    assertTrue( c1.liberties.contains( p1.n ) )
    assertTrue( c1.liberties.contains( p1.s ) )
    assertTrue( c1.liberties.contains( p1.e ) )
    assertTrue( c1.liberties.contains( p1.w ) )

    val c2 = chains.find( (c) => { c.color == White() } ).get

    assertTrue( c2.liberties != null )
    assertTrue( c2.liberties.size == 4 )
    assertTrue( c2.liberties.contains( p2.n ) )
    assertTrue( c2.liberties.contains( p2.s ) )
    assertTrue( c2.liberties.contains( p2.e ) )
    assertTrue( c2.liberties.contains( p2.w ) )
  }

  @Test
  def test_liberties_capture : Unit = {

    val game = testGame

    val b1 = new Position(3,3)
    Game.execute( game, Some(game.black), new Place( Black(), b1 ) )

    val w1 = new Position(16,16)
    Game.execute( game, Some(game.white), new Place( White(), w1 ) )

    val b2 = w1.n
    Game.execute( game, Some(game.black), new Place( Black(), b2 ) )

    val w2 = new Position(3,16)
    Game.execute( game, Some(game.white), new Place( White(), w2 ) )

    val b3 = w1.e
    Game.execute( game, Some(game.black), new Place( Black(), b3 ) )

    val w3 = new Position(2,16)
    Game.execute( game, Some(game.white), new Place( White(), w3 ) )

    val b4 = w1.s
    Game.execute( game, Some(game.black), new Place( Black(), b4 ) )

    val w4 = new Position(1,16)
    Game.execute( game, Some(game.white), new Place( White(), w4 ) )

    val b5 = w1.w
    Game.execute( game, Some(game.black), new Place( Black(), b5 ) )

    assertTrue( game.currentState.captures.size == 1 )


  }
}