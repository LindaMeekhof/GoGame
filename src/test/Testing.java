package test;

import servermodel.Board;
import servermodel.Stone;

public class Testing {

	public static void main(String[] args) {
		Board board = new Board();
		System.out.println("test");
		System.out.println("\ncurrent game situation: \n\n");
        System.out.println(board.toString()

        		+ "\n");
        board.setField(0, 0, Stone.w);
        System.out.println("next situation");
        System.out.println(board.toString());
	    
        board.reset();
        System.out.println("next situation");
        System.out.println(board.toString());
	}
}
