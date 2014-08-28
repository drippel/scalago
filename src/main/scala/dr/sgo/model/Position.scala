package dr.sgo.model

class Position( val X : Int, val Y : Int ) {

  override def toString() : String = { "[" + X +"," + Y + "]" }

  override def equals( obj : Any ) : Boolean = {

    if( obj.isInstanceOf[Position] ){

      val p = obj.asInstanceOf[Position]

      return p.X == X && p.Y == Y

    }

    return false

  }

  override def hashCode() : Int = { X.hashCode + Y.hashCode }

  def n() = { new Position( X, ( Y - 1 )) }
  def s() = { new Position( X, ( Y + 1 )) }
  def e() = { new Position( X + 1, Y ) }
  def w() = { new Position( X - 1, Y ) }

  def ne() = { new Position( X + 1, Y - 1 ) }
  def nw() = { new Position( X - 1, Y - 1 ) }
  def se() = { new Position( X + 1, Y + 1 ) }
  def sw() = { new Position( X - 1, Y + 1 ) }

  def lateral() = { List(n,s,e,w) }
  def diagonal() = { List(ne,nw,se,sw) }
  def neighbors() = { lateral ++ diagonal }
}

object Position {

  def validPositions( ps : List[Position], size : Int ) : List[Position] = {
    ps.filter( (p) => { p.X >= 0 && p.X < size && p.Y >= 0 && p.Y < size } )
  }

  implicit def tupleToPos( t : (Int,Int) ) : Position = {
    new Position( t._1, t._2 )
  }
}