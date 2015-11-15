package ca.polymtl.inf4402.tp2.server;

import java.rmi.ConnectException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import ca.polymtl.inf4402.tp2.shared.Operation;
import ca.polymtl.inf4402.tp2.shared.ServerInterface;

public class Server implements ServerInterface {

	public static Map<Integer, Integer> servers = new HashMap<Integer, Integer>();
	private final int MAX_OPERATIONS_Qi = 20;

	static {
		servers.put(5003, 5004);
		servers.put(5005, 5006);
		servers.put(5008, 5009);
	}

	int serverPort = 5021;
	int rmiPort = 5020;

	public static void main(String[] args) {
		Server server = new Server();
		server.run();
	}

	public Server() {
		super();
	}

	private void run() {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try {
			ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(this, serverPort);
			Registry registry = LocateRegistry.getRegistry(rmiPort);

			registry.rebind("server", stub);
			System.out.println("Server ready.");
		} catch (ConnectException e) {
			System.err.println("Impossible de se connecter au registre RMI. Est-ce que rmiregistry est lancé ?");
			System.err.println();
			System.err.println("Erreur: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Erreur: " + e.getMessage());
		}

	}


	private boolean isAccepted(int requestedNumberOfOperations_ui) {
		double tauxRefus = ((double) requestedNumberOfOperations_ui - (double) MAX_OPERATIONS_Qi)
				/ (9 * (double) MAX_OPERATIONS_Qi);

		if (tauxRefus < 0) {
			tauxRefus = 1;
		}

		double randomNumber = Math.random();
		return randomNumber > tauxRefus;
	}

	@Override
	public Integer executeCalculations(Operation[] operations, int low, int high) {
		int numberOfOperations = high - low + 1;
		if (numberOfOperations == 0) {
			return null;
		}

		int total = 0;

		for (int i = low; i <= high && i < operations.length; i++) {
			total += operations[i].execute() % 5000;
		}

		return total;
	}
}
