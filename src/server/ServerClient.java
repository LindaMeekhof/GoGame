package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import general.Protocol;

public class Serverclient extends Thread {
	
	private BufferedReader in;
	private BufferedWriter out;
	private ServerGO server;
	private Socket sock;

	/**
	 * Constructs a Serverclient.
	 * @param playername
	 * @param sockArg
	 * @throws IOException
	 */
	public Serverclient(ServerGO serverArgument, Socket sockArgument) throws IOException {
		this.server = serverArgument;
		this.sock = sockArgument; 
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
	}

	private String line;
	/**
	 * Handle the clientInput which comes through the Socket.
	 */
	@Override
	public void run() {
		//String line;
		try { 
			while (in.readLine() != null) {
				line = in.readLine();
				System.out.println(line);
			} 
		} catch (IOException e) {
			System.out.println("No line was read"); 
			//sentGameData("Graag valid input");//vragen om input
		}
	}
	
	//returns the line which comes from the player.
	public String inputPlayer() {
		return line;
	}
	
	/** 
	 * Send gameData to the clients. 
	 */
	public void sentGameData(String message) {
		try {
			out.write(message);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			System.out.println("Error with sending data to the client");
			//remove this Serverclient of the list from Serverclients in the server.
			server.removeServerclient(this);
		}
	}

	
	
}
