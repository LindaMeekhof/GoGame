package servermodel;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nedap.go.gui.GOGUI;
import com.nedap.go.gui.GOGUIImpl;
import com.nedap.go.gui.GoGUIIntegrator;
import com.nedap.go.gui.InvalidCoordinateException;

import general.Protocol.General;
import serverview.TUIserver;



/**
 * This is the board for GO.
 * Final exercise module 1 Nedap University.
 * 
 * @author Linda.Meekhof
 *
 */

public class Board {
	//Constants
	private int dimension; 
	private GoGUIIntegrator gogui;
	
	
	/**
	 * The DIM by DIM fields of the board GO. The numbering of the fields are ......
	 * 
	 */
	//@ private invariant fields.length == DIM*DIM;
    /*@ invariant (\forall int i; 0 <= i & i < DIM*DIM;
        getField(i) == Stone.__ || getField(i) == Stone.w || getField(i) == Stone.b); */
	private Stone[] fields;
	private TUIserver gotui;

	
//	public Board() {
//		
//	}
	/**
	 * The constructor creates an empty playing board
	 */
	/*
	 * (\forall int i; 0 <+ i & i < DIM*DIM;
	 * this.getField(i) == Stone.__);
	 */
	// Constructor
	public Board() {
//		this.dimension = dimension;
//		fields = new Stone[dimension * dimension];
//		
//		// All fields are initially empty
//		for (int i = 0; i < dimension * dimension; i++) {
//			fields[i] = Stone.__;
//		}

	}
	
	public void boardInit(int dimension) {
		fields = new Stone[dimension * dimension];
		
		// All fields are initially empty
		for (int i = 0; i < dimension * dimension; i++) {
			fields[i] = Stone.__;

		}
		gogui = new GoGUIIntegrator(false, true, dimension);
		gogui.startGUI();
		gogui.setBoardSize(9);
		this.dimension = dimension;
		gotui = new TUIserver(this);
		gotui.showBoard();
	}
	
	/**
     * Creates a deep copy of this field. THis can be useful for trying positions and for the rule: 
     * No stone may be played so as to recreate any previous board position
     */
    /*@ ensures \result != this;
        ensures (\forall int i; 0 <= i & i < DIM * DIM;
                                \result.getField(i) == this.getField(i));
      @*/
	public Board deepCopy() {
		Board copyboard = new Board();
		for (int i = 0; i < dimension * dimension; i++) {
			copyboard.fields[i] = this.fields[i];
		}
		return copyboard;
	}
	
	/**
	 * Returns the content of the field. This can be Stone.b, Stone.w or Stone.__
	 * @param i the index of the field
	 * @return the Stone of the field
	 */
	/*pure*/
	//@ requires this.isField(i);
	//@ ensures \result == Stone.w || \result == Stone.b || \result == Stone.__
    public Stone getFields(int i) {
		return fields[i]; 
	}
    
    //@ requires board != null;
    /*pure*/
    /**
     * Returns the dimension of the board.
     * @return
     */
    public int getDIM() {
		return dimension;
	}

    /**
     * Sets the dimension of the board.
     * @param dIM
     */
	public void setDIM(int dIM) {
		dimension = dIM;
	}

	/**
     * Returns the content of the field. This can be Stone.b, Stone.w or Stone.__.
     * Requires the method index(row, col).
     * @param row
     * @param col
     * @return the Stone on the field (b, w, or empty)
     */
    /*pure*/
    //@ requires this.isField(i);
    //@ ensures \result == Stone.w || Stone.b || Stone.__ 
    public Stone getField(int row, int col) {
    		return fields[index(row, col)];
    }
    
    
    /**
     * This method sets the field, indicated with the index i, to the given stone parameter.
     * @param i is the index of the field.
     * @param stone is the given Stone mark to be placed.
     */
    //@ requires this.isField(i);
    //@ ensures this.fields == stone;
	public void setFields(int i, Stone stone) {
		this.fields[i] = stone;
	}
	
	
	
	/**
	 * This method sets the field, indicated with row and col, to the given stone parameter.
	 * @param row is the field's row.
	 * @param col is the field's  column.
	 * @param stone is the given Stone mark to be placed.
	 * @throws InvalidCoordinateException 
	 */
	//@ requires this.isField(i);
	//@ ensures this.fields == stone;
	public void setField(int row, int col, Stone stone) {
		this.fields[index(row, col)] = stone;
		gogui.addStone(col, row, stone.equals(Stone.w));
		gotui.addStone();
	}

	/**
     * Calculates the index in the linear array of fields from a (row, col)
     * pair. When DIM = 19 the linear array consist of 361 index numbers.
     * @return the index belonging to the (row,col)-field
     */
    //@ requires 0 <= row & row < DIM;
    //@ requires 0 <= col & col < DIM;
    /*@pure*/
    public int index(int row, int col) {
		return row * dimension + col;
    }
    
    public int indexToRow(int index) {
    		return index / dimension;
    }
    
    public int indexToCol(int index) {
    		return index % dimension;
    }
    
    public int[] indexToRowCol(int index) {
    	int[] coordinates = new int[2];
    	int row = index / dimension;
    	coordinates[0] = row;
    	int col = index % dimension;
    	coordinates[1] = col;
    	return coordinates;
    }
    
    /**
     * Returns true if the index of a field is a valid index. 
     * When DIM = 19 the linear array consist of 361 index numbers.
     * @return true if 0<= index < DIM*DIM
     * (Use in TUI to indicate if it is a valid field).
     * overbodig??
     */
    /*@pure*/
    //@ ensures \result == (0 <=index && index < DIM * DIM);
    public boolean isField(int index) {
     	return (0 <= index) && (index < dimension * dimension); 
    }
	
    /**
     * 
     * @param row
     * @param col
     * @return true if 0 <= row < DIM && 0 <= col < DIM
     */
    /*pure*/
    //@ ensures \result == (0 <= row < DIM && 0<= col < DIM)
    public boolean isField(int row, int col) {
     	return (0 <= row) && (row < dimension) && 
   			 (0 <= col) && (col < dimension);
    }
    
    /*pure*/
    //@ requires this.isField(i)
    //@ ensures \result == (this.getField(i) == Stone.__);
    public boolean isEmptyField(int i) { 
	 	return getFields(i) == Stone.__;
    }
    
    /*pure*/
    //@ requires this.isField(i)
    //@ ensures \result == (this.getField(row, col) == Stone.__);
	public boolean isEmptyField(int row, int col) {
		return getField(row, col) == Stone.__;
	}
	
	/*pure*/
	public boolean isFull() {
		boolean isFull = true;
		for (int i = 0; i < dimension * dimension; i++) {
			if (isEmptyField(i)) { 
				isFull = false;
			}
		}
		return isFull;
	}
	

	
	//@ ensures (\forall int i; 0 <= i & i < DIM*DIM; this.field == Stone.__);
	public void reset() {
		for (int i = 0; i < dimension * dimension; i++) {
			fields[i] = Stone.__;
			gogui.clearBoard();
			gotui.showBoard();
		}
	}
	
	/** 
	 * moet nog checken op previous board.
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isValidMove(int row, int col) {
		return  isField(row, col) && 
				isEmptyField(row, col);
	
	}
		

	/**
	 * Wanneer 1 van de stenen een vrije libertie heeft, is de groep niet gecaptured.
	 * laatste niet meenemen
	 * @param stonechain
	 * @return
	 */
	public Boolean groupHasLiberties(Stonegroup stonechain) {
		ArrayList<Integer> listIntersection = (ArrayList<Integer>) stonechain.getStonegroup();
		for (int i = 0; i < listIntersection.size(); i++) {
			int intersection = listIntersection.get(i);
			int[] coordinates = indexToRowCol(intersection);
			int row = coordinates[0];
			int col = coordinates[1];
			if (row > 0)  {
				if (getFields(index(row - 1, col)).equals(Stone.__)) { //top
					return true;
				}
			} 
			if (row < dimension - 1) {
				if (getFields(index(row + 1, col)).equals(Stone.__)) { //down
					return true;
				}
			}
			if (col > 0) {
				if (getFields(index(row, col - 1)).equals(Stone.__)) { //left
					return true;
				}
			}
			if (col < dimension - 1) {
				if (getFields(index(row, col + 1)).equals(Stone.__)) { //right
					return true;
				}
			}
		}
		return false;
	}
	

//	/**
//	 * Stone has liberties.
//	 * @param indexIntersection
//	 * @return
//	 */
//	public Boolean stoneHasLiberties1(int indexIntersection) {
//	
//		int[] coordinates = indexToRowCol(indexIntersection);
//		int row = coordinates[0];
//		int col = coordinates[1];
//		if (row > 0)  {
//			if (getFields(index(row - 1, col)).equals(Stone.__)) { //top
//				return true;
//			}
//		} 
//		if (row < dimension - 1) {
//			if (getFields(index(row + 1, col)).equals(Stone.__)) { //down
//				return true;
//			}
//		}
//		if (col > 0) {
//			if (getFields(index(row, col - 1)).equals(Stone.__)) {//left
//				return true;
//			}
//		}
//		if (col < dimension - 1) {
//			if (getFields(index(row, col + 1)).equals(Stone.__)) {//right
//				return true;
//			}
//		}
//		return false;
//	}
	
	/**
	 * Getting neighbor integers.
	 */
	public ArrayList<Integer> gettingNeighbors(int row, int col) {
		ArrayList<Integer> neighbors = new ArrayList<Integer>();
		//of moet het een map zijn.
		if (row > 0)  {
			neighbors.add(index(row - 1, col)); //top
		}
		if (row < dimension - 1) {
			neighbors.add(index(row + 1, col)); //down
		}
		if (col > 0) {
			neighbors.add(index(row, col - 1)); //left
		}
		if (col < dimension - 1) {
			neighbors.add(index(row, col + 1)); //right
		}
		return neighbors;	
	}
	

	private ArrayList<Stonegroup> stonegroups = new ArrayList<Stonegroup>();



	
	public List<Stonegroup> getAllStonegroups() {
		return stonegroups;
	}
	
/**
 * Create a new chaingroup with the last set.
 * @param row
 * @param col
 * @param stoneColor
 */
	
	public void createNewChain(int row, int col, Stone stoneColor) {
		ArrayList<Integer> neighbors = (ArrayList<Integer>) gettingNeighbors(row, col);

		//create a new stonegroup with the aangrenzende stenen en de nieuwe steen
		Stonegroup newStonegroup = new Stonegroup(stoneColor);
		Set<Stonegroup> neighborGroup = new HashSet<>();
		for (Stonegroup stoneChain : stonegroups) {
			if (stoneChain.getStone().equals(stoneColor)) {
				// intereer over de lijst van intersections
				for (int i = 0; i < stoneChain.getStonegroup().size(); i++) {
					for (int itersection = 0; itersection < neighbors.size(); itersection++) {
						if (stoneChain.getStonegroup().contains(neighbors.get(itersection))) {

							neighborGroup.add(stoneChain);
							
						}
					}

				}
			}
		}
		//remove the groups that are neighbors and make it one group.
		for (Stonegroup neigh : neighborGroup) {
			newStonegroup.getStonegroup().addAll(neigh.getStonegroup());
			stonegroups.remove(neigh);
		}
		newStonegroup.getStonegroup().add(index(row, col)); 
		stonegroups.add(newStonegroup);
	}	
	


	/**
	 * When a stonegroup is captured by the other player. 
	 * These captured stones are removed from the board.
	 * Remove the set of stones that are captured.
	 * Laatste group niet meerekenen
	 */
	//@ requires isCaptured();
	//@ ensures \result == Stone.__;
	public void removeStonesCaptured() {
		HashSet<Stonegroup> capturedGroups = new HashSet<Stonegroup>();
		
		for (int i = 0; i < stonegroups.size() - 1; i++) {
			Stonegroup group = stonegroups.get(i);
			if (!groupHasLiberties(group)) {
				//alle intersections need to be changed in empty
				for (int indexList = 0; indexList < group.getStonegroup().size(); indexList++) {
					setFields(group.getStonegroup().get(indexList), Stone.__);
					capturedGroups.add(group);
					int[] coordinates =  indexToRowCol(group.getStonegroup().get(indexList));
					int row = coordinates[0];
					int col = coordinates[1];
					gogui.removeStone(col,  row);
				}
			}
		}	
		
		//remove capturedGroups form the list of Stonegroups.
		for (Stonegroup capStone : capturedGroups) {
			stonegroups.remove(capStone);
		}
	}

	public void removeStones(Stonegroup stonegroup) {
		List<Integer> listOfIndex = stonegroup.getStonegroup();
		for (int i = 0; i < listOfIndex.size(); i++) {
			int indexStone = listOfIndex.get(i);
			setFields(indexStone, Stone.__);
			int[] coordinates =  indexToRowCol(i);
			int row = coordinates[0];
			int col = coordinates[1];
			//gogui.removeStone(col, row);
		}
	}
	
	public void updateBoard(int row, int col, Stone stoneColor) {
		//create and update chaingroup with the last move
		createNewChain(row, col, stoneColor);
		removeStonesCaptured();
		
		// the last created stone
		if (!groupHasLiberties(stonegroups.get(stonegroups.size() - 1))) {
			removeStones(stonegroups.get(stonegroups.size() - 1));
			gogui.removeStone(col, row);
		}
		
		// update gotui and add this board representation to previousBoards.
		previousBoards.add(toString());
		gotui.showBoard();
	}



//	/**
//	 *  A map which contains the neighbors colour and index of the intersection.
//	 *  his length of the list can be 4, 3 (side of the field) or 2 (corner of the field).
//	 *  
//	 */
//	public Map<Integer, Stone> getNeighborMap(int row, int col) {
//		Map<Integer, Stone> neighbors = new HashMap<Integer, Stone>();
//		if (row > 0)  {
//			neighbors.put(index(row - 1, col), getField(row - 1, col)); //top
//		}
//		if (row < dimension - 1) {
//			neighbors.put(index(row + 1, col), getField(row + 1, col)); //down
//		}
//		if (col > 0) {
//			neighbors.put(index(row, col - 1), getField(row, col - 1)); //left
//		}
//		if (col < dimension - 1) {
//			neighbors.put(index(row, col + 1), getField(row, col + 1)); //right
//		}
//		return neighbors;	
//	}
	

	
//	public Map<String, Integer> endScore() { 
//		int scoreBlack = countScore(Stone.b);
//		int scoreWhite = countScore(Stone.b);
//
//
//		// Mapping the score to the color
//		Map<String, Integer> scores = new HashMap<String, Integer>();
//		scores.put("BLACK", scoreBlack);
//		scores.put("WHITE", scoreWhite);
//
//		return scores;
//	}
	

	/**
	 * The winner with the biggest area wins. 
	 * A player's area consists of all the intersections the player 
	 * has either occupied or surrounded. 
	 * In case of an equal score there is a draw. 
	 * @return the stone color which has won the game.
	 */
	/* pure*/
	public boolean isWinner(Stone stone) {
		return countScore(stone) > countScore(stone.other());
	}
	
	/** 
	 * This gives a String representation of this GO-board.
	 */
	public String toString() {
		//String board = "   0   1   2   3   4" + "\n";
		String board = "";
		for (int i = 0; i < dimension; i++) {
			String row = "" + i + "";
			for (int j = 0; j < dimension; j++) {
				row = row + " " + getField(i, j).toString() + " ";
				// alle velden van 1 rij
			}
			board = board + row + "\n";
		}
		return board;
	}	
	
	Set<String> previousBoards = new LinkedHashSet();
	//opslaan van de array die het board vormt
	
	public boolean isPreviousBoard(String nextBoard) {
		for (String previous : previousBoards) {
			if (nextBoard.equals(previous)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Counting score.
	 */
	public int countScore(Stone stone) {
		int count = 0;
		for (int i = 0; i < fields.length; i++) {
			if (getFields(i).equals(stone)) {
				count = count + 1;
			}
		}
		int area = capturedArea(stone);
		count = count + area;
		return count;
	}
	
	
// Area scoring ------------------------------------------------------------------------------------
	//islandList is de lijst met alle stonegroup met kleur empty.
	private List<Stonegroup> islandList = new ArrayList<Stonegroup>();
	
// de lijst met alle lege velden
	private ArrayList<Integer> emptyFieldList = new ArrayList<Integer>();
	
	public ArrayList<Integer> getEmptyFieldList() {
		return emptyFieldList;
	}

	public void setEmptyFieldList(ArrayList<Integer> emptyFieldList) {
		this.emptyFieldList = emptyFieldList;
	}

	
	public int capturedArea(Stone color) {
		int areaCaptured = 0;
		
		//Een lijst met alle neighbors of the group.
		ArrayList<Stone> neighborGroup = new ArrayList<Stone>();
		
		//Alle groepen bij langs in de isLandlist, alle intersections
		for (Stonegroup emptyGroup : islandList) {

			for (int i = 0;  i < emptyGroup.getStonegroup().size(); i++) {
				int intersection = emptyGroup.getStonegroup().get(i);

				int[] coordinates = indexToRowCol(intersection);
				int row = coordinates[0];
				int col = coordinates[1];
				
				ArrayList<Stone> neighbor = gettingNeighborsColor(row, col);
				//alle neighbors worden toegevoegd. Neighborcolor can be white, black and empty
				neighborGroup.addAll(neighbor);
			}
			// als het alleen maar jou kleur heeft of empty, dan is de group gecaptured
			if (neighborGroup.contains(color) && !neighborGroup.contains(color.other())) {
				areaCaptured = areaCaptured + emptyGroup.getStonegroup().size();
			} 
		}
		return areaCaptured;
	}
	
	/**
	 * First gather all the empty fields in a list --> emptyFieldList.
	 * After that create all the chains, by iterating through the list of emptyFieldList.
	 */
	public void createEmptyFieldChainComplete() {
		for (int i = 0; i < dimension * dimension; i++) {
			if (fields[i].equals(Stone.__)) {
				emptyFieldList.add(i);
			}
		}
		
		for (int i = 0; i < emptyFieldList.size(); i++) {
			createIsland(i, Stone.__); 
		}
	}
	
	/**
	 * Create a new Chain with emptyFields who are connected.
	 * @param emptyField
	 * @param emptyColor
	 */
	public void createIsland(int emptyField, Stone emptyColor) {
		//from index to row and col
		int[] coordinates = indexToRowCol(emptyField);
		int row = coordinates[0];
		int col = coordinates[1];

		ArrayList<Integer> neighborsneighborsIslandStone = (ArrayList<Integer>) gettingNeighbors(row, col);

		//create a new stonegroup with  the neighboring stones and the new emptystonefield
		Stonegroup emptyStonegroup = new Stonegroup(emptyColor);
		
		//set van groepen die grenzen aan het lege veld
		Set<Stonegroup> neighborGroup = new HashSet<>();

		if (!islandList.isEmpty()) {
			for (Stonegroup stoneChain : islandList) {
				
				if (stoneChain.getStone().equals(emptyColor)) { //dubbele check
					// intereer over de lijst van intersections
					for (int i = 0; i < stoneChain.getStonegroup().size(); i++) {
						for (int intersection = 0; intersection < neighborsneighborsIslandStone.size(); intersection++) {
							if (stoneChain.getStonegroup().contains(neighborsneighborsIslandStone.get(intersection))) {
								neighborGroup.add(stoneChain);
							}
						}
					}
				}
			}
			for (Stonegroup neigh : neighborGroup) {
				emptyStonegroup.getStonegroup().addAll(neigh.getStonegroup());
				islandList.remove(neigh);
			}
		}
		
		//remove the groups that are neighbors and make it one group.	
		emptyStonegroup.getStonegroup().add(index(row, col)); 
		islandList.add(emptyStonegroup);
	}
	
	/**
	 * Getting neighbor Stonecolor.
	 */
	public ArrayList<Stone> gettingNeighborsColor(int row, int col) {
		ArrayList<Stone> neighbors = new ArrayList<Stone>();
		
		if (row > 0)  {
			neighbors.add(getField(row - 1, col)); //top
		}
		if (row < dimension - 1) {
			neighbors.add(getField(row + 1, col)); //down
		}
		if (col > 0) {
			neighbors.add(getField(row, col - 1)); //left
		}
		if (col < dimension - 1) {
			neighbors.add(getField(row, col + 1)); //right
		}
		return neighbors;	
	}

// Main --------------------------------------------------------
//	public static void main(String[] args) {
//	//	GOGUIImpl gogui = new GOGUIImpl();
//		Board board = new Board();
//		board.boardInit(5);
//		Board board2 = new Board();
//		board2.boardInit(5);
//		
//		board.setField(1, 2, Stone.b);
//		
//		board.updateBoard(1, 2, Stone.b);
//		board.setField(2, 1, Stone.b);
//		board.updateBoard(2, 1, Stone.b);
//		board.setField(3, 2, Stone.b);
//		board.updateBoard(3, 2, Stone.b);
//		board.setField(2, 3, Stone.b);
//		board.updateBoard(2, 3, Stone.b);
//		
//	//	System.out.println("hello" + board.toString());
//		
//		board.setField(2, 2, Stone.w);
//		board.updateBoard(2, 2, Stone.w);
//		
//		board.setField(0, 0, Stone.w);
//	//	System.out.println("hello2" + board.toString());
//		board.updateBoard(0, 0, Stone.w);
//		
//		board.setField(1, 1, Stone.w);
//		board.updateBoard(1, 1, Stone.w);
//		
//		board.setField(0, 1, Stone.b);
//		board.updateBoard(0, 1, Stone.b);
//		
//		board.setField(2, 0, Stone.w);
//		board.updateBoard(2, 0, Stone.w);
//		
//		board.setField(1, 0, Stone.b);
//		board.updateBoard(1, 0, Stone.b);
//	
//		board2.setFields(7, Stone.__);
//		board2.setFields(11, Stone.__);
//		board2.setFields(12, Stone.__);
//	
//		
//		board2.setFields(2, Stone.w);
//		board2.setFields(6, Stone.w);
//		board2.setFields(8, Stone.w);
//		board2.setFields(10, Stone.w);
//		board2.setFields(13, Stone.w);
//		board2.setFields(16, Stone.w);
//		board2.setFields(17, Stone.w);
//		board2.setFields(0, Stone.w);
//		
//		board2.createEmptyFieldChainComplete();
//		System.out.println("emtyfield" + board.getEmptyFieldList());
//	}
	
} //class
