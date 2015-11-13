package ca.polymtl.inf4402.tp2.client;

import java.util.ArrayList;
import java.util.List;
import ca.polymtl.inf4402.tp2.server.WannabeServer;
import ca.polymtl.inf4402.tp2.shared.Operation;
import ca.polymtl.inf4402.tp2.shared.ServerInterface;

public class Client {

  private ServerInterface localServerStub = null;

  public static void main(String[] args) {
    Client client = new Client();
    client.run("C:\\Users\\Wissam\\workspace\\inf4410\\TP2\\donnees-2317.txt");
  }

  public Client() {
    super();

    // if (System.getSecurityManager() == null) {
    // System.setSecurityManager(new SecurityManager());
    // }

    // localServerStub = loadServerStub("127.0.0.1");
  }

  private List<ServerStubWrapper> serversWrapper = new ArrayList<ServerStubWrapper>();
  private Operation[] operations;
  private final int OFFSET_RETRY = 10;
  private int numberOfServersDown = 0;

  private void setup(String path) {
    List<String> lines = Utils.readFile(path);
    operations = new Operation[lines.size()];

    for (int i = 0; i < lines.size(); i++) {
      String[] split = lines.get(i).split(" ");
      operations[i] = new Operation(split[0], split[1]);
    }

    List<WannabeServer> servers = new ArrayList<WannabeServer>();
    servers.add(new WannabeServer());
    servers.add(new WannabeServer());
    servers.add(new WannabeServer());

    for (WannabeServer server : servers) {
      serversWrapper.add(new ServerStubWrapper(server, true));
    }
  }

  private void run(String path) {
    setup(path);

    int total = execute(0, -1, operations.length);

    System.out.println(total);
  }

  int numberOfAttempts = 0;
  private final int MAX_ATTEMPTS = 2;

  private int execute(int low, int high, int numberOfOperations) {
    if (numberOfAttempts > MAX_ATTEMPTS) {
      System.out.println("Le résultat complet n'a pu être calculé");
      return 0;
    }

    int total = 0;
    int actualNumberOfOperationsExecuted = 0;

    for (ServerStubWrapper serverWrapper : serversWrapper) {
      WannabeServer server = serverWrapper.getServer();
      if (!serverWrapper.isUp()) {
        numberOfServersDown++;
        continue;
      }
      int operationsByServer = calculateNumberOfOperationsPerServer(numberOfOperations, serversWrapper.size() - numberOfServersDown);

      low = high + 1;
      high += operationsByServer;

      Integer result = server.executeCalculations(operations, low, high);
      if (result == null) {
        System.out.println("retry");
        // Retry second time with less ops
        high -= OFFSET_RETRY;
        result = server.executeCalculations(operations, low, high);
      }

      if (result != null) {
        actualNumberOfOperationsExecuted += high - low + 1;
        System.out.println(result);
        total += result;
      }
    }

    boolean isCompleted = actualNumberOfOperationsExecuted == numberOfOperations;

    System.out.println("Actual " + actualNumberOfOperationsExecuted);

    numberOfAttempts++;
    return total + (!isCompleted ? execute(low, high, numberOfOperations - actualNumberOfOperationsExecuted) : 0);
  }

  private int calculateNumberOfOperationsPerServer(int countOperations, int countServer) {
    return countOperations / countServer;
  }
  //
  // private ServerInterface loadServerStub(String hostname) {
  // ServerInterface stub = null;
  //
  // try {
  // Registry registry = LocateRegistry.getRegistry(hostname, 5001);
  // stub = (ServerInterface) registry.lookup("server");
  // } catch (IllegalArgumentException e) {
  // System.out.println(e.getMessage());
  // } catch (NotBoundException e) {
  // System.out.println("Erreur: Le nom '" + e.getMessage() + "' n'est pas défini dans le registre.");
  // } catch (AccessException e) {
  // System.out.println("Erreur: " + e.getMessage());
  // } catch (RemoteException e) {
  // System.out.println("Erreur: " + e.getMessage());
  // }
  //
  // return stub;
  // }

}
