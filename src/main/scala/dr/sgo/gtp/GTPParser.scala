package dr.sgo.gtp

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

  /*
   * 6 Commands
6.1 Required Commands
All implementations are required to support the following commands:

protocol_version
name
version
known_command
list_commands
quit
boardsize
clear_board
komi
play
genmove

6.2 Protocol Subsets
6.2.1 Tournament
The tournament subset adds the commands:

fixed_handicap
place_free_handicap
set_free_handicap

6.2.2 Regression
The regression subset adds the commands:

loadsgf
reg_genmove


6.3 List of All Commands

6.3.1 Adminstrative Commands

    protocol_version
    arguments   none
    effects     none
    output  version_number
        int version_number - Version of the GTP Protocol
    fails   never
    comments    For this specification 2.

    name
    arguments   none
    effects     none
    output  name
        string* name - Name of the engine
    fails   never
    comments    E.g. ``GNU Go'', ``GoLois'', ``Many Faces of Go''. The name does not include any version information, which is provided by the version command.

    version
    arguments   none
    effects     none
    output  version
        string* version - Version of the engine
    fails   never
    comments    E.g. ``3.1.33'', ``10.5''. Engines without a sense of version number should return the empty string.

    known_command
    arguments   command_name
        string command_name - Name of a command
    effects     none
    output  known
        boolean known - ``true'' if the command is known by the engine, ``false'' otherwise
    fails   never
    comments    The protocol makes no distinction between unknown commands and known but unimplemented ones. Do not declare a command as known if it is known not to work.

    list_commands
    arguments   none
    effects     none
    output  commands
        string& commands - List of commands, one per row
    fails   never
    comments    Include all known commands, including required ones and private extensions.

    quit
    arguments   none
    effects     The session is terminated and the connection is closed.
    output  none
    fails   never
    comments    The full response of this command must be sent before the engine closes the connection. The controller must receive the response before the connection is closed on its side.

6.3.2 Setup Commands

    boardsize
    arguments   size
        int size - New size of the board.
    effects     The board size is changed. The board configuration, number of captured stones, and move history become arbitrary.
    output  none
    fails   Syntax error. If the engine cannot handle the new size, fails with the error message "unacceptable size".
    comments    In GTP version 1 this command also did the work of clear_board. This may or may not be true for implementations of GTP version 2. Thus the controller must call clear_board explicitly. Even if the new board size is the same as the old one, the board configuration becomes arbitrary.

    clear_board
    arguments   none
    effects     The board is cleared, the number of captured stones is reset to zero for both colors and the move history is reset to empty.
    output  none
    fails   never
    comments

    komi
    arguments   new_komi
        float new_komi - New value of komi.
    effects     Komi is changed.
    output  none
    fails   syntax error
    comments    The engine must accept the komi even if it should be ridiculous.

    fixed_handicap
    arguments   number_of_stones
        int number_of_stones - Number of handicap stones.
    effects     Handicap stones are placed on the board according to the specification in section 4.1.1.
    output  vertices
        vertex* vertices - A list of the vertices where handicap stones have been placed.
    fails   syntax error, invalid number of stones, board not empty
    comments    This command is only valid if the board is empty. See section 4.1.1 for valid number of handicap stones. The handicap stones are not included in the move history.

    place_free_handicap
    arguments   number_of_stones
        int number_of_stones - Number of handicap stones.
    effects     Handicap stones are placed on the board on the vertices the engine prefers. See also section 4.1.2.
    output  vertices
        vertex* vertices - A list of the vertices where handicap stones have been placed.
    fails   syntax error, invalid number of stones, board not empty, bad vertex list
    comments    This command is only valid if the board is empty. The engine may place fewer than the requested number of stones on the board under certain circumstances, as discussed in section 4.1.2. The controller can check this by counting the number of vertices in the response. The handicap stones are not included in the move history. Vertices must not be repeated or include ``pass''.

    set_free_handicap
    arguments   vertices
        vertex* vertices - A list of vertices where handicap stones should be placed on the board.
    effects     Handicap stones are placed on the vertices as requested.
    output  none
    fails   syntax error, board not empty, bad vertex list
    comments    This command is only valid if the board is empty. The list must have at least two elements and no more than the number of board vertices minus one. The engine must accept the handicap placement. The handicap stones are not included in the move history. Vertices must not be repeated or include ``pass''.

6.3.3 Core Play Commands

    play
    arguments   move
        move move - Color and vertex of the move
    effects     A stone of the requested color is played at the requested vertex. The number of captured stones is updated if needed and the move is added to the move history.
    output  none
    fails   syntax error, illegal move. In the latter case, fails with the error message ``illegal move''.
    comments    Consecutive moves of the same color are not considered illegal from the protocol point of view.

    genmove
    arguments   color
        color color - Color for which to generate a move.
    effects     A stone of the requested color is played where the engine chooses. The number of captured stones is updated if needed and the move is added to the move history.
    output  vertex
        vertex$\vert$string vertex - Vertex where the move was played or the string ``resign''.
    fails   never
    comments    Notice that ``pass'' is a valid vertex and should be returned if the engine wants to pass. Use ``resign'' if you want to give up the game. The controller is allowed to use this command for either color, regardless who played the last move.

    undo
    arguments   none
    effects     The board configuration and the number of captured stones are reset to the state before the last move. The last move is removed from the move history.
    output  none
    fails   If the engine is unable to take back the last move, fails with the error message "cannot undo".
    comments    If you want to take back multiple moves, use this command multiple times. The engine may fail to undo if the move history is empty or if the engine only maintains a partial move history, which has been exhausted by previous undos. It is never possible to undo handicap placements. Use clear_board if you want to start over. An engine which never is able to undo should not include this command among its known commands.

6.3.4 Tournament Commands

    time_settings
    arguments   main_time byo_yomi_time byo_yomi_stones
        int main_time - Main time measured in seconds.
        int byo_yomi_time - Byo yomi time measured in seconds.
        int byo_yomi_stones - Number of stones per byo yomi period.
    effects     The time settings are changed.
    output  none
    fails   syntax error
    comments    The interpretation of the parameters is discussed in section 4.2. The engine must accept the requested values. This command gives no provision for negotiation of the time settings.

    time_left
    arguments   color time stones
        color color - Color for which the information applies.
        int time - Number of seconds remaining.
        int stones - Number of stones remaining.
    effects     none
    output  none
    fails   syntax error
    comments    While the main time is counting, the number of remaining stones is given as 0.

    final_score
    arguments   none
    effects     none
    output  score
        string score - Score as described in section 4.3.
    fails   If the engine is unable to determine the score, fails with error message ``cannot score''.
    comments    If the engine never is able to determine the score, leave the command unimplemented.

    final_status_list
    arguments   status
        string status - Requested status.
    effects     none
    output  stones
        vertex*& stones - Stones with the requested status.
    fails   syntax error
    comments    See section 4.3 for details.


6.3.5 Regression Commands

    loadsgf
    arguments   filename move_number
        string filename - Name of an sgf file.
        int move_number - Optional move number.
    effects     Board size and komi are set to the values given in the sgf file. Board configuration, number of captured stones, and move history are found by replaying the game record up to the position before move_number or until the end if omitted.
    output  none
    fails   Syntax error. If the file does not exist or cannot be read in because it is broken in some way, fails with the error message ``cannot load file''.
    comments    Due to the syntactical limitations of this protocol, the filename cannot include spaces, hash signs (#), or control characters. The command requires the controller and the engine to share file system, or at least that the controller has sufficient knowledge about the file system of the engine. If move_number is larger than the number of moves in the file, read until the end of the file. This command has no support for sgf files with variations or game collections.

    reg_genmove
    arguments   color
        color color - Color for which to generate a move.
    effects     none
    output  vertex
        vertex$\vert$string vertex - Vertex where the engine would want to play a move or the string ``resign''.
    fails   never
    comments    This command differs from genmove in that it does not play the generated move. It is also advisable to turn off any move randomization since that may cause meaningless regression fluctuations.

6.3.6 Debug Commands

    showboard
    arguments   none
    effects     none
    output  board
        string*& board - A diagram of the board position.
    fails   never
    comments    The engine may draw the board as it likes. It is, however, required to place the coordinates as described in section 2.11. This command is only intended to help humans with debugging and the output should never need to be parsed by another program.

   */


}