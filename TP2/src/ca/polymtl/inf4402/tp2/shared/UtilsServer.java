package ca.polymtl.inf4402.tp2.shared;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UtilsServer {

  public static List<String> readFile(String path) {
    List<String> lines = new ArrayList<String>();
    BufferedReader br = null;

    try {

      String currentLine;

      br = new BufferedReader(new FileReader(path));

      while ((currentLine = br.readLine()) != null) {
        lines.add(currentLine);
      }

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (br != null)
          br.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }

    return lines;
  }
}
