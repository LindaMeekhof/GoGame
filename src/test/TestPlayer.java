package test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clientmodel.HumanPlayer;
import clientmodel.Player;
import servermodel.Board;
import servermodel.Stone;


class TestPlayer {
	/**Testvariable for determining choice.*/
	private Board board;
	private Player player;
	private Player player2;
		
	@BeforeEach
	void setUp() throws Exception {
		board = new Board();
		player = new HumanPlayer("Linda", Stone.b);
		player2 = new HumanPlayer("Linda", Stone.w);
	}

	//test if the makeMove works
	@Test
	void test() {
		//enter 1_1
		player.makeMove(board);
		assertEquals(Stone.b, board.getField(1, 1));

		
	}
	//test if the field stays the colour it was
	@Test
	void test1() {
		//enter 1_1
		player.makeMove(board);
		assertEquals(Stone.b, board.getField(1, 1));
		//enter 1_1
		//enter 1_2
		player2.makeMove(board);
		assertEquals(Stone.b, board.getField(1, 1));
	}
	
	@Test
	void test3() {
		//enter 1_1
		player.makeMove(board);
		assertEquals(Stone.b, board.getField(1, 1));
		//enter 1_2
		player2.makeMove(board);
		assertEquals(Stone.w, board.getField(1, 2));
	}

}
