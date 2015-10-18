package ca.polymtl.inf4402.tp1.server;

import java.rmi.ConnectException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import ca.polymtl.inf4402.tp1.shared.ServerInterface;
import ca.polymtl.inf4402.tp1.shared.Utils;

public class Server implements ServerInterface {
	private Map<String, FilePoly> fileMap = new HashMap<String, FilePoly>();
	private final byte[] EMPTY_FILE = new byte[0];
	private static int clientId = 0;

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
			ServerInterface stub = (ServerInterface) UnicastRemoteObject
					.exportObject(this, 0);

			Registry registry = LocateRegistry.getRegistry();
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

	@Override
	public String generateClientId() {
		return "Client # " + ++clientId;
	}

	@Override
	public String create(String filename) {
		if (fileMap.containsKey(filename)) {
			return filename + " existe déjà";
		}
		try {
			createInMemory(filename, EMPTY_FILE, null);
		} catch (NoSuchAlgorithmException e) {
			return "Erreur lors de la création du fichier " + filename;
		}

		return filename + " à été ajouté";
	}

	private void createInMemory(String filename, byte[] data, String clientId)
			throws NoSuchAlgorithmException {
		String checksum = Utils.toMd5(data);
		fileMap.put(filename, new FilePoly(filename, clientId, checksum, data));
	}

	@Override
	public String lock(String filename, String clientId, String checksum) {
		FilePoly fileToRetrieve = fileMap.get(filename);

		if (fileToRetrieve == null) {
			throw new IllegalArgumentException("Le fichier " + filename
					+ " n'existe pas.");
		}

		if (fileToRetrieve.isLocked()) {
			throw new IllegalArgumentException(filename
					+ " est déjà verrouillé par "
					+ fileToRetrieve.getClientId());
		}

		fileToRetrieve.setClientId(clientId);

		return filename + " a été verrouillé";
	}

	@Override
	public byte[] get(String filename, String checksum) {
		FilePoly fileToRetrieve = fileMap.get(filename);

		if (fileToRetrieve == null) {
			throw new IllegalArgumentException("Le fichier " + filename
					+ " n'existe pas.");
		}

		if (fileToRetrieve.getChecksum().equals(checksum)) {
			return null;
		}

		return fileToRetrieve.getData();
	}

	@Override
	public String list() {

		StringBuilder output = new StringBuilder();

		for (FilePoly file : fileMap.values()) {
			String clientId = file.getClientId() != null ? "vérouillé par "
					+ file.getClientId() : " non vérrouillé";
			output.append(file.getFilename()).append(" ").append(clientId)
					.append("\n");
		}

		return output.append(fileMap.values().size()).append(" fichier(s)")
				.toString();
	}

	@Override
	public String push(String filename, byte[] data, String clientId) {
		FilePoly fileToRetrieve = fileMap.get(filename);

		if (!fileToRetrieve.getClientId().equals(clientId)) {
			return filename + " doit être verouillé";
		}

		try {
			createInMemory(filename, data, clientId);
		} catch (NoSuchAlgorithmException e) {
			return "Erreur lors de la création du fichier " + filename;
		}
		return filename + " a été envoyé au serveur";
	}
}
