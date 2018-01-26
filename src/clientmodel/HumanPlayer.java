package clientmodel;

import java.util.Scanner;

import servermodel.Board;
import servermodel.Stone;

public class HumanPlayer extends Player {

	/**
	 * Creates a new human player for the game GO.
	 * @param name of the player
	 * @param stone which represents the colour.
	 */
	public HumanPlayer() {
		super();
	}

	@Override
	public String determineMove(Board board) {
		String prompt = "Enter your choice";
		boolean validMove = false;
		String choice = null;
		while (!validMove) {
			choice = readString(prompt);
			//anders kan je niet splitten.
			if (containsOneUnderscore(choice)) {
				String[] word = choice.split("_");
				if (word.length == 2 && isInteger(word[0]) && isInteger(word[1])) {
					int row = Integer.parseInt(word[0]);
					int col = Integer.parseInt(word[1]);
					validMove = board.isField(row, col) && board.isEmptyField(row, col); 
					// niet een eerdere move
				} else {
					System.out.println("invalid input, expected input are numbers");
				}
			} else {
				System.out.println(" invalid input, x_y is needed ");
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
	/**
	 * This method checks if the input string contains and "_".
	 * @param userInput
	 * @return true if it contains an underscore
	 */
	private boolean containsOneUnderscore(String userInput) {
		return userInput.contains("_"); 
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


	public static void main(String[] args) {
		HumanPlayer player = new HumanPlayer();
		HumanPlayer player2 = new HumanPlayer();
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
