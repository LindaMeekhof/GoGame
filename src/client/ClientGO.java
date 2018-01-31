package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import clientmodel.ComputerPlayer;
import clientmodel.HumanPlayer;
import clientmodel.Player;



public class ClientGO {
	private static final String USAGE
		= "usage: client.ClientGO <name> <address> <port>";

	/** Starts a Client application.
	 * When the argument length does not match
	 */
	public static void main(String[] args) {
	
		//exit if the arguments do not match
		if (args.length != 4) {
			System.out.println(USAGE);
			System.exit(0);
		}

		String name = args[0]; //set name of client, is ook de naam van de speler.
		InetAddress addr = null;
		int port = 0;
		Socket sock = null;
		
		// check args[1] - the IP-adress is unknown.
        try {
            addr = InetAddress.getByName(args[1]);
        } catch (UnknownHostException e) {
            System.out.println(USAGE);
            System.out.println("ERROR: host " + args[1] + " unknown");
            System.exit(0);                
        }
        
        // parse args[2] - the port is not a number
        try {
            port = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.out.println(USAGE);
            System.out.println("ERROR: port " + args[2]
            		           + " is not an integer");
            System.exit(0);
        }
        
        // try to open a Socket to the server
        try {
            sock = new Socket(addr, port);
        } catch (IOException e) {
            System.out.println("ERROR: could not create a socket on " + addr
                    + " and port " + port);
        }
    
 		   
        System.out.println("Your first argument is: " + args[3]);	
   

//        Player player; 
//        if (args[3].equals("-N")) { 
//        	player = new HumanPlayer(name);
//        } else {
//        	player = new ComputerPlayer(name);
//        }
        PlayerController client; 
      
	/**
	 * Creates a PlayerController. This is for two-way communication with the server.
	 */
        try {

        	if (args[3].equals("-N")) { 
        		client = new HumPlayer(name, sock);
        	} else {
        		client = new HumPlayer(name, sock);
    //    		client = new ComputerPlayer(name, sock);
        		// 	PlayerController client = new Computer(name, sock, player);
        	}

        	// 	PlayerController client = new PlayerController(name, sock, player);
        	Thread serverInputHandler = new Thread(client);
        	serverInputHandler.start(); 

        	//TerminalInput
        	while (true) {
        		((HumPlayer) client).readStringConsole("...");
        	}
      
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        
      
	} //main
}

	


