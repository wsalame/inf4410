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
    List<Operation> operationList = new ArrayList<Operation>();

    for (String line : lines) {
      String[] split = line.split(" ");
      operationList.add(new Operation(split[0], split[1]));
    }

    List<WannabeServer> servers = new ArrayList<WannabeServer>();
    servers.add(new WannabeServer());
    servers.add(new WannabeServer());
    servers.add(new WannabeServer());

    Map<WannabeServer, Integer> operationsByServerMap = new HashMap<WannabeServer, Integer>();

    final int numberOfOperationsPerServer = operationList.size() / servers.size();

    int totalAdded = 0;
    for (int i = 0; i < servers.size() - 1; i++) {
      totalAdded += numberOfOperationsPerServer;
      operationsByServerMap.put(servers.get(i), numberOfOperationsPerServer);
    }

    operationsByServerMap.put(servers.get(servers.size() - 1), operationList.size() - totalAdded);

    Operation[] operations = operationList.toArray(new Operation[operationList.size()]);
    int low = 0;
    int high = 0;

    int total = 0;
    for (Entry<WannabeServer, Integer> entry : operationsByServerMap.entrySet()) {
      WannabeServer server = entry.getKey();
      int operationsByServer = entry.getValue();

      high += operationsByServer;

      int result = server.executeCalculations(operations, low, high);
      low = high + 1;

      System.out.println(result);
      total += result;
    }

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
