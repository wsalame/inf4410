package ca.polymtl.inf4402.tp1.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;

import ca.polymtl.inf4402.tp1.shared.ServerInterface;
import ca.polymtl.inf4402.tp1.shared.ServerInterface.AllowedCommand;
import ca.polymtl.inf4402.tp1.shared.Utils;

public class Client {

	private final String CLIENT_ID_FILENAME = "clientId.txt";

	private ServerInterface localServerStub = null;

	public static void main(String[] args) {
		try {
			if (args.length == 0 || args.length > 2) {
				throw new IllegalArgumentException("Veuillez rentrez un nombre de parametres valides");
			}

			AllowedCommand command = AllowedCommand.valueOfCustom(args[0]);

			if (command == null) {
				throw new IllegalArgumentException("Veuillez rentrez une commande valide");
			}

			String filename = null;

			if (args.length > 1) {
				filename = args[1];
			}

			Client client = new Client();
			client.run(command, filename);
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
		}
	}

	public Client() {
		super();

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		localServerStub = loadServerStub("127.0.0.1");
	}

	private void run(AllowedCommand command, String filename) throws NoSuchAlgorithmException {
		String checksum = filename != null ? getChecksum(filename) : null;
		try {
			switch (command) {
			case CREATE:
				System.out.println(localServerStub.create(filename));
				break;
			case GET:
				get(filename);
				System.out.println(filename + " a ete synchronise");
				break;
			case LIST:
				System.out.println(localServerStub.list());
				break;
			case LOCK:
				if (checksum != null) {
					String clientId = hasClientId() ? getClientIdFromLocal() : generateClientIdAndSave(localServerStub.generateClientId());

					System.out.println(localServerStub.lock(filename, clientId, checksum));

					get(filename);
				} else {
					System.out.println("Vous devez 'get' le fichier en premier !");
				}
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

	private void get(String filename) throws RemoteException, NoSuchAlgorithmException{
		String checksum = getChecksum(filename);
		byte[] remoteData = localServerStub.get(filename, checksum);
		if (remoteData != null) {
			saveFileToLocal(filename, remoteData);
		}
	}

	private ServerInterface loadServerStub(String hostname) {
		ServerInterface stub = null;

		try {
			Registry registry = LocateRegistry.getRegistry(hostname);
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

	private String generateClientIdAndSave(String clientId) {
		try {
			PrintWriter out = new PrintWriter(CLIENT_ID_FILENAME);
			out.print(clientId);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return clientId;
	}

	private byte[] getDataFromLocal(String filename) {
		byte[] dataEncoded = null;
		try {
			Path path = Paths.get(filename);
			if(path != null && path.toFile().exists()){
				dataEncoded = Files.readAllBytes(path);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dataEncoded;
	}

	private void saveFileToLocal(String filename, byte[] newData) {
		PrintWriter out;
		try {
			out = new PrintWriter(filename);
			out.print(new String(newData, "UTF-8"));
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private String getClientIdFromLocal() {
		String clientId = null;
		try {
			byte[] data = getDataFromLocal(CLIENT_ID_FILENAME);
			clientId = new String(data, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientId;
	}

	private boolean hasClientId() {
		File f = new File(CLIENT_ID_FILENAME);
		return f.exists() && !f.isDirectory();
	}
	
	private String getChecksum(String filename){
		String checksum = null;
		try {
			byte[] data = getDataFromLocal(filename);
			if(data != null){
				checksum =  Utils.toMd5(data);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return checksum;
	}
}
