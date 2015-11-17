package ca.polymtl.inf4402.tp2.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
  Integer executeCalculations(Operation[] operations, int low, int high) throws RemoteException;

  boolean ping() throws RemoteException;
}