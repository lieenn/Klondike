package cs3500.klondike.controller;

import java.util.Scanner;

import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * Executes the command "md" to move the drawCard to the given cascadePile.
 */

public class MD implements ICommand {
  @Override
  public boolean run(Scanner scan, KlondikeModel model) {
    if (scan.hasNext()) {
      int dest = IOUtils.validateInt(scan);
      if (dest == -1) {
        return false;
      }
      model.moveDraw(dest - 1);
      return true;
    } else {
      throw new IllegalStateException("Need more inputs");
    }
  }
}
