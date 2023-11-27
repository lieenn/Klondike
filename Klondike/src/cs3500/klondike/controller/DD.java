package cs3500.klondike.controller;

import java.util.Scanner;

import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * Executes the command "dd" to discard topmost card in the drawPile.
 */

public class DD implements ICommand {

  @Override
  public boolean run(Scanner scan, KlondikeModel model) {
    model.discardDraw();
    return true;
  }
}
