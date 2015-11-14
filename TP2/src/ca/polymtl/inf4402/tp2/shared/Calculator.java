package ca.polymtl.inf4402.tp2.shared;

import java.util.HashMap;
import java.util.Map;

/**
 * Methodes utilitaires pour effectuer les operations du TP2.
 * 
 * L'implementation des operations est volontairement non-optimale.
 * 
 * @author Simon Delisle et Francois Doray
 * 
 */
public class Calculator {

  static Map<Integer, Integer> cache = new HashMap<Integer, Integer>();

  public static int fib(int x) {
    if (x == 0)
      return 0;
    if (x == 1)
      return 1;

    if (cache.containsKey(x)) {
      return cache.get(x);
    }

    int result = fib(x - 1) + fib(x - 2);
    cache.put(x, result);
    return result;
  }

  public static int prime(int x) {
    int highestPrime = 0;

    for (int i = 1; i <= x; ++i) {
      if (isPrime(i) && x % i == 0 && i > highestPrime)
        highestPrime = i;
    }

    return highestPrime;
  }

  private static boolean isPrime(int x) {
    if (x <= 1)
      return false;

    for (int i = 2; i < x; ++i) {
      if (x % i == 0)
        return false;
    }

    return true;
  }

}
