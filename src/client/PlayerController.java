package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;


import clientmodel.Player;
import general.Protocol;
import general.Protocol.Client;
import general.Protocol.General;
import general.Protocol.Server;
import servermodel.Board;

public class PlayerController extends Protocol implements Runnable { 

	
	/**
	 * Handles the input from the Server and input from the console.
	 */
	private BufferedReader in;
	private BufferedWriter out;
	private Socket sock;
	private String namePlayer; //name of the Player
	private Player player;
	private Board board;
	
	


	/**
	 * Constructor creates a ClientController.
	 */
	public PlayerController(String playerName, Socket sockArg, Player player) throws IOException {
		this.namePlayer = playerName;
		this.sock = sockArg;
		in = new BufferedReader(new InputStreamReader(sock.getInputStream())); 
		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		this.player = player; 
	//	board = new Board(); //dit moet straks heet bord worden van de client
	}

// Getters and setters ---------------------------------------------------------------
	/**
	 * Get the name of the player.
	 * @return name name of player.
	 */
	public String getName() {
		return namePlayer;
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}


	/**
	 * Read the input of the Console with scanner; what the human player inputs.
	 * @param prompt
	 * @return
	 */
	public String readStringConsole(String prompt) {
		String value = null;
		boolean stringRead = false;
		@SuppressWarnings("resource")
		Scanner line = new Scanner(System.in);
		while (!stringRead) {
			System.out.print(prompt);
			try (Scanner scannerLine = new Scanner(line.nextLine())) {
				if (scannerLine.hasNextLine()) {
					stringRead = true;
					value = scannerLine.nextLine();
					//System.out.println(value); //output terminal
					//sends a message directly to out.
					String[] inputConsole = value.split(" ");
					String commando = inputConsole[0].toUpperCase();

					switch (commando) {
						//ook nog checken op validmove
						case Client.MOVE:
							if (isInteger(inputConsole[1]) && isInteger(inputConsole[2])) {
								sendMessage(Client.MOVE + General.DELIMITER1 + inputConsole[1] + General.DELIMITER2 + inputConsole[2]);
							} else if (inputConsole[1].equals("PASS")){
								sendMessage(Client.MOVE + General.DELIMITER1 + Client.PASS);
							} else {
								print("Please enter a valid move");
							}
							break;
						case Client.SETTINGS:
							if (inputConsole.length == 3 && isColor(inputConsole[1]) && isInteger(inputConsole[2])) {
								sendMessage(Client.SETTINGS + General.DELIMITER1 + inputConsole[1].toUpperCase() + General.DELIMITER1 + inputConsole[2]);
							} else {
								print("please enter valid settings");
							}
							//System.out.println("settings" + value); 
							break;
						case Client.QUIT:
							sendMessage(commando);
							System.out.println(commando);
							shutdown();
							break;
						case Client.REQUESTGAME:
							sendMessage(commando);
							break;
						case Client.CHAT:
							sendMessage(Client.CHAT + General.DELIMITER1 + value);
							break;
						default:
							System.out.println("unknown input"); break;
					}


					//	sendMessage(value);

				}
			}
		} 
		return value;
	}

	/**
	 * Handles the inputstream which comes from the ServerGO.
	 */
	@Override
	public void run() {

		String line;
		try {
			while (in.readLine() != null) {
				line = in.readLine();
				System.out.println(line);
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
					print(readableInput);
//					sendMessage(Client.NAME + General.DELIMITER1 + getName() + 
//							General.DELIMITER1 + Client.VERSION + General.DELIMITER1 + 
//							Client.VERSIONNO + General.DELIMITER1 + Client.EXTENSIONS + General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + 
//							General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + 
//							General.DELIMITER1 + 0 + General.DELIMITER1 + 0);
					sendMessage(Client.NAME + General.DELIMITER1 + getName());
				}
//----------------------------------------------------------------------
// Getting Start and sending Settings
				if (words[0].equals(Server.START)) {	
					print("Please enter you settings -> SETTINGS <color> <bordsize>");
				}
			
//------------------------------------------------------------------------				
// command turn for making a move	
				if (words[0].equals(Server.TURN)) {
					print(readableInput);
				}
//------------------------------------------------------------------------
// the game is ended
				if (words[0].equals(Server.ERROR)) {
					print(readableInput);
				}
// error commands
				if (words[0].equals(Server.UNKNOWN)) {
					print(readableInput);
				}
				if (words[0].equals(Server.INVALID)) {
					print(readableInput);
				}
				if (words[0].equals(Server.NAMETAKEN)) {
					print(readableInput);
				}
				if (words[0].equals(Server.INCOMPATIBLEPROTOCOL)) {
					print(readableInput);
				}
				if (words[0].equals(Server.OTHER)) {
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
	private boolean isInteger(String expectedInt) {
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
	private static void print(String message) {
		System.out.println(message);
	}
	
	

	
// main --------------------------------------------------------------



	

	
} 
