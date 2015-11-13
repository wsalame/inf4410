package ca.polymtl.inf4402.tp2.shared;

public class TimeWatch {
  private long starts = -1;
  private long ends = -1;

  public void start() {
    reset();
    starts = System.currentTimeMillis();
  }

  public void reset() {
    starts = -1;
    ends = -1;
  }

  public void stop() {
    ends = System.currentTimeMillis();
  }

  public long getTime() {
    return ends - starts;
  }

  @Override
  public String toString() {
    return getTime() / 1000.0 + " s";
  }
}
