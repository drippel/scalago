package dr.sgo.model

import scala.collection.mutable.ListBuffer

object Groups {

  def byColor( board : Board, color : Color ) : List[Position] = {

    val ps = for( col <- 0 until board.size;
        row <- 0 until board.size;
        if( board.grid(col)(row).color == color ) ) yield {
      new Position( col, row )
    }

    ps.toList
  }

  def blacks( board : Board ) : List[Position] = { byColor( board, Black() ) }

  def whites( board : Board ) : List[Position] = { byColor( board, White() ) }

  def findChains( board : Board ) : List[Chain] = {

    val bcs = findChains( board, blacks(board) )
    val wcs = findChains( board, whites(board) )

    bcs ++ wcs
  }

  def findChains( board : Board, positions : List[Position] ) : List[Chain] = {
    val found = ListBuffer[Chain]()

    for( p <- positions ){
      if( !found.exists( ( c ) => { c.positions.contains(p) } ) ){
        found += followChain( board, p )
      }
    }

    found.toList
  }

  def validPositions( ps : List[Position], size : Int ) : List[Position] = {
    ps.filter( (p) => { p.X >= 0 && p.X < size && p.Y >= 0 && p.Y < size } )
  }

  def followChain( board : Board, position : Position ) : Chain = {

    val chain = new Chain()
    chain.color = board.grid(position.X)(position.Y).color

    def followChainInner( ip : Position ) : Unit = {

      if( board.grid(ip.X)(ip.Y).color == chain.color ){
        if( !chain.positions.contains(ip) ){

          // add to chain
          chain.positions += ip

          // visit neighbors
          val vps = validPositions( ip.lateral, board.size )
          // val neighbors = vps.filter( (p) => {} )
          vps.foreach( (p) => followChainInner(p))
        }
      }

    }

    followChainInner(position)

    chain

  }



}