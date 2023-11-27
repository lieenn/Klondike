package cs3500.klondike.model.hw02;

import java.io.InputStreamReader;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;

/**
 * Main class to play Klondike, for testing purposes.
 */

public class Main {

  /**
   * Takes in user inputs and return the current state of the game.
   * @param args user's inputs as string
   */
  public static void main(String [] args) {
    KlondikeModel model = new BasicKlondike();
    Readable in = new InputStreamReader( System.in );
    Appendable out = System.out;
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(model, model.getDeck(), false, 7, 3);
  }
}
