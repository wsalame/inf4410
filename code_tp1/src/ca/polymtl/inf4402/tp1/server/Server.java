package ca.polymtl.inf4402.tp1.server;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import ca.polymtl.inf4402.tp1.shared.ServerInterface;

public class Server implements ServerInterface {

	// Key = Filename, Value = checksum md5
	private Map<String, FilePolymtl> fileMap = new HashMap<String, FilePolymtl>();
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
			ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(this, 0);

			Registry registry = LocateRegistry.getRegistry();
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

	@Override
	public String generateClientId() {
		return "Client # " + ++clientId;
	}

	@Override
	public String create(String filename) {
		if (fileMap.containsKey(filename)) {
			return filename + " existe deja";
		}
		try {
			String checksum = toMd5(EMPTY_FILE);
			fileMap.put(filename, new FilePolymtl(filename, null, checksum));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "Erreur lors de la creation du fichier " + filename;
		}
		return filename + "a ete ajoute";
	}
	
	private String toMd5(byte[] file) throws NoSuchAlgorithmException{
		byte[] digest = MessageDigest.getInstance("MD5").digest(file);
		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}
		return sb.toString();
	}

	@Override
	public String lock(String filename, String clientId, String checksum) throws RemoteException {
		FilePolymtl fileToRetrieve = fileMap.get(filename);
		
		if(fileToRetrieve.isLocked()){
			return filename + " est deja verouille par " + fileToRetrieve.getClientId();
		}
		
		fileToRetrieve.setClientId(clientId);

		return filename + " a ete verouille";
	}
}