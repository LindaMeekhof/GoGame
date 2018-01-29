package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import general.Protocol;
import general.Protocol.General;
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
	private boolean isTerminated;
	private boolean isFinished;
	private boolean timeOut;
	
	private int version;

	private String currentMove;
	private String previousMove;
	private Integer[] endResult;
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
		board = new Board();
		inputUser = new ArrayList<String>();
		
		settingsInput = false;
		versionReseived = false;
		versionPlayer1 = false;
		versionPlayer2 = false;
		
		//termination of the game
		isTerminated = false;
		playerQuitGame = false;
		isFinished = false;
		timeOut = false;
	}

	
	/** Handle the input form both users and take action.
	 * 
	 */
	@Override
	public void run() {
		//Set this Gamecontroller in the ServerClients.
		players[0].setGamecontroller(this);
		players[1].setGamecontroller(this);
		
		
		
		System.out.println("The gamecontroller is started + 1 ");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

// ----------------------------------------------------------------------
//  sending information about name versionnumber and extenions.
		sendMessageToBoth(Server.NAME + General.DELIMITER1 + serverGO.getName() + 
					General.DELIMITER1 + Server.VERSION + General.DELIMITER1 + Server.VERSIONNO + 
					General.DELIMITER1 + Server.EXTENSIONS + 0 + General.DELIMITER1 + 0 + 
					General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + 
					General.DELIMITER1 + 0 + General.DELIMITER1 + 0);
		
		
		//System.out.println("waiting for input from player 0");
				
// during game -----------------------------------------------------------		
		while (!isTerminated) {
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			if (!inputUser.isEmpty()) {
// Splitting inputStream ---------------------------------------------------------
				System.out.println("De gamecontroller heeft input ontvangen");

				String inputReadable = inputUser.get(0).replaceAll("\\" + General.DELIMITER1, " ");;
				
		
				
		
				String[] words = inputUser.get(0).split("\\" + General.DELIMITER1);
				String idPlayer = words[0];
				String commando = words[1];
				print(words[0]);
				print(words[1]);
				print(players[0].getClientTag());
				print(players[1].getClientTag());
				
//--------------------------------------------------------------------------------
//getting name, version and extensions. Wanneer beide clients data hebben opgestuurd START
				if (commando.equals(Client.NAME)) {
					
					if (idPlayer.equals(players[0].getClientTag())) {
						print(inputReadable);
						versionPlayer1 = true;
//						System.out.println("" + version);
//						print("version ontvangen");
						inputUser.remove(0);
						players[0].setNamePlayer(words[2]);
						
					}
					else {
						print(inputReadable);
						versionPlayer2 = true;
						System.out.println("" + version);
						print("version ontvangen2");
						inputUser.remove(0);
						players[1].setNamePlayer(words[2]);
					}
					
					if (versionPlayer1 && versionPlayer2) {
						versionReseived = true; 
						System.out.println("beide versies ontvangen");
						
					//sending START en vragen om de gegevens kleur en bordgrootte 
						players[currentPlayer].sentGameData(Server.START);
					}
				}
//----------------------------------------------------------------------------
//Starting game when settings are send back. kleur en boardsize --> moet nog && settingsInput
				
				else if (isID(idPlayer) && words.length == 4 && commando.equalsIgnoreCase(Client.SETTINGS) 
						&& isColor(words[2]) && isInteger(words[3]) && versionReseived) {
					
					//set up game
					board.boardInit(Integer.parseInt(words[3])); 
					setColor(words[2]);
					settingsInput = true;
					
					
					//printing test
					print(inputUser.get(0));
					print("the dimension is set");
//					System.out.println(board.getDIM());
//					System.out.println(players[0].getStone());
					
					//print board
					print(board.toString());
					
					
					inputUser.remove(0);
					
					//Ask the first player to make the first move.
					sendMessageToBoth(Server.TURN + General.DELIMITER1 + 
							players[currentPlayer].getNamePlayer() + General.DELIMITER1
							+ Server.FIRST + General.DELIMITER1 + players[currentPlayer].getNamePlayer());

				}
	//--------------------------------------------------------------------
	//Move for game row_colom
	//checken of de message van de currentplayer is door middel van id die aan de line is gekoppeld.		
				else if (isID(idPlayer) && commando.equalsIgnoreCase(Client.MOVE))  {
					String[] coordinates = words[2].split(General.DELIMITER2);
//					System.out.println(coordinates[0]);
//					System.out.println(coordinates[1]);

					if (isInteger(coordinates[0]) && isInteger(coordinates[1])) {
						int row = Integer.parseInt(coordinates[0]);	
						int col = Integer.parseInt(coordinates[1]);
						if (board.isValidMove(row, col)) { 
						
							
							Stone stone = players[currentPlayer].getStone();
							board.setField(row, col, stone);
							System.out.println(board.toString());
							inputUser.remove(0); 
							
							currentMove = words[2];
							lastPlayer = currentPlayer;
							currentPlayer = (currentPlayer + 1) % 2;
							sendMessageToBoth("test");
							sendMessageToBoth(Server.TURN + General.DELIMITER1 + players[lastPlayer].getName() + 
									General.DELIMITER1 + currentMove + General.DELIMITER1 
									+ players[currentPlayer].getName());
							
							//board update
							board.updateBoard(row, col, stone);
							
						} else {
							sendMessageToCurrent(Server.INVALID + "please enter a valid move");
							inputUser.remove(0); 
						}
					} else if (words[2].equalsIgnoreCase(Client.PASS)) {
						// isFinished = true; pass bij twee passen
						currentMove = Client.PASS;
						inputUser.remove(0);
						sendMessageToBoth(Server.TURN + General.DELIMITER1 + "playername" + 
								General.DELIMITER1 + words[2] + General.DELIMITER1 
								+ "playersname die aan de beurt is");
						currentPlayer = (currentPlayer + 1) % 2;
						
					}
					else {
						sendMessageToCurrent(Server.INVALID + "please enter a valid move, integers please");
						inputUser.remove(0); 
						print(inputUser.get(0));
					}
				}
				else if (!isID(idPlayer) && commando.equalsIgnoreCase(Client.MOVE)) {
						print(inputUser.get(0));
						inputUser.remove(0); 
						players[opponent].sentGameData("This is not your turn");
				}
	// ---------------------------------------------------------------------
	//QUIT during the game. Deze speler wordt afgesloten.
				else if (commando.equalsIgnoreCase(Client.QUIT)) {
					print("One of the players quit");
					playerQuitGame = true;
					print(inputUser.get(0));
					inputUser.remove(0); 
				}
				//
				else {
					print(words + "test");
					inputUser.remove(0);
				}
			}
		}
	// -----------------------------------------------------------------------
	//When the game is terminated send a message to both, and shutdown the program properly. 
		sendMessageToBoth(Server.ENDGAME + General.DELIMITER1 + determineReasonTermination()
		 	+ General.DELIMITER1 + endScore());
		shutdown();
		if (playerQuitGame) {
	//TODO	
		}

	} // end run method
	
	
	
	
	
	// Getters and setters ----------------------------------------
	
	public Board getBoard() {
		return board;
	}
	
	private int opponent(int currentPlayer) {
		if (currentPlayer == 1) {
			return opponent = 0;
		} else
			return opponent = 1;
	}
	
	
	//waarom private
	private void setColor(String color) {
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
	


	
	//waarom private
//	private void getName(String color) {
//		if (players[0].getName(color).equals("BLACK")) {
//			players[0].setStone("BLACK");
//			players[1].setStone("WHITE");
//		} else {
//			players[0].setStone("WHITE");
//			players[1].setStone("BLACK");
//		}
//	}

//	public void setBoard(String settings) {
//		//set dimension board
//		int size = Integer.parseInt(settings);
//		this.board = new Board(size);
//		boardSize = size;
//	}
//	
	

	public String stoneToName() {
		//TODO
		return "";
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
	
	// Other methods-----------------------------------------------------
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
	 * Creates the list of messages that are send.
	 * @param line
	 */
	public void notify(String line) {
		
		inputUser.add(line);
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
	
	public void showBoard() {
		System.out.print(board.toString());
	}
	
	/**
	 * This method determines if the game is ended, whatever the reason may be.
	 * @return true if it is ended
	 */
	public Boolean isTerminated() {
		return isFinished || playerQuitGame || timeOut;
	}

	/**
	 * When the previousMove and currentMove are 
	 * both equal to pass or the board is full the game will end.
	 * @return
	 */
	public Boolean isFinished() {
		return previousMove.equals("PASS") && currentMove.equals("PASS") 
				|| board.isFull();
	}
	
	public Boolean isColor(String expectedColor) {
		return expectedColor.equals("WHITE") || expectedColor.equals("BLACK");
	}
	
	public Boolean isID(String clientID) {
		return players[currentPlayer].getClientTag().equals(clientID);
	}
	
	/**
	 * Determines if the move is valid. The field should be empty on the board. 
	 * No stone may be played so as to recreate any previous board position.
	 * @return
	 */
	public boolean isValidMove(int row, int col, Board board) {
		return board.isField(row, col) &&
				board.isEmptyField(row, col) && 
				!board.isPreviousBoard(board); //TODO
		
	}
	
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
	
	public String endResult() {
		String endresult;
		if (board.isWinner(players[0].getStone())) {
			endresult = players[0].getNamePlayer() + General.DELIMITER1 + board.countScore(players[0].getStone()) + 
					General.DELIMITER1 + players[1].getNamePlayer() + General.DELIMITER1 + board.countScore(players[1].getStone());
		} else if (board.isWinner(players[1].getStone())) {
			endresult = players[1].getNamePlayer() + General.DELIMITER1 + board.countScore(players[1].getStone()) + 
					General.DELIMITER1 + players[0].getNamePlayer() + General.DELIMITER1 + board.countScore(players[0].getStone());
		} else {
			endresult = players[1].getNamePlayer() + General.DELIMITER1 + board.countScore(players[1].getStone()) + 
					General.DELIMITER1 + players[0].getNamePlayer() + General.DELIMITER1 + board.countScore(players[0].getStone());
		}
		return endresult;
	}
	
	/**
	 * Shutdown the game property.
	 */
	public void shutdown() {
		//TODO
	}
	

}
