package servermodel;

import java.util.ArrayList;
import java.util.List;

public class Stonegroup {
	
	private Stone stone;
	private List<Integer> stonegroup;
	
	/**
	 * Constructor which makes a stonegroup.
	 * @param stone
	 */
	public Stonegroup(Stone stone) {
		this.stone = stone;
		this.stonegroup = new ArrayList<Integer>();
	}

	/**
	 * This method returns the stone color of the group.
	 */
	public Stone getStone() {
		return stone;
	}

	/**
	 * This method returns the intersections which form the group of stones.
	 * @return
	 */
	public List<Integer> getStonegroup() {
		return stonegroup;
	}	
	
	
}
