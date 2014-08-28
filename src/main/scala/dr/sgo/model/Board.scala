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

  def getRow( r : Int ) : List[Stone] = {

    val row = for( i <- 0 until size ) yield {
      grid(i)(r)
    }

    row.toList
  }

  def getCol( c : Int ) : List[Stone] = {

    val col = for( j <- 0 until size ) yield {
      grid(c)(j)
    }

    col.toList
  }

  def isOpen( pos : Position ) = { grid(pos.X)(pos.Y).color == Open() }

  def clearStones( ps : List[Position] ) = {
    for( p <- ps ){ grid(p.X)(p.Y) = new Stone( Open() )}
  }
}
