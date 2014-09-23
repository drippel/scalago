package dr.sgo.model.play

import scala.collection.mutable.ListBuffer

abstract class Handicap extends Play {
  val positions = ListBuffer[(Int,Int)]()
}