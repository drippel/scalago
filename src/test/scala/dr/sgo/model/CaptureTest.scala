package dr.sgo.model

import org.junit.Test
import dr.sgo.model.Position._
import dr.sgo.model.Stone._
import dr.sgo.model.play.Add
import dr.sgo.model.play.Place
import org.junit.Assert._

class CaptureTest {

  def testGame( size : Int ) = {
    val white = new Player("white")
    val black = new Player("black")
    Game.initializeGame( size, white, black )
  }

  @Test
  def test_capture_1() = {

    val game = testGame(19)

    val p = new Position(5,5)

    Game.execute( game, Some(game.black), new Add( Black(), List( p.n, p.s, p.w ) ) )
    Game.execute( game, Some(game.white), new Add( White(), List( p ) ) )

    Game.execute( game, Some( game.black), new Place( Black(), p.e ) )

    val state = game.currentState

    assertTrue( state.captures != null )
    assertTrue( state.captures.size == 1 )

    val cap = state.captures.head

    assertTrue( cap.color == White() )
    assertTrue( cap.positions.size == 1 )
    assertTrue( cap.positions.contains( p ) )
  }

  @Test
  def test_capture_2() = {

    val game = testGame(19)

    Game.execute( game, Some(game.black), new Add( Black(), List( (5,4), (6,5), (6,6), (5,7) ) ) )
    Game.execute( game, Some(game.white), new Add( White(), List( (5,5), (5,6) ) ) )

    Game.execute( game, Some(game.black), new Place( Black(), (4,5) ) )

    val groups = game.currentState.groups.filter( (g) => { g.color == White() } )
    assertTrue( groups.size == 1 )
    assertTrue( groups.head.liberties.size == 1 )

    Game.execute( game, Some(game.black), new Place( Black(), (4,6) ) )
    val state = game.currentState
    val g2 = state.groups.filter( (g) => { g.color == White() } )
    assertTrue( g2.size == 0 )
    assertTrue( state.captures != null )
    assertTrue( state.captures.size == 1 )

    val cap = state.captures.head
    assertTrue( cap.color == White() )
    assertTrue( cap.positions.size == 2 )
    assertTrue( cap.positions.contains( new Position(5,5)))
    assertTrue( cap.positions.contains( new Position(5,6)))
  }

}