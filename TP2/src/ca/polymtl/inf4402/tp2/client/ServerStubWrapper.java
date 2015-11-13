package ca.polymtl.inf4402.tp2.client;

import ca.polymtl.inf4402.tp2.server.WannabeServer;

public class ServerStubWrapper {

  private WannabeServer server;
  private boolean isUp;

  public ServerStubWrapper(WannabeServer server, boolean isUp) {
    this.server = server;
    this.isUp = isUp;
  }

  public WannabeServer getServer() {
    return server;
  }

  public boolean isUp() {
    return isUp;
  }

  public void setUp(boolean isUp) {
    this.isUp = isUp;
  }
}
