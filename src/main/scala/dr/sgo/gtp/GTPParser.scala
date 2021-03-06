package dr.sgo.gtp

import dr.sgo.engine.RandomMoveEngine
import dr.sgo.model.play.{Place, FreeHandicap, FixedHandicap}
import dr.sgo.sgf.parser.{GameTree, SGFParser}
import dr.sgo.ui.console.SGoConsole
import org.apache.commons.lang3.StringUtils
import dr.sgo.model._
import org.apache.commons.io.IOUtils
import java.io.FileReader
import java.io.File
import org.apache.commons.lang3.math.NumberUtils

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

class GTPParser {

  // commands are in the form of
  // id SPACE command SPACE args NL

  // response
  // =id SPACE result NL NL

  // error
  // ?id SPACE message NL NL

  // comments
  // # comments NL

  // id is optional in both cases
  // http://www.lysator.liu.se/~gunnar/gtp/gtp2-spec-draft2/gtp2-spec.html

}

object GTPParser {

  def readFile(fileName : String) : List[String] = {
    IOUtils.readLines(new FileReader(new File(fileName))).toList
  }

  def parseLines(lines : List[String]) : List[GTPCommand] = {
    lines.map((l) => {
      parseLine(l)
    }).flatten
  }

  def parseLine(line : String) : Option[GTPCommand] = {

    if( StringUtils.isBlank(line) ) {
      None
    } else {
      val stripped = if( line.contains("#") ) {
        line.substring(0, line.indexOf('#'))
      }
      else {
        line
      }

      if( StringUtils.isBlank(stripped) ) {
        None
      } else {

        val parts = StringUtils.split(stripped)

        val t = if( parts(0)(0).isDigit ) {
          (parts.head, parts.tail)
        }
        else {
          ("", parts)
        }

        Some(new GTPCommand(t._1, t._2.head, t._2.tail.toList))

      }
    }
  }

  def execute(cmds : List[GTPCommand]) : GTPContext = {

    val ctx = new GTPContext()
    ctx.games += Game.initializeGame(19, new Player("white"), new Player("black"))

    for( c <- cmds ) {
      execute(ctx, c)
    }

    ctx
  }

  def isKnown( command : GTPCommand) : GTPResponse = {
    val found = if( knownCommands.contains( command.cmd ) ){ "true" } else { "false" }
    new GTPResponse( "=", command.id, found )
  }

  def listCommands(command : GTPCommand) : GTPResponse = {
    new GTPResponse( "=", command.id, knownCommands.mkString( "\n" ) )
  }

  def quit(context : GTPContext, command : GTPCommand) : GTPResponse = {
    context.ready = false
    new GTPResponse( "=", command.id, "" )
  }

  def setBoardsize(context : GTPContext, command : GTPCommand) : GTPResponse = {

    // validate the param
    if( command.args.isEmpty ){
      new ErrorResponse( command.id, "unacceptable size" )
    }
    else {
      if( !NumberUtils.isDigits( command.args(0) ) ){
        new ErrorResponse( command.id, "unacceptable size" )
      }
      else {

        val size = NumberUtils.toInt( command.args(0), -1 )
        if( size >= 9 && size <= 19 && ( size % 2 == 1 ) ){
          val curr = context.games.last
          context.games += Game.initializeGame( size, curr.white, curr.black )
          new SuccessResponse( command.id, "ok" )
        }
        else {
          new ErrorResponse( command.id, "unacceptable size" )
        }

      }
    }

  }

  def clearBoard(context : GTPContext, command : GTPCommand) : GTPResponse = {
    val curr = context.games.last
    context.games += Game.initializeGame( curr.size, curr.white, curr.black )
    new SuccessResponse( command.id, "ok" )
  }

  def setKomi(context : GTPContext, command : GTPCommand) : GTPResponse = {

    if( command.args.isEmpty ){
      new ErrorResponse( command.id, "invalid komi" )
    }
    else {
      if( !NumberUtils.isNumber( command.args(0) ) ){
        new ErrorResponse( command.id, "invalid komi" )
      }
      else {
        context.games.last.komi = Some( new Komi( command.args(0) ) )
        new SuccessResponse( command.id, "ok" )
      }
    }

  }

  def fixedHandicap(context : GTPContext, command : GTPCommand) : GTPResponse = {

    if( command.args.isEmpty ){
      new ErrorResponse( command.id, "invalid handicap" )
    }
    else {
      if( !NumberUtils.isDigits( command.args(0) ) ) {
        new ErrorResponse( command.id, "invalid handicap" )
      }
      else{
        val handicap = NumberUtils.toInt( command.args(0), -1 )
        if( handicap > 1 && handicap < 10 ){
          // if the game is in progress - setting a handicap is not allowed
          if( context.games.last.inProgress() ){
            new ErrorResponse( command.id, "game in progress" )
          }
          else {
            val play = new FixedHandicap( handicap )
            Game.execute( context.games.last, Some(context.games.last.black), play )
            val ts = play.positions.map( (p) => { translate(p) } )
            val msg = ts.mkString( " " )
            new SuccessResponse( command.id, msg )
          }
        }
        else {
          new ErrorResponse( command.id, "invalid handicap" )
        }
      }
    }

  }

  def translate( pos : (Int,Int) ) : String = {
    Cols(pos._1).toString + ( pos._2 + 1 )
  }

  def translate( coords : String ) : Option[(Int,Int)] = {
    val x = coords.head.toUpper
    val y = coords.tail

    if( !Cols.contains(x) ){
      None
    }
    else {
      if( !NumberUtils.isDigits(y)){
        None
      }
      else {
        Some((Cols.indexOf(x), (y.toInt - 1) ))
      }
    }
  }

  val Cols = List( 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T' )

  def showboard(context : GTPContext, cmd : GTPCommand ) : GTPResponse = {
    new SuccessResponse( cmd.id, "\n" + SGoConsole.toString( context.games.last ) )
  }

  def setFreeHandicap(context : GTPContext, command : GTPCommand) : GTPResponse = {

    if( command.args.isEmpty ){
      new ErrorResponse( command.id, "missing vertices" )
    }
    else {

      val positions = command.args.map( (s) => { translate(s) } )

      if( positions.find( (p) => { p match{ case None => true case _ => false } } ).size > 0 ) {
        new ErrorResponse( command.id, "invalid vertices" )
      }
      else {
        // all are good lets add
        if( context.games.last.inProgress() ){
          new ErrorResponse( command.id, "game in progress" )
        }
        else{
          val play = new FreeHandicap( positions.flatten )
          Game.execute( context.games.last, Some(context.games.last.black), play )
          new SuccessResponse( command.id, "" )
        }
      }

    }
  }


  def toColor( s : String ) : Option[Color] = {
    val color = s(0).toUpper match {
      case 'W' => Some(White())
      case 'B' => Some(Black())
      case _ => None
    }
    color
  }

  def play(context : GTPContext, command : GTPCommand) : GTPResponse = {

    if( command.args.isEmpty || command.args.size != 2 ){
      new ErrorResponse( command.id, "syntax error" )
    }
    else {
      val color = toColor( command.args(0) )

      val pos = translate( command.args(1) )

      if( color.isEmpty || pos.isEmpty ){
        new ErrorResponse( command.id, "syntax error" )
      }
      else {
        val p = new Place( color.get, pos.get )
        val g = context.games.last
        // TODO: logic here to make sure its a valid move
        Game.execute( g, Some(g.getPlayer(color.get)), p )
        new SuccessResponse( command.id, "" )
      }
    }
  }

  def generateMove(context : GTPContext, command : GTPCommand, playIt : Boolean = true ) : GTPResponse = {

    if( command.args.isEmpty || command.args.size != 1 ){
      new ErrorResponse( command.id, "syntax error" )
    }
    else {

      val color = toColor( command.args(0) )

      if( color.isEmpty ){
        new ErrorResponse( command.id, "syntax error" )
      }
      else {

        val engine = new RandomMoveEngine()
        val move = engine.generateMove( context.games.last.currentState().board, color.get )

        if( move.position.isEmpty ){
          new ErrorResponse( command.id, "syntax error" )
        }
        else {

          val pos = move.position.get
          if( playIt ) {
            val play = new Place(color.get, pos)
            val game = context.games.last
            Game.execute(game, Some(game.getPlayer(color.get)), play)
          }

          new SuccessResponse( command.id, translate( (pos.X, pos.Y ) ) )

        }
      }
    }

  }

  def undo(context : GTPContext, command : GTPCommand) : GTPResponse = {

    if( context.games.last.canUndo() ){
       context.games.last.undo()
       new SuccessResponse( command.id, "" )
    }
    else {
      new ErrorResponse( command.id, "cannot undo" )
    }
  }

  def loadSGF(context : GTPContext, command : GTPCommand) : GTPResponse = {

    if( command.args.isEmpty || command.args.size != 2 ){
      new ErrorResponse( command.id, "syntax error" )
    }
    else {

      val file = command.args(0)
      val parser = new SGFParser()
      parser.loadSGF( file ) match {

        case ("OK", Some(t)) => {

          var game = GameTree.convert(t)
          context.games += game


          // TODO: need to roll to the move specified

          new SuccessResponse( command.id, "" )
        }
        case _ => {
          new ErrorResponse( command.id, "syntax error" )
        }

      }
    }
  }

  def setTimeSettings(context : GTPContext, command : GTPCommand) : GTPResponse = {
    if( command.args.isEmpty || command.args.size != 3 ){
      new ErrorResponse( command.id, "syntax error" )
    }
    else {
      // make sure all args are ints
      if( command.args.exists( (s) => { !NumberUtils.isDigits( s ) } ) ){
        new ErrorResponse( command.id, "syntax error" )
      }
      else {

        val nums = command.args.map( (s) => { NumberUtils.toInt(s) } )
        val state = context.games.last.currentState()
        state.mainTime = nums(0)
        state.byoYomiTime = nums(1)
        state.byoYomiMoves = nums(2)
        new SuccessResponse( command.id, "" )
      }
    }
  }

  def setTimeLeft(context : GTPContext, command : GTPCommand) : GTPResponse = {

    if( command.args.isEmpty || command.args.size != 3 ){
      new ErrorResponse( command.id, "syntax error" )
    }
    else {

      val color = toColor( command.args(0) )
      if( color.isEmpty ){
        new ErrorResponse( command.id, "syntax error" )
      }
      else {

        if( !NumberUtils.isDigits( command.args(1) ) || !NumberUtils.isDigits( command.args(2) ) ){
          new ErrorResponse( command.id, "syntax error" )
        }
        else {

          val nums = command.args.tail.map( (s) => { NumberUtils.toInt(s) } )

          val state = context.games.last.currentState()
          color match {
            case Some(Black()) => {
              state.blackTimeLeft = nums(0).toString
              state.blackMovesLeft = nums(1).toString
            }
            case Some(White()) => {
              state.whiteTimeLeft = nums(0).toString
              state.whiteMovesLeft = nums(1).toString
            }
          }
          new SuccessResponse( command.id, "" )
        }
      }
    }
  }

  def finalScore(context : GTPContext, command : GTPCommand) : GTPResponse = {
    new SuccessResponse( command.id, "0" )
  }

  def finalStatusList(context : GTPContext, command : GTPCommand) : GTPResponse = {
    if( command.args.isEmpty ) {
      new ErrorResponse( command.id, "syntax error" )
    }
    else {

      val engine = new RandomMoveEngine()
      val analysis = engine.analyze( context.games.last.currentState().board )

      command.args(0).toLowerCase() match {
        case "alive" => {
          val ps = analysis.alive
          val ts = ps.map( (p) => { (p.X,p.Y) } )
          val coords = ts.map( (p) => { translate(p) } )
          new SuccessResponse( command.id, coords.mkString(" " ) )
        }
        case "seki" => {
          val ps = analysis.seki
          val ts = ps.map( (p) => { (p.X,p.Y) } )
          val coords = ts.map( (p) => { translate(p) } )
          new SuccessResponse( command.id, coords.mkString(" " ) )
        }
        case "dead" => {
          val ps = analysis.dead
          val ts = ps.map( (p) => { (p.X,p.Y) } )
          val coords = ts.map( (p) => { translate(p) } )
          new SuccessResponse( command.id, coords.mkString(" " ) )
        }
        case _ => {  new ErrorResponse( command.id, "syntax error" ) }
      }
    }
  }

  def execute(ctx : GTPContext, cmd : GTPCommand) : GTPResponse = {

    // required commands
    cmd.cmd.toLowerCase() match {
      // required commands
      case "protocol_version" => { new GTPResponse("=", cmd.id, "2") }
      case "name" => { new GTPResponse("=", cmd.id, "sgo") }
      case "version" => { new GTPResponse("=", cmd.id, "0.1") }
      case "known_command" => { isKnown( cmd ) }
      case "list_commands" => { listCommands( cmd ) }
      case "quit" => { quit( ctx, cmd ) }
      case "boardsize" => { setBoardsize( ctx, cmd ) }
      case "clear_board" => { clearBoard( ctx, cmd ) }
      case "komi" => { setKomi( ctx, cmd ) }
      case "play" => { play( ctx, cmd ) }
      case "genmove" => { generateMove( ctx, cmd ) }
      // tournament
      case "fixed_handicap" => { fixedHandicap( ctx, cmd ) }
      case "place_free_handicap" => { fixedHandicap( ctx, cmd ) }
      case "set_free_handicap" => { setFreeHandicap( ctx, cmd ) }
      // regression
      case "loadsgf" => { loadSGF( ctx, cmd ) }
      case "reg_genmove" => { generateMove( ctx, cmd, false ) }
      // play commands
      case "undo" => { undo( ctx, cmd ) }
      // other tournament commands
      case "time_settings" => { setTimeSettings( ctx, cmd ) }
      case "time_left" => { setTimeLeft( ctx, cmd ) }
      case "final_score" => { finalScore( ctx, cmd ) }
      case "final_status_list" => { finalStatusList( ctx, cmd ) }
      // debug
      case "showboard" => { showboard( ctx, cmd ) }
        // gnugo commands
      case "aa_confirm_safety" => { new UnimplementedCommand(cmd.id, "aa_confirm_safety") }
      case "accurate_approxlib" => { new UnimplementedCommand(cmd.id, "accurate_approxlib") }
      case "accuratelib" => { new UnimplementedCommand(cmd.id, "accuratelib") }
      case "advance_random_seed" => { new UnimplementedCommand(cmd.id, "advance_random_seed") }
      case "all_legal" => { new UnimplementedCommand(cmd.id, "all_legal") }
      case "all_move_values" => { new UnimplementedCommand(cmd.id, "all_move_values") }
      case "analyze_eyegraph" => { new UnimplementedCommand(cmd.id, "analyze_eyegraph") }
      case "analyze_semeai" => { new UnimplementedCommand(cmd.id, "analyze_semeai") }
      case "analyze_semeai_after_move" => { new UnimplementedCommand(cmd.id, "analyze_semeai_after_move") }
      case "attack" => { new UnimplementedCommand(cmd.id, "attack") }
      case "attack_either" => { new UnimplementedCommand(cmd.id, "attack_either") }
      case "black" => { new UnimplementedCommand(cmd.id, "playblack") }
      case "block_off" => { new UnimplementedCommand(cmd.id, "block_off") }
      case "break_in" => { new UnimplementedCommand(cmd.id, "break_in") }
      case "captures" => { new UnimplementedCommand(cmd.id, "captures") }
      case "clear_cache" => { new UnimplementedCommand(cmd.id, "clear_cache") }
      case "color" => { new UnimplementedCommand(cmd.id, "color") }
      case "combination_attack" => { new UnimplementedCommand(cmd.id, "combination_attack") }
      case "combination_defend" => { new UnimplementedCommand(cmd.id, "combination_defend") }
      case "connect" => { new UnimplementedCommand(cmd.id, "connect") }
      case "countlib" => { new UnimplementedCommand(cmd.id, "countlib") }
      case "cputime" => { new UnimplementedCommand(cmd.id, "cputime") }
      case "decrease_depths" => { new UnimplementedCommand(cmd.id, "decrease_depths") }
      case "defend" => { new UnimplementedCommand(cmd.id, "defend") }
      case "defend_both" => { new UnimplementedCommand(cmd.id, "defend_both") }
      case "disconnect" => { new UnimplementedCommand(cmd.id, "disconnect") }
      case "does_attack" => { new UnimplementedCommand(cmd.id, "does_attack") }
      case "does_defend" => { new UnimplementedCommand(cmd.id, "does_defend") }
      case "does_surround" => { new UnimplementedCommand(cmd.id, "does_surround") }
      case "dragon_data" => { new UnimplementedCommand(cmd.id, "dragon_data") }
      case "dragon_status" => { new UnimplementedCommand(cmd.id, "dragon_status") }
      case "dragon_stones" => { new UnimplementedCommand(cmd.id, "dragon_stones") }
      case "draw_search_area" => { new UnimplementedCommand(cmd.id, "draw_search_area") }
      case "dump_stack" => { new UnimplementedCommand(cmd.id, "dump_stack") }
      case "echo" => { new UnimplementedCommand(cmd.id, "echo") }
      case "echo_err" => { new UnimplementedCommand(cmd.id, "echo_err") }
      case "estimate_score" => { new UnimplementedCommand(cmd.id, "estimate_score") }
      case "eval_eye" => { new UnimplementedCommand(cmd.id, "eval_eye") }
      case "experimental_score" => { new UnimplementedCommand(cmd.id, "experimental_score") }
      case "eye_data" => { new UnimplementedCommand(cmd.id, "eye_data") }
      case "findlib" => { new UnimplementedCommand(cmd.id, "findlib") }
      case "finish_sgftrace" => { new UnimplementedCommand(cmd.id, "finish_sgftrace") }
      case "fixed_handicap" => { new UnimplementedCommand(cmd.id, "fixed_handicap") }
      case "followup_influence" => { new UnimplementedCommand(cmd.id, "followup_influence") }
      case "genmove_black" => { new UnimplementedCommand(cmd.id, "genmove_black") }
      case "genmove_white" => { new UnimplementedCommand(cmd.id, "genmove_white") }
      case "get_connection_node_counter" => { new UnimplementedCommand(cmd.id, "get_connection_node_counter") }
      case "get_handicap" => { new UnimplementedCommand(cmd.id, "get_handicap") }
      case "get_komi" => { new UnimplementedCommand(cmd.id, "get_komi") }
      case "get_life_node_counter" => { new UnimplementedCommand(cmd.id, "get_life_node_counter") }
      case "get_owl_node_counter" => { new UnimplementedCommand(cmd.id, "get_owl_node_counter") }
      case "get_random_seed" => { new UnimplementedCommand(cmd.id, "get_random_seed") }
      case "get_reading_node_counter" => { new UnimplementedCommand(cmd.id, "get_reading_node_counter") }
      case "get_trymove_counter" => { new UnimplementedCommand(cmd.id, "get_trymove_counter") }
      case "gg-undo" => { new UnimplementedCommand(cmd.id, "gg_undo") }
      case "gg_genmove" => { new UnimplementedCommand(cmd.id, "gg_genmove") }
      case "half_eye_data" => { new UnimplementedCommand(cmd.id, "half_eye_data") }
      case "help" => { new UnimplementedCommand(cmd.id, "help") }
      case "increase_depths" => { new UnimplementedCommand(cmd.id, "increase_depths") }
      case "initial_influence" => { new UnimplementedCommand(cmd.id, "initial_influence") }
      case "invariant_hash_for_moves" => { new UnimplementedCommand(cmd.id, "invariant_hash_for_moves") }
      case "invariant_hash" => { new UnimplementedCommand(cmd.id, "invariant_hash") }
      case "is_legal" => { new UnimplementedCommand(cmd.id, "is_legal") }
      case "is_surrounded" => { new UnimplementedCommand(cmd.id, "is_surrounded") }
      case "kgs-genmove_cleanup" => { new UnimplementedCommand(cmd.id, "kgs_genmove_cleanup") }
      case "ladder_attack" => { new UnimplementedCommand(cmd.id, "ladder_attack") }
      case "last_move" => { new UnimplementedCommand(cmd.id, "last_move") }
      case "level" => { new UnimplementedCommand(cmd.id, "set_level") }
      case "limit_search" => { new UnimplementedCommand(cmd.id, "limit_search") }
      case "list_stones" => { new UnimplementedCommand(cmd.id, "list_stones") }
      case "move_influence" => { new UnimplementedCommand(cmd.id, "move_influence") }
      case "move_probabilities" => { new UnimplementedCommand(cmd.id, "move_probabilities") }
      case "move_reasons" => { new UnimplementedCommand(cmd.id, "move_reasons") }
      case "move_uncertainty" => { new UnimplementedCommand(cmd.id, "move_uncertainty") }
      case "move_history" => { new UnimplementedCommand(cmd.id, "move_history") }
      case "new_score" => { new UnimplementedCommand(cmd.id, "estimate_score") }
      case "orientation" => { new UnimplementedCommand(cmd.id, "set_orientation") }
      case "owl_attack" => { new UnimplementedCommand(cmd.id, "owl_attack") }
      case "owl_connection_defends" => { new UnimplementedCommand(cmd.id, "owl_connection_defends") }
      case "owl_defend" => { new UnimplementedCommand(cmd.id, "owl_defend") }
      case "owl_does_attack" => { new UnimplementedCommand(cmd.id, "owl_does_attack") }
      case "owl_does_defend" => { new UnimplementedCommand(cmd.id, "owl_does_defend") }
      case "owl_substantial" => { new UnimplementedCommand(cmd.id, "owl_substantial") }
      case "owl_threaten_attack" => { new UnimplementedCommand(cmd.id, "owl_threaten_attack") }
      case "owl_threaten_defense" => { new UnimplementedCommand(cmd.id, "owl_threaten_defense") }
      case "place_free_handicap" => { new UnimplementedCommand(cmd.id, "place_free_handicap") }
      case "popgo" => { new UnimplementedCommand(cmd.id, "popgo") }
      case "printsgf" => { new UnimplementedCommand(cmd.id, "printsgf") }
      case "query_boardsize" => { new UnimplementedCommand(cmd.id, "query_boardsize") }
      case "query_orientation" => { new UnimplementedCommand(cmd.id, "query_orientation") }
      case "reg_genmove" => { new UnimplementedCommand(cmd.id, "reg_genmove") }
      case "report_uncertainty" => { new UnimplementedCommand(cmd.id, "report_uncertainty") }
      case "reset_connection_node_counter" => { new UnimplementedCommand(cmd.id, "reset_connection_node_counter") }
      case "reset_life_node_counter" => { new UnimplementedCommand(cmd.id, "reset_life_node_counter") }
      case "reset_owl_node_counter" => { new UnimplementedCommand(cmd.id, "reset_owl_node_counter") }
      case "reset_reading_node_counter" => { new UnimplementedCommand(cmd.id, "reset_reading_node_counter") }
      case "reset_search_mask" => { new UnimplementedCommand(cmd.id, "reset_search_mask") }
      case "reset_trymove_counter" => { new UnimplementedCommand(cmd.id, "reset_trymove_counter") }
      case "restricted_genmove" => { new UnimplementedCommand(cmd.id, "restricted_genmove") }
      case "same_dragon" => { new UnimplementedCommand(cmd.id, "same_dragon") }
      case "set_free_handicap" => { new UnimplementedCommand(cmd.id, "set_free_handicap") }
      case "set_random_seed" => { new UnimplementedCommand(cmd.id, "set_random_seed") }
      case "set_search_diamond" => { new UnimplementedCommand(cmd.id, "set_search_diamond") }
      case "set_search_limit" => { new UnimplementedCommand(cmd.id, "set_search_limit") }
      case "start_sgftrace" => { new UnimplementedCommand(cmd.id, "start_sgftrace") }
      case "surround_map" => { new UnimplementedCommand(cmd.id, "surround_map") }
      case "test_eyeshape" => { new UnimplementedCommand(cmd.id, "test_eyeshape") }
      case "top_moves" => { new UnimplementedCommand(cmd.id, "top_moves") }
      case "top_moves_black" => { new UnimplementedCommand(cmd.id, "top_moves_black") }
      case "top_moves_white" => { new UnimplementedCommand(cmd.id, "top_moves_white") }
      case "tryko" => { new UnimplementedCommand(cmd.id, "tryko") }
      case "trymove" => { new UnimplementedCommand(cmd.id, "trymove") }
      case "tune_move_ordering" => { new UnimplementedCommand(cmd.id, "tune_move_ordering") }
      case "unconditional_status" => { new UnimplementedCommand(cmd.id, "unconditional_status") }
      case "white" => { new UnimplementedCommand(cmd.id, "playwhite") }
      case "worm_cutstone" => { new UnimplementedCommand(cmd.id, "worm_cutstone") }
      case "worm_data" => { new UnimplementedCommand(cmd.id, "worm_data") }
      case "worm_stones" => { new UnimplementedCommand(cmd.id, "worm_stones") }
      case _ => { new UnknownCommand(cmd.id, cmd.cmd) }
    }

  }

  val knownCommands = List[String](
      "protocol_version",
      "name" ,
      "version" ,
      "known_command" ,
      "list_commands" ,
      "quit" ,
      "boardsize" ,
      "clear_board" ,
      "komi" ,
      "play" ,
      "genmove" ,
      "fixed_handicap" ,
      "place_free_handicap" ,
      "set_free_handicap" ,
      "loadsgf" ,
      "reg_genmove" ,
      "undo" ,
      "time_settings" ,
      "time_left" ,
      "final_score" ,
      "final_status_list" ,
      "showboard"
      /* ,
      "aa_confirm_safety" ,
      "accurate_approxlib" ,
      "accuratelib" ,
      "advance_random_seed" ,
      "all_legal" ,
      "all_move_values" ,
      "analyze_eyegraph" ,
      "analyze_semeai" ,
      "analyze_semeai_after_move" ,
      "attack" ,
      "attack_either" ,
      "black" ,
      "block_off" ,
      "break_in" ,
      "captures" ,
      "clear_cache" ,
      "color" ,
      "combination_attack" ,
      "combination_defend" ,
      "connect" ,
      "countlib" ,
      "cputime" ,
      "decrease_depths" ,
      "defend" ,
      "defend_both" ,
      "disconnect" ,
      "does_attack" ,
      "does_defend" ,
      "does_surround" ,
      "dragon_data" ,
      "dragon_status" ,
      "dragon_stones" ,
      "draw_search_area" ,
      "dump_stack" ,
      "echo" ,
      "echo_err" ,
      "estimate_score" ,
      "eval_eye" ,
      "experimental_score" ,
      "eye_data" ,
      "final_status_list" ,
      "findlib" ,
      "finish_sgftrace" ,
      "fixed_handicap" ,
      "followup_influence" ,
      "genmove_black" ,
      "genmove_white" ,
      "get_connection_node_counter" ,
      "get_handicap" ,
      "get_komi" ,
      "get_life_node_counter" ,
      "get_owl_node_counter" ,
      "get_random_seed" ,
      "get_reading_node_counter" ,
      "get_trymove_counter" ,
      "gg-undo" ,
      "gg_genmove" ,
      "half_eye_data" ,
      "help" ,
      "increase_depths" ,
      "initial_influence" ,
      "invariant_hash_for_moves" ,
      "invariant_hash" ,
      "is_legal" ,
      "is_surrounded" ,
      "kgs-genmove_cleanup" ,
      "ladder_attack" ,
      "last_move" ,
      "level" ,
      "limit_search" ,
      "list_stones" ,
      "move_influence" ,
      "move_probabilities" ,
      "move_reasons" ,
      "move_uncertainty" ,
      "move_history" ,
      "new_score" ,
      "orientation" ,
      "owl_attack" ,
      "owl_connection_defends" ,
      "owl_defend" ,
      "owl_does_attack" ,
      "owl_does_defend" ,
      "owl_substantial" ,
      "owl_threaten_attack" ,
      "owl_threaten_defense" ,
      "place_free_handicap" ,
      "popgo" ,
      "printsgf" ,
      "query_boardsize" ,
      "query_orientation" ,
      "reg_genmove" ,
      "report_uncertainty" ,
      "reset_connection_node_counter" ,
      "reset_life_node_counter" ,
      "reset_owl_node_counter" ,
      "reset_reading_node_counter" ,
      "reset_search_mask" ,
      "reset_trymove_counter" ,
      "restricted_genmove" ,
      "same_dragon" ,
      "set_free_handicap" ,
      "set_random_seed" ,
      "set_search_diamond" ,
      "set_search_limit" ,
      "start_sgftrace" ,
      "surround_map" ,
      "test_eyeshape" ,
      "top_moves" ,
      "top_moves_black" ,
      "top_moves_white" ,
      "tryko" ,
      "trymove" ,
      "tune_move_ordering" ,
      "unconditional_status" ,
      "white" ,
      "worm_cutstone" ,
      "worm_data" ,
      "worm_stones"
      */
    )
}