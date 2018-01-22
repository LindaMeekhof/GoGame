package clientmodel;

import servermodel.Board;
import servermodel.Stone;

public class ComputerPlayer extends Player {

	public ComputerPlayer(String name, Stone stone) {
		super(name, stone);
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
