package test;

import java.util.Scanner;

import servermodel.Board;


public class Test {

	public static void main(String[] args) {
		
	  //  Test test = new Test();
	    //Board board = new Board();
	   // test.determineMove(board);
	    	   // System.out.println(test.determineMove(board));
	    //test.readInt("what is your choice?");
//	    int testing = test.readInt("what is your choice");
//	    System.out.println(testing);
	    
//	    int test1 = test.determineMove(board);
//	    System.out.println(test1);
	    
	   // String testing = test.determineMove(board);
	   // System.out.println(testing);
	    
		
	 //   String testing1 = test.determineMove(board);
	   // System.out.println(testing1);
	}
	//pattern
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
	
	/**
	 * This method checks if the input string contains and "_".
	 * @param userInput
	 * @return true if it contains an underscore
	 */
	private boolean containsOneUnderscore(String userInput) {
		if (userInput.contains("_")) {
			return true;
		} else {
			return false;
		}
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
}


