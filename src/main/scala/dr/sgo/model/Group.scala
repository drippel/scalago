package dr.sgo.model

import scala.collection.mutable.ListBuffer

class Group {

  var color : Color = Open()
  val positions = ListBuffer[Position]()

  var libertiesTotal = 0
  var libertiesFirst = 0
  var libertiesSecond = 0
  var libertiesThird = 0
  var libertiesFourth = 0

  val cutpoints = ListBuffer[Position]()

  override def toString() : String = {
    "[" + color +" " + positions.mkString(" ") + "]"
  }

}