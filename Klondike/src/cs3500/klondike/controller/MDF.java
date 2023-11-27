package cs3500.klondike.controller;

import java.util.Scanner;

import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * Executes the command "mdf" to move the drawCards to the given foundationPile.
 */

public class MDF implements ICommand {

  @Override
  public boolean run(Scanner scan, KlondikeModel model) {
    if (scan.hasNext()) {
      int found = IOUtils.validateInt(scan);
      if (found == -1) {
        return false;
      }
      model.moveDrawToFoundation(found - 1);
      return true;
    } else {
      throw new IllegalStateException("Need more inputs");
    }
  }
}
