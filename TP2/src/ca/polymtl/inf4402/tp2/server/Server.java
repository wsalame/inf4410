package ca.polymtl.inf4402.tp2.server;

import java.rmi.ConnectException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import ca.polymtl.inf4402.tp2.shared.Operation;
import ca.polymtl.inf4402.tp2.shared.ServerInterface;
import ca.polymtl.inf4402.tp2.shared.UtilsServer;

public class Server implements ServerInterface {

  private int maxOperations_Qi;

  int rmiPort;
  int serverPort;

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

    List<String> configLines = UtilsServer.readFile("serverConfig");
    rmiPort = Integer.parseInt(configLines.get(0).substring(configLines.get(0).indexOf("=") + 1));
    serverPort = Integer.parseInt(configLines.get(1).substring(configLines.get(1).indexOf("=") + 1));
    maxOperations_Qi = Integer.parseInt(configLines.get(2).substring(configLines.get(2).indexOf("=") + 1));

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
    double tauxRefus = ((double) requestedNumberOfOperations_ui - (double) maxOperations_Qi) / (9 * (double) maxOperations_Qi);

    if (tauxRefus < 0) {
      tauxRefus = 0;
    }

    double randomNumber = Math.random();

    return randomNumber > tauxRefus;
  }

  @Override
  public boolean ping() {
    return true;
  }

  @Override
  public Integer executeCalculations(Operation[] operations, int low, int high) {
    int numberOfOperations = high - low + 1;
    if (!isAccepted(numberOfOperations) || numberOfOperations <= 0) {
      return null;
    }

    int total = 0;

    for (int i = low; i <= high && i < operations.length; i++) {
      total += operations[i].execute() % 5000;
    }

    return total;
  }
}
