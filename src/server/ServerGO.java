package server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class ServerGO extends Thread {
	private static final String USEINPUT = "The server needs: " + "<name><port>";

/** Starts a server-application.
 * @throws IOException */
	public static void main(String[] args) throws IOException {
		
		/**
		 * When the <name> and <port> do not correspond to the expected. Ask for them.
		 */
		if (args.length != 2) {
			System.out.println(USEINPUT);
			System.exit(0);
		}
		
		String name = args[0];
		int port = 0;
		
		/**
		 * pars args[1] which is the portnumber. Check if the argument is a number.
		 */
		try {
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.out.println(USEINPUT);
			System.out.println("ERROR: port " + args[1]
					+ " is not an integer");
			System.exit(0);  //normal termination.
		}
	
		ServerGO server = new ServerGO(Integer.parseInt(args[1]), name);
		System.out.println("Server is listening to clients who want to connect");
		server.start();
		
		
	}
	
	private int clientnumber;
	private int port;
	private String serverName;
	private ArrayList<ServerClient> availableServerClient;
	private ArrayList<ServerClient> playingServerClient;
//	private ArrayList<Gamecontroller> gamethreads;
	
	/** Constructs a new Server object. */
	public ServerGO(int portArg, String serverName) {
		this.port = portArg;
		this.serverName = "LindaServer";
		this.availableServerClient = new ArrayList<ServerClient>();
		this.playingServerClient = new ArrayList<ServerClient>();
//		this.gamethreads = new ArrayList<Gamecontroller>(); 
	}
	
	/**
	 * Create a ServerClient threads which handles the communication with the client.
	 * in new thread omdat de server moet blijven reageren op andere spelers die binnenkomen.
	 * bij twee spelers moet er ook een Gamecontroller thread worden aangemaakt.
	 */
	public void run() { 
		try (ServerSocket ssock = new ServerSocket(port);) {
			int clientIdentity = 0;
			while (true) { 
				
				Socket sock = ssock.accept();
				
				print("New client connected!"); 
				print("clientnumber " + (clientIdentity++) + "is connected");
				ServerClient client = new ServerClient(this, sock, clientIdentity);
				client.start();
				addServerclient(client);
						
				if (availableServerClient.size() == 2) {
					Thread t1 = new Thread(new Gamecontroller(this, availableServerClient.get(0), 
							availableServerClient.get(1)));
					t1.start();
//					Thread t1 = new Thread(new Gamecontroller(this, availableServerClient.get(0), 
//							availableServerClient.get(1)));
//					t1.start();
					
					this.playingServerClient.add(availableServerClient.get(0));
					this.playingServerClient.add(availableServerClient.get(1));
					this.availableServerClient.remove(0);
					this.availableServerClient.remove(0);

				}
				
			} 
		} catch (IOException e) {
			System.out.println("Error accepting clients");
		}  

	}
		
 
// Getters and setters ---------------------------------------------------	
	/**
	 * This method returns the serverName.
	 * @return
	 */
	public String getServerName() {
		return serverName;
	}


//	public void setServerName(String serverName) {
//		this.serverName = serverName;
//	}

	
	
	/**
	 * Adds a ServerClient to the list availableServerClient.
	 * @param client
	 */
	public void addServerclient(ServerClient client) {
		availableServerClient.add(client);
	}
	
	/**
	 * Removes the ServerClient from the list of availableClients.
	 * @param client
	 */
	public  void removeServerclient(ServerClient client) {
		availableServerClient.remove(client);
	}
	
	/**
	 * Print out a message.
	 * @param message
	 */
	public void print(String message) { 
		System.out.println(message);
	}


	

	
}
