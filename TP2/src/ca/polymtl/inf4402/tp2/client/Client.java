package ca.polymtl.inf4402.tp2.client;

import ca.polymtl.inf4402.tp2.shared.ServerInterface;

public class Client {

  private ServerInterface localServerStub = null;

  public static void main(String[] args) {
    Client client = new Client();
    client.run();
  }

  public Client() {
    super();

    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }

    // localServerStub = loadServerStub("127.0.0.1");
  }

  private void run() {

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
