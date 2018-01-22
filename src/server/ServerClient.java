package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import general.Protocol;

public class ServerClient extends Protocol implements Runnable  {
	//name of the player
	private String name;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	
	//extends protocol??
	public ServerClient(String playername, Socket sockArg) throws IOException {
		name = playername;
		sock = sockArg; 
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
	}

	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Handle the clientInput which comes through the Socket.
	 */
	public void readClientInput() {
		String line;
		try { 
			while (in.readLine() != null) {
				line = in.readLine();
				System.out.println("de Serverclient heeft een line ontvangen");
				//split the line for the right arguments.
				String[] words = line.split(General.DELIMITER1);
				if (words.length == 12 && words[0].equals(Client.NAME) 
						&& words[2].equals(Client.VERSION) && words[3].equals(Protocol.VERSION_NO)) {
					System.out.println("The Server received startup settings: name, version etc");
					//send name door naar game
				}
				
			} 
		
		} catch (IOException e) {
			System.out.println("geen line gelezen"); //vragen om input
			e.printStackTrace();
		}

	}
	
	/** 
	 * Send gameData to the clients. Andere thread game.
	 */
	public void sentGameData() {
		
	}

	
}
