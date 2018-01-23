package clientmodel;

import servermodel.Board;
import servermodel.Stone;

/**
 * Abstract class for a player in the game Go.
 * Final exercise module 1 Nedap University.
 * 
 * @author Linda.Meekhof
 *
 */

public abstract class Player {
	
	protected String name;
	protected Stone stone;
	
	
	/**
	 * Creates a player for the game GO. 
	 * This constructor has the parameter name and stone colour.
	 */
	public Player() {
		this.stone = stone;
	}

	/**
	 * Returns the name of the player.
	 * @return the name of the player.
	 */
	 /*@ pure */
	
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the player. (This will be given by the server)
	 * @param name 
	 */
	//@requires name != null;
	//@ensures this.getName == name;
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the colour stone of the player.
	 * @return the colour of the stone.
	 */
	 /*@ pure */
	public Stone getStone() {
		return stone;
	}

	/**
	 * Sets the colour of the stone. (This will be given by the server)
	 * @param stone
	 */
	//@requires stone != null;
	//@ensures this.getStone == name;
	public void setStone(Stone stone) {
		this.stone = stone;
	}
	
	/**
	 * Determine which field to choose for the next move.
	 * @param board of the current game
	 * @return the player's choice
	 */
	//@ requires board != null;
	//@ requires board != isFull;
	//@ ensures board.isField(\result) & board.isEmptyField(\result); ??
	public abstract String determineMove(Board board);
	
	
	/**
	 * Determines if the move is valid. The field should be empty on the board. 
	 * No stone may be played so as to recreate any previous board position.
	 * @return
	 */
	public boolean isValidMove(int row, int col, Board board) {
		return board.isField(row, col) &&
				board.isEmptyField(row, col) && 
				!board.isPreviousBoard(board); // moet nog implementeren.0
		
	}
	
	/**
	 * The player makes a move on the board.
	 * @param board the current board
	 */
	/*@ requires board != null & !board.isFull();*/
	public void makeMove(Board board) {
//		if (isValidMove(row, col, board)) {
		String myChoice = determineMove(board);
		
		//beetje dubbelop
		String[] words = myChoice.split("_");
		int row = Integer.parseInt(words[0]);
		int col = Integer.parseInt(words[1]);
		board.setField(row, col, getStone());
		
		//choice wordt gestuurd naar server
		//makeMoveToServer(board);
	}
	
//	public String makeMoveToServer(Board board) {
//		String myChoice = determineMove(board);
//		return myChoice;
//	}
//	
}
