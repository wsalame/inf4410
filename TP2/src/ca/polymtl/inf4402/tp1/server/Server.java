package ca.polymtl.inf4402.tp1.server;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ca.polymtl.inf4402.tp1.shared.ServerInterface;
import ca.polymtl.inf4402.tp1.shared.Utils;

public class Server implements ServerInterface {

	public static Map<Integer, Integer> servers = new HashMap<Integer, Integer>();
	static{
		servers.put(5001, 5002);
		servers.put(5005, 5006);
		servers.put(5008, 5009);
		
	}
	public static void main(String[] args) {
		Server server = new Server();
		server.run();
	}

	public Server() {
		super();
	}

	private void run() {
		for(Entry<Integer, Integer> entries : servers.entrySet()){
			
			int serverPort = entries.getValue();
			int rmiPort = entries.getKey();
			
			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new SecurityManager());
			}

			try {
				ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(this, serverPort);

				Registry registry = LocateRegistry.getRegistry(rmiPort);
				registry.rebind("server", stub);
				System.out.println("Server ready.");

			} catch (ConnectException e) {
				System.err
				    .println("Impossible de se connecter au registre RMI. Est-ce que rmiregistry est lancé ?");
				System.err.println();
				System.err.println("Erreur: " + e.getMessage());
			} catch (Exception e) {
				System.err.println("Erreur: " + e.getMessage());
			}
		}
		
		
	}
}
