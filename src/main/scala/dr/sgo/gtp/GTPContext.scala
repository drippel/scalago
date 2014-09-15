package dr.sgo.gtp

import scala.collection.mutable.ListBuffer
import dr.sgo.model.Game

class GTPContext {

  var ready = true
  val games = ListBuffer[Game]()
}