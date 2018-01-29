package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import general.Protocol;
import general.Protocol.General;
import servermodel.Stone;

public class ServerClient extends Thread {
	
	private BufferedReader in;
	private BufferedWriter out;
	private ServerGO server;
	private Socket sock;
	private Gamecontroller gamecontroller;
	private Stone stone;
	private String clientTag;
	private String namePlayer;
	private String stoneColorString;

	

	private Boolean isTurn;

	
	
	/**
	 * Constructs a Serverclient.
	 * @param playername
	 * @param sockArg
	 * @throws IOException
	 */
	public ServerClient(ServerGO serverArgument, Socket sockArgument, int identityNumber) throws IOException {
		this.clientTag = Integer.toString(identityNumber);
		this.server = serverArgument;
		this.sock = sockArgument; 
		in = new BufferedReader(new InputStreamReader(sockArgument.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(sockArgument.getOutputStream()));
		
		isTurn = false;
	}

	private String line;
	
	// Getters and setters --------------------------------------------------
	
	public String getNamePlayer() {
		return namePlayer;
	}


	public void setNamePlayer(String namePlayer) {
		this.namePlayer = namePlayer;
	}
	
	public String getClientTag() {
		return clientTag;
	}

	
//	returns the line which comes from the player.
	public String inputPlayer() {
		return line;
	}
	
	
	//nog niet gebruikt
	public void setTurn(boolean isTurn) {
		this.isTurn = isTurn;
	}
	
	public void setGamecontroller(Gamecontroller gamecontroller) {
		this.gamecontroller = gamecontroller;
	}
	
	public void setStone(String colour) {
		if (colour.equalsIgnoreCase("BLACK")) {
			stone = Stone.b;
		} else {
			stone = Stone.w;
		}
	}
	
	public Stone getStone() {
		return stone;
	}
	
	public String getStoneColorString() {
		return stoneColorString;
	}


	public void setStoneColorString(String stoneColorString) {
		this.stoneColorString = stoneColorString;
	}
//	public String getStoneString() {
//		if (stone.equals(Stone.b)) {
//			return "BLACK";
//		}
//		if (stone.equals(Stone.w)) {
//			return "WHITE";
//		}
//		return null;
//	}
	
	// InputStream client ----------------------------------------------------
	/**
	 * Handle the clientInput which comes through the Socket.
	 */
	@Override
	public void run() {
		//String line;
		try { 
			// String line;
			while (in.readLine() != null) {
				line = in.readLine();
				//System.out.println(line);
				gamecontroller.notify(clientTag + General.DELIMITER1 + line);
			} 
			shutdown();
		} catch (IOException e) {
			System.out.println("No line was read"); 
		}
	}
	

	// Outputstream client -----------------------------------------------------
	/** 
	 * Send gameData to the clients. 
	 */
	public void sentGameData(String message) {
		try {
			out.newLine();
			out.write(message);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			System.out.println("Error with sending data to the client");
			//remove this Serverclient of the list from Serverclients in the server.
			//server.removeServerclient(this);
		}
	}

	// Methods Serverclient -------------------------------------------------------
	
	private void shutdown() {
		server.removeServerclient(this);
		System.out.println("removed this serverclient");
	}
	
	public boolean isTurn() {
		return isTurn;
	}
	
}
