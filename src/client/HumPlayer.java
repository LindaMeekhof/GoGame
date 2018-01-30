package client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import general.Protocol.Client;
import general.Protocol.General;
import general.Protocol;
import general.Protocol.Server;

public class HumPlayer extends PlayerController {

//	public HumPlayer(String playerName, Socket sockArg, Player player) throws IOException {
//		super(playerName, sockArg, player);
//	}

	public HumPlayer(String playerName, Socket sockArg) throws IOException {
		super(playerName, sockArg);
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
						case "MOVE":
							if (isInteger(inputConsole[1]) && isInteger(inputConsole[2])) {
								sendMessage(Client.MOVE + General.DELIMITER1 + inputConsole[1] + General.DELIMITER2 + inputConsole[2]);
							} else if (inputConsole[1].equals("PASS")){
								sendMessage(Client.MOVE + General.DELIMITER1 + Client.PASS);
							} else {
								print("Please enter a valid move");
							}
							break;
						case "SETTINGS":
							if (inputConsole.length == 3 && isColor(inputConsole[1]) && isInteger(inputConsole[2])) {
								sendMessage(Client.SETTINGS + General.DELIMITER1 + inputConsole[1].toUpperCase() + General.DELIMITER1 + inputConsole[2]);
								//setBoard en Color
								setBoardandColor(inputConsole[1], inputConsole[2]);
							} else {
								print("please enter valid settings");
							}
							//System.out.println("settings" + value); 
							break;
						case "QUIT":
							sendMessage(commando);
							System.out.println(commando);
							shutdown();
							break;
						case "REQUESTGAME":
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
	
	
}
