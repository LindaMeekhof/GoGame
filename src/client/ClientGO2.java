package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;



public class ClientGO2 {
	private static final String USAGE
		= "usage: client.ClientGO <name> <address> <port>";

	/** Starts a Client application.
	 * When the argument length does not match
	 */
	public static void main(String[] args) {
		//exit if the arguments do not match
		if (args.length != 3) {
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
	
        System.out.println("the socked is open");
	
        //communicatieObject aanmaken. In dit geval een ClientController
        // create Peer object and start the two-way communication
        try {
            Clientcontroller client = new Clientcontroller(name, sock);
           
            Thread serverInputHandler = new Thread(client);
            serverInputHandler.start(); //run method regelt de inputstream die komt vanaf de server
          
            client.readStringConsole("input");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    
      
	} //main
}

	


