package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import servermodel.Board;
import servermodel.Stone;
import servermodel.Stonegroup;



class TestBoard {
	private Board board;
	private Board board2;
	private Board board3;
	
	private Stone black;
	private Stone white;
	private ArrayList<Integer> buurMan;
	private Board board4;
	private Stonegroup chain1;
	private ArrayList<Stonegroup> stonegroups;

	
	@BeforeEach
	void setUp() throws Exception {
		board = new Board();
		board2 = new Board();
		board3 = new Board();
		board4 = new Board();
		black = Stone.b;
		white = Stone.w;
		
		board.boardInit(3);
		board2.boardInit(5);
		board3.boardInit(12);
		
		chain1 = new Stonegroup(black);
		
		stonegroups = new ArrayList<Stonegroup>();

	}
	
	@Test
	void testBoardInit() {
		board4.boardInit(4);
		assertEquals(Stone.__, board4.getFields(0));
	}
	
	@Test
	void testIndex() {
		assertEquals(0, board2.index(0, 0));
        assertEquals(17, board2.index(3, 2));
        assertEquals(24, board2.index(4, 4));
	}
	
	
	@Test
	public void testIsFieldIndex() {
		assertFalse(board2.isField(-1));
		assertTrue(board2.isField(0));
		assertTrue(board2.isField(17));
		assertTrue(board2.isField(24));
		assertFalse(board2.isField(25));
	}

	@Test
	public void testIsFieldRowCol() {
		assertFalse(board2.isField(-1, 0));
		assertFalse(board2.isField(0, -1));
		assertTrue(board2.isField(0, 0));
		assertFalse(board2.isField(12, 13));
		assertTrue(board2.isField(4, 4));

	}
	
	@Test
	public void testSetAndGetFieldIndex() {
		assertEquals(Stone.__, board.getFields(0));
		board.setFields(0,  black);
		assertEquals(black, board.getFields(0));
	}
	

	@Test
	public void testSetFieldRowCol() {
		board2.setField(0, 0, black);
		board2.setField(3, 2, white);
		assertEquals(black, board2.getField(0, 0));
		assertEquals(white, board2.getField(3, 2));
		assertEquals(Stone.__, board2.getField(1, 0));
	}
	
	@Test
	   public void testReset() {
        board2.reset();
        assertEquals(Stone.__, board2.getField(0, 0));
        assertEquals(Stone.__, board2.getField(4, 4)); 
    }
	
	@Test
	public void testIsEmptyFieldIndex() {
		board2.setFields(0, black);
		board2.setFields(2, white);
		assertFalse(board2.isEmptyField(0));
		assertTrue(board2.isEmptyField(1));
		assertFalse(board2.isEmptyField(2));
	}

	@Test
	public void testIsEmptyFieldRowCol() {
		board.setField(0, 0, Stone.b);
		assertFalse(board.isEmptyField(0, 0));
		assertTrue(board.isEmptyField(0, 1));
		assertTrue(board.isEmptyField(1, 0));
	}
	

    @Test
    public void testIsFull() {
        for (int i = 0; i < 8; i++) {
            board.setFields(i, black);
        }
        assertFalse(board.isFull());

        board.setFields(8, white);
        assertTrue(board.isFull());
    }
    
    @Test 
    public void isValidMove() {
    	assertTrue(board.isValidMove(0, 0));
    	board.setField(0, 0, black);
    	assertFalse(board.isValidMove(0, 0));
    }

	@Test
	void testIndexToRowCol() {
				
		int row = board2.indexToRow(5);
		int col = board2.indexToCol(5);
		
		int[] coordinates = board.indexToRowCol(5);
		int row1 = coordinates[0];
		int col2 = coordinates[1];
		
		assertEquals(1, coordinates[0]);
		assertEquals(2, coordinates[1]);
	}
	

	// has liberties
	@Test
	void testLibertiesGroup() {
		
		Stonegroup stonegroup = new Stonegroup(black);
		stonegroup.getStonegroup().add(7);
		stonegroup.getStonegroup().add(11);
		stonegroup.getStonegroup().add(12);
		
		for (int i = 0; i < stonegroup.getStonegroup().size(); i++) {
			System.out.println(stonegroup.getStonegroup().get(i));
		}
		
		System.out.println(board2.groupHasLiberties(stonegroup));
		assertTrue(board2.groupHasLiberties(stonegroup));
	}
	

	@Test
	void testLibertiesGroup1() {
	//	Board board1 = new Board(5);
		Stonegroup stonegroup = new Stonegroup(black);
		stonegroup.getStonegroup().add(7);
		stonegroup.getStonegroup().add(11);
		stonegroup.getStonegroup().add(12);
		
		for (int i = 0; i < stonegroup.getStonegroup().size(); i++) {
			System.out.println(stonegroup.getStonegroup().get(i));
		}
		
		board2.setFields(2, white);
		board2.setFields(6, white);
		board2.setFields(8, white);
		board2.setFields(10, white);
		board2.setFields(16, white);
		board2.setFields(17, white);
		board2.setFields(13, white);
		board2.setFields(7, black);
		board2.setFields(11, black);
		board2.setFields(12, black);
		System.out.print("liberties should be 0" + board2.groupHasLiberties(stonegroup));
	
		assertFalse(board2.groupHasLiberties(stonegroup));
	}

	
	@Test
	void testLibertiesGroup2() {
		//Board board1 = new Board(5);
		Stonegroup stonegroup = new Stonegroup(black);
		stonegroup.getStonegroup().add(7);
		stonegroup.getStonegroup().add(11);
		stonegroup.getStonegroup().add(12);
		
		for (int i = 0; i < stonegroup.getStonegroup().size(); i++) {
			System.out.println(stonegroup.getStonegroup().get(i));
		}
		board2.setField(1, 2, black);
		board2.setField(2, 1, black);
		board2.setField(2, 2, black);
		
		assertTrue(board2.groupHasLiberties(stonegroup));	
		
		board2.setField(0, 2, white);
		board2.setField(1, 1, white);
		board2.setField(1, 3, white);
		board2.setField(2, 0, white);
		board2.setField(3, 1, white);
		board2.setField(3, 2, white);
		board2.setField(2, 3, white);

		
		System.out.print("lib met row col" + board2.groupHasLiberties(stonegroup));
	
		assertFalse(board2.groupHasLiberties(stonegroup));
	}

	
// de methode heet nu createNewChain
	@Test
	void testConnectedChainGroup() {
		Stonegroup newChain = new Stonegroup(black);
		newChain.getStonegroup().add(7);
		newChain.getStonegroup().add(11);
		newChain.getStonegroup().add(12);

		board2.getAllStonegroups().add(newChain);

		assertEquals(1, board2.getAllStonegroups().size());

		Stonegroup newChain2 = new Stonegroup(black);
		newChain2.getStonegroup().add(18);
		newChain2.getStonegroup().add(19);
		newChain2.getStonegroup().add(23);

		board2.getAllStonegroups().add(newChain2);
		assertEquals(2, board2.getAllStonegroups().size());
	}


	@Test 
	void testCreateNewChain() {
		//Board board2 = new Board(5);
		
		board2.createNewChain(2, 2, black);
		board2.createNewChain(2, 1, black);
		System.out.println("size_create" + board2.getAllStonegroups().size());
		assertEquals(1, board2.getAllStonegroups().size());
		
		board2.createNewChain(4, 2, black);
		System.out.println("size_create.." + board2.getAllStonegroups().size());
		assertEquals(2, board2.getAllStonegroups().size());
		
		board2.createNewChain(3, 2, black);
		System.out.println("size_create..." + board2.getAllStonegroups().size());
		assertEquals(1, board2.getAllStonegroups().size());		
	}

	
	@Test 
	void testCreateNewChain2() {
		Board newBoard1 = new Board();
		newBoard1.boardInit(5);
		
		
		newBoard1.createNewChain(2, 2, black);
		newBoard1.createNewChain(2, 1, white);
		System.out.println("size_create" + newBoard1.getAllStonegroups().size());
		assertEquals(2, newBoard1.getAllStonegroups().size());
		
		newBoard1.createNewChain(4, 2, black);
		System.out.println("size_create.." + newBoard1.getAllStonegroups().size());
		assertEquals(3, newBoard1.getAllStonegroups().size());
		
		newBoard1.createNewChain(3, 2, black);
		System.out.println("size_create..." + newBoard1.getAllStonegroups().size());
		assertEquals(2, newBoard1.getAllStonegroups().size());		
	}

	
	
	
	@Test
	void removeStones() {
		//chain of black stones
		chain1.getStonegroup().add(7);
		chain1.getStonegroup().add(11);
		chain1.getStonegroup().add(12);
		
		board2.setFields(7, black);
		board2.setFields(11, black);
		board2.setFields(12, black);
		
		
		assertEquals(black, board2.getFields(7));
		board2.removeStones(chain1);
		assertEquals(Stone.__, board2.getFields(7));
	}
	
	@Test
	void removeStonesCaptured() {
		chain1.getStonegroup().add(7);
		chain1.getStonegroup().add(11);
		chain1.getStonegroup().add(12);
		
		board2.setFields(7, black);
		board2.setFields(11, black);
		board2.setFields(12, black);
		
		stonegroups.add(chain1);
		assertEquals(1, stonegroups.size());
		assertEquals(black, board2.getFields(7));
		
		board.removeStonesCaptured();
		assertEquals(black, board2.getFields(7));
		
		board2.setFields(2, white);
		board2.setFields(6, white);
		board2.setFields(8, white);
		board2.setFields(10, white);
		board2.setFields(13, white);
		board2.setFields(16, white);
		board2.setFields(17, white);
		
		//klopt de laatste wordt niet beoordeeld, pas bij update
		board.removeStonesCaptured();
		assertEquals(black, board2.getFields(7));
	}
	

	@Test
	void update() {
		// zie main method board
	}
	
	@Test
	void gettingNeighborSize() {
		//testing length
		
		assertEquals(2, board2.gettingNeighborsColor(0, 0).size());
		assertEquals(3, board2.gettingNeighborsColor(0, 1).size());
		assertEquals(4, board2.gettingNeighborsColor(1, 1).size());
	}
	
	@Test
	void gettingNeighborColor() {
		//testing length
		board2.setField(0,  1, black);
		
		System.out.println("dit" + board2.gettingNeighborsColor(0, 0));
		assertTrue(board2.gettingNeighborsColor(0, 0).contains(Stone.b));
		assertTrue(board2.gettingNeighborsColor(0, 0).contains(Stone.__));
	}
	
	@Test
	void previousBoard() {
		
	}
	
	
	
	@Test
	void areaScoring() {
		ArrayList<Integer> emptyFieldList = new ArrayList<Integer>();
		
		board2.setFields(7, Stone.__);
		board2.setFields(11, Stone.__);
		board2.setFields(12, Stone.__);
	
		
		board2.setFields(2, white);
		board2.setFields(6, white);
		board2.setFields(8, white);
		board2.setFields(10, white);
		board2.setFields(13, white);
		board2.setFields(16, white);
		board2.setFields(17, white);
		board2.setFields(0, black);
		
		board2.createEmptyFieldChainComplete();
		System.out.println("emtyfield" + emptyFieldList.size());
		
	}
	
	@Test
	void countScore() {
		board.setFields(1, Stone.b);
		board.setFields(3, Stone.b);
		board.setFields(5, Stone.b);
		board.setFields(7, Stone.b);
		
		System.out.println("score" + board.countScore(black));
	}
	
	@Test
	void isWinner() {
		
	}
}



// old tests ----------------------------------------------------
//@Test
//void testHasLiberties2() {
//	
//	// test with row and col
//	board.setFields(1, Stone.b);
//	
//	assertTrue(board.stoneHasLiberties1(4));
//	board.setFields(3, Stone.b);
//	
//	assertTrue(board.stoneHasLiberties1(4));
//	board.setFields(5, Stone.b);
//	
//	assertTrue(board.stoneHasLiberties1(4));
//	board.setFields(7, Stone.b);
//
//	assertFalse(board.stoneHasLiberties1(4));
//	
//}

//@Test
//void testLiberties() {
//	int lib = board.getFreeLibertiesStone(1, 1);
//	buurMan = board.gettingNeighbors(0, 1);
//	System.out.println("aantal buurmannnen:" + buurMan);
//	System.out.println(buurMan.size());
//	System.out.println(lib);
//	board.setFields(1, Stone.b);
//	board.setFields(7, Stone.b);
//	System.out.println(lib);
//}

//@Test
//void testGetNeighborMap() {
//	assertEquals(2, board.getNeighborMap(0, 0).size());
//	System.out.println("Size neighbors hoekput" + board.getNeighborMap(0, 0).size());
//	System.out.println("zijkant" + board.getNeighborMap(1, 0).size());
//	assertEquals(3, board.getNeighborMap(0, 1).size());
//	System.out.println("midden" + board.getNeighborMap(1, 1).size());
//	assertEquals(4, board.getNeighborMap(1, 1).size());
//	
//	HashMap<Integer, Stone> buurman = (HashMap<Integer, Stone>) board.getNeighborMap(1, 1);
//	
//	for (Integer intersection : buurman) {
//		intersection.get
//	}


//@Test
//void testHasLiberties() {
//board.setFields(1, Stone.b);
//System.out.println("aantal liberties" + board.stoneHasLiberties(4));
//assertEquals(3, board.stoneHasLiberties(4));
//board.setFields(3, Stone.b);
//System.out.println("aantal liberties.." + board.stoneHasLiberties(4));
//assertEquals(2, board.stoneHasLiberties(4));
//board.setFields(5, Stone.b);
//System.out.println("aantal liberties..test." + board.stoneHasLiberties(4));
//assertEquals(1, board.stoneHasLiberties(4));
//board.setFields(7, Stone.b);
//System.out.println("aantal liberties..test..." + board.stoneHasLiberties(4));
//assertEquals(0, board.stoneHasLiberties(4));
//
//}

//van twee naar 1 groep 
//@Test 
//void testConnectedChainGroups1() {
//	Board newBoard = new Board(5);
//	
//	newBoard.getConnectedChainGroup(2, 2, black);
//	System.out.println("size" + newBoard.getAllStonegroups().size());
//	assertEquals(1, newBoard.getAllStonegroups().size());
//	
//	newBoard.getConnectedChainGroup(1, 2, black);
//	newBoard.getConnectedChainGroup(2, 1, black);
//	System.out.println("size nummer" + newBoard.getAllStonegroups().size());
//	//assertEquals(1, newBoard.getAllStonegroups().size());
//	for (Stonegroup stone : newBoard.getAllStonegroups()) {
//		System.out.println("this is it" + stone.getStonegroup());
//	}
//}