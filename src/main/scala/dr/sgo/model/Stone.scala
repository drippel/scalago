package dr.sgo.model

abstract class Color( val value : Int )
case class Open extends Color(0)
case class Black extends Color(1)
case class White extends Color(-1)

class Stone( val color : Color ){
  override def clone() : Stone = { new Stone(color) }
}

