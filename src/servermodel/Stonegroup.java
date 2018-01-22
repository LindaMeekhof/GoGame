package servermodel;

import java.util.ArrayList;
import java.util.List;

public class Stonegroup {

	private Stone stone;
	private List<Integer> stonegroup;
	//integer refereert naar een intersectie op het bord.
	
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
//	
//	public int getFreeLiberties() {
//		List<Integer> stonegroup = getStonegroup();
//		for (int i = 0; i < stonegroup.size(); i++) {
//			
//		}
//		return 0;
//	}
	
	
}
