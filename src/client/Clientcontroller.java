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
import servermodel.Board;

public class Clientcontroller extends Protocol implements Runnable {

	/**
	 * Handles the input from the Server and input from the console.
	 */
	private BufferedReader in;
	private BufferedWriter out;
	private Socket socked;
	private String namePlayer; //name of the Player
	private Player player;

	
	
	/**
	 * Constructor creates a ClientController.
	 */
	public Clientcontroller(String playerName, Socket sockArg) throws IOException {
		this.namePlayer = playerName;
		this.socked = sockArg;
		in = new BufferedReader(new InputStreamReader(socked.getInputStream())); 
		out = new BufferedWriter(new OutputStreamWriter(socked.getOutputStream()));
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
				if (scannerLine.hasNext()) {
					stringRead = true;
					value = scannerLine.next();
					System.out.println(value);  //output terminal
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
		System.out.println("Client waiting for server input");
		String line;
		try {
			while (in.readLine() != null) {
				line = in.readLine();
				System.out.println(line);
				
				String[] words = line.split(General.DELIMITER1);
				// Start game
				
				if (words.length == 12 && words[0].equals(Server.NAME) && words[1].equals(Server.VERSION) && words[4].equals(Server.EXTENSIONS) ) {
					System.out.println(line);
					sendMessage(Client.NAME + General.DELIMITER1 + this.getName() + General.DELIMITER1 + Client.VERSION + Client.VERSIONNO + Client.EXTENSIONS); 
				}
				//Start game; voorkeur bord doorgeven
				if (words.length == 2 && words[0].equals(Server.START)) {
					print(words[0]);
					String input = readStringConsole("Choice color and boardsize");
					String[] settings = input.split(" ");
					sendMessage(Client.SETTINGS + settings[0] + General.DELIMITER1 + settings[1]);
				}
				
				
						
//					System.out.println("The client heeft start ontvangen" + words[1] + "voor het aantal deelnemers");
//					
//					//de speler mag de kleur en grootte van het bord kiezen.
//					//valideren van de input voordat het wordt verstuurd, anders opnieuw vragen.
//					
//					String inputLine = readString("Enter colour(BLACK or WHITE) and boardsize");
//					if (containsEuroteken(inputLine)) {
//						String[] word = inputLine.split("_");
//						if (word.length == 2 && word[0] == "BLACK" || word[0] == "WHITE" && isInteger(word[1])) {
//						out.write(Protocol.Client.SETTINGS + inputLine);
//						out.newLine();
//						out.flush();	
//						
//						} else {
//							System.out.println("invalid input: color and boardsize expected");
//							//moet nog een keer de input lezen van de console
//							
//						}
//					}
//					
//				}	
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
			socked.close();
		} catch (IOException e) {
			System.out.println("Error with shutting down");
		}
	}
	 
	//Thread t1 = new Thread(new Runner());
	public void main(String[] args) throws IOException {
		Clientcontroller controller = new Clientcontroller("Linda", socked);
		controller.readStringConsole("Read string");
	}
	
	
	
} //class
