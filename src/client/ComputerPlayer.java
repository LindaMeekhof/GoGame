package client;

import java.io.IOException;
import java.net.Socket;

import clientmodel.Player;
import servermodel.Board;
import servermodel.Stone;

public class ComputerPlayer extends PlayerController {

	public ComputerPlayer(String playerName, Socket sockArg) throws IOException {
		super(playerName, sockArg);
	}

	/**
	 * Determines the next field for the next move. This is a random move.
	 * @param board the G0-game board
	 */
	@Override
	public String determineMove(Board board) {
		//random numbers kiezen en deze dan als string doorsturen
		int boardsize = board.getDIM();
		int row = (int) (boardsize * Math.random());
		int col = (int) (boardsize * Math.random());
		return row + "_" + col;
	}

	//main method to test
//	public static void main(String[] args) {
//		Board board = nesw Board();
//		Player player = new ComputerPlayer("Computer", Stone.b);
//	}
}
