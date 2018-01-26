package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

import clientmodel.HumanPlayer;
import clientmodel.Player;
import general.Protocol;
import general.Protocol.Client;
import general.Protocol.General;
import general.Protocol.Server;
import servermodel.Board;

public class PlayerController extends Protocol implements Runnable{

	
	/**
	 * Handles the input from the Server and input from the console.
	 */
	private BufferedReader in;
	private BufferedWriter out;
	private Socket sock;
	private String namePlayer; //name of the Player
	private Player player;

	
	
	/**
	 * Constructor creates a ClientController.
	 */
	public PlayerController(String playerName, Socket sockArg) throws IOException {
		this.namePlayer = playerName;
		this.sock = sockArg;
		in = new BufferedReader(new InputStreamReader(sock.getInputStream())); 
		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		player = new HumanPlayer();
		//board = new Board(); //dit moet straks heet bord worden van de client
	}
	
	/**
	 * Get the name of the player.
	 * @return name name of player.
	 */
	public String getName() {
		return namePlayer;
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
					sendMessage(value);
				//	sendMessage("testing");
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
// -----------------------------------------------------------------
//getting and sending name and server version
				if (words[0].equals(Server.NAME)) {
					String inputNew = line.replaceAll("\\" + General.DELIMITER1, " ");
					System.out.println(inputNew);
					System.out.println("name and version are read");

					sendMessage(Client.NAME + General.DELIMITER1 + getName() + 
							General.DELIMITER1 + Client.VERSION + General.DELIMITER1 + 
							Client.VERSIONNO + General.DELIMITER1 + Client.EXTENSIONS + 0 + General.DELIMITER1 + 0 + 
							General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + 
							General.DELIMITER1 + 0 + General.DELIMITER1 + 0);
				}
//----------------------------------------------------------------------
//getting Start and sending Settings
//				if (words[0].equals(Server.START)) {
//					System.out.println("settings are asked");
//					String input = readStringConsole("Please enter you Settings");
//					System.out.println(input);
//			//		sendMessage(Client.SETTINGS + settings[0] + General.DELIMITER1 + settings[1]);
//				}
//				
//				//Start game; voorkeur bord doorgeven
//				if (words.length == 12 && words[0].equals(Server.START)) {
//					print(words[0]);
//					String input = readStringConsole("Choice color and boardsize");
//					String[] settings = input.split(" ");
//					sendMessage(Client.SETTINGS + settings[0] + General.DELIMITER1 + settings[1]);
//				}
			}
		} catch (IOException e) {
			// System.out.println(" tekst");
			e.printStackTrace();
		}
	}
				

	
		
	private boolean containsEuroteken(String userInput) {
		return userInput.contains(Protocol.General.DELIMITER1); 
	}
	
	private boolean containsOneUnderscore(String userInput) {
		return userInput.contains(Protocol.General.DELIMITER2); 
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
	
	/**
	 * Shutdown the client.
	 */
	public void shutdown() {
		try {
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
			System.out.println("Error with shutting down");
		}
	}
	

	 
	//Thread t1 = new Thread(new Runner());
	public void main(String[] args) throws IOException {
		PlayerController controller = new PlayerController("Linda", sock);
		controller.readStringConsole("Read string");
	}
	
	
	
} //class
