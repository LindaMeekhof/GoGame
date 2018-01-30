package servermodel;


import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestStonegroup {
	private Stonegroup stone;
	private Stonegroup stone2;
	private Stonegroup stone3;
	private List<Stonegroup> stonegroups;
	
	private Board board;
	
	@BeforeEach
	
	void setUp() throws Exception {
		stone = new Stonegroup(Stone.b);
		stone2 = new Stonegroup(Stone.w);
		stone3 = new Stonegroup(Stone.__);
		stonegroups = new ArrayList<Stonegroup>();
		board = new Board();
	}

	@Test
	void testListSize() {
		stonegroups.add(stone);
		stonegroups.add(stone2);
		stonegroups.add(stone3);
		
		assertEquals(3, stonegroups.size());
	}
	
	@Test
	void testFindStone() {
		stonegroups.add(stone);
		stonegroups.add(stone2);
		stonegroups.add(stone3);
		
		assertEquals(stone, stonegroups.get(0));
		assertEquals(stone2, stonegroups.get(1));
		assertEquals(stone3, stonegroups.get(2));
	}
	
	@Test
	void testFindStoneColour() {
		stonegroups.add(stone);
		stonegroups.add(stone2);
		stonegroups.add(stone3);
		
		assertEquals(Stone.b, stonegroups.get(0).getStone());
		assertEquals(Stone.w, stonegroups.get(1).getStone());
	}
	
	@Test
	void testRemoveStonegroup() {
		stonegroups.add(stone);
		stonegroups.add(stone2);
		stonegroups.add(stone3);

		assertEquals(Stone.b, stonegroups.get(0).getStone());
		assertEquals(Stone.w, stonegroups.get(1).getStone());
		
		stonegroups.remove(0);
		assertEquals(Stone.w, stonegroups.get(0).getStone());
	}
	

	
//	@Test
//	void testBoardNeighborSize() {
//		board.setField(1, 1, Stone.b);
//		assertEquals(Stone.b, board.getField(1, 1));
//		assertEquals(4, board.getNeighbors(1, 1).size());
//		
//		//Corner
//		assertEquals(2, board.getNeighbors(0, 0).size());
//		//Side
//		assertEquals(3, board.getNeighbors(0, 1).size());		
//	}
//	
//	@Test
//	void testBoardNeighborColour() {
//		board.setField(1, 1, Stone.b);
//		board.setField(1, 0, Stone.w);
//		assertEquals(Stone.b, board.getField(1, 1));
//		assertTrue(board.getNeighbors(1, 1).contains(Stone.w));
//		assertFalse(board.getNeighbors(1, 1).contains(Stone.b));
//			
//	}

}
