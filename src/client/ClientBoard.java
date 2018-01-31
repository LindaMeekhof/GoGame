package client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nedap.go.gui.GoGUIIntegrator;

import clientView.TUIclient;

/**
 * This is the board for GO.
 * Final exercise module 1 Nedap University.
 * 
 * @author Linda.Meekhof
 *
 */

public class ClientBoard {
	
	private int dimension; 
	private GoGUIIntegrator gogui;
	private List<Stonegroup> islandList = new ArrayList<Stonegroup>();
	private ArrayList<Integer> emptyFieldList = new ArrayList<Integer>();
	private ArrayList<Stonegroup> stonegroups = new ArrayList<Stonegroup>();
	private ArrayList<String> previousBoards = new ArrayList<String>();
	/**
	 * The DIM by DIM fields of the board GO. The numbering of the fields are ......
	 * 
	 */
	//@ private invariant fields.length == DIM*DIM;
    /*@ invariant (\forall int i; 0 <= i & i < DIM*DIM;
        getField(i) == Stone.__ || getField(i) == Stone.w || getField(i) == Stone.b); */
	private Stone[] fields;
	private TUIclient gotui;

// Constructor and boardInit -----------------------------------------------------------------

	/**
	 * The constructor creates an empty playing board
	 */
	/*
	 * (\forall int i; 0 <+ i & i < DIM*DIM;
	 * this.getField(i) == Stone.__);
	 */

	public ClientBoard() {

	}
	
	public void boardInit(int dim) {
		fields = new Stone[dim * dim];
		
		// All fields are initially empty
		for (int i = 0; i < dim * dim; i++) {
			fields[i] = Stone.__;

		}
		gogui = new GoGUIIntegrator(false, true, dim);
		gogui.startGUI();
		gogui.setBoardSize(9);
		this.dimension = dim;
		gotui = new TUIclient(this);
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
	public ClientBoard deepCopy() {
		ClientBoard copyboard = new ClientBoard();
		
		copyboard.fields = new Stone[dimension * dimension];
		for (int i = 0; i < dimension * dimension; i++) {
			copyboard.fields[i] = this.fields[i];
		}
		return copyboard;
	}

	// Getters and setters -----------------------------------------------------------------
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
	 * This method sets the field, indicated with row and col, to the given stone parameter.
	 * @param row is the field's row.
	 * @param col is the field's  column.
	 * @param stone is the given Stone mark to be placed.
	 * @throws InvalidCoordinateException 
	 */
	//@ requires this.isField(i);
	//@ ensures this.fields == stone;
	public void setFieldWithoutGUI(int row, int col, Stone stone) {
		this.fields[index(row, col)] = stone;
	}
	
	/**
	 * Getting the list with all the Stonegroups.
	 * @return
	 */
	public List<Stonegroup> getAllStonegroups() {
		return stonegroups;
	}
	
	/**
	 * Get the emptyFieldList.
	 * @return
	 */
	public ArrayList<Integer> getEmptyFieldList() {
		return emptyFieldList;
	}

	/**
	 * Set the emptyFieldlist.
	 * @param emptyFieldList
	 */
	public void setEmptyFieldList(ArrayList<Integer> emptyFieldList) {
		this.emptyFieldList = emptyFieldList;
	}

	
// Other methods ----------------------------------------------------------------------	
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

    /**
     * Converting the index to X-coordinate and Y-coordinate.
     * @param index
     * @return
     */
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
     * Checks if the row and column input is a field on the board or that it not.
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
    
    /**
     * Checks if the field is empty.
     * @param i
     * @return true if it is an empty field.
     */
    /*pure*/
    //@ requires this.isField(i)
    //@ ensures \result == (this.getField(i) == Stone.__);
    public boolean isEmptyField(int i) { 
	 	return getFields(i) == Stone.__;
    }
    
    /**
     * Checks if the field is empty.
     * @param row
     * @param col
     * @return true if it is an empty field.
     */
    /*pure*/
    //@ requires this.isField(i)
    //@ ensures \result == (this.getField(row, col) == Stone.__);
	public boolean isEmptyField(int row, int col) {
		return getField(row, col) == Stone.__;
	}
	
	/**
	 * Checks if the board is full. In this case not necessary
	 * because of the implemented rules there is an infinite loop.
	 * @return
	 */
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
	
	/**
	 * Resets the board by setting all the fields to Stone.__ (empty field).
	 */
	//@ ensures (\forall int i; 0 <= i & i < DIM*DIM; this.field == Stone.__);
	public void reset() {
		for (int i = 0; i < dimension * dimension; i++) {
			fields[i] = Stone.__;
			gogui.clearBoard();
			gotui.showBoard();
		}
	}
	
	/** 
	 * Checks if the input coordinates are a field on the board and if this field is empty.
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isValidMove(int row, int col, Stone stone) {
		return  isField(row, col) && isEmptyField(row, col);
	}
		
	/**
	 * When a stone or Stonegroup has one liberty, then the group is not captured.
	 * The last created Stonegroup should not be evaluated, only after the removing 
	 * the captured stones of the other player. Then evaluate the last created group of chains.
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
	
	
	/**
	 * Getting neighbor integers.
	 */
	public ArrayList<Integer> gettingNeighbors(int row, int col) {
		ArrayList<Integer> neighbors = new ArrayList<Integer>();
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
				//all intersections need to be changed in empty
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

	/**
	 * Removes the stones of the board. 
	 * This are the stones that together are the Stonegroup.
	 * @param stonegroup
	 */
	public void removeStones(Stonegroup stonegroup) {
		List<Integer> listOfIndex = stonegroup.getStonegroup();
		for (int i = 0; i < listOfIndex.size(); i++) {
			int indexStone = listOfIndex.get(i);
			setFields(indexStone, Stone.__);
			int[] coordinates =  indexToRowCol(i);
			int row = coordinates[0];
			int col = coordinates[1];
			gogui.removeStone(col, row);
		}
	}
	
	/**
	 * Updates the board. First evaluating all the stonegroups.
	 * Remove the stones that are captured by setting the last stone.
	 * Then evaluate the last created stonegroup for capturing.
	 * @param row
	 * @param col
	 * @param stoneColor
	 */
	public void updateBoard(int row, int col, Stone stoneColor) {
		// step 1 create and update chaingroup with the last move
		createNewChain(row, col, stoneColor);
		removeStonesCaptured();
		
		// step 2 the last created stone
		if (!groupHasLiberties(stonegroups.get(stonegroups.size() - 1))) {
			removeStones(stonegroups.get(stonegroups.size() - 1));
			gogui.removeStone(col, row);
		}
		
		// step 3 update gotui and add this board representation to previousBoards.
		previousBoards.add(toString());
		gotui.showBoard();
	}


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
	

	/**
	 * Checks if the board of the next game is the same as a previous board situation.
	 * @param row
	 * @param col
	 * @param myStone
	 * @return
	 */
	public boolean isPreviousBoard(int row, int col, Stone myStone) {
		ClientBoard boardCopy = deepCopy();
		
		boardCopy.setFieldWithoutGUI(row, col, myStone);
		String nextBoard = boardCopy.toString();
		if (previousBoards.contains(nextBoard)) {
			return true;
		} else {
			return false;
		}
	}

// Area scoring ------------------------------------------------------------------------------------
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

	/**
	 * Calculates the point for the captured areas.
	 * @param color
	 * @return
	 */
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

		ArrayList<Integer> neighborsneighborsIslandStone = gettingNeighbors(row, col);

		//create a new stonegroup with  the neighboring stones and the new emptystonefield
		Stonegroup emptyStonegroup = new Stonegroup(emptyColor);
		
		//set van groepen die grenzen aan het lege veld
		Set<Stonegroup> neighborGroup = new HashSet<>();

		if (!islandList.isEmpty()) {
			for (Stonegroup stoneChain : islandList) {
				
				if (stoneChain.getStone().equals(emptyColor)) { //dubbele check
					// intereer over de lijst van intersections
					for (int i = 0; i < stoneChain.getStonegroup().size(); i++) {
						for (int intersection = 0; intersection 
								< neighborsneighborsIslandStone.size(); intersection++) {
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
}