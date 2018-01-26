package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
	private static final int NumberPlayers = 2;
	private int amountPlayers;
	private String lastMessage;
	private boolean settingsInput;
	private boolean versionReseived;
	private boolean playerQuitGame;
	private boolean isTerminated;
	private String lastMove;
	private boolean isFinished;

	
	/** 
	 * Creates a Gamecontroller with two Serverclients for the in and output control.
	 * @param player1
	 * @param player2
	 */
	public Gamecontroller(ServerGO serverGO, ServerClient player1, ServerClient player2) {
		players = new ServerClient[NumberPlayers];
//		this.player1 = player1;
//		this.player2 = player2;
		this.serverGO = serverGO;
		players[0] = player1;
		players[1] = player2;
		currentPlayer = 0;
	//	this.board = new Board();
		inputUser = new ArrayList<String>();
		
		settingsInput = false;
		versionReseived = false;
		playerQuitGame = false;
		isTerminated = false;
		isFinished = false;
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
		sendMessageToBoth(Server.NAME + "$" + serverGO.getName() + 
					General.DELIMITER1 + Server.VERSION + General.DELIMITER1 + Server.VERSIONNO + 
					General.DELIMITER1 + Server.EXTENSIONS + 0 + General.DELIMITER1 + 0 + 
					General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + 
					General.DELIMITER1 + 0 + General.DELIMITER1 + 0);
		
//

//		
//		//sending TURN naar beide spelers.
//		for (int i = 0; i < players.length; i++) {
//			players[i].sentGameData(Server.TURN + General.DELIMITER1 + "speler1" 
//					+ General.DELIMITER1 + "rij" + General.DELIMITER2 + 
//					"col" + General.DELIMITER1 + "speler2");
//		}
//		
//		//sending TURN bij de eerste keer
//		for (int i = 0; i < players.length; i++) {
//			players[i].sentGameData(Server.TURN + General.DELIMITER1 
//					+ players[0].getName() + General.DELIMITER1 + 
//					Server.FIRST + General.DELIMITER1 + "speler1");
//		}
//		
//		//sending ENDGAME het spel is gestopt om welke reden dan ook. FINISHED, ABORTED, TIMEOUT.
//		for (int i = 0; i < players.length; i++) {
//			players[i].sentGameData(Server.ENDGAME + General.DELIMITER1 + "reden" +  
//					General.DELIMITER1 + "winnende speler" + General.DELIMITER1 + "score" 
//					+ General.DELIMITER1 + "verliezende speler" + General.DELIMITER1 + "score");
//		}
		
		
		
		System.out.println("waiting for input from player 0");
		
		
		
		while (!isTerminated) {
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (!inputUser.isEmpty()) {

				System.out.println("De gamecontroller heeft input ontvangen");

				String inputReadable;
				int versionInput = 0;
				
				String[] words = inputUser.get(0).split("\\" + General.DELIMITER1);

				//getting name, version and extensions. Wanneer beide clients data hebben opgestuurd
				if (words.length == 12 && words[0].equals(Client.NAME)) {
					inputReadable = inputUser.get(0).replaceAll("\\" + General.DELIMITER1, " ");
					System.out.println(inputReadable);
					inputUser.remove(0);
					versionInput += 1;
					if (versionInput > 2) {
						versionReseived = true; //beide versies ontvangen.
						System.out.println(versionInput);
						//sending START en vragen om de gegevens kleur en bordgrootte 
							players[currentPlayer].sentGameData(Server.START + General.DELIMITER1 + "board.getDIM()" + 
									General.DELIMITER1 + "players[1].getStoneString()" + General.DELIMITER1
									+ boardSize + General.DELIMITER1 + players[0].getName() + "players[1].getName()");
						}
				}
//----------------------------------------------------------------------------
//Starting game when settings are send back. kleur en boardsize --> moet nog && settingsInput
				else if (words[0].equalsIgnoreCase(Client.SETTINGS) && isInteger(words[2])) {
					System.out.println(inputUser.get(0));
					setBoard(words[2]); 
					System.out.println("the dimension is set");
					System.out.println(board.getDIM());
					System.out.println(board.toString());
					settingsInput = true;
					inputUser.remove(0);
					// set names
					//player[0].setName(words[0];
					setColor(words[1]);
					System.out.println(players[0].getStone());
				}
	//--------------------------------------------------------------------
	//Move for game row_colom
	//checken of de message van de currentplayer is.			
				else if (words[0].equalsIgnoreCase(Client.MOVE))  {
					String[] coordinates = words[1].split(General.DELIMITER2);
//					System.out.println(coordinates[0]);
//					System.out.println(coordinates[1]);

					if (isInteger(coordinates[0]) && isInteger(coordinates[1])) {
						int row = Integer.parseInt(coordinates[0]);	
						int col = Integer.parseInt(coordinates[1]);
						if (isValidMove(row, col)) { //klopt nog niet validmove is always true;
							//Stone stone = Stone.b;
							Stone stone = players[currentPlayer].getStone();
							board.setField(row, col, stone);
							System.out.println(board.toString());
							inputUser.remove(0); 
							lastMove = words[1];
							currentPlayer = (currentPlayer + 1) % 2;
							sendMessageToBoth(Server.TURN + General.DELIMITER1 + "playername" + 
									General.DELIMITER1 + words[1] + General.DELIMITER1 
									+ "playersname die aan de beurt is");
						} else {
							sendMessageToBoth("please enter a valid move");
							inputUser.remove(0); 
						}
					} else if (words[1].equalsIgnoreCase(Client.PASS)) {
						isFinished = true;
						lastMove = Client.PASS;
						inputUser.remove(0);
						sendMessageToBoth(Server.TURN + General.DELIMITER1 + "playername" + 
								General.DELIMITER1 + words[1] + General.DELIMITER1 
								+ "playersname die aan de beurt is");
					}
					else {
						sendMessageToBoth("please enter a valid move, integers please");
						inputUser.remove(0); 
					}
				}
	
	// ---------------------------------------------------------------------
	//QUIT during the game. Deze speler wordt afgesloten.
				else if (words[0].equals(Client.QUIT)) {
					//bericht naar beide
					playerQuitGame = true;
					shutdown();
				}
				//
				else {
					//	inputNew = inputUser.get(0).replaceAll("\\" + General.DELIMITER1, " ");
					//	System.out.println(inputNew);
					System.out.println(words[0] + "test");
					inputUser.remove(0);
				}
				
				//review if the game is terminated
				
			}
		}	
	}
	
	private void setColor(String color) {
		if (color.equalsIgnoreCase("BLACK")) {
			players[0].setStone("BLACK");
			players[1].setStone("WHITE");
		} else {
			players[0].setStone("WHITE");
			players[1].setStone("BLACK");
		}
	}


	
	
	public void setBoard(String settings) {
		//set dimension board
		int size = Integer.parseInt(settings);
		this.board = new Board(size);
		boardSize = size;
	}

	
	
	public void notify(String line, int identiteit) {
		//System.out.println(line);
		//System.out.println(inputUser.isEmpty());
		
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
	
	public void sendMessageToBoth(String message) {
		players[0].sentGameData(message);
		players[1].sentGameData(message);
	}

	public void shutdown() {
		//TODO
	}
	
	public boolean isValidMove(int row, int col) {
//		return  board.isField(row, col) && 
//				board.isEmptyField(row, col) && board.isPreviousBoard(board);
		return false;
	}
	
}
