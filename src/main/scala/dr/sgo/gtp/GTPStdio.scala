package dr.sgo.gtp

import dr.sgo.model.{Player, Game}

class GTPStdio {

}

object GTPStdio {

  def main( args : Array[String] ) : Unit = {
    
    val ctx = new GTPContext()
    ctx.games += Game.initializeGame( 19, new Player( "white" ), new Player( "black" ) )
    
    while( ctx.ready ){    
        GTPParser.parseLine( Console.readLine ) match {
          case Some(c) => {
            sendResponse( GTPParser.execute( ctx, c ) )
          }
          case None => {
            // nada
          }
        }
    }
    
  }

  def sendResponse( rsp : GTPResponse ) : Unit = {
    Console.out.print( rsp )
  }
}