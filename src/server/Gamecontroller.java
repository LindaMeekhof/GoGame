package server;

import general.Protocol;
import servermodel.Board;

public class Gamecontroller extends Protocol implements Runnable {


	private ServerGO serverGO;
	private Serverclient[] players;
	private int currentPlayer;
	private int amountPlayers = 2;
	private Board board;
	
	/** 
	 * Creates a Gamecontroller with two Serverclients for the in and output control.
	 * @param player1
	 * @param player2
	 */
	public Gamecontroller(ServerGO serverGO, Serverclient player1, Serverclient player2) {
		players = new Serverclient[2];
//		this.player1 = player1;
//		this.player2 = player2;
		this.serverGO = serverGO;
		players[0] = player1;
		players[1] = player2;
		this.board = new Board();
	}

	
	/** Handle the input form both users and take action.
	 * 
	 */
	@Override
	public void run() {
		System.out.println("The gamecontroller is started");
		
		// version command
		players[0].sentGameData(Server.NAME + General.DELIMITER1 + serverGO.getName() + Server.VERSION + Server.VERSIONNO + Server.EXTENSIONS );
		players[1].sentGameData(Server.NAME + General.DELIMITER1 + serverGO.getName() + Server.VERSION + Server.VERSIONNO + Server.EXTENSIONS );
		
		System.out.println("waiting for input from player 0");
		String line = players[0].inputPlayer();
		System.out.println("received");

		//split the line for the right arguments.
		System.out.println(line);
		String[] words = line.split(General.DELIMITER1);
				
		if (words.length == 12 && words[0].equals(Client.NAME) 
				&& words[2].equals(Client.VERSION) && words[3].equals(Protocol.VERSION_NO)) {
			System.out.println("The Server received startup settings: name, version etc");
			//Ask for settings
			players[0].sentGameData(Server.START + General.DELIMITER1 + this.amountPlayers);
		}
		if (words.length == 3 && words[0].equals(Client.SETTINGS)) {
			System.out.println("settings ontvangen");
			//make sure the second is an int
			setupGame("line");
			// return the startwaarden naar beide spelers
			players[0].sentGameData(Server.START + General.DELIMITER1 + amountPlayers + 
					General.DELIMITER1 + words[0] + General.DELIMITER1 + words[1]);
			players[1].sentGameData(Server.START + General.DELIMITER1 + amountPlayers + 
					General.DELIMITER1 + words[0] + General.DELIMITER1 + words[1]);
		}
		
		//Playing the game
		
	}
	
	
	public void setupGame(String settings) {
		String[] setting = settings.split(General.DELIMITER1);
		int size = Integer.parseInt(setting[1]);
		board.setDIM(size);
		//player1 colour
	//	String colour = settings[0]; 
	}
	

}
