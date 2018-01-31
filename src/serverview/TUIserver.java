package serverview;


import com.nedap.go.gui.InvalidCoordinateException;

import server.Gamecontroller;
import servermodel.Board;


public class TUIserver {

	private Object board;
	private Gamecontroller controller;

	public TUIserver(Board board) {
	//	this.controller = gamecontroller;
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
		System.out.println("ServerTUI");
		System.out.println(message);
	}
	
}
