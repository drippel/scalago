package dr.sgo.gtp

import dr.sgo.model.Game

class GTPResponse( val prefix : String, val id : String, val message : String ) {
  var game : Game = null

  override def toString() : String = {
    var rsp = prefix
    if( id != null ){
      rsp += id
    }

    if( message != null ) {
      rsp += " " + message
    }

    rsp += "\n\n"

    rsp

  }
}

class UnknownCommand( id : String, cmd : String ) extends GTPResponse( "?", id, cmd ) {

  override def toString() : String = {
    var rsp = prefix
    if( id != null ){
      rsp += id
    }

    if( message != null ) {
      rsp += " Unknown command:"+ message
    }

    rsp += "\n\n"

    rsp

  }
}

class UnimplementedCommand( id : String, cmd : String ) extends GTPResponse( "?", id, cmd ) {

  override def toString() : String = {
    var rsp = prefix
    if( id != null ){
      rsp += id
    }

    if( message != null ) {
      rsp += " Unimplemented command:"+ message
    }

    rsp += "\n\n"

    rsp

  }
}

class ErrorResponse( id : String, msg : String ) extends GTPResponse( "?", id, msg )
class SuccessResponse( id : String, msg : String ) extends GTPResponse( "=", id, msg )
