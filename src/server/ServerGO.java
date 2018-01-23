package server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class ServerGO {
	private static final String USEINPUT = "The server needs: " + "<name><port>";

/** Starts a server-application.
 * @throws IOException */
	public static void main(String[] args) throws IOException {
		
		//when the <name> and <port> do not correspond to the expected. Ask for them.
		if (args.length != 2) {
			System.out.println(USEINPUT);
			System.exit(0);
		}
		
		String name = args[0]; //sets the name of the server, not necessary
		int port = 0;
		
		//pars args[1] which is the portnumber. Check if the argument is a number.
		try {
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) { //if not a number
			System.out.println(USEINPUT);
			System.out.println("ERROR: port " + args[1]
					+ " is not an integer");
			System.exit(0);  //normal termination.
		}

		
		
		ServerGO server = new ServerGO(Integer.parseInt(args[1]), name);
		System.out.println("Server is listening to clients who want to connect");
		server.resetServerclient();
		server.acceptClient();
		
	}
	
	private int port;
	private ArrayList<Serverclient> threads;
	private ArrayList<Gamecontroller> gamethreads;
	
	/** Constructs a new Server object. */
	public ServerGO(int portArg, String serverName) {
		this.port = portArg;
		this.serverName = serverName;
		this.threads = new ArrayList<Serverclient>();
		this.gamethreads = new ArrayList<Gamecontroller>(); //kan nog niks toevoegen en weghalen.
	}
	
	
	//Create a ServerClient threads which handles the communication with the client.
	//in new thread omdat de server moet blijven reageren op andere spelers die binnenkomen.
	//bij twee spelers moet er ook een Gamecontroller thread worden aangemaakt.
	public void acceptClient() { 
		try (ServerSocket ssock = new ServerSocket(port);) {
			
			while (true) { 

				Socket sock = ssock.accept();
				System.out.println("New client connected!"); 
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				out.write("hoihoi");
				out.newLine();
				out.flush();
				Serverclient client = new Serverclient(this, sock);
				client.start();
				addServerclient(client);
				
				//steeds meer threads
				if (threads.size() == 2) {
					Thread t1 = new Thread(new Gamecontroller(this, threads.get(0), threads.get(1)));
					t1.start();
				}
			} 
		} catch (IOException e) {
			System.out.println("Error accepting clients");
		}  

	}
		
 
	
	public void addServerclient(Serverclient client) {
		threads.add(client);
	}
	
	// wanneer de client niet meer reageert
	public void removeServerclient(Serverclient client) {
		threads.remove(client);
	}
	
	public void resetServerclient() {
//		for (int i = 0; i < threads.size(); i++) {
//			removeServerclient(threads.get(i));
//		}
		threads.removeAll(threads);
	}
	
	private String serverName; 
	
	public String getName() {
		return serverName;
	}
	
}
