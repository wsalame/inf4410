package ca.polymtl.inf4402.tp2.client;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import ca.polymtl.inf4402.tp2.shared.Operation;
import ca.polymtl.inf4402.tp2.shared.ServerInterface;

public class Client {

	private ServerInterface localServerStub = null;

	public static void main(String[] args) {
		Client client = new Client();
		client.run("donnees-2317.txt");
	}

	public Client() {
		super();

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		localServerStub = loadServerStub("127.0.0.1");
	}

	private List<ServerStubWrapper> serversWrapper = new ArrayList<ServerStubWrapper>();
	private Operation[] operations;
	private int numberOfServersDown = 0;

	private void setup(String path) {
		List<String> lines = Utils.readFile(path);
		operations = new Operation[lines.size()];

		for (int i = 0; i < lines.size(); i++) {
			String[] split = lines.get(i).split(" ");
			operations[i] = new Operation(split[0], split[1]);
		}

		List<ServerInterface> servers = new ArrayList<ServerInterface>();
		servers.add(localServerStub);

		for (ServerInterface server : servers) {
			serversWrapper.add(new ServerStubWrapper(server, true));
		}
	}

	private void run(String path) {
		setup(path);

		int total = execute(operations);

		System.out.println(total);
	}

	int numberOfAttempts = 0;
	private final int MAX_ATTEMPTS = 2;

	private int execute(Operation[] operations) {
		if (numberOfAttempts > MAX_ATTEMPTS) {
			System.out.println(
					"Le résultat complet n'a pu être calculé, car une partie des calculs ont été refusés trop de fois (max retries exceeded)");
			return 0;
		}

		int high = -1;
		int total = 0;
		int actualNumberOfOperationsExecuted = 0;
		int operationsByServer = calculateNumberOfOperationsPerServer(operations.length,
				serversWrapper.size() - numberOfServersDown);
		int low = 0;
		for (ServerStubWrapper serverWrapper : serversWrapper) {
			ServerInterface server = serverWrapper.getServer();
			if (!serverWrapper.isUp()) {
				numberOfServersDown++;
				continue;
			}

			low = high + 1;
			high += operationsByServer;

			Integer result = null;
			try {
				result = server.executeCalculations(operations, low, high);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			if (result == null) {
				System.out.println("retry");
				// Retry second time with less ops
				high = low + operationsByServer / 3;
				if (high < 0) {
					high = 0;
				}
				try {
					result = server.executeCalculations(operations, low, high);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}

			if (result != null) {
				actualNumberOfOperationsExecuted += high - low + 1;
				System.out.println(result);
				total += result;
			}
		}

		boolean isCompleted = actualNumberOfOperationsExecuted >= operations.length;

		Operation[] operationsNew = null;
		if (!isCompleted) {
			operationsNew = new Operation[operations.length - high - 1];
			int j = 0;
			for (int i = high + 1; i < operations.length; i++, j++) {
				operationsNew[j] = operations[i];
			}
		}

		numberOfAttempts++;
		return total + (!isCompleted ? execute(operationsNew) : 0);
	}

	private int calculateNumberOfOperationsPerServer(int countOperations, int countServer) {
		double number = (double) countOperations / (double) countServer;
		return BigDecimal.valueOf(number).setScale(0, RoundingMode.UP).intValue();
	}

	private ServerInterface loadServerStub(String hostname) {
		ServerInterface stub = null;

		try {
			Registry registry = LocateRegistry.getRegistry(hostname, 5020);
			stub = (ServerInterface) registry.lookup("server");
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (NotBoundException e) {
			System.out.println("Erreur: Le nom '" + e.getMessage() + "' n'est pas défini dans le registre.");
		} catch (AccessException e) {
			System.out.println("Erreur: " + e.getMessage());
		} catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}

		return stub;
	}

}
