package cs3500.klondike.controller;

import java.io.IOException;
import java.util.Scanner;

/**
 * Utils class for IO.
 */

public class IOUtils {
  static void writeIO(Appendable appendable, String mgs) {
    try {
      appendable.append(mgs);
    } catch (IOException e) {
      throw new IllegalStateException("IO Failed");
    }
  }

  protected static int validateInt(Scanner scan) {
    while (scan.hasNext()) {
      if (scan.hasNext("[qQ]")) {
        return -1;
      }
      String in = scan.next();
      try {
        return Integer.parseInt(in);
      } catch (NumberFormatException e) {
        //continue is implied
      }
    }
    throw new IllegalStateException("No input given");
  }
}
