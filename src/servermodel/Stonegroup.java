package servermodel;

import java.util.ArrayList;
import java.util.List;

public class Stonegroup {
	
	private Stone stone;
	private List<Integer> stonegroup;
	//integer refereert naar een intersectie op het bord.
	private int dimensionBoard; //initieeren
	
//	public Stonegroup(Stone stone, List<Integer> stonegroup) {
//		this.stone = stone;
//		this.stonegroup = stonegroup;
//	}
	
	public Stonegroup(Stone stone) {
		this.stone = stone;
		this.stonegroup = new ArrayList<Integer>();
	}

	public Stone getStone() {
		return stone;
	}

	public List<Integer> getStonegroup() {
		return stonegroup;
	}	
	
	

//	public Boolean groupHasLiberties() {
//		for (int i = 0; i < stonegroup.size(); i++) {
//			int intersection = stonegroup.get(i);
//			int[] coordinates = indexToRowCol(intersection);
//			int row = coordinates[0];
//			int col = coordinates[1];
//			if (row > 0)  {
//				if (getFields(index(row - 1, col)).equals(Stone.__)) { //top
//					return true;
//				}
//			} 
//			if (row < dimension) {
//				if (getFields(index(row + 1, col)).equals(Stone.__)) { //down
//					return true;
//				}
//			}
//			if (col > 0) {
//				if (getFields(index(row, col - 1)).equals(Stone.__)) {//left
//					return true;
//				}
//			}
//			if (col < dimension) {
//				if (getFields(index(row, col + 1)).equals(Stone.__)) {//right
//					return true;
//				}
//			}
//		}
//		return false;
//	}
	
	public int[] indexToRowCol(int index) {
		int[] coordinates = new int[2];
		int row = index / dimensionBoard;
		coordinates[0] = row;
		int col = index % dimensionBoard;
		coordinates[1] = col;
		return coordinates;
	    }
	
}
