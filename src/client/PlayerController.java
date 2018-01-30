package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

import clientView.TUIclient;
import clientmodel.Player;
import general.Protocol.Client;
import general.Protocol.General;
import general.Protocol.Server;
import client.Stonegroup;
import client.Stone;

public class PlayerController implements Runnable { 

	
	/**
	 * Handles the input from the Server and input from the console.
	 */
	private BufferedReader in;
	private BufferedWriter out;
	private Socket sock;
	private String namePlayer; //name of the Player
//	private Player player;
	private ClientBoard board;
	private Stone stone;
	private TUIclient view;


	/**
	 * Constructor creates a ClientController.
	 */
//	public PlayerController(String playerName, Socket sockArg, Player player) throws IOException {
//		this.namePlayer = playerName;
//		this.sock = sockArg;
//		in = new BufferedReader(new InputStreamReader(sock.getInputStream())); 
//		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
//		this.player = player; 
//		board = new Board(); 
//	}
	public PlayerController(String playerName, Socket sockArg) throws IOException {
		this.namePlayer = playerName;
		this.sock = sockArg;
		in = new BufferedReader(new InputStreamReader(sock.getInputStream())); 
		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
	//	this.player = player; 
		board = new ClientBoard(); 
		this.view = new TUIclient(board);
	}

// Getters and setters ---------------------------------------------------------------
	/**
	 * Get the name of the player.
	 * @return name name of player.
	 */
	public String getName() {
		return namePlayer;
	}
	
	public void setName(String name) {
		this.namePlayer = name;
	}
	
	public Stone getStone() {
		return stone;
	}

	public void setStone(Stone stone) {
		this.stone = stone;
	}
	
	
//	public Player getPlayer() {
//		return player;
//	}
//
//	public void setPlayer(Player player) {
//		this.player = player;
//	}


//	/**
//	 * Read the input of the Console with scanner; what the human player inputs.
//	 * @param prompt
//	 * @return
//	 */
//	public String readStringConsole(String prompt) {
//		String value = null;
//		boolean stringRead = false;
//		@SuppressWarnings("resource")
//		Scanner line = new Scanner(System.in);
//		while (!stringRead) {
//			System.out.print(prompt);
//			try (Scanner scannerLine = new Scanner(line.nextLine())) {
//				if (scannerLine.hasNextLine()) {
//					stringRead = true;
//					value = scannerLine.nextLine();
//					//System.out.println(value); //output terminal
//					//sends a message directly to out.
//					String[] inputConsole = value.split(" ");
//					String commando = inputConsole[0].toUpperCase();
//
//					switch (commando) {
//						//ook nog checken op validmove
//						case Client.MOVE:
//							if (isInteger(inputConsole[1]) && isInteger(inputConsole[2])) {
//								sendMessage(Client.MOVE + General.DELIMITER1 + inputConsole[1] + General.DELIMITER2 + inputConsole[2]);
//							} else if (inputConsole[1].equals("PASS")){
//								sendMessage(Client.MOVE + General.DELIMITER1 + Client.PASS);
//							} else {
//								print("Please enter a valid move");
//							}
//							break;
//						case Client.SETTINGS:
//							if (inputConsole.length == 3 && isColor(inputConsole[1]) && isInteger(inputConsole[2])) {
//								sendMessage(Client.SETTINGS + General.DELIMITER1 + inputConsole[1].toUpperCase() + General.DELIMITER1 + inputConsole[2]);
//							} else {
//								print("please enter valid settings");
//							}
//							//System.out.println("settings" + value); 
//							break;
//						case Client.QUIT:
//							sendMessage(commando);
//							System.out.println(commando);
//							shutdown();
//							break;
//						case Client.REQUESTGAME:
//							sendMessage(commando);
//							break;
//						case Client.CHAT:
//							sendMessage(Client.CHAT + General.DELIMITER1 + value);
//							break;
//						default:
//							System.out.println("unknown input"); break;
//					}
//
//
//					//	sendMessage(value);
//
//				}
//			}
//		} 
//		return value;
//	}

	/**
	 * Handles the inputstream which comes from the ServerGO.
	 */
	@Override
	public void run() {

		String line;
		try {
			while (in.readLine() != null) {
				line = in.readLine();
				//	System.out.println(line);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String[] words = line.split("\\" + General.DELIMITER1);
				String readableInput = line.replaceAll("\\" + General.DELIMITER1, " ");
				// -----------------------------------------------------------------
				//getting and sending name and server version
				if (words[0].equals(Server.NAME)) {
					System.out.println(namePlayer);
					view.print(readableInput);
					sendMessage(Client.NAME + General.DELIMITER1 + getName() + 
							General.DELIMITER1 + Client.VERSION + General.DELIMITER1 + 
							Client.VERSIONNO + General.DELIMITER1 + Client.EXTENSIONS + 
							General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + 
							General.DELIMITER1 + 0 + General.DELIMITER1 + 
							0 + General.DELIMITER1 + 0 + General.DELIMITER1 + 
							0 + General.DELIMITER1 + 0);
					//	sendMessage(Client.NAME + General.DELIMITER1 + getName());
				}
				//----------------------------------------------------------------------
				// Getting Start and sending Settings
				else if (words.length == 6 && words[0].equals(Server.START)) {	
					view.print("this are the final settings");
					view.print(readableInput);
					// Step 1. update ClientBoard and playerColor: START 2 WHITE 12 name name
					setBoardandColor(words[2], words[3]);
					// view.print(board.toString());
				}
				//Getting Start the first time: enter valid settings 				
				else if (words[0].equals(Server.START)) {	
					view.print("Please enter you settings -> SETTINGS <color> <bordsize>");
				}

				//--------------------------------------------------------------------			
				// command turn for making a move	
				else if (words[0].equals(Server.TURN)) {
					view.print(readableInput);

					//set field on board
					addStone(words[2], words[1]);
				}
				//------------------------------------------------------------------------
				// the game is ended
				else if (words[0].equals(Server.ERROR)) {
					view.print(readableInput);
				}
				// error commands
				else if (words[0].equals(Server.UNKNOWN)) {
					view.print(readableInput);
				}
				else if (words[0].equals(Server.INVALID)) {
					view.print(readableInput);
				}
				else if (words[0].equals(Server.NAMETAKEN)) {
					view.print(readableInput);
				}
				else if (words[0].equals(Server.INCOMPATIBLEPROTOCOL)) {
					view.print(readableInput);
				}
				else if (words[0].equals(Server.OTHER)) {
					view.print(readableInput);
				}
				else {
					print(readableInput);
				}
			} //run
		} catch (IOException e) {
			// System.out.println(" tekst");
			e.printStackTrace();
		}
	}
				
	public Boolean isColor(String expectedColor) {
		return expectedColor.equalsIgnoreCase("WHITE") || expectedColor.equalsIgnoreCase("BLACK");
	}
	
//	private boolean containsEuroteken(String userInput) {
//		return userInput.contains(Protocol.General.DELIMITER1); 
//	}
	
//	private boolean containsOneUnderscore(String userInput) {
//		return userInput.contains(Protocol.General.DELIMITER2); 
//	}
	
	/**
	 * This checks if the user input contains to integer.
	 * @param expectedInt
	 * @return true if it is an integer.
	 */
	public boolean isInteger(String expectedInt) {
		try {
			Integer.parseInt(expectedInt);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Shutdown the client.
	 */
	public void shutdown() {
		try {
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
		}
	}
	
// ------------------------------------------------------------
// input and output
	/**
	 * Send the message to the server.
	 * @param message
	 */
	public void sendMessage(String message) {
		try {
			out.newLine(); //misschien hier niet nodig
			out.write(message);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			//somethings going wrong shutdown();
		}
	}
	
	/**
	 * Sending a message to the terminal.
	 * @param message
	 */
	public static void print(String message) {
		System.out.println(message);
	}
	
	public void addStone(int row, int col, Stone steen) {
		board.setField(row, col, steen);
	}
 
	//setBoard and Color
	public void setBoardandColor(String stoneColor, String boardsize) {
		board.boardInit(Integer.parseInt(boardsize));
		
		if (stoneColor.equalsIgnoreCase("BLACK")) {
			setStone(Stone.b);
		} else {
			setStone(Stone.w);
		}
	}
	
	public Stone getStoneWithName(String nameParsed) {
		if (getName().equals(nameParsed)) {
			return getStone();
		} else {
			return getStone().other();
		}
	}
	

	public void addStone(String set, String nameParsed) {
		String[] coordinates = set.split(General.DELIMITER2);
		if (isInteger(coordinates[0]) && isInteger(coordinates[1])) {
			int row = Integer.parseInt(coordinates[0]);	
			int col = Integer.parseInt(coordinates[1]);

			Stone myStone = getStoneWithName(nameParsed);
			board.setField(row, col, stone);

			//board update
			board.updateBoard(row, col, stone);

		}
	}

	
// main --------------------------------------------------------------



		
	
} 
