package server;

import java.util.ArrayList;

import general.Protocol;
import servermodel.Board;

public class Gamecontroller extends Protocol implements Runnable {


	private ServerGO serverGO;
	private Serverclient[] players;
	private int currentPlayer;
	private int boardSize;
	private Board board;
	private static final int NumberPlayers = 2;
	private int amountPlayers;
	
	/** 
	 * Creates a Gamecontroller with two Serverclients for the in and output control.
	 * @param player1
	 * @param player2
	 */
	public Gamecontroller(ServerGO serverGO, Serverclient player1, Serverclient player2) {
		players = new Serverclient[NumberPlayers];
//		this.player1 = player1;
//		this.player2 = player2;
		this.serverGO = serverGO;
		players[0] = player1;
		players[1] = player2;
		this.board = new Board();
		inputUser = new ArrayList<String>();
	}

	
	/** Handle the input form both users and take action.
	 * 
	 */
	@Override
	public void run() {
		players[0].setGamecontroller(this);
		players[1].setGamecontroller(this);
		
		
		
		System.out.println("The gamecontroller is started + 1 ");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// version command
	
//		players[0].sentGameData("test1");
//		players[1].sentGameData("testing");
//		players[1].sentGameData("testing2");
//		players[1].sentGameData("testing3");
		
		//sending information about name versionnumber and extenions.
		for (int i = 0; i < players.length; i++) {
			players[i].sentGameData(Server.NAME + "$" + serverGO.getName() + 
					General.DELIMITER1 + Server.VERSION + General.DELIMITER1 + Server.VERSIONNO + 
					General.DELIMITER1 + Server.EXTENSIONS + 0 + General.DELIMITER1 + 0 + 
					General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + 
					General.DELIMITER1 + 0 + General.DELIMITER1 + 0);
		}
 
		//sending START en vragen om de gegevens kleur en bordgrootte  
		for (int i = 0; i < players.length; i++) {
			players[i].sentGameData(Server.START + General.DELIMITER1 + board.getDIM() + 
					General.DELIMITER1 + "players[1].getStoneString()" + General.DELIMITER1
					+ boardSize + General.DELIMITER1 + players[0].getName() + "players[1].getName()");
		}
		
		//sending TURN naar beide spelers.
		for (int i = 0; i < players.length; i++) {
			players[i].sentGameData(Server.TURN + General.DELIMITER1 + "speler1" 
					+ General.DELIMITER1 + "rij" + General.DELIMITER2 + 
					"col" + General.DELIMITER1 + "speler2");
		}
		
		//sending TURN bij de eerste keer
		for (int i = 0; i < players.length; i++) {
			players[i].sentGameData(Server.TURN + General.DELIMITER1 
					+ players[0].getName() + General.DELIMITER1 + 
					Server.FIRST + General.DELIMITER1 + "speler1");
		}
		
		//sending ENDGAME het spel is gestopt om welke reden dan ook. FINISHED, ABORTED, TIMEOUT.
		for (int i = 0; i < players.length; i++) {
			players[i].sentGameData(Server.ENDGAME + General.DELIMITER1 + "reden" +  
					General.DELIMITER1 + "winnende speler" + General.DELIMITER1 + "score" 
					+ General.DELIMITER1 + "verliezende speler" + General.DELIMITER1 + "score");
		}
		
		
		
		System.out.println("waiting for input from player 0");
		
		
		boolean starting = true;
		while (starting) {
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (!inputUser.isEmpty()) {

			System.out.println("De gamecontroller heeft input ontvangen");
				System.out.println("waarom blokhaken");
				System.out.println(inputUser); //aha hij geeft de array weer
				System.out.println(inputUser.get(0));
				//String[] words = inputUser.get(0).split("//" + General.DELIMITER1);
				inputUser.remove(0);
				//getting name, version and extensions. Wanneer beide clients data hebben opgestuurd
				
			
//				if (words.length == 12 && words[0].equals(Client.NAME)) {
//					System.out.println(inputUser);
//
//					inputUser.remove(0);
//				}
//				//Starting game when settings are send back.
//				if (words.length == 2 && words[0].equals(Client.SETTINGS)) {
//					System.out.println(inputUser.get(0));
//					inputUser.remove(0);
//
//				}

			}
		}
	 
		//System.out.println("waiting for input from player 0");

		
	}
	
	private ArrayList<String> inputUser;
	
	
	public void setupGame(String settings) {
		String[] setting = settings.split(General.DELIMITER1);
		int size = Integer.parseInt(setting[1]);
		board.setDIM(size);
		//player1 colour
	//	String colour = settings[0]; 
	}
	
	public void notify(String line) {
		//System.out.println("notify, line of client");
		//System.out.println(line);
		//System.out.println(inputUser.isEmpty());
		inputUser.add(line);
	}
	


}
