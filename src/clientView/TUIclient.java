package clientView;

import client.ClientBoard;
import client.PlayerController;

public class TUIclient {

	
	private ClientBoard board;
	private PlayerController player;

	public TUIclient(ClientBoard board) {
		this.board = board;
	}
	
	/**
	 * Na het opzetten van het bord
	 */
	public void showBoard() {
		System.out.println("\ncurrent game situation: \n\n" + board.toString()
		+ "\n");
	}
	
	
	public void showEndScore() {
		
	}
	
	/**
	 * observer naar bord
	 */
	private void updateView() {
		System.out.println("\ncurrent game situation: \n\n" + board.toString()
			+ "\n");
	}
	
	/**
	 * Sending a message to the terminal.
	 * @param message
	 */
	public void print(String message) {
		System.out.println("this is the tui");
		System.out.println(message);
	}
}
