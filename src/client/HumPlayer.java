package client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import general.Protocol.Client;
import general.Protocol.General;
import general.Protocol;
import general.Protocol.Server;
import servermodel.Board;

public class HumPlayer extends PlayerController {


	/**
	 * Creating a HumanPlayer to play the GOGAME. 
	 * @param playerName
	 * @param sockArg
	 * @throws IOException
	 */
	public HumPlayer(String playerName, Socket sockArg) throws IOException {
		super(playerName, sockArg);
	}
	/**
	 * Read the input of the Console with scanner; what the human player inputs. 
	 * The player can enter all the commands at any time.
	 * COMMANDS are name, move, settings, quit, exit, requestgame and chat.
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
						case "NAME":
							sendMessage(value.toUpperCase());
							break;
							//ValidMove moet nog gecheckt worden.
						case "MOVE":
							if (inputConsole.length == 3 && isInteger(inputConsole[1]) && 
								isInteger(inputConsole[2])) {
								sendMessage(Client.MOVE + General.DELIMITER1 + 
										inputConsole[1] + General.DELIMITER2 + inputConsole[2]);
							} else if (inputConsole[1].equalsIgnoreCase("PASS")) {
								sendMessage(Client.MOVE + General.DELIMITER1 + Client.PASS);
							} else {
								print("Please enter a valid move");
							}
							break;
						case "SETTINGS":
							if (inputConsole.length == 3 && isColor(inputConsole[1]) 
								&& isInteger(inputConsole[2]) 
								&& Integer.parseInt(inputConsole[2]) < 20 
								&& Integer.parseInt(inputConsole[2]) > 4) {
								sendMessage(Client.SETTINGS + General.DELIMITER1 
										+ inputConsole[1].toUpperCase() + General.DELIMITER1 
										+ inputConsole[2]);
	
								//setBoard and Color
								setBoardandColor(inputConsole[1], inputConsole[2]);	
							} else {
								print("please enter valid settings");
							}
							break;
						case "QUIT":
							sendMessage(Client.QUIT);
							System.out.println(Client.QUIT);
							//shutdown();
							break;
						case "EXIT":
							sendMessage("QUIT");
							System.out.println("QUIT");
							shutdown();
							break;
						case "REQUESTGAME":
							sendMessage(Client.REQUESTGAME + General.DELIMITER1 + Client.RANDOM);
							break;
						case Client.CHAT:
							sendMessage(Client.CHAT + General.DELIMITER1 + value);
							break;
						default:
							System.out.println("unknown input"); break;
					}
				}
			}
		} 
		return value;
	}
	@Override
	public String determineMove(Board board) {
		// TODO Auto-generated method stub
		return null;
	}
}
