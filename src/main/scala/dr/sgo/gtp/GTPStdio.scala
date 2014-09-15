package dr.sgo.gtp

class GTPStdio {

}

object GTPStdio {

  def main( args : Array[String] ) : Unit = {
    
    val ctx = new GTPContext()
    
    while( ctx.ready ){    
        GTPParser.parseLine( Console.readLine ) match {
          case Some(c) => {}
          case None => {}
        }
    }
    
  }
}