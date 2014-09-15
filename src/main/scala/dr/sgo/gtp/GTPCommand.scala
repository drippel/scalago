package dr.sgo.gtp

class GTPCommand( val id : String, val cmd : String, val args : List[String] ) {

  override def toString() : String = { "[id="+ id +" cmd="+ cmd +" args="+ args.mkString(",") +"]" }
}

