package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

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

//	private Boolean isTurn;
//	private String line;
	
	/**
	 * Constructs a Serverclient.
	 * @param playername
	 * @param sockArg
	 * @throws IOException
	 */
	public ServerClient(ServerGO serverArgument, Socket sockArgument, int identityNumber) 
			throws IOException {
		this.clientTag = Integer.toString(identityNumber);
		this.server = serverArgument;
		this.sock = sockArgument; 
		in = new BufferedReader(new InputStreamReader(sockArgument.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(sockArgument.getOutputStream()));
	}

	// Getters and setters --------------------------------------------------
	
	/**
	 * Get the name of the player. 
	 * @return
	 */
	public String getNamePlayer() {
		return namePlayer;
	}

	/**
	 * Set the name of the player.This comes from the client.
	 * @param namePlayer
	 */
	public void setNamePlayer(String namePlayer) {
		this.namePlayer = namePlayer;
	}
	
	/**
	 * Get the clientTag. The clientTag is used for separating the messages.
	 * @return
	 */
	public String getClientTag() {
		return clientTag;
	}
	
	/**
	 * set the gamGamecontroller.
	 * @param gamecontroller
	 */
	public void setGamecontroller(Gamecontroller gamecontroller) {
		this.gamecontroller = gamecontroller;
	}	

	/**
	 * Get the stone (Stone.w or stone.b) of this ServerClient.
	 * @return
	 */
	public Stone getStone() {
		return stone;
	}
	
	/**
	 * Get the stone (Stone.w or stone.b) of this ServerClient.
	 * @param colour
	 */
	public void setStone(String colour) {
		if (colour.equalsIgnoreCase("BLACK")) {
			stone = Stone.b;
		} else {
			stone = Stone.w;
		}
	}
	
	/**
	 * Get stone in String format: WHITE or BLACK.
	 * @return
	 */
	public String getStoneColorString() {
		return stoneColorString;
	}

	/**
	 * Set stone in String format for this ServerClient.
	 * @param stoneColorString
	 */
	public void setStoneColorString(String stoneColorString) {
		this.stoneColorString = stoneColorString;
	}
	
	/**
	 * Handle the clientInput which comes through the Socket.
	 */
	@Override
	public void run() {
		//String line;
		try { 
			String line;
			while ((line = in.readLine()) != null) {
				String[] words = line.split("\\" + General.DELIMITER1);
				if (words[0].equals("EXIT")) {
					System.out.println("kdfjlae");
					shutdown(); 
				} else {			
					gamecontroller.notify(clientTag + General.DELIMITER1 + line);		
				}
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
		//	out.newLine();
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
	
	public void shutdown() {
		server.removeServerclient(this);
		// server stop.
		System.out.println("removed this serverclient");
	}
	
}
