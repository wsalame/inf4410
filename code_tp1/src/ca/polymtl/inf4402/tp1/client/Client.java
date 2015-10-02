package ca.polymtl.inf4402.tp1.client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import ca.polymtl.inf4402.tp1.shared.ServerInterface;
import ca.polymtl.inf4402.tp1.shared.ServerInterface.AllowedCommand;

public class Client {

	public static void main(String[] args) {
		try {
			if (args.length == 0 || args.length > 2) {
				throw new IllegalArgumentException("Veuillez rentrez un nombre de parametres valides");
			}

			AllowedCommand command = AllowedCommand.valueOfCustom(args[0]);

			if (command == null) {
				throw new IllegalArgumentException("Veuillez rentrez une commande valide");
			}

			String fileName = null;

			if (args.length > 1) {
				fileName = args[1];
			}

			Client client = new Client();
			client.run(command, fileName);
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	private ServerInterface localServerStub = null;

	public Client() {
		super();

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		localServerStub = loadServerStub("127.0.0.1");
	}

	private void run(AllowedCommand command, String fileName) {
		try {
			switch (command) {
			case CREATE:
				System.out.println(localServerStub.create(fileName));
				break;
			case GET:
				break;
			case LIST:
				break;
			case LOCK:
				break;
			case PUSH:
				break;
			case SYNC_LOCAL_DIR:
				break;
			}
		} catch (RemoteException e) {
			System.out.println("Erreur avec le serveur");
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
}
