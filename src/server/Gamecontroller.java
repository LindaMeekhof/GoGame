package server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import general.Protocol;
import servermodel.Board;
import servermodel.Stone;
import serverview.TUIserver;

public class Gamecontroller extends Protocol implements Runnable {
	
	private ServerGO serverGO;
	private ServerClient[] players;
	private int currentPlayer;
//	private int boardSize;
	private Board board;
	private static final int NUMBER_OF_PLAYERS = 2;
	private boolean versionReseived;
	private boolean versionPlayer1;
	private boolean versionPlayer2;
	private int lastPlayer;
	private String currentMove;
	private String previousMove;	
	
	//terminated game
	private boolean playerQuitGame;
	private boolean isFinished;
	private boolean timeOut;

	private boolean hasBoard;
	private int abortedPlayer;
	private boolean isAborted;
	private int dimensionBoard;
	private int amountBlackStones;
	private int amountWhiteStones;
	private boolean stoneBasketEmpty;
	private TUIserver view;
//	private boolean settingsInput;
	private List<String> inputUser;
	
	/** 
	 * Creates a Gamecontroller with two Serverclients for the in and output control.
	 * @param player1
	 * @param player2
	 */
	public Gamecontroller(ServerGO serverGO, ServerClient player1, ServerClient player2) {
		players = new ServerClient[NUMBER_OF_PLAYERS];
		this.serverGO = serverGO;
		players[0] = player1;
		players[1] = player2;
		currentPlayer = 0;
		lastPlayer = 0;
		previousMove = "-1_-1";
		board = new Board();
		
		inputUser = Collections.synchronizedList(new ArrayList<String>());
//		settingsInput = false;
		versionReseived = false;
		versionPlayer1 = false;
		versionPlayer2 = false;
		
		//termination of the game
		playerQuitGame = false;
		isFinished = false;
		timeOut = false;
		hasBoard = false;
		isAborted = false;
		stoneBasketEmpty = false; 
		
		this.view = new TUIserver(board);
	}

	
	/** Handle the input form both users and take action.
	 * 
	 */
	@Override
	public void run() {
//Set this Gamecontroller in the ServerClients.
		players[0].setGamecontroller(this);
		players[1].setGamecontroller(this);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.out.println("interrupted while sleeping");
		}

//  sending information about name VERSIONNUMBER and extensions. ------------
	
		sendMessageToBoth(Server.NAME + General.DELIMITER1 + serverGO.getName() + 
					General.DELIMITER1 + Server.VERSION + General.DELIMITER1 + Server.VERSIONNO + 
					General.DELIMITER1 + Server.EXTENSIONS + General.DELIMITER1 + 0 + 
					General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + General.DELIMITER1 + 
					0 + General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + General.DELIMITER1 + 0);
				
// The game -----------------------------------------------------------		
		while (!isTerminated()) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				System.out.println("interrupted while sleeping");
			}
			
			if (!inputUser.isEmpty()) {
// Splitting inputStream ---------------------------------------------------------

			
				String inputReadable = inputUser.get(0).replaceAll("\\" + General.DELIMITER1, " ");
				String[] words = inputUser.get(0).split("\\" + General.DELIMITER1);
				String idPlayer = words[0];
				String commando = words[1];

// Getting name, version and extension information----------------------------------------------
// Both players have send the information -> the game can start
				if (commando.equals(Client.NAME)) {

					if (words.length > 5 && isInteger(words[4])) {
						if (idPlayer.equals(players[0].getClientTag())) {
							print(inputReadable);
							versionPlayer1 = true;
						
							players[0].setNamePlayer(words[2]);

							// step 1. check VERSIONNUMBER
							if (words[4].equals("6")) {
								print("versionnumbers are equal");
							} else {
								players[0].sentGameData(Server.INCOMPATIBLEPROTOCOL);
							}
							
							// Step 2 remove the message
							inputUser.remove(0);
						} else {
							print(inputReadable);
							versionPlayer2 = true;
							
							players[1].setNamePlayer(words[2]);

							// step 1. check VERSIONNUMBER
							if (words[4].equals("6")) {
								view.print("versionnumbers are equal");
							} else {
								players[0].sentGameData(Server.INCOMPATIBLEPROTOCOL);
							}

							//Step 2 remove the message
							inputUser.remove(0);
						}
					} else {
						players[playerByIDtag(words[0])].sentGameData(Server.INCOMPATIBLEPROTOCOL + 
								General.DELIMITER1 + "no valid versioninput");
					}

					if (versionPlayer1 && versionPlayer2) {
						versionReseived = true; 
						view.print("beide versies ontvangen");
					}	

					// step 2. sending START ask for color and board size
					players[currentPlayer].sentGameData(Server.START + 
							General.DELIMITER1 + NUMBER_OF_PLAYERS);
				}
//----------------------------------------------------------------------------
//Starting game when settings are send back. Color and board size. 
				else if (isIDcurrentPlayer(idPlayer) && words.length == 4 
						&& commando.equalsIgnoreCase(Client.SETTINGS) 
						&& isColor(words[2]) && isInteger(words[3]) && versionReseived) {
					
					//Step 1. set up game
					dimensionBoard = Integer.parseInt(words[3]);
					amountBlackStones = dimensionBoard / 2 + 1;
					amountWhiteStones = dimensionBoard / 2;
					
					board.boardInit(Integer.parseInt(words[3])); 
					setColor(words[2]);
					hasBoard = true;
	//				settingsInput = true;
					
					//Step 2. print board TUI
					print(board.toString());
					
					//Step 3. remove this message
					inputUser.remove(0);
					
					//Step 4. Ask the first player to make the first move.
					players[0].sentGameData(Server.START + General.DELIMITER1 + NUMBER_OF_PLAYERS
							+ General.DELIMITER1 + words[2] + General.DELIMITER1 + 
							words[3] + General.DELIMITER1 + 
							players[0].getNamePlayer() + General.DELIMITER1 + 
							players[1].getNamePlayer());
					
					players[1].sentGameData(Server.START + General.DELIMITER1 + NUMBER_OF_PLAYERS 
							+ General.DELIMITER1 + turn(words[2]) + General.DELIMITER1 + 
							words[3] + General.DELIMITER1 + players[0].getNamePlayer() + 
							General.DELIMITER1 + players[1].getNamePlayer());
					
					this.currentPlayer = setCurrentPlayer();
					
					//Step 5. Starting the game with asking for the first turn
					sendMessageToBoth(Server.TURN + General.DELIMITER1 + 
							players[currentPlayer].getNamePlayer() + General.DELIMITER1
							+ Server.FIRST + General.DELIMITER1 + 
							players[currentPlayer].getNamePlayer());
				}
	//--------------------------------------------------------------------
	// Move for game row_colom	
				else if (isIDcurrentPlayer(idPlayer) && commando.equalsIgnoreCase(Client.MOVE))  {
					
					// Step 1 split words for coordinates
					String[] coordinates = words[2].split(General.DELIMITER2);
					
					// Step 2 Determine if it are coordinates or PASS
					if (isInteger(coordinates[0]) && isInteger(coordinates[1])) {
					
						int row = Integer.parseInt(coordinates[0]);	
						int col = Integer.parseInt(coordinates[1]);
						
						// Step 3 check if it is a valid move
						Stone testStone = players[currentPlayer].getStone();
						if (board.isValidMove(row, col, testStone) && 
								!board.isPreviousBoard(row, col, testStone)) { 
						
						// Step 4 update board, game and TUI
							Stone stone = players[currentPlayer].getStone();
							board.setField(row, col, stone);
							view.print(board.toString());
							
							turnPlayer(words[2]);
							
							
							sendMessageToBoth(Server.TURN + General.DELIMITER1 + 
									players[lastPlayer].getNamePlayer() + 
									General.DELIMITER1 + currentMove + General.DELIMITER1 
									+ players[currentPlayer].getNamePlayer());
							
							//board update	
							board.updateBoard(row, col, stone);
							stoneBasketEmpty = emptyBasket();
							calculateStone(players[currentPlayer].getStone());
							
							//Step 5 remove message
							inputUser.remove(0);
						} else {
							sendMessageToCurrent(Server.INVALID + "please enter a valid move");
							inputUser.remove(0); 
						}
					} else if (words[2].equalsIgnoreCase(Client.PASS)) {
						
						currentMove = Client.PASS;
						inputUser.remove(0);
						
						// Check if the game is finished with two consecutive passes
						finished();
						turnPlayer(words[2]);
						
						sendMessageToBoth(Server.TURN + General.DELIMITER1 + 
								players[lastPlayer].getNamePlayer() + 
								General.DELIMITER1 + currentMove + General.DELIMITER1 
								+ players[currentPlayer].getNamePlayer());
					} else {
						sendMessageToCurrent(Server.INVALID + 
								"please enter a valid move, integers please");
						inputUser.remove(0); 
						print(inputUser.get(0));
					}
	// the opponent sends a move. ----------------------------------------------			
				}
				else if (!isIDcurrentPlayer(idPlayer) && commando.equalsIgnoreCase(Client.MOVE)) {
					print(inputUser.get(0));
					inputUser.remove(0); 
					players[opponent(currentPlayer)].sentGameData(Server.OTHER + 
							"This is not your turn");
				} 
	//QUIT during the game.--------------------------------------------------

				else if (commando.equalsIgnoreCase(Client.QUIT)) {
					view.print("One of the players quit");
					//Send information dat de speler is gestopt.
					abortedPlayer = determineAbortedPlayer(words[0]);
					isAborted = true; 
					
					playerQuitGame = true;
					print(inputUser.get(0));
					inputUser.remove(0); 
					
					//Add them to the Serverlist availableServerclient.
					serverGO.addServerclient(players[0]);
					serverGO.addServerclient(players[1]);
				} else {
					print(words + "test");
					inputUser.remove(0);
				}
			}
		}
	
	// -----------------------------------------------------------------------
	//When the game is terminated send a message to both, and shutdown the program properly. 
		
		view.print("game over");
		if (hasBoard) {
			//Step 1. Sending the information about the endscore
			view.showEndResult(endResult());
			sendMessageToBoth(Server.ENDGAME + General.DELIMITER1 
				+ determineReasonTermination() + General.DELIMITER1 
				+ endResult());
			
			//Step 2. shutdown
			shutdown();
		}
	} // end run method
	
	/**
	 * When the amount of stones is 0. The game is ended.
	 * @return
	 */
	public Boolean emptyBasket() {
		return amountBlackStones == 0 || amountWhiteStones == 0;	
	}
	
// Getters and setters ------------------------------------------------------
	

	public Board getBoard() {
		return board;
	}

	/**
	 * When getting the settings from the client, set the color. 
	 * @param color
	 */
	public void setColor(String color) {
		if (color.equalsIgnoreCase("BLACK")) {
			players[0].setStone("BLACK");
			players[0].setStoneColorString("BLACK");
			players[1].setStone("WHITE");
			players[1].setStoneColorString("WHITE");

		} else {
			players[0].setStone("WHITE");
			players[0].setStoneColorString("WHITE");
			players[1].setStone("BLACK");
			players[1].setStoneColorString("BLACK");
		}
	}

// Other methods, mainly checks -----------------------------------------------------
	
	/**
	 * After the set the player, this method switches the players and set the previousmove.
	 * @param currentMadeMove
	 */
	private void turnPlayer(String currentMadeMove) {
		currentMove = currentMadeMove;
		lastPlayer = currentPlayer; 
		previousMove = currentMove;
		currentPlayer = (currentPlayer + 1) % 2;
	}

	/**
	 * Calculate the amount of stones. When the player has no stones left the game is ended.
	 * @param stone
	 */
	public void calculateStone(Stone stone) {
		if (stone.equals(Stone.b)) {
			amountBlackStones -= 1;
		} else {
			amountWhiteStones -= 1;
		}
		
	}
	/**
	 * Get the opponent of the currentplayer. 
	 * @param currentPlayer
	 * @return
	 */
	public int opponent(int current) {
		if (current == 1) {
			return 0;
		} else {
			return 1;
		}
	}
	
	/**
	 * Determine which player left the game. This score should be 0.
	 * @param idTag
	 * @return
	 */
	public int determineAbortedPlayer(String idTag) {
		if (players[0].getClientTag().equals(idTag)) {
			return 0;
		} else {
			return 1;
		}
	} 
	
	/**
	 * Determine which player send the message.
	 * @param idTag
	 * @return
	 */
	public int playerByIDtag(String idTag) {
		if (players[0].getClientTag().equals(idTag)) {
			return 0;
		} else {
			return 1;
		}
	} 
	
	
	/** 
	 * In GO black starts with the first turn. This method returns which ServerClient is black. 
	 * This can set the currentPlayer to the right player at the client side.
	 * @return
	 */
	public int setCurrentPlayer() {
		if (players[0].getStoneColorString().equals("BLACK")) {
			return 0;
		} else {
			return 1;
		}	
	}

	/**
	 * This checks if the user input contains to integer.
	 * @param expectedInt
	 * @return true if it is an integer.
	 */
	private boolean isInteger(String expectedInt) {
		try {
			Integer.parseInt(expectedInt);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Show the board.
	 */
	public void showBoard() {
		print(board.toString());
	}
	
	/**
	 * Checks if the color given by the client is a valid color. WHITE or BLACK
	 * @param expectedColor
	 * @return
	 */
	public boolean isColor(String expectedColor) {
		return expectedColor.equals("WHITE") || expectedColor.equals("BLACK");
	}
	
	/**
	 * Checks if the messageTag is corresponding to the currentPlayer. 
	 * The other player should not be able to set a stone on the board.
	 * @param clientID
	 * @return
	 */
	public boolean isIDcurrentPlayer(String clientID) {
		return players[currentPlayer].getClientTag().equals(clientID);
	}
	
	/**
	 * Determines if the move is valid. The field should be empty on the board. 
	 * No stone may be played so as to recreate any previous board position.
	 * @return
	 */
	public boolean isValidMove(int row, int col, Stone stone) {
		return board.isField(row, col) &&
				board.isEmptyField(row, col) && 
				!board.isPreviousBoard(row, col, stone);
		
	}
	
// Communication ----------------------------------------------
	/**
	 * Prints messages to System.out.
	 * @param message
	 */
	private void print(String message) {
		System.out.println(message);
		
	}
	
	/**
	 * Sends a message to both players in the game.
	 * @param message
	 */
	public void sendMessageToBoth(String message) {
		players[0].sentGameData(message);
		players[1].sentGameData(message);
	}
	
	/**
	 * Sends a message to the current player.
	 * @param message
	 */
	public void sendMessageToCurrent(String message) {
		players[currentPlayer].sentGameData(message);
	}
	
	/**
	 * Creates the list of messages that are sent by the 
	 * players at the clientside.
	 * @param line
	 */
	public void notify(String line) {
		inputUser.add(line);
	}
	
// Terminate program methods -----------------------------------------------------------------------
	/**
	 * Determine the reason why the game ended. 
	 * Game can end when the game is finished (two times pass or board is full). 
	 * Aborted when one of the players leaves or
	 * timeout when the timer is finished.
	 * @return
	 */
	private String determineReasonTermination() {
		if (isFinished) {
			return Server.FINISHED;
		} else if (playerQuitGame) {
			return Server.ABORTED;
		} else {
			return Server.TIMEOUT;
		}
	}
	
	/**
	 * When the previousMove and currentMove are 
	 * both equal to pass or the board is full the game will end.
	 * @return
	 */
	public void finished() {
		if (previousMove.equalsIgnoreCase(Client.PASS) && 
				currentMove.equalsIgnoreCase(Client.PASS)) {
			isFinished = true;
		} else {
			isFinished = false;
		}
	}
	
	/**
	 * The game is ended when the game isFinished, playerQuitGame or timeOut.
	 * @return true if isFinished, playerQuitGame or timeOut is true.
	 */
	public boolean isTerminated() {
		return isFinished || playerQuitGame || timeOut || stoneBasketEmpty;
	}
	
	/**
	 * Should time the current player. When time is over the game is ended.
	 * @return
	 */
	public boolean isTimeOut() {
		//TODO
		return false;
	}
	
// Scoring -----------------------------------------------------------------------------

	/**
	 * This gives the endResult of the game. 
	 * When a player aborted the game then his or her score is 0.
	 * @return
	 */
	public String endResult() {
		String endresult;
		if (isAborted && abortedPlayer == 0) {
			endresult = players[1].getNamePlayer() + General.DELIMITER1 + 
					board.countScore(players[1].getStone()) + General.DELIMITER1 +
					players[0].getNamePlayer()  + General.DELIMITER1 + 0;
		} else if (isAborted && abortedPlayer == 1) {
			endresult = players[0].getNamePlayer() + General.DELIMITER1 + 
					board.countScore(players[0].getStone()) + General.DELIMITER1 +
					players[1].getNamePlayer()  + General.DELIMITER1 + 0;
		} else if (board.isWinner(players[0].getStone())) {
			endresult = players[0].getNamePlayer() + General.DELIMITER1 + 
					board.countScore(players[0].getStone()) + General.DELIMITER1 
					+ players[1].getNamePlayer() + General.DELIMITER1 + 
					board.countScore(players[1].getStone());
		} else if (board.isWinner(players[1].getStone())) {
			endresult = players[1].getNamePlayer() + General.DELIMITER1 + 
					board.countScore(players[1].getStone()) + General.DELIMITER1 + 
					players[0].getNamePlayer() + General.DELIMITER1 + 
					board.countScore(players[0].getStone());
		} else {
			endresult = players[1].getNamePlayer() + General.DELIMITER1 + 
					board.countScore(players[1].getStone()) + 
					General.DELIMITER1 + players[0].getNamePlayer() + 
					General.DELIMITER1 + board.countScore(players[0].getStone());
		}
		return endresult;
	}

// Other methods ----------------------------------------------------------------------
	
	/**
	 * Switching the color.
	 */
	public String turn(String color) {
		if (color.equals("BLACK")) {
			return "WHITE";
		} else {
			return "BLACK";
		}
	}
	/**
	 * Shutdown the game property.
	 */
	public void shutdown() {
		//TODO
	}
}
