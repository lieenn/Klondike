package cs3500.klondike.controller;

import java.util.Scanner;

import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * Represents different types of command that a user may give the controller.
 */

public interface ICommand {
  boolean run(Scanner scan, KlondikeModel model);
}
