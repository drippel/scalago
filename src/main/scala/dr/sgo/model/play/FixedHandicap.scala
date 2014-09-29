package dr.sgo.model.play

import dr.sgo.model.{Black, GameState}

import scala.collection.mutable.ListBuffer

class FixedHandicap( size : Int ) extends Handicap {


  override def execute(state : GameState) : Unit = {

    positions ++= handicapMap(state.board.size)(size)
    for( p <- positions ) {
      state.board.setStone(Black(), p)
    }
  }

  def undoable() :Boolean = false

  val handicapMap = Map(
    9 -> Map(
      2 -> List( (2,2), (6,6) ),
      3 -> List( (2,2), (6,6), (2,6) ),
      4 -> List( (2,2), (6,6), (2,6), (6,2) ),
      5 -> List( (2,2), (6,6), (2,6), (6,2), (4,4) ),
      6 -> List( (2,2), (6,6), (2,6), (6,2), (2,4), (6,4) ),
      7 -> List( (2,2), (6,6), (2,6), (6,2), (2,4), (6,4), (4,4) ),
      8 -> List( (2,2), (6,6), (2,6), (6,2), (2,4), (6,4), (4,2), (4,6) ),
      9 -> List( (2,2), (6,6), (2,6), (6,2), (2,4), (6,4), (4,2), (4,6), (4,4) )
    ),
    11 -> Map(
      2 -> List( (2,2), (8,8) ),
      3 -> List( (2,2), (8,8), (2,8) ),
      4 -> List( (2,2), (8,8), (2,8), (8,2) ),
      5 -> List( (2,2), (8,8), (2,8), (8,2), (5,5) ),
      6 -> List( (2,2), (8,8), (2,8), (8,2), (2,5), (8,5)),
      7 -> List( (2,2), (8,8), (2,8), (8,2), (2,5), (8,5), (5,5) ),
      8 -> List( (2,2), (8,8), (2,8), (8,2), (2,5), (8,5), (5,2), (5,8) ),
      9 -> List( (2,2), (8,8), (2,8), (8,2), (2,5), (8,5), (5,2), (5,8), (5,5) )
    ),
    13 -> Map(
      2 -> List( (3,3), (9,9) ),
      3 -> List( (3,3), (9,9), (3,9) ),
      4 -> List( (3,3), (9,9), (3,9), (9,3) ),
      5 -> List( (3,3), (9,9), (3,9), (9,3), (6,6) ),
      6 -> List( (3,3), (9,9), (3,9), (9,3), (3,6), (9,6) ),
      7 -> List( (3,3), (9,9), (3,9), (9,3), (3,6), (9,6), (6,6) ),
      8 -> List( (3,3), (9,9), (3,9), (9,3), (3,6), (9,6), (6,3), (6,9) ),
      9 -> List( (3,3), (9,9), (3,9), (9,3), (3,6), (9,6), (6,3), (6,9), (6,6) )
    ),
    15 -> Map(
      2 -> List( (3,3), (11,11) ),
      3 -> List( (3,3), (11,11), (3,11) ),
      4 -> List( (3,3), (11,11), (3,11), (11,3) ),
      5 -> List( (3,3), (11,11), (3,11), (11,3), (7,7) ),
      6 -> List( (3,3), (11,11), (3,11), (11,3), (3,7), (11,7) ),
      7 -> List( (3,3), (11,11), (3,11), (11,3), (3,7), (11,7), (7,7) ),
      8 -> List( (3,3), (11,11), (3,11), (11,3), (3,7), (11,7), (7,3), (7,11) ),
      9 -> List( (3,3), (11,11), (3,11), (11,3), (3,7), (11,7), (7,3), (7,11), (7,7) )
    ),
    17 -> Map(
      2 -> List( (3,3), (13,13) ),
      3 -> List( (3,3), (13,13), (3,13) ),
      4 -> List( (3,3), (13,13), (3,13), (13,3) ),
      5 -> List( (3,3), (13,13), (3,13), (13,3), (8,8) ),
      6 -> List( (3,3), (13,13), (3,13), (13,3), (3,8), (13,8) ),
      7 -> List( (3,3), (13,13), (3,13), (13,3), (3,8), (13,8), (8,8) ),
      8 -> List( (3,3), (13,13), (3,13), (13,3), (3,8), (13,8), (8,3), (8,13) ),
      9 -> List( (3,3), (13,13), (3,13), (13,3), (3,8), (13,8), (8,3), (8,13), (8,8) )
    ),
    19 -> Map(
      2 -> List( (3,3), (15,15) ),
      3 -> List( (3,3), (15,15), (3,15) ),
      4 -> List( (3,3), (15,15), (3,15), (15,3) ),
      5 -> List( (3,3), (15,15), (3,15), (15,3), (9,9) ),
      6 -> List( (3,3), (15,15), (3,15), (15,3), (3,9), (15,9) ),
      7 -> List( (3,3), (15,15), (3,15), (15,3), (3,9), (15,9), (9,9) ),
      8 -> List( (3,3), (15,15), (3,15), (15,3), (3,9), (15,9), (9,3), (9,15) ),
      9 -> List( (3,3), (15,15), (3,15), (15,3), (3,9), (15,9), (9,3), (9,15), (9,9) )
    )
  )
}
