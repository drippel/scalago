package dr.sgo.model

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.HashSet

object Liberties {

  def calculateLiberties( board : Board, groups : List[Group] ) : Unit = {
    for( g <- groups ){
      calculateLiberties( board, g )
    }
  }

  def calculateLiberties( board : Board, group : Group ) : Unit = {

    val allLibs = HashSet[Position]()

    for( p <- group.positions ) {
      val libs = calculateLiberties( board, p )
      allLibs ++= libs

      libs.size match {
        case 1 => { group.libertiesFirst += 1 }
        case 2 => { group.libertiesSecond += 1 }
        case 3 => { group.libertiesThird += 1 }
        case 4 => { group.libertiesFourth += 1 }
        case _ => {}
      }
    }

    group.liberties ++= allLibs.toList
  }

  def calculateLiberties( board : Board, position : Position ) : List[Position] = {

    val neighbors = Position.validPositions( position.lateral, board.size )

    for( n <- neighbors; if( board.isOpen( n ) ) ) yield n

  }
}