package servermodel;

import java.util.Scanner;

import clientmodel.Player;

public class GOGame {

	private int numberPlayers;
	private Board board;
   
	private Player[] players; //kan ik hier zo bij clientside
	 /*@ private invariant 0 <= current  && current < NUMBER_PLAYERS; */
	private int currentPlayer;
	
	
	//Constructors
/**
 * Creates a new GO-game.
 * @param numberPlayers
 * @param board
 * @param player1 is the first player
 * @param player2 is the second player
 * @param currentPlayer is the player who starts
 */
	public GOGame(int numberPlayers, Board board, Player player1, Player player2, int currentPlayer) {
		this.numberPlayers = numberPlayers;
		this.board = board;
		players[0] = player1;
		players[1] = player2;
		this.currentPlayer = currentPlayer; //the currentplayer is black
	}
	
	public void start() {
		boolean play = true;
		while (play) {
			reset();
			play();
			play = readAnotherPlay("\n> Play another time? (y/n)?", "y", "n");
		}
	}
	
	//is true if y is inputted.
	private boolean readAnotherPlay(String prompt, String yes, String no) {
		String respons;
		do {
			System.out.print(prompt);
			Scanner input = new Scanner(System.in);
			respons = input.hasNextLine() ? input.nextLine() : null;
		} while (respons == null || (!respons.equals(yes) && !respons.equals(no)));
		return respons.equals(yes);
	}
	
	private void play(int index, Stone stone) {
		updateView();
    	while (!board.gameOver()) {
    		board.setFields(index, stone); //makeMove is in player class, waar je niet bij kan...
    		currentPlayer = (currentPlayer + 1) % 2;
    		updateView();
    	}
    	printResult();
	}
	
	private void reset() {
		board.reset();
		currentPlayer = 0; // moet weer op zwart staan
	}
	
	//misschien moet deze naar de TUI?
	private void updateView() {
		System.out.println("\ncurrent game situation: \n\n" + board.toString()
			+ "\n");
	}
	
	//misschien moet deze naar de TUI?
	//score moet er nog in verwerkt worden.
	/**
	 * This prints the results when a game is ended. This can be a draw.
	 */
	private void printResult() {
		if (board.hasWinner()) {
			if (board.isWinner(players[0].getStone())) {
				System.out.println("Player " + players[0].getName());
			} else if (board.isWinner(players[1].getStone())) {
				System.out.println("Player " + players[1].getName());
			} else {
				System.out.println("This is a draw. There is no winner");
			}
		}
	}
	
}
