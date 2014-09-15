package dr.sgo.gtp

import dr.sgo.model.Game

class GTPResponse( val prefix : String, val id : String, val message : String ) {
  var game : Game = null
}

class UnknownCommand( id : String, cmd : String ) extends GTPResponse( "?", id, cmd ) {
}