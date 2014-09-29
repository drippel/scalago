package dr.sgo.model.play

import dr.sgo.model.GameState
import dr.sgo.model.Color
import dr.sgo.model.Position
import dr.sgo.model.Stone
import dr.sgo.model.Groups
import dr.sgo.model.Liberties
import dr.sgo.model.Open

class Place( val color : Color, val pos : Position ) extends Play {

  def execute( state : GameState  ) : Unit = {

    state.board.setStone( new Stone( color ), pos )

    // eval for captures
    val groups = Groups.findGroups( state.board )

    Liberties.calculateLiberties( state.board, groups )

    val zeroLibs = groups.filter( (g) => { g.liberties.size == 0 })

    val captures = zeroLibs.filter( (g) => { g.color != color && g.color != Open() })

    if( captures.isEmpty ){
      state.groups ++= groups
    }
    else {

      state.captures ++= captures

      for( c <- captures ){ state.board.clearStones( c.positions.toList ) }

      val newGroups = Groups.findGroups( state.board )
      Liberties.calculateLiberties( state.board, newGroups )

      state.groups ++= newGroups

    }

  }

  def undoable() = true

}