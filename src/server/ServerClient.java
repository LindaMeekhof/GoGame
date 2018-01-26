package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import general.Protocol;
import servermodel.Stone;

public class ServerClient extends Thread {
	
	private BufferedReader in;
	private BufferedWriter out;
	private ServerGO server;
	private Socket sock;
	private Gamecontroller gamecontroller;
	private Stone stone;
	private int identiteit;
	private boolean isTurn;
	
	/**
	 * Constructs a Serverclient.
	 * @param playername
	 * @param sockArg
	 * @throws IOException
	 */
	public ServerClient(ServerGO serverArgument, Socket sockArgument, int identiteit) throws IOException {
		this.identiteit = identiteit;
		this.server = serverArgument;
		this.sock = sockArgument; 
		in = new BufferedReader(new InputStreamReader(sockArgument.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(sockArgument.getOutputStream()));
		
		isTurn = false;
	}

	private String line;
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
				gamecontroller.notify(line, identiteit);
			} 
			shutdown();
		} catch (IOException e) {
			System.out.println("No line was read"); 
		}
	}
	
//	returns the line which comes from the player.
	public String inputPlayer() {
		return line;
	}
	
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

	private void shutdown() {
		server.removeServerclient(this);
		System.out.println("removed this serverclient");
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
	
	public String getStoneString() {
		if (stone.equals(Stone.b)) {
			return "BLACK";
		}
		if (stone.equals(Stone.w)) {
			return "WHITE";
		}
		return null;
	}

	public boolean isTurn() {
		return isTurn;
	}

	public void setTurn(boolean isTurn) {
		this.isTurn = isTurn;
	}
	
//	public void isTurnSwitch() {
//		if (isTurn == true) {
//			isTurn = false;
//		} else {
//			isTurn = true;
//		}
//	}
	
	
}
