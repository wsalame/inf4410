package ca.polymtl.inf4402.tp1.client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

import ca.polymtl.inf4402.tp1.shared.ServerInterface;

public class Client {
	public enum Server {
		LOCAL("local"), LOCAL_RMI("localrmi"), DISTANT_RMI("distantrmi");
		private static Map<String, Server> servers = new HashMap<String, Client.Server>(3);

		static {
			for (Server server : Server.values()) {
				servers.put(server.getArgumentName(), server);
			}
		}

		private String argumentName;

		private Server(String argumentName) {
			this.argumentName = argumentName;
		}

		public String getArgumentName() {
			return argumentName;
		}

		public static Server valueOfCustom(String argumentName) {
			return servers.get(argumentName);
		}
	}

	public static void main(String[] args) {
		String distantHostname = null;
		int power = 0;
		Server serverToCall = Server.LOCAL;

		if (args.length > 0) {
			distantHostname = args[0];

			if (args.length > 1) {
				power = Integer.valueOf(args[1]);
			}

			if (args.length > 2) {
				serverToCall = Server.valueOfCustom(args[2]);
			}
		}

		Client client = new Client(distantHostname);
		client.run(power, serverToCall);

	}

	FakeServer localServer = null; // Pour tester la latence d'un appel de
									// fonction normal.
	private ServerInterface localServerStub = null;
	private ServerInterface distantServerStub = null;

	public Client(String distantServerHostname) {
		super();

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		localServer = new FakeServer();
		localServerStub = loadServerStub("127.0.0.1");

		if (distantServerHostname != null) {
			distantServerStub = loadServerStub(distantServerHostname);
		}
	}

	private void run(int power, Server serverToCall) {
		int fileSize = (int) Math.pow(10, power);
		if (serverToCall != null) {
			switch (serverToCall) {
			case DISTANT_RMI:
				appelRMIDistant(fileSize);
				break;
			case LOCAL:
				appelNormal(fileSize);
				break;
			case LOCAL_RMI:
				appelRMILocal(fileSize);
				break;
			default:
				break;
			}
		}else{
			System.out.println("Svp rentrez un serveur (local, localrmi, distantrmi)");
		}
	}

	private ServerInterface loadServerStub(String hostname) {
		ServerInterface stub = null;

		try {
			Registry registry = LocateRegistry.getRegistry(hostname);
			stub = (ServerInterface) registry.lookup("server");
		} catch (NotBoundException e) {
			System.out.println("Erreur: Le nom '" + e.getMessage() + "' n'est pas défini dans le registre.");
		} catch (AccessException e) {
			System.out.println("Erreur: " + e.getMessage());
		} catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}

		return stub;
	}

	private void appelNormal(int size) {
		long start = System.nanoTime();
		localServer.execute(new byte[size]);
		long end = System.nanoTime();

		System.out.println((end - start) + " ns");
	}

	private void appelRMILocal(int size) {
		try {
			long start = System.nanoTime();
			localServerStub.execute(new byte[size]);
			long end = System.nanoTime();

			System.out.println((end - start) + " ns");
		} catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}
	}

	private void appelRMIDistant(int size) {
		try {
			long start = System.nanoTime();
			distantServerStub.execute(new byte[size]);
			long end = System.nanoTime();

			System.out.println((end - start) + " ns");
		} catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}
	}
}
