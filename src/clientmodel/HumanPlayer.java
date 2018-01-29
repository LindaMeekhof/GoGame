package clientmodel;

import java.util.Scanner;

import general.Protocol.Client;
import general.Protocol.General;
import servermodel.Board;
import servermodel.Stone;

public class HumanPlayer extends Player {

	/**
	 * Creates a new human player for the game GO.
	 * @param name of the player
	 * @param stone which represents the colour.
	 */
	public HumanPlayer(String name) {
		super(name);
	}

	@Override
	public String determineMove(Board board) {
		String prompt = "Enter your choice";
		boolean validMove = false;
		String choice = null;
		while (!validMove) {
			choice = readString(prompt);
			//anders kan je niet splitten.
			if (containsSpace(choice)) {
				String[] word = choice.split(" ");
				if (word.length == 3 && isInteger(word[1]) && isInteger(word[2])) {
					int row = Integer.parseInt(word[1]);
					int col = Integer.parseInt(word[2]);
					validMove = board.isField(row, col) && board.isEmptyField(row, col); 
					// niet een eerdere move
				} else {
					System.out.println("invalid input, expected input are numbers");
				}
			} else {
				System.out.println(" invalid input, Move X-coordinate Y-coordinate is needed ");
			}
		}
		return choice;	
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

	//kan korter geschreven worden
//	/**
//	 * This method checks if the input string contains and "_".
//	 * @param userInput
//	 * @return true if it contains an underscore
//	 */
	private boolean containsSpace(String userInput) {
		return userInput.contains(" "); 
	}
		
	private String readString(String prompt) {
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
				}
			}
		} 
		return value;
	}

	
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
//						case "MOVE":
//							if (isInteger(inputConsole[1]) && isInteger(inputConsole[2])) {
//								sendMessage(Client.MOVE + General.DELIMITER1 + inputConsole[1] + General.DELIMITER2 + inputConsole[2]);
//							} else if (inputConsole[1].equals("PASS")){
//								sendMessage(Client.MOVE + General.DELIMITER1 + Client.PASS);
//							} else {
//								print("Please enter a valid move");
//							}
//							break;
//						case "SETTINGS":
//							if (inputConsole.length == 3 && isColor(inputConsole[1]) && isInteger(inputConsole[2])) {
//								sendMessage(Client.SETTINGS + General.DELIMITER1 + inputConsole[1].toUpperCase() + General.DELIMITER1 + inputConsole[2]);
//							} else {
//								print("please enter valid settings");
//							}
//							//System.out.println("settings" + value); 
//							break;
//						case "QUIT":
//							sendMessage(commando);
//							System.out.println(commando);
//							shutdown();
//							break;
//						case "REQUESTGAME":
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

	public static void main(String[] args) {	

	//	Board board = new Board();
		
//		String testing = player.determineMove(board);
//		System.out.println(testing);
//		player.makeMove(board);
//		Stone stone = board.getField(1, 1);
//		System.out.println(stone);
//		player.makeMove(board);
	//	String testing2 = player.determineMove(board);
	//	System.out.println(testing2);
		
//		player.makeMove(board);
//		Stone stone = board.getField(1, 1);
//		System.out.println(stone);
//		player2.makeMove(board);
//		Stone stone2 = board.getField(1, 2);
//		System.out.println(stone2);
	}

} //class
