package clientmodel;

import java.util.Scanner;

import servermodel.Board;
import servermodel.Stone;

public class HumanPlayer1 extends Player {

	/**
	 * Creates a new human player for the game GO.
	 * @param name
	 * @param stone
	 */
	public HumanPlayer1(String name, Stone stone) {
		super(name, stone);
	}


	@Override
	public String determineMove(Board board) {
	
		String choice = readString("Please enter the coordinates (x_y)");
		String[] words = choice.split("_");
		//moet zeker weten dat het numbers zijn
		int row = Integer.parseInt(words[0]);
		int col = Integer.parseInt(words[1]);
		//check valid move
		boolean valid = board.isField(row, col) && board.isEmptyField(row, col);
		// wanneer het geen geldige move is, weer vragen over input.
		while (!valid) {
			System.out.println("ERROR: field " + choice
                    + " is no valid choice. Try again.");
			choice = readString("what is your choice");
			String[] word = choice.split("_");
			int row1 = Integer.parseInt(word[0]);
			int col1 = Integer.parseInt(word[1]);
			valid = board.isField(row, col) && board.isEmptyField(row1, col1);
		}
		return choice;
	}
	
	/**
	 * Read the input of the console, for determining the nest move.
	 * Input is x_y.
	 * @param prompt the question to prompt the user
	 * @return String with x and y coordinates
	 */
	//reads the following inputline
	private static String readString(String prompt) {
		Scanner in = new Scanner(System.in);
		String result = null;
		System.out.print(prompt);
		if (in.hasNextLine()) {
			result = in.nextLine();
		}
		return result;
	}

	public static void main(String[] args) {
		HumanPlayer1 newHuman = new HumanPlayer1("Linda", Stone.b);
		Board board = new Board();
		System.out.println("test");
		System.out.println(newHuman.determineMove(board));
		System.out.println(newHuman.determineMove(board));
	}
} //class
