package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import servermodel.Board;
import servermodel.Stone;
import servermodel.Stonegroup;

class TestBoard {
	private Board board;
	private Stone black;
	private Stone white;
	private ArrayList<Integer> buurMan;
	
	@BeforeEach
	void setUp() throws Exception {
		board = new Board(3);
		black = Stone.b;
		white = Stone.w;
	}

	@Test
	void testSetField() {
		board.setField(1, 1, white);
		
		assertEquals(white, board.getField(1, 1)); 
	}

	@Test
	void testEmptyField() {
		board.setField(0, 0, white);
		board.setField(2, 1, white);
		
		assertEquals(white, board.getField(0, 0)); 
	
		assertFalse(board.isEmptyField(0, 0));
		assertFalse(board.isEmptyField(2, 1));
	}
	
	@Test 
	void testValidMove() {
		board.setField(0, 0, white);
		board.setField(2, 1, white);
		
		//geen empty field.
		assertFalse(board.isValidMove(2, 1));

	}
	
	@Test
	void testIndexToRowCol() {
				
		int row = board.indexToRow(5);
		int col = board.indexToCol(5);
		
		System.out.println(row);
		System.out.println(col);
		
		int[] coordinates = board.indexToRowCol(5);
		int row1 = coordinates[0];
		int col2 = coordinates[1];

		System.out.println(row1);
		System.out.println(col2);
		
		assertEquals(1, coordinates[0]);
		assertEquals(2, coordinates[1]);
	}
	
//	@Test
//	void testLiberties() {
//		int lib = board.getFreeLibertiesStone(1, 1);
//		buurMan = board.gettingNeighbors(0, 1);
//		System.out.println("aantal buurmannnen:" + buurMan);
//		System.out.println(buurMan.size());
//		System.out.println(lib);
//		board.setFields(1, Stone.b);
//		board.setFields(7, Stone.b);
//		System.out.println(lib);
//	}
	
	void testUpdate() {
		board.setField(0, 1, black);
		board.setField(3, 3, white);
		board.setField(1, 0, black);
		board.setField(3, 2, white);
		board.setField(1, 2, black);
		board.setField(0, 0, white);
		board.setField(2, 1, black);
		board.setField(1, 1, white); //gecaptured als het goed is
//		
//		List<Stone> neighbors = new 
//		
//		assertTrue(hasStone(Stone.w));
//		
	}
	
//	@Test
//	void testGetNeighborMap() {
//		assertEquals(2, board.getNeighborMap(0, 0).size());
//		System.out.println("Size neighbors hoekput" + board.getNeighborMap(0, 0).size());
//		System.out.println("zijkant" + board.getNeighborMap(1, 0).size());
//		assertEquals(3, board.getNeighborMap(0, 1).size());
//		System.out.println("midden" + board.getNeighborMap(1, 1).size());
//		assertEquals(4, board.getNeighborMap(1, 1).size());
//		
//		HashMap<Integer, Stone> buurman = (HashMap<Integer, Stone>) board.getNeighborMap(1, 1);
//		
//		for (Integer intersection : buurman) {
//			intersection.get
//		}
//	}
	
//	@Test
//	void testHasLiberties() {
//		board.setFields(1, Stone.b);
//		System.out.println("aantal liberties" + board.stoneHasLiberties(4));
//		assertEquals(3, board.stoneHasLiberties(4));
//		board.setFields(3, Stone.b);
//		System.out.println("aantal liberties.." + board.stoneHasLiberties(4));
//		assertEquals(2, board.stoneHasLiberties(4));
//		board.setFields(5, Stone.b);
//		System.out.println("aantal liberties..test." + board.stoneHasLiberties(4));
//		assertEquals(1, board.stoneHasLiberties(4));
//		board.setFields(7, Stone.b);
//		System.out.println("aantal liberties..test..." + board.stoneHasLiberties(4));
//		assertEquals(0, board.stoneHasLiberties(4));
//		
//	}
	
	@Test
	void testHasLiberties2() {
		
		// test with row and col
		board.setFields(1, Stone.b);
		
		assertTrue(board.stoneHasLiberties1(4));
		board.setFields(3, Stone.b);
		
		assertTrue(board.stoneHasLiberties1(4));
		board.setFields(5, Stone.b);
		
		assertTrue(board.stoneHasLiberties1(4));
		board.setFields(7, Stone.b);
	
		assertFalse(board.stoneHasLiberties1(4));
		
	}
	
	// has liberties
	@Test
	void testLibertiesGroup() {
		Board board1 = new Board(5);
		Stonegroup stonegroup = new Stonegroup(black);
		stonegroup.getStonegroup().add(7);
		stonegroup.getStonegroup().add(11);
		stonegroup.getStonegroup().add(12);
		
		for (int i = 0; i < stonegroup.getStonegroup().size(); i++) {
			System.out.println(stonegroup.getStonegroup().get(i));
		}
		
		System.out.println(board1.groupHasLiberties(stonegroup));
		assertTrue(board1.groupHasLiberties(stonegroup));
	}
	
	@Test
	void testLibertiesGroup1() {
		Board board1 = new Board(5);
		Stonegroup stonegroup = new Stonegroup(black);
		stonegroup.getStonegroup().add(7);
		stonegroup.getStonegroup().add(11);
		stonegroup.getStonegroup().add(12);
		
		for (int i = 0; i < stonegroup.getStonegroup().size(); i++) {
			System.out.println(stonegroup.getStonegroup().get(i));
		}
		
//		board1.setField(0, 2, white);
//		board1.setField(1, 1, white);
//		board1.setField(1, 3, white);
//		board1.setField(2, 0, white);
//		board1.setField(3, 1, white);
//		board1.setField(3, 2, white);
//		board1.setField(2, 3, white);
		
		board1.setFields(2, white);
		board1.setFields(6, white);
		board1.setFields(8, white);
		board1.setFields(10, white);
		board1.setFields(16, white);
		board1.setFields(17, white);
		board1.setFields(13, white);
		board1.setFields(7, black);
		board1.setFields(11, black);
		board1.setFields(12, black);
		System.out.print("liberties should be 0" + board1.groupHasLiberties(stonegroup));
	
		assertFalse(board1.groupHasLiberties(stonegroup));
	}
	
	@Test
	void testLibertiesGroup2() {
		Board board1 = new Board(5);
		Stonegroup stonegroup = new Stonegroup(black);
		stonegroup.getStonegroup().add(7);
		stonegroup.getStonegroup().add(11);
		stonegroup.getStonegroup().add(12);
		
		for (int i = 0; i < stonegroup.getStonegroup().size(); i++) {
			System.out.println(stonegroup.getStonegroup().get(i));
		}
		
		board1.setField(0, 2, white);
		board1.setField(1, 1, white);
		board1.setField(1, 3, white);
		board1.setField(2, 0, white);
		board1.setField(3, 1, white);
		board1.setField(3, 2, white);
		board1.setField(2, 3, white);

		board1.setField(1, 2, black);
		board1.setField(2, 1, black);
		board1.setField(2, 2, black);
		System.out.print("lib met row col" + board1.groupHasLiberties(stonegroup));
	
		assertFalse(board1.groupHasLiberties(stonegroup));
	}
	
	@Test
	void testConnectedChainGroup() {
		Board board1 = new Board(5);
		Stonegroup newChain = new Stonegroup(black);
		newChain.getStonegroup().add(7);
		newChain.getStonegroup().add(11);
		newChain.getStonegroup().add(12);
		
		board1.getAllStonegroups().add(newChain);
		
		assertEquals(1, board1.getAllStonegroups().size());
		
		Stonegroup newChain2 = new Stonegroup(black);
		newChain2.getStonegroup().add(18);
		newChain2.getStonegroup().add(19);
		newChain2.getStonegroup().add(23);
		
		board1.getAllStonegroups().add(newChain2);
		assertEquals(2, board1.getAllStonegroups().size());
		
	}
	
	//van twee naar 1 groep
	@Test 
	void testConnectedChainGroups1() {
		Board newBoard = new Board(5);
		
		newBoard.getConnectedChainGroup(2, 2, black);
		System.out.println("size" + newBoard.getAllStonegroups().size());
		assertEquals(1, newBoard.getAllStonegroups().size());
		
		newBoard.getConnectedChainGroup(1, 2, black);
		newBoard.getConnectedChainGroup(2, 1, black);
		System.out.println("size nummer" + newBoard.getAllStonegroups().size());
		//assertEquals(1, newBoard.getAllStonegroups().size());
		for (Stonegroup stone : newBoard.getAllStonegroups()) {
			System.out.println("this is it" + stone.getStonegroup());
		}
	}
	
	@Test 
	void testCreateNewChain() {
		Board newBoard1 = new Board(5);
		
		newBoard1.createNewChain(2, 2, black);
		newBoard1.createNewChain(2, 1, black);
		System.out.println("size_create" + newBoard1.getAllStonegroups().size());
		assertEquals(1, newBoard1.getAllStonegroups().size());
		
		newBoard1.createNewChain(4, 2, black);
		System.out.println("size_create.." + newBoard1.getAllStonegroups().size());
		assertEquals(2, newBoard1.getAllStonegroups().size());
		
		newBoard1.createNewChain(3, 2, black);
		System.out.println("size_create..." + newBoard1.getAllStonegroups().size());
		assertEquals(1, newBoard1.getAllStonegroups().size());		
	}
	
	@Test 
	void testCreateNewChain2() {
		Board newBoard1 = new Board(5);
		
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
	void isCaptured() {
		
	}
	
	@Test
	void removeChain() {
		Board newBoard2 = new Board(5);
		newBoard2.createNewChain(2, 2, black);
		newBoard2.createNewChain(2, 1, white);
		
		
	}
	
}
