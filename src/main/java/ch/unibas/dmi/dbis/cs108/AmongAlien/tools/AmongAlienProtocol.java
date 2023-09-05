package ch.unibas.dmi.dbis.cs108.AmongAlien.tools;

import com.google.common.annotations.Beta;

/**
 * This Enum includes all command witch server and client uses for their communication.
 * The commands have a description and are sorted by them use. It is clearly documented
 * how they work. If there are parameters needed they will be added in [ ]. In the brackets
 * we describe what argument should be given as a parameter and if there can be more than
 * just one parameter, the next ones will be added in them own brackets. If a command requires
 * no parameters there are simply no brackets.
 *
 * @author Joel Erbsland, Rayes Diyab and Hamza Zarah
 */
public enum AmongAlienProtocol {
    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*/
    /*--------------------------------------------------------------------------------*/
    /*                                Server --> Client                               */
    /*--------------------------------------------------------------------------------*/
    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*/
    /* The following commands are commands send from server to client. That means the */
    /* client has to interpret these commands and the server has to send the correct  */
    /* commands in the right case.                                                    */
    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*/

    /*----------------------------------------------------------------------*/
    /*                        Commands for connection                       */
    /*----------------------------------------------------------------------*/


    /**
     * Server sends a pong-request to the client.
     *
     * Use this commands as followed:
     * /PONG
     */
    PONG,

    /*----------------------------------------------------------------------*/
    /*                              Lobby Commands                          */
    /*----------------------------------------------------------------------*/

    /**
     * Server sends the highscore as a String to the client.
     *
     * Use this commands as followed:
     * /YOURSCORE [String including the Highscorelist]
     */
    YOURSCORE,

    /*----------------------------------------------------------------------*/
    /*                               Game Commands                          */
    /*----------------------------------------------------------------------*/

    /**
     * Sets the intial Name of the player.
     *
     * Use this commands as followed:
     * /INIT [Players initial name]
     */
    INIT,

    /**
     * Server gives clients the new positions of the players.
     *
     * Use this commands as followed:
     * /NEWPOS [xPos:yPos:num]:[xPos:yPos:num]: ... :[xPos:yPos:num]
     */
    NEWPOS,

    /**
     * Server sends position of the new Materials to the client.
     *
     * Use this commands as followed:
     * /NEWMAT:[xPos:yPos:matID]:[xPos:yPos:matID]: ... :[xPos:yPos:matID]
     */
    NEWMAT,

    /**
     * Material is taken and removed from map
     *
     * Use this commands as followed:
     * /DELMAT [xPos]:[yPos]:[2]
     */
    DELMAT,

    /**
     * The voting starts and every screen has to change to voting.
     *
     * Use this commands as followed:
     * /VOTESTART
     */
    VOTESTART,

    /**
     * To actualize the Map with a given Array column:row:textureID:fildUSE
     *
     * Use this commands as followed:
     * /ACTMAP [column]:[row]:[textureID]:[fildUSE]:
     *         [column]:[row]:[textureID]:[fildUSE]:
     *         ... :
     *         [column]:[row]:[textureID]:[fildUSE]
     */
    ACTMAP,

    /**
     * To actualize the Materiallist of a Player.
     *
     * Use this commands as followed:
     * /ACTMAT materials[11]:materials[21]:materials[30]:materials[31]:materials[32]:materials[34]
     * Humanreadable: note that ao means amountOf
     * /ACTMAT [ao Stone]:[ao Wood]:[ao Clay]:[ao Strawberry]:[ao Berrybush]:[ao Fish]
     */
    ACTMAT,

    /*----------------------------------------------------------------------*/
    /*                               Chat Commands                          */
    /*----------------------------------------------------------------------*/

    /**
     * Server sends Message to Client
     *
     * Use this commands as followed:
     * /RECMSG [Message]
     */
    RECMSG,



    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*/
    /*--------------------------------------------------------------------------------*/
    /*                                Client --> Server                               */
    /*--------------------------------------------------------------------------------*/
    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*/
    /* The following commands are commands send from client to server. That means the */
    /* server has to interpret these commands and the client has to send the correct  */
    /* commands in the right case.                                                    */
    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*/

    /*----------------------------------------------------------------------*/
    /*                        Commands for connection                       */
    /*----------------------------------------------------------------------*/


    /**
     * The server will be closed.
     *
     * Use this commands as followed:
     * /QUIT
     */
    @Beta QUIT,

    /**
     * Prints a PING
     *
     * Use this commands as followed:
     * /PING
     */
    PING,

    /*----------------------------------------------------------------------*/
    /*                              Lobby Commands                          */
    /*----------------------------------------------------------------------*/

    /**
     * To set the username, can just be written in chat.
     *
     * Use this commands as followed:
     * /SETNAME [new username]
     */
    SETNAME,

    /**
     * If a player entered a lobby name an presses the start lobby
     * button.
     *
     * Use this commands as followed:
     * /NEWLOBBY [Lobbyname]
     */
    NEWLOBBY,

    /**
     * If a player presses the join butten behind a lobby.
     *
     * Use this commands as followed:
     * /JOINLOBBY [Lobbyname]
     */
    JOINLOBBY,

    /**
     * If a player presses the leave button on his GUI.
     *
     * Use this commands as followed:
     * /LEAVELOBBY
     */
    LEAVELOBBY,

    /**
     * If a client presses the startgame button on his GUI and the
     * lobby has status startable, then this command will be sent
     * to the server who then starts the game.
     *
     * Use this commands as followed:
     * /STARTGAME
     */
    STARTGAME,

    /**
     * Client asks server for the highscorelist as String. Server answers with
     * the /YOURSCORE [Highscorelist] command.
     *
     * Use this commands as followed:
     * /GETSCORE
     */
    GETSCORE,

    /*----------------------------------------------------------------------*/
    /*                               Game Commands                          */
    /*----------------------------------------------------------------------*/

    /**
     * Client requests new position to be set for him
     *
     * Use this commands as followed:
     * /SETPOS [xPos]:[yPos]
     */
    SETPOS,

    /**
     * for sending position
     *
     * Use this commands as followed:
     * /SENDPOS
     */
    SENDPOS,

    /**
     * If a player takes a material
     *
     * Use this commands as followed:
     * /ADDMAT [xPos]:[yPos]:2
     */
    ADDMAT,

    /**
     * Vote for a specific player in the voting
     *
     * Use this commands as followed:
     * /VOTEFOR [player voted]
     */
    VOTEFOR,

    /**
     * Server sends Client the material he just picked up.
     *
     * Use this commands as followed:
     * /TAKEMAT [Materials ID]
     */
    TAKEMAT,

    /*----------------------------------------------------------------------*/
    /*                               Chat Commands                          */
    /*----------------------------------------------------------------------*/

    /**
     * To show commands on chatwindow
     *
     * Use this commands as followed:
     * /HELP
     */
    HELP,

    /**
     * To send a message to everyone in the same game
     *
     * Use this commands as followed:
     * /SETMSG [Message]
     */
    SETMSG,

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*/
    /*--------------------------------------------------------------------------------*/
    /*                               Unmatched commands                               */
    /*--------------------------------------------------------------------------------*/
    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*/

    /**
     * sets the Playernumber
     *
     * Use this commands as followed:
     * /PLAYERNUM
     */
    @Beta PLAYERNUM,

    /**
     * Server changes Map.
     *
     * Use this commands as followed:
     * /SETMAP [Mapname]
     */
    @Beta SETMAP,

    /**
     * Client wants do a Task at his position.
     *
     * Use this commands as followed:
     * /DOTASK [xPos]:[yPos]
     */
    DOTASK,

    /**
     * Server starts Day.
     *
     * Use this commands as followed:
     * /STARTDAY [numMembers]:[gameNum]
     */
    STARTDAY,

    /**
     * Server actualizes the Playerlist.
     * Playerlist as String looks as followed: [Player1]:[Player2]: ... :[PlayerN]
     *
     * Use this commands as followed:
     * /UPDATEPLIST [Playerlist as String]
     */
    UPDATEPLIST,

    /**
     * Server says that the Humans won.
     *
     * Use this commands as followed:
     * /WONGAME
     */
    @Beta WONGAME,

    /**
     * Server says that Player have won.
     *
     * Use this commands as followed:
     * /GAMEISWON
     */
    @Beta GAMEISWON,

    /**
     * Server says that the Aliens won.
     *
     * Use this commands as followed:
     * /GAMEISLOST
     */
    @Beta GAMEISLOST,

    /**
     * Server says that the Aliens won.
     *
     * Use this commands as followed:
     * /LOSTGAME
     */
    @Beta LOSTGAME,

    /**
     * Server updates Lobbylist.
     *
     * Use this commands as followed:
     * /UPDATELOBBYLIST
     * or
     * /UPDATELOBBYLIST [updated Lobbylist]
     */
    UPDATELOBBYLIST,

    /**
     * Server updates the names in the Lobby.
     * Playerlist as String looks as followed: [Player1]:[Player2]: ... :[PlayerN]
     *
     * Use this commands as followed:
     * /UPDATELOBBYPLAYERS
     * or
     * /UPDATELOBBYPLAYERS [Playerlist as String]
     */
    UPDATELOBBYPLAYERS,

    /**
     * Server tells client who is ejected, client sends response.
     *
     * Use this commands as followed:
     * /EJECTION [Playernum]
     * or to Skip vote:
     * /EJECTION [10]
     */
    EJECTION,

    /**
     * Server changes hunger of the client.
     *
     * Use this commands as followed:
     * /CHANGEHUNGER [new Hungervalue 0.0 up to 1.0]
     */
    CHANGEHUNGER,

    /**
     * Server changes safety of the village.
     *
     * Use this commands as followed:
     * /CHANGESAFETY [new Safetyvalue 0.0 up to 1.0]
     */
    CHANGESAFETY,

    /**
     * Players can choose their Playerimage. Server tells Client who choose what.
     *
     * Use this commands as followed:
     * /SETPLAYERIMAGE [Playernum = number of Image]
     */
    SETPLAYERIMAGE,

    /**
     * Server tells player to switch to revealGUI and his number and if he is an Alien.
     *
     * Use this commands as followed:
     * /REVEAL [number and Boolean]
     * ex. 5true
     */
    @Beta REVEAL,

    /**
     * Server tells players everyones outfit/Image.
     *
     * Use this commands as followed:
     * /SETOUTFIT
     */
    SETOUTFIT,

    /**
     * Updates the List of the currently in the Clients Lobby joined Players
     *
     * Use this commands as followed:
     * /UPDATEGAMECLIENTS [new Clientlist]
     */
    UPDATEGAMECLIENTS,

    /**
     * Server tells who is the Alien.
     *
     * Use this commands as followed:
     * /MAKEALIEN [Boolean]
     */
    MAKEALIEN,

    /**
     * Tells the Client that the Connection to the Server has been lost
     *
     * Use this commands as followed:
     * /CONNECTIONLOST
     */
    CONNECTIONLOST,

    /**
     * Server tells client which playernumbers are still active.
     * Playerlist as String looks as followed: [Player1]:[Player2]: ... :[PlayerN]
     *
     * Use this commands as followed:
     * /SETACTIVE [List of active Players as String like PlayerList]
     */
    SETACTIVE,

    /**
     * Alien marks a player.
     *
     * Use this commands as followed:
     * /MARK
     */
    MARK,

    /**
     * Sends the Server a Login request with the UserName
     *
     * Use this commands as followed:
     * /LOGIN [userName]
     */
    LOGIN,

    /**
     * Sends the Server a Logout request
     * The Server deletes the User from the Server
     * tells the Client to quit
     *
     * Use this commands as followed:
     * /LOGOUT
     */
    LOGOUT,

    /**
     *  Client asks Server to Update the List with all Players for him
     *  which the Server sends back to him
     *
     *  Use this commands as followed:
     *  /UPDATEPLAYERS
     */
    UPDATEPLAYERS,

    /**
     *  Client asks the Server to quit the Lobby GUI
     *  The Servers tells him to QUIT and updates the Server accordingly
     *
     *  Use this commands as followed:
     *  /QUITLOBBYGUI
     */
    QUITLOBBYGUI,

    /**
     *  Server tells the Client to update his GUI
     *  with his new Name
     *
     *  Use this commands as followed:
     *  /UPDATENAME [userName]
     */
    UPDATENAME,

    /**
     *  Server tells Client to update his GUI
     *  with the LobbyName he just joined
     *
     *  Use this commands as followed:
     *  /UPDATEPLAYERLOBBY [Lobbyname]
     */
    UPDATEPLAYERLOBBY,

    /**
     *  Server tells Client to update his GUI
     *  and change the CurrentLobby Name to blank
     *  after leaving the Lobby
     *
     *  Use this commands as followed:
     *  /UPDATELEFTLOBBY
     */
    UPDATELEFTLOBBY,

    /**
     *  Server tells Client to change Scene
     *  to the Lobby GUI
     *
     *  Use this commands as followed:
     *  /LOBBYSCREEN
     */
    LOBBYSCREEN,

    /**
     *  Server tells the Client that he is the one
     *  who has been voted out and tells him to
     *  Change Scene to the Lobby GUI
     *
     *  Use this commands as followed:
     *  /VOTEDOUT
     */
    VOTEDOUT,

    /**
     *  Server tells the Client who the latest Marked Player
     *  by an alien is
     *
     *  Use this commands as followed:
     *  /UPDATEMARKED [marked Player]
     */
    UPDATEMARKED,

    /**
     *  Server tells the Marked Player that he is Marked
     *  and kindly tells him to leave the Game because he
     *  has been abducted
     *
     *  Use this commands as followed:
     *  /ABDUCTED
     */
    ABDUCTED,

    /**
     *  Client asks Server to Quit the Game and close the Client
     *  after winning or losing a Game
     *
     *  Use this commands as followed:
     *  /QUITGAMEGUI
     */
    QUITGAMEGUI,

    /**
     *  Client asks Server to provide a List
     *  with all Games (Open, Ongoing and Finished)
     *
     *  Use this commands as followed:
     *  /GETGAMELIST
     */
    GETGAMELIST,

    /**
     * Server tells player that he has run out of food
     * And that he has been removed from the game
     * and tells him to return to the Lobby
     */
    HUNGERED,

    /**
     * Server gives the player updated Lobby infos
     * like a new Lobbylist
     * The Lobby Players and the Clients Name
     */
    UPDATELOBBYGUI,

    /**
     * Server sends the CLient a List with all
     * the Games on the Server (Open, Ongoing and Finished)
     *
     * Use this commands as followed:
     * /GAMELIST [List of Games]
     */
     GAMELIST
}
