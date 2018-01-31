package clientView;

import client.ClientBoard;

public class TUIclient {

	
	private ClientBoard board;


	public TUIclient(ClientBoard board) {
		this.board = board;
	}
	
	/**
	 * After boardInit the TUI should show the empty board.
	 */
	public void showBoard() {
		print("\ncurrent game situation: \n\n" + board.toString()
			+ "\n");
	}
	
	public void addStone() {
		print("\ncurrent game situation after move: \n\n" + board.toString()
			+ "\n");
	}
	
	public void showEndResult(String endScore) {
		print(endScore);
	}
	
	/**
	 * Shows the board after updating the board. 
	 * After checking captured stones and removing these stones.
	 */
	public void updateView() {
		print("\ncurrent game situation after update: \n\n" + board.toString()
			+ "\n");
	}
	
	/**
	 * Sending a message to the user.
	 * @param message
	 */
	public void print(String message) {
		System.out.println("this is the tui");
		System.out.println(message);
	}
}
