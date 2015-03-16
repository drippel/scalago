package dr.sgo.engine

import dr.sgo.model.{Position, Board}

import scala.collection.mutable.ListBuffer

class Analysis( val board : Board ) {

  val alive = ListBuffer[Position]()
  val dead  = ListBuffer[Position]()
  val seki  = ListBuffer[Position]()

}
