package ca.polymtl.inf4402.tp2.client;
import ca.polymtl.inf4402.tp2.shared.ServerInterface;
public class ServerStubWrapper {

  private ServerInterface server;
  private boolean isUp;

  public ServerStubWrapper(ServerInterface server, boolean isUp) {
    this.server = server;
    this.isUp = isUp;
  }

  public ServerInterface getServer() {
    return server;
  }

  public boolean isUp() {
    return isUp;
  }

  public void setUp(boolean isUp) {
    this.isUp = isUp;
  }
}
