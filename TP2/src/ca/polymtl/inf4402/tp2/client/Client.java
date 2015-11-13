package ca.polymtl.inf4402.tp2.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

  volatile int total = 0;

  private void run(String path) {

    List<String> lines = Utils.readFile(path);
    Operation[] operations = new Operation[lines.size()];

    for (int i = 0; i < lines.size(); i++) {
      String[] split = lines.get(i).split(" ");
      operations[i] = new Operation(split[0], split[1]);
    }

    List<WannabeServer> servers = new ArrayList<WannabeServer>();
    servers.add(new WannabeServer());
    servers.add(new WannabeServer());
    servers.add(new WannabeServer());

    Map<WannabeServer, Integer> operationsByServerMap = new HashMap<WannabeServer, Integer>();

    final int numberOfOperationsPerServer = operations.length / servers.size();

    int totalAdded = 0;
    for (int i = 0; i < servers.size() - 1; i++) {
      totalAdded += numberOfOperationsPerServer;
      operationsByServerMap.put(servers.get(i), numberOfOperationsPerServer);
    }

    operationsByServerMap.put(servers.get(servers.size() - 1), operations.length - totalAdded);

    int low = 0;
    int high = -1;

    int total = 0;
    final int OFFSET_RETRY = 10;

    int actualNumberOfOperationsExecuted = 0;
    for (Entry<WannabeServer, Integer> entry : operationsByServerMap.entrySet()) {
      WannabeServer server = entry.getKey();
      int operationsByServer = entry.getValue();

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

    System.out.println(actualNumberOfOperationsExecuted);

    System.out.println(total);
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
