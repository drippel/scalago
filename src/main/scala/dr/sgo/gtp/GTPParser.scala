package dr.sgo.gtp

import org.apache.commons.lang3.StringUtils
import dr.sgo.model.Game
import org.apache.commons.io.IOUtils
import java.io.FileReader
import java.io.File
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer
import dr.sgo.model.Player

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

  def execute(ctx : GTPContext, cmd : GTPCommand) : GTPResponse = {

    // required commands
    cmd.cmd.toLowerCase() match {
      // required commands
      case "protocol_version" => {
        new GTPResponse("=", cmd.id, "2")
      }
      case "name" => {
        new GTPResponse("=", cmd.id, "sgo")
      }
      case "version" => {
        new GTPResponse("=", cmd.id, "0.1")
      }
      case "known_command" => {
        new UnimplementedCommand(cmd.id, "known_command")
      }
      case "list_commands" => {
        new UnimplementedCommand(cmd.id, "list_commands")
      }
      case "quit" => {
        new UnimplementedCommand(cmd.id, "quit")
      }
      case "boardsize" => {
        new UnimplementedCommand(cmd.id, "boardsize")
      }
      case "clear_board" => {
        new UnimplementedCommand(cmd.id, "clear_board")
      }
      case "komi" => {
        new UnimplementedCommand(cmd.id, "komi")
      }
      case "play" => {
        new UnimplementedCommand(cmd.id, "play")
      }
      case "genmove" => {
        new UnimplementedCommand(cmd.id, "genmove")
      }
      // tournament
      case "fixed_handicap" => {
        new UnimplementedCommand(cmd.id, "fixed_handicap")
      }
      case "place_free_handicap" => {
        new UnimplementedCommand(cmd.id, "place_free_handicap")
      }
      case "set_free_handicap" => {
        new UnimplementedCommand(cmd.id, "set_free_handicap")
      }
      // regression
      case "loadsgf" => {
        new UnimplementedCommand(cmd.id, "loadsgf")
      }
      case "reg_genmove" => {
        new UnimplementedCommand(cmd.id, "reg_genmove")
      }
      // play commands
      case "undo" => {
        new UnimplementedCommand(cmd.id, "undo")
      }
      // other tournament commands
      case "time_settings" => {
        new UnimplementedCommand(cmd.id, "time_settings")
      }
      case "time_left" => {
        new UnimplementedCommand(cmd.id, "time_left")
      }
      case "final_score" => {
        new UnimplementedCommand(cmd.id, "final_score")
      }
      case "final_status_list" => {
        new UnimplementedCommand(cmd.id, "final_status_list")
      }
      // debug
      case "showboard" => {
        new UnimplementedCommand(cmd.id, "showboard")
      }
        // gnugo commands
      case "aa_confirm_safety" => {
        new UnimplementedCommand(cmd.id, "aa_confirm_safety")
      }
      case "accurate_approxlib" => {
        new UnimplementedCommand(cmd.id, "accurate_approxlib")
      }
      case "accuratelib" => {
        new UnimplementedCommand(cmd.id, "accuratelib")
      }
      case "advance_random_seed" => {
        new UnimplementedCommand(cmd.id, "advance_random_seed")
      }
      case "all_legal" => {
        new UnimplementedCommand(cmd.id, "all_legal")
      }
      case "all_move_values" => {
        new UnimplementedCommand(cmd.id, "all_move_values")
      }
      case "analyze_eyegraph" => {
        new UnimplementedCommand(cmd.id, "analyze_eyegraph")
      }
      case "analyze_semeai" => {
        new UnimplementedCommand(cmd.id, "analyze_semeai")
      }
      case "analyze_semeai_after_move" => {
        new UnimplementedCommand(cmd.id, "analyze_semeai_after_move")
      }
      case "attack" => {
        new UnimplementedCommand(cmd.id, "attack")
      }
      case "attack_either" => {
        new UnimplementedCommand(cmd.id, "attack_either")
      }
      case "black" => {
        new UnimplementedCommand(cmd.id, "playblack")
      }
      case "block_off" => {
        new UnimplementedCommand(cmd.id, "block_off")
      }
      case "break_in" => {
        new UnimplementedCommand(cmd.id, "break_in")
      }
      case "captures" => {
        new UnimplementedCommand(cmd.id, "captures")
      }
      case "clear_cache" => {
        new UnimplementedCommand(cmd.id, "clear_cache")
      }
      case "color" => {
        new UnimplementedCommand(cmd.id, "color")
      }
      case "combination_attack" => {
        new UnimplementedCommand(cmd.id, "combination_attack")
      }
      case "combination_defend" => {
        new UnimplementedCommand(cmd.id, "combination_defend")
      }
      case "connect" => {
        new UnimplementedCommand(cmd.id, "connect")
      }
      case "countlib" => {
        new UnimplementedCommand(cmd.id, "countlib")
      }
      case "cputime" => {
        new UnimplementedCommand(cmd.id, "cputime")
      }
      case "decrease_depths" => {
        new UnimplementedCommand(cmd.id, "decrease_depths")
      }
      case "defend" => {
        new UnimplementedCommand(cmd.id, "defend")
      }
      case "defend_both" => {
        new UnimplementedCommand(cmd.id, "defend_both")
      }
      case "disconnect" => {
        new UnimplementedCommand(cmd.id, "disconnect")
      }
      case "does_attack" => {
        new UnimplementedCommand(cmd.id, "does_attack")
      }
      case "does_defend" => {
        new UnimplementedCommand(cmd.id, "does_defend")
      }
      case "does_surround" => {
        new UnimplementedCommand(cmd.id, "does_surround")
      }
      case "dragon_data" => {
        new UnimplementedCommand(cmd.id, "dragon_data")
      }
      case "dragon_status" => {
        new UnimplementedCommand(cmd.id, "dragon_status")
      }
      case "dragon_stones" => {
        new UnimplementedCommand(cmd.id, "dragon_stones")
      }
      case "draw_search_area" => {
        new UnimplementedCommand(cmd.id, "draw_search_area")
      }
      case "dump_stack" => {
        new UnimplementedCommand(cmd.id, "dump_stack")
      }
      case "echo" => {
        new UnimplementedCommand(cmd.id, "echo")
      }
      case "echo_err" => {
        new UnimplementedCommand(cmd.id, "echo_err")
      }
      case "estimate_score" => {
        new UnimplementedCommand(cmd.id, "estimate_score")
      }
      case "eval_eye" => {
        new UnimplementedCommand(cmd.id, "eval_eye")
      }
      case "experimental_score" => {
        new UnimplementedCommand(cmd.id, "experimental_score")
      }
      case "eye_data" => {
        new UnimplementedCommand(cmd.id, "eye_data")
      }
      case "final_status_list" => {
        new UnimplementedCommand(cmd.id, "final_status_list")
      }
      case "findlib" => {
        new UnimplementedCommand(cmd.id, "findlib")
      }
      case "finish_sgftrace" => {
        new UnimplementedCommand(cmd.id, "finish_sgftrace")
      }
      case "fixed_handicap" => {
        new UnimplementedCommand(cmd.id, "fixed_handicap")
      }
      case "followup_influence" => {
        new UnimplementedCommand(cmd.id, "followup_influence")
      }
      case "genmove_black" => {
        new UnimplementedCommand(cmd.id, "genmove_black")
      }
      case "genmove_white" => {
        new UnimplementedCommand(cmd.id, "genmove_white")
      }
      case "get_connection_node_counter" => {
        new UnimplementedCommand(cmd.id, "get_connection_node_counter")
      }
      case "get_handicap" => {
        new UnimplementedCommand(cmd.id, "get_handicap")
      }
      case "get_komi" => {
        new UnimplementedCommand(cmd.id, "get_komi")
      }
      case "get_life_node_counter" => {
        new UnimplementedCommand(cmd.id, "get_life_node_counter")
      }
      case "get_owl_node_counter" => {
        new UnimplementedCommand(cmd.id, "get_owl_node_counter")
      }
      case "get_random_seed" => {
        new UnimplementedCommand(cmd.id, "get_random_seed")
      }
      case "get_reading_node_counter" => {
        new UnimplementedCommand(cmd.id, "get_reading_node_counter")
      }
      case "get_trymove_counter" => {
        new UnimplementedCommand(cmd.id, "get_trymove_counter")
      }
      case "gg-undo" => {
        new UnimplementedCommand(cmd.id, "gg_undo")
      }
      case "gg_genmove" => {
        new UnimplementedCommand(cmd.id, "gg_genmove")
      }
      case "half_eye_data" => {
        new UnimplementedCommand(cmd.id, "half_eye_data")
      }
      case "help" => {
        new UnimplementedCommand(cmd.id, "help")
      }
      case "increase_depths" => {
        new UnimplementedCommand(cmd.id, "increase_depths")
      }
      case "initial_influence" => {
        new UnimplementedCommand(cmd.id, "initial_influence")
      }
      case "invariant_hash_for_moves" => {
        new UnimplementedCommand(cmd.id, "invariant_hash_for_moves")
      }
      case "invariant_hash" => {
        new UnimplementedCommand(cmd.id, "invariant_hash")
      }
      case "is_legal" => {
        new UnimplementedCommand(cmd.id, "is_legal")
      }
      case "is_surrounded" => {
        new UnimplementedCommand(cmd.id, "is_surrounded")
      }
      case "kgs-genmove_cleanup" => {
        new UnimplementedCommand(cmd.id, "kgs_genmove_cleanup")
      }
      case "ladder_attack" => {
        new UnimplementedCommand(cmd.id, "ladder_attack")
      }
      case "last_move" => {
        new UnimplementedCommand(cmd.id, "last_move")
      }
      case "level" => {
        new UnimplementedCommand(cmd.id, "set_level")
      }
      case "limit_search" => {
        new UnimplementedCommand(cmd.id, "limit_search")
      }
      case "list_stones" => {
        new UnimplementedCommand(cmd.id, "list_stones")
      }
      case "move_influence" => {
        new UnimplementedCommand(cmd.id, "move_influence")
      }
      case "move_probabilities" => {
        new UnimplementedCommand(cmd.id, "move_probabilities")
      }
      case "move_reasons" => {
        new UnimplementedCommand(cmd.id, "move_reasons")
      }
      case "move_uncertainty" => {
        new UnimplementedCommand(cmd.id, "move_uncertainty")
      }
      case "move_history" => {
        new UnimplementedCommand(cmd.id, "move_history")
      }
      case "new_score" => {
        new UnimplementedCommand(cmd.id, "estimate_score")
      }
      case "orientation" => {
        new UnimplementedCommand(cmd.id, "set_orientation")
      }
      case "owl_attack" => {
        new UnimplementedCommand(cmd.id, "owl_attack")
      }
      case "owl_connection_defends" => {
        new UnimplementedCommand(cmd.id, "owl_connection_defends")
      }
      case "owl_defend" => {
        new UnimplementedCommand(cmd.id, "owl_defend")
      }
      case "owl_does_attack" => {
        new UnimplementedCommand(cmd.id, "owl_does_attack")
      }
      case "owl_does_defend" => {
        new UnimplementedCommand(cmd.id, "owl_does_defend")
      }
      case "owl_substantial" => {
        new UnimplementedCommand(cmd.id, "owl_substantial")
      }
      case "owl_threaten_attack" => {
        new UnimplementedCommand(cmd.id, "owl_threaten_attack")
      }
      case "owl_threaten_defense" => {
        new UnimplementedCommand(cmd.id, "owl_threaten_defense")
      }
      case "place_free_handicap" => {
        new UnimplementedCommand(cmd.id, "place_free_handicap")
      }
      case "popgo" => {
        new UnimplementedCommand(cmd.id, "popgo")
      }
      case "printsgf" => {
        new UnimplementedCommand(cmd.id, "printsgf")
      }
      case "query_boardsize" => {
        new UnimplementedCommand(cmd.id, "query_boardsize")
      }
      case "query_orientation" => {
        new UnimplementedCommand(cmd.id, "query_orientation")
      }
      case "reg_genmove" => {
        new UnimplementedCommand(cmd.id, "reg_genmove")
      }
      case "report_uncertainty" => {
        new UnimplementedCommand(cmd.id, "report_uncertainty")
      }
      case "reset_connection_node_counter" => {
        new UnimplementedCommand(cmd.id, "reset_connection_node_counter")
      }
      case "reset_life_node_counter" => {
        new UnimplementedCommand(cmd.id, "reset_life_node_counter")
      }
      case "reset_owl_node_counter" => {
        new UnimplementedCommand(cmd.id, "reset_owl_node_counter")
      }
      case "reset_reading_node_counter" => {
        new UnimplementedCommand(cmd.id, "reset_reading_node_counter")
      }
      case "reset_search_mask" => {
        new UnimplementedCommand(cmd.id, "reset_search_mask")
      }
      case "reset_trymove_counter" => {
        new UnimplementedCommand(cmd.id, "reset_trymove_counter")
      }
      case "restricted_genmove" => {
        new UnimplementedCommand(cmd.id, "restricted_genmove")
      }
      case "same_dragon" => {
        new UnimplementedCommand(cmd.id, "same_dragon")
      }
      case "set_free_handicap" => {
        new UnimplementedCommand(cmd.id, "set_free_handicap")
      }
      case "set_random_seed" => {
        new UnimplementedCommand(cmd.id, "set_random_seed")
      }
      case "set_search_diamond" => {
        new UnimplementedCommand(cmd.id, "set_search_diamond")
      }
      case "set_search_limit" => {
        new UnimplementedCommand(cmd.id, "set_search_limit")
      }
      case "start_sgftrace" => {
        new UnimplementedCommand(cmd.id, "start_sgftrace")
      }
      case "surround_map" => {
        new UnimplementedCommand(cmd.id, "surround_map")
      }
      case "test_eyeshape" => {
        new UnimplementedCommand(cmd.id, "test_eyeshape")
      }
      case "top_moves" => {
        new UnimplementedCommand(cmd.id, "top_moves")
      }
      case "top_moves_black" => {
        new UnimplementedCommand(cmd.id, "top_moves_black")
      }
      case "top_moves_white" => {
        new UnimplementedCommand(cmd.id, "top_moves_white")
      }
      case "tryko" => {
        new UnimplementedCommand(cmd.id, "tryko")
      }
      case "trymove" => {
        new UnimplementedCommand(cmd.id, "trymove")
      }
      case "tune_move_ordering" => {
        new UnimplementedCommand(cmd.id, "tune_move_ordering")
      }
      case "unconditional_status" => {
        new UnimplementedCommand(cmd.id, "unconditional_status")
      }
      case "white" => {
        new UnimplementedCommand(cmd.id, "playwhite")
      }
      case "worm_cutstone" => {
        new UnimplementedCommand(cmd.id, "worm_cutstone")
      }
      case "worm_data" => {
        new UnimplementedCommand(cmd.id, "worm_data")
      }
      case "worm_stones" => {
        new UnimplementedCommand(cmd.id, "worm_stones")
      }
      case _ => {
        new UnknownCommand(cmd.id, cmd.cmd)
      }
    }

  }

}