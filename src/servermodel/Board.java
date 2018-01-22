package servermodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is the board for GO.
 * Final exercise module 1 Nedap University.
 * 
 * @author Linda.Meekhof
 *
 */

public class Board {
	//Constants
	private int DIM = 13; // misschien kan de gebruiker die ook wel invoeren.
	private static final String LINE = "---"; // voor TUI belijning
	
	
	/**
	 * The DIM by DIM fields of the board GO. The numbering of the fields are ......
	 * 
	 */
	//@ private invariant fields.length == DIM*DIM;
    /*@ invariant (\forall int i; 0 <= i & i < DIM*DIM;
        getField(i) == Stone.__ || getField(i) == Stone.w || getField(i) == Stone.b); */
	private Stone[] fields;
	
	
	/**
	 * The constructor creates an empty playing board
	 */
	/*
	 * (\forall int i; 0 <+ i & i < DIM*DIM;
	 * this.getField(i) == Stone.__);
	 */
	// Constructor
	public Board() {
		fields = new Stone[DIM * DIM];
		
		// All fields are initially empty
		for (int i = 0; i < DIM * DIM; i++) {
			fields[i] = Stone.__;
		}
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
		for (int i = 0; i < DIM * DIM; i++) {
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
		return DIM;
	}

    /**
     * Sets the dimension of the board.
     * @param dIM
     */
	public void setDIM(int dIM) {
		DIM = dIM;
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
	 * Klopt deze?
	 * This method sets the field, indicated with row and col, to the given stone parameter.
	 * @param row is the field's row.
	 * @param col is the field's  column.
	 * @param stone is the given Stone mark to be placed.
	 */
	//@ requires this.isField(i);
	//@ ensures this.fields == stone;
	public void setField(int row, int col, Stone stone) {
		this.fields[index(row, col)] = stone;
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
		return row * DIM + col;
    }
    
    public int[] indexToRowCol(int index) {
	    	int[] coordinates = new int[2];
	    	int row = index / DIM;
	    	coordinates[0] = row;
	    	int col = index % DIM;
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
     	return (0 <= index) && (index < DIM * DIM); 
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
     	return (0 <= row) && (row < DIM) && 
   			 (0 <= col) && (col < DIM);
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
		return getField(col, row) == Stone.__;
	}
	
	/*pure*/
	public boolean isFull() {
		boolean isFull = true;
		for (int i = 0; i < DIM * DIM; i++) {
			if (isEmptyField(i)) { 
				isFull = false;
			}
		}
		return isFull;
	}
	
	/**
	 * Returns true if the board is full or both players have skipped their turn after each other.
	 * @return true is game is over.
	 */
	/*pure*/
	// ensures \result == this.isFull() || twee keer pass
	public boolean gameOver() {
		return false;
	}
	
	//@ ensures (\forall int i; 0 <= i & i < DIM*DIM; this.field == Stone.__);
	public void reset() {
		for (int i = 0; i < DIM * DIM; i++) {
			fields[i] = Stone.__;
		}
	}
	
//	public void connectGroup(int row, int col, Stone stone){
//		 boolean connect = false;
//		 
//		 
//		 if(true) {
//			 //plaats in de groep
//			 //1 aangrenzende steen met dezelfde kleur
//		 }
//		 if(false) {
//			 List <fields> stoneFields = new ArrayList<Stones>();
//			 //maak een nieuwe groep met deze positie
//			 //geen aangrenzende stenen met dezelfde kleur
//		 }
//	}
//	/**
//	 * This method should return a set/list of fields that are captured by the other player.
//	 * 
//	 * @return
//	 */
//	public boolean isCaptured(int row, int col, Stone stone) {
//		boolean isCap = false;
//		//getting the color of the field
//		getField(row, col);
//         
//			for (int i = row-1; i <= row+2; i += 2) { 
//				isCap = getField(i, col) == stone.other();	
//			}	
//			for (i = col-1; i <= (col+2); i += 2) { 
//				isCap = getField(row, i) == stone.other();
//			}	
//		}
//	}


	
	/**
	 * This method gets an ArrayList which represents the 
	 * neighboring intersections of an index (represented by int row and int col).
	 * This length of the list can be 4, 3 (side of the field) or 2 (corner of the field).
	 * @param row 
	 * @param col
	 */
	public List<Stone> getNeighbors1(int row, int col) {
		List<Stone> neighbors = new ArrayList<Stone>();
		//of moet het een map zijn.
		if (row > 0)  {
			neighbors.add(getField(row - 1, col)); //top
		}
		if (row < DIM - 1) {
			neighbors.add(getField(row + 1, col)); //down
		}
		if (col > 0) {
			neighbors.add(getField(row, col - 1)); //left
		}
		if (col < DIM - 1) {
			neighbors.add(getField(row, col + 1)); //right
		}
		return neighbors;	
	}
	
	public List<Integer> gettingNeighbors(int row, int col) {
		List<Integer> neighbors = new ArrayList<Integer>();
		//of moet het een map zijn.
		if (row > 0)  {
			neighbors.add(index(row - 1, col)); //top
		}
		if (row < DIM - 1) {
			neighbors.add(index(row + 1, col)); //down
		}
		if (col > 0) {
			neighbors.add(index(row, col - 1)); //left
		}
		if (col < DIM - 1) {
			neighbors.add(index(row, col + 1)); //right
		}
		return neighbors;	
	}
	
	/**
	 * Is connected to the another group with the same color en returns the new group.
	 */
	public Stonegroup getStoneGroupConnected(int row, int col) {
		//list with the neighbors intersections
		List<Integer> neigh = gettingNeighbors(row, col);
		//create a new stonegroup with the aangrenzende stenen en de nieuwe steen
		Stonegroup newStonegroup = new Stonegroup(null);
		for (Integer myNeighbor : neigh) {
			//can be more than one group
			if (getFields(myNeighbor).equals(Stone.b)) {
				//of een andere stonecolour maar in dit voorbeeld stone.b
				//get Stonegroup waarbij deze intersectie hoort
				Stonegroup foundStonegroup = searchStoneGroup(myNeighbor);
				//foundStonegroup.getStonegroup().
				
				// nieuwe groep
				newStonegroup.getStonegroup().addAll(foundStonegroup.getStonegroup());
				
				//remove the group from the list
				stonegroups.remove(foundStonegroup);
			}
			
		}
		newStonegroup.getStonegroup().add(index(row, col)); //stone niet myNeighbor
		return newStonegroup;
	}
	
	private Set<Stonegroup> stonegroups = new LinkedHashSet<Stonegroup>();
	// nog initieeren aan het begin bij het maken van het board
	// List<Stonegroup> stonegroups = new ArrayList<Stonegroup>(); linkedlist
	/**
	 * Returns the Stonegroup which belongs to a certain intersection of the field. 
	 * @param searchInt
	 * @return
	 */
	public Stonegroup searchStoneGroup(int searchInt) {
		Stonegroup foundGroup = null;
		for (Stonegroup stonegroup : stonegroups) {
			List<Integer> intStone = stonegroup.getStonegroup();
			for (int i = 0; i < intStone.size(); i++) {
				if (intStone.get(i).equals(searchInt)) {
					foundGroup = stonegroup;
				}
			}
		}
		return foundGroup;
	}
	
	
	public int getFreeLiberties(Stonegroup expectedStonegroup) {
		int liberty = 0;
		List<Integer> stonegroup = expectedStonegroup.getStonegroup();
		for (int i = 0; i < stonegroup.size(); i++) {
			int[] rowAndCol = indexToRowCol(i);
			int row = rowAndCol[0];
			int col = rowAndCol[1];
			List<Stone> buurStones = getNeighbors1(row, col);
			for (Stone stone : buurStones) {
				if (stone.equals(Stone.__)) {
					liberty = liberty + 1;
				}
			}	
		}
		return liberty;
	}
	
	/** 
	 * Search in the set of stonegroups for possible captured stonegroups. 
	 * @return Set<Stonegroups> which are captured
	 */
	private Set<Stonegroup> capturedStonegroup() {
		// door de groepen stones
		Set<Stonegroup> capturedStonegroups = new HashSet();
		for (Stonegroup stonegroup : stonegroups) {
			if (getFreeLiberties(stonegroup) == 0) {
				capturedStonegroups.add(stonegroup);
			}
		}			
		return capturedStonegroups;
	}
	
	/**
	 * When a stone or group of stones is captured by the other player. 
	 * These captured stones are removed from the board.
	 * Remove the set of stones that are captured.
	 */
	//@ requires isCaptured();
	//@ ensures \result == Stone.__;
	private void removeStones(Set<Stonegroup> stonegroup) {
		for (Stonegroup capturedGroup : stonegroup) {
			List<Integer> stoneIntegers = capturedGroup.getStonegroup();
			for (int i = 0; i < stoneIntegers.size(); i++) {
				int index = stoneIntegers.get(i);
				setFields(index, Stone.__);
			}
		}
	}
	
	/**
	 *  A map which contains the neighbors colour and index of the intersection.
	 *  his length of the list can be 4, 3 (side of the field) or 2 (corner of the field).
	 *  
	 */
	public Map<Integer, Stone> getNeighborMap(int row, int col) {
		Map<Integer, Stone> neighbors = new HashMap<Integer, Stone>();
		if (row > 0)  {
			neighbors.put(index(row - 1, col), getField(row - 1, col)); //top
		}
		if (row < DIM - 1) {
			neighbors.put(index(row + 1, col), getField(row + 1, col)); //down
		}
		if (col > 0) {
			neighbors.put(index(row, col - 1), getField(row, col - 1)); //left
		}
		if (col < DIM - 1) {
			neighbors.put(index(row, col + 1), getField(row, col + 1)); //right
		}
		return neighbors;	
	}
	
	
	/**
	 * Get the Stonegroups aangrenzend van de gelegde steen.
	 * @return
	 */
//	private Stonegroup connectedToSameColour(int row, int col, Stone stoneColour) {
//		//set a stone
//		setField (row, col, stoneColour);
//		Stonegroup connectedGroup;
//		//look if the neighbors have the same colour
//		Map<Integer, Stone> neigh = getNeighborMap(row, col);
//		
//		for (int i = 0; i < neigh.size(); i++) {
//			if (getField(row, col).equals(neigh)) {
//				return connectedGroup = neigh;
//			}
//		}
//		returns connectedGroup;
//	}
	
//
//	private Stonegroup connectedStoneGroup(int row, int col) {
//		List<Stone> stones = getNeighbors1(0,0);
//		Stone stoneIndex = stones.get(1);
//		
//		Stonegroup stoneGroup = new Stonegroup(Stone.b);
//		for (int i = 0; i < stonegroups.size(); i++) {
//			stonegroups.get(i).getStone();
//		}
//		
//		for (Stonegroup group : stonegroups) {
//			group.getStone();
//		}
//		return null;
//	}
//	
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
		//ook de captured lege velden moeten geteld worden.
		return count;
	}
	
	/** 
	 * Misschien is deze methode niet nodig
	 * Returns true if the game has a winner. 
	 * This is the case when one player(with color stones) has the most area. 
	 * @return true if the GO-board has a winner.
	 */
	/*pure*/
	public boolean hasWinner() {
		return isWinner(Stone.w) || isWinner(Stone.b);
	}
	
	/**
	 * The winner with the biggest area wins. 
	 * A player's area consists of all the intersections the player has either occupied or surrounded. 
	 * In case of an equal score there is a draw. 
	 * @return the stone color which has won the game.
	 */
	public boolean isWinner(Stone stone) {
		return false;
	}
	
	/** 
	 * This gives a String representation of this GO-board.
	 */
	public String toString() {
		String board = "   0   1   2   3   4" + "\n";
		for (int i = 0; i < DIM; i++) {
			String row = "" + i + "";
			for (int j = 0; j < DIM; j++) {
				row = row + " " + getField(i, j).toString() + " ";
				// alle velden van 1 rij
			}
			board = board + row + "\n";
		}
		return board;
	}	
	
	Set<Board> previousBoards = new LinkedHashSet();
	//opslaan van de array die het board vormt
	
	public boolean isPreviousBoard(Board nextBoard) {
		for (Board previous : previousBoards) {
			if (nextBoard.equals(previous)) {
				return true;
			}
			// to do bijhouden wat de verschillende borden waren en hier doorheen itereren
			// bij elke beurt copy maken en deze opslaan.
		}
		return false;
	}
	 
} //class
