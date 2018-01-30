package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.nedap.go.gui.GOGUI;
import com.nedap.go.gui.GOGUIImpl;

import general.Protocol;

import servermodel.Board;
import servermodel.Stone;

public class Gamecontroller extends Protocol implements Runnable {
	
	private ArrayList<String> inputUser;
	private ServerGO serverGO;
	private ServerClient[] players;
	private int currentPlayer;
	private int boardSize;
	private Board board;
	private static final int NUMBER_OF_PLAYERS = 2;
	
	//private int amountPlayers;
	private String lastMessage;
	private boolean settingsInput;
	private boolean versionReseived;
	private boolean versionPlayer1;
	private boolean versionPlayer2;
	private int lastPlayer;
	private int opponent;
	
	//terminated game
	private boolean playerQuitGame;
	//private boolean isTerminated;
	private boolean isFinished;
	private boolean timeOut;
	
	private int version;

	private String currentMove;
	//private String lastMove;
	private String previousMove;
	private Integer[] endResult;
	private boolean hasBoard;
	private int abortedPlayer;
	private boolean isAborted;
	private GOGUI gogui;
	
	private int dimensionBoard;
	private int amountBlackStones;
	private int amountWhiteStones;
	private boolean stoneBasketEmpty;
	
	/** 
	 * Creates a Gamecontroller with two Serverclients for the in and output control.
	 * @param player1
	 * @param player2
	 */
	public Gamecontroller(ServerGO serverGO, ServerClient player1, ServerClient player2) {
		players = new ServerClient[NUMBER_OF_PLAYERS];
//		this.player1 = player1;
//		this.player2 = player2;
		this.serverGO = serverGO;
		players[0] = player1;
		players[1] = player2;
		currentPlayer = 0;
		lastPlayer = 0;
		previousMove = "-1_-1";
		board = new Board();
		inputUser = new ArrayList<String>();
		
		settingsInput = false;
		versionReseived = false;
		versionPlayer1 = false;
		versionPlayer2 = false;
		
		//termination of the game
		//isTerminated = false;
		playerQuitGame = false;
		isFinished = false;
		timeOut = false;
		hasBoard = false;
		isAborted = false;
		stoneBasketEmpty = false;
	}

	
	/** Handle the input form both users and take action.
	 * 
	 */
	@Override
	public void run() {
//Set this Gamecontroller in the ServerClients.
		players[0].setGamecontroller(this);
		players[1].setGamecontroller(this);
		
		System.out.println("The gamecontroller is started");
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
		
		
		//System.out.println("waiting for input from player 0");
				
// during game -----------------------------------------------------------		
		while (!isTerminated()) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				System.out.println("interrupted while sleeping");
			}
			
			if (!inputUser.isEmpty()) {
// Splitting inputStream ---------------------------------------------------------
				System.out.println("De gamecontroller heeft input ontvangen");
				System.out.println(inputUser);
				String inputReadable = inputUser.get(0).replaceAll("\\" + General.DELIMITER1, " ");
		
				String[] words = inputUser.get(0).split("\\" + General.DELIMITER1);
				String idPlayer = words[0];
				String commando = words[1];
//				print(words[0]);
//				print(words[1]);
//				print(players[0].getClientTag());
//				print(players[1].getClientTag());
				
//--------------------------------------------------------------------------------
//getting name, version and extensions. Wanneer beide clients data hebben opgestuurd START
				if (commando.equals(Client.NAME)) {
					
					if (idPlayer.equals(players[0].getClientTag())) {
						print(inputReadable);
						versionPlayer1 = true;
//						System.out.println("" + version);
//						print("version ontvangen");
						
						players[0].setNamePlayer(words[2]);
						
						// step 1. check VERSIONNUMBER
						if (words[4].equals("6")) {
							print("versionnumbers are equal");
						} else {
							players[0].sentGameData(Server.INCOMPATIBLEPROTOCOL);
						}
						
						// Step 2 remove the message
						inputUser.remove(0);
						
					}
					else {
						print(inputReadable);
						versionPlayer2 = true;
						System.out.println("" + version);
						print("version ontvangen2");
						
						players[1].setNamePlayer(words[2]);
						
						// step 1. check VERSIONNUMBER
						if (words[4].equals("6")) {
							print("versionnumbers are equal");
						} else {
							players[0].sentGameData(Server.INCOMPATIBLEPROTOCOL);
						}
						
						//Step 2 remove the message
						inputUser.remove(0);
					}
					
					if (versionPlayer1 && versionPlayer2) {
						versionReseived = true; 
						System.out.println("beide versies ontvangen");
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
					settingsInput = true;
					
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
//					System.out.println(coordinates[0]);
//					System.out.println(coordinates[1]);
					
					// Step 2 Determine if it are coordinates or PASS
					if (isInteger(coordinates[0]) && isInteger(coordinates[1])) {
					
						int row = Integer.parseInt(coordinates[0]);	
						int col = Integer.parseInt(coordinates[1]);
						
						// Step 3 check if it is a valid move
						if (board.isValidMove(row, col)) { 
						
						// Step 4 update board, game and TUI
							Stone stone = players[currentPlayer].getStone();
							board.setField(row, col, stone);
							System.out.println(board.toString());
							print("this is a previousmove bij coordinates" + previousMove);
							turnPlayer(words[2]);
							print("this is a previousmove bij coordinates" + previousMove);
							
							sendMessageToBoth(Server.TURN + General.DELIMITER1 + 
									players[lastPlayer].getNamePlayer() + 
									General.DELIMITER1 + currentMove + General.DELIMITER1 
									+ players[currentPlayer].getNamePlayer());
							
							//board update
							
							calculateStone(players[currentPlayer].getStone());
							
							stoneBasketEmpty = emptyBasket();
							board.updateBoard(row, col, stone);
							
							//Step 5 remove message
							inputUser.remove(0);
						} else {
							sendMessageToCurrent(Server.INVALID + "please enter a valid move");
							inputUser.remove(0); 
						}
					} else if (words[2].equalsIgnoreCase(Client.PASS)) {
						// isFinished = true; PASS two times in a row.
						
						currentMove = Client.PASS;
						inputUser.remove(0);
						
						
						finished();
						turnPlayer(words[2]);
						
						sendMessageToBoth(Server.TURN + General.DELIMITER1 + 
								players[lastPlayer].getNamePlayer() + 
								General.DELIMITER1 + currentMove + General.DELIMITER1 
								+ players[currentPlayer].getNamePlayer());
					}
					else {
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
					players[opponent].sentGameData("This is not your turn");
				}
				// ---------------------------------------------------------------------
	//QUIT during the game. Deze speler wordt afgesloten.
				else if (commando.equalsIgnoreCase(Client.QUIT)) {
					print("One of the players quit");
					//Beide spelers blijven in de available list;
					//Send information dat de speler is gestopt.
					abortedPlayer = determineAbortedPlayer(words[0]);
					isAborted = true; 
					
					playerQuitGame = true;
					print(inputUser.get(0));
					inputUser.remove(0); 
	
				}
				else {
					print(words + "test");
					inputUser.remove(0);
				}
			}
		}
	
	// -----------------------------------------------------------------------
	//When the game is terminated send a message to both, and shutdown the program properly. 
		
		print("game over");
		if (hasBoard) {
			
			sendMessageToBoth(Server.ENDGAME + General.DELIMITER1 
				+ determineReasonTermination() + General.DELIMITER1 
				+ endResult());
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
	
	
	private void turnPlayer(String currentMadeMove) {
		currentMove = currentMadeMove;
		lastPlayer = currentPlayer; 
		previousMove = currentMove;
		currentPlayer = (currentPlayer + 1) % 2;
	}


	public Board getBoard() {
		return board;
	}
	
	public String stoneToName() {
		//TODO
		return "";
	}


//	public void getName(String color) {
//		if (players[0].getName(color).equals("BLACK")) {
//			players[0].setStone("BLACK");
//			players[1].setStone("WHITE");
//		} else {
//			players[0].setStone("WHITE");
//			players[1].setStone("BLACK");
//		}
//	}

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
	public int opponent(int currentPlayer) {
		if (currentPlayer == 1) {
			return 0;
		} else
			return 1;
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
	public Boolean isColor(String expectedColor) {
		return expectedColor.equals("WHITE") || expectedColor.equals("BLACK");
	}
	
	/**
	 * Checks if the messageTag is corresponding to the currentPlayer. 
	 * The other player should not be able to set a stone on the board.
	 * @param clientID
	 * @return
	 */
	public Boolean isIDcurrentPlayer(String clientID) {
		return players[currentPlayer].getClientTag().equals(clientID);
	}
	
	/**
	 * Determines if the move is valid. The field should be empty on the board. 
	 * No stone may be played so as to recreate any previous board position.
	 * @return
	 */
	public boolean isValidMove(int row, int col, String boardString) {
		return board.isField(row, col) &&
				board.isEmptyField(row, col) && 
				!board.isPreviousBoard(boardString); //TODO
		
	}
	
// Communication ----------------------------------------------
	private void print(String message) {
		System.out.println(message);
		
	}
	
	public void sendMessageToBoth(String message) {
		players[0].sentGameData(message);
		players[1].sentGameData(message);
	}
	
	public void sendMessageToCurrent(String message) {
		players[currentPlayer].sentGameData(message);
	}
	
	/**
	 * Creates the list of messages that are send.
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
	public Boolean isTerminated() {
		return isFinished || playerQuitGame || timeOut || stoneBasketEmpty;
	}
		
	public Boolean isTimeOut() {
		//TODO
		return false;
	}
	

// Scoring -----------------------------------------------------------------------------
	
	
	/**
	 * This method maps the players name with the score.
	 */
	public Map<String, Integer> endScore() { 
		int scorePlayer1 = board.countScore(players[0].getStone());
		int scorePlayer2 = board.countScore(players[1].getStone());


		// Mapping the score to the color
		Map<String, Integer> scores = new HashMap<String, Integer>();
		scores.put(players[0].getName(), scorePlayer1);
		scores.put(players[1].getName(), scorePlayer2);

		return scores;
	}
	
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
