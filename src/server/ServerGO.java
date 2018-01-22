package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerGO {
	private static final String USEINPUT = "The server needs: " + "<name><port>";

/** Starts a server-application.*/
	public static void main(String[] args) {
		
		//when the <name> and <port> do not correspond to the expected. Ask for them.
		if (args.length != 2) {
			System.out.println(USEINPUT);
			System.exit(0);
		}
		
		String name = args[0]; //sets the name of the server
		int port = 0;
		ServerSocket mySocket = null;
		
		//pars args[1] which is the portnumber. Check if the argument is a number.
		try {
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) { //if not a number
			System.out.println(USEINPUT);
			System.out.println("ERROR: port " + args[1]
					+ " is not an integer");
			System.exit(0);  //normal termination.
		}
		
		// Try to set up a ServerSocket
		try {
			mySocket = new ServerSocket(port);
		} catch (IOException e) { //if not a number
			System.out.println(USEINPUT);
			System.out.println("ERROR: port " + args[1]
					+ " is not an integer");
			System.exit(0);  //normal termination.
		}

		System.out.println("the ServerSocket is started and listening");
		
		//Create a ServerClient which handles the communication with the client.
		//in new thread omdat de server moet blijven reageren op andere spelers die binnenkomen.
		try {
			Socket incoming = mySocket.accept();
			
		} catch (IOException e) {
			//TO DO
		}
	}
	
}
