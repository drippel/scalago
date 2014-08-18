package dr.sgo.model

class Board( val size : Int ) {

  val grid = Array.fill[Stone](size, size){ new Stone( Open() ) }

  override def clone() : Board = {

    val b = new Board(size)
    for( i <- 0 until size ){
      for( j <- 0 until size ){
        b.grid(i)(j) = grid(i)(j)
      }
    }
    b
  }

  def setStone( stone : Stone, pos : Position ) = { grid(pos.X)(pos.Y) = stone }

  def getStone( pos : Position ) = { grid(pos.X)(pos.Y) }
}