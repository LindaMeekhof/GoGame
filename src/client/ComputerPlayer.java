package client;

import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import general.Protocol.General;
import servermodel.Board;

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
		
		
		Set<Integer> emptyField = new HashSet<Integer>();
		for (int i = 0; i < board.getDIM() * board.getDIM(); i++) {
			if (board.isEmptyField(i)) {
				emptyField.add(i);
			}
		}
		
    		// random field kiezen voor next move Math.random() 
		int emptyFieldSize = emptyField.size();
		int randomNumber = (int) (Math.random() * (emptyFieldSize - 1));
		//array is a fixed size daarom eerst set --> naar array

		int[] coordinates = board.indexToRowCol((int) emptyField.toArray()[randomNumber]);
		int row = coordinates[0];
		int col = coordinates[1];
		return row + General.DELIMITER1 + col;
	}

	//main method to test
//	public static void main(String[] args) {
//		Board board = nesw Board();
//		Player player = new ComputerPlayer("Computer", Stone.b);
//	}
}
