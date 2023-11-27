package cs3500.klondike.controller;

import java.util.Scanner;

import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * Executes the command "mpp" to move given card(s) from given srscPile to given destPile.
 */

public class MPP implements ICommand {
  @Override
  public boolean run(Scanner scan, KlondikeModel model) {
    if (scan.hasNext()) {
      int src = IOUtils.validateInt(scan);
      int num = IOUtils.validateInt(scan);
      int dest = IOUtils.validateInt(scan);
      if (src == -1 || num == -1 || dest == -1) {
        return false;
      }
      model.movePile(src - 1, num, dest - 1);
      return true;
    } else {
      throw new IllegalStateException("Need more inputs");
    }
  }
}
