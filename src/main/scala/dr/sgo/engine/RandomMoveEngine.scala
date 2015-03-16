package dr.sgo.engine

import dr.sgo.model.{Position, Color, Board}

import scala.util.Random

class RandomMoveEngine extends Engine {

  val rand = new Random()

   def generateMove(board : Board, color : Color ) : Move = {

    val open = board.openPositions()
    if( open.isEmpty ){
      new Move( None, "no open positions" )
    }
    else {
      val i = rand.nextInt( open.size )
      // TODO: need to check validity of move, suicide, etc.
      new Move( Some(open(i)), "random position" )
    }
  }


  def analyze( board : Board ) : Analysis = {
    val analysis = new Analysis( board.clone() )
    analysis
  }
}
