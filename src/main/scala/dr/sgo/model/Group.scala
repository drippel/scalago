package dr.sgo.model

import scala.collection.mutable.ListBuffer

class Group {

  var color : Color = Open()
  val positions = ListBuffer[Position]()
  val liberties = ListBuffer[Position]()

  var libertiesFirst = 0
  var libertiesSecond = 0
  var libertiesThird = 0
  var libertiesFourth = 0

  val cutpoints = ListBuffer[Position]()

  override def toString() : String = {
    "[" + color +" " + positions.mkString(" ") + "]"
  }

  override def equals( obj : Any ) : Boolean = {

    if( !obj.isInstanceOf[Group] ){
      false
    }
    else {

      val g = obj.asInstanceOf[Group]
      g.positions.equals(positions)

    }

  }

}