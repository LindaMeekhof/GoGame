package serverview;


import com.nedap.go.gui.InvalidCoordinateException;

import server.Gamecontroller;


public class TUIserver {

	private Object board;
	private Gamecontroller controller;

	public TUIserver(Gamecontroller gamecontroller) {
		this.controller = gamecontroller;
	}
	
	
	private void update() {
        System.out.println("\ncurrent game situation: \n\n" + controller.toString()
                + "\n");
    }



	public void setBoardSize(int size) throws InvalidCoordinateException {
		controller.getBoard();
		
	}



	public void addStone(int x, int y, boolean white) throws InvalidCoordinateException {
		// TODO Auto-generated method stub
		
	}



	public void removeStone(int x, int y) throws InvalidCoordinateException {
		// TODO Auto-generated method stub
		
	}



	public void addAreaIndicator(int x, int y, boolean white) throws InvalidCoordinateException {
		// TODO Auto-generated method stub
		
	}



	public void addHintIndicator(int x, int y) throws InvalidCoordinateException {
		// TODO Auto-generated method stub
		
	}



	public void removeHintIdicator() {
		// TODO Auto-generated method stub
		
	}



	public void clearBoard() {
	
		
	}


	
	
}
