package test;

import java.awt.List;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import servermodel.Board;
import servermodel.Stone;
import servermodel.Stonegroup;

public class testingBoard {

	public static void main(String[] args) {
		int size = 3;
		Board board = new Board(size);
		
//		board.setField(1,  2,  Stone.b);
//		board.setField(2, 1, Stone.w);
//		board.setField(1, 1, Stone.b);
		
	//	board.getStoneGroupConnected(1, 2, Stone.b);
		
	//	System.out.println(board.getDeNieuweGroep().getStone());
//		System.out.println(board.getFreeLiberties(board.getDeNieuweGroep()));
		
		Stonegroup stonegroup1 = new Stonegroup(Stone.b);
		
		Stone color = stonegroup1.getStone();
		System.out.println(color);
		
		ArrayList<Integer> intersections = (ArrayList<Integer>) stonegroup1.getStonegroup();
		intersections.add(2);
		intersections.add(4);
		System.out.println(intersections);
		
		//Testing getting neighbors
		ArrayList<Integer> neighbors = (ArrayList<Integer>) board.gettingNeighbors(0, 1);
		System.out.println(neighbors);
		System.out.println(neighbors.size());
		
		//testing getting color
		ArrayList<Stone> neighborColor = (ArrayList<Stone>) board.getNeighborsColor(1, 1);
		System.out.println(neighborColor);
		
		Stonegroup stonegroup2 = new Stonegroup(Stone.b);
		Stonegroup stonegroup3 = new Stonegroup(Stone.b);
		Stonegroup stonegroup4 = new Stonegroup(Stone.b);
		
		stonegroups.add(stonegroup3);
		stonegroups.add(stonegroup2);
		
		stonegroup2.getStonegroup().add(4);
		stonegroup2.getStonegroup().add(6);
		
		for (Stonegroup stone : stonegroups) {
			System.out.println(stone.getStone());
		}
		
		
		//testing searching in list of intersections in stonegroup.
		for (Stonegroup stone : stonegroups) {
			ArrayList<Integer> intersectionList = (ArrayList<Integer>) stone.getStonegroup();
			for (int i = 0; i < intersectionList.size(); i++) {
				int index = intersectionList.get(i);
				System.out.println(index);
			}
		}
		
		//getFreeLiberties
		int liberties = board.getFreeLibertiesGroup(stonegroup2);
		System.out.println(liberties);
		int liberties2 = board.getFreeLibertiesGroup(stonegroup1);
		System.out.println(liberties2);
		System.out.println(liberties2);
		
		
		//testing Freeliberties
		Stonegroup stonegroup5 = new Stonegroup(Stone.b);
		System.out.println(board.getFreeLibertiesGroup(stonegroup5));
		stonegroup5.getStonegroup().add(4);
		board.setField(1, 0, Stone.b);
		board.setField(0, 1, Stone.b);
		System.out.println(board.getFreeLibertiesGroup(stonegroup5));
		
		//testingFreeliberties enkele field
		int lib = board.getFreeLibertiesStone(1, 1);
		System.out.println(lib);
		board.setField(1, 0, Stone.b);
		board.setField(0, 1, Stone.b);
		System.out.println(lib);
	}
	
	private static Set<Stonegroup> stonegroups = new LinkedHashSet<Stonegroup>();

}
