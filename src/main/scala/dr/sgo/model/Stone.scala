package dr.sgo.model

abstract class Color( val value : Int )
case class Open extends Color(0)
case class Black extends Color(1)
case class White extends Color(-1)

class Stone( val color : Color ){
  override def clone() : Stone = { new Stone(color) }

}

object Stone {

  implicit def intToColor( i : Int ) : Color = {
    i match {
      case 1 => { Black() }
      case -1 => { White() }
      case 0 => { Open() }
      case _ => { Open() }
    }
  }

  implicit def intToStone( i : Int ) : Stone = {
    new Stone( i )
  }

  implicit def colorToStone( c : Color ) : Stone = { new Stone( c ) }
}

