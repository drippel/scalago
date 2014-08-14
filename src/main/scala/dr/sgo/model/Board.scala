package dr.sgo.model

class Board( val size : Int ) {

  val grid = Array.fill[Stone](size, size){ new Stone( Open() ) }

  override def clone() : Board = {

    val b = new Board(size)
    for( i <- 0 to size ){
      for( j <- 0 to size ){
        b.grid(i)(j) = grid(i)(j)
      }
    }
    b
  }
}