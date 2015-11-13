package ca.polymtl.inf4402.tp2.server;

import ca.polymtl.inf4402.tp2.shared.Operation;

public class WannabeServer {

  private final int MAX_OPERATIONS_Qi = 13;

  private boolean isAccepted(int requestedNumberOfOperations_ui) {
    double tauxRefus = ((double) requestedNumberOfOperations_ui - (double) MAX_OPERATIONS_Qi) / (9 * (double) MAX_OPERATIONS_Qi);

    if (tauxRefus < 0) {
      tauxRefus = 1;
    }

    double randomNumber = Math.random();
    return randomNumber > tauxRefus;
  }

  public Integer executeCalculations(Operation[] operations, int low, int high) {
    int numberOfOperations = high - low;
    if (!isAccepted(numberOfOperations) || numberOfOperations == 0) {
      return null;
    }

    int total = 0;

    for (int i = low; i <= high; i++) {
      total += operations[i].execute() % 5000;
    }

    return total;
  }
}