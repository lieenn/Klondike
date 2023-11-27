package cs3500.klondike.controller;

import java.util.Scanner;

import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * Executes the command "mpf" to move the topmost cards
 * of the given cascadePile to the given foundationPile.
 */

public class MPF implements ICommand {

  @Override
  public boolean run(Scanner scan, KlondikeModel model) {
    if (scan.hasNext()) {
      int src = IOUtils.validateInt(scan);
      int found = IOUtils.validateInt(scan);
      if (src == -1 || found == -1) {
        return false;
      }
      model.moveToFoundation(src - 1, found - 1);
      return true;
    } else {
      throw new IllegalStateException("Need more inputs");
    }
  }
}
