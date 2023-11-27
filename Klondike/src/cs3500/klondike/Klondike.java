
package cs3500.klondike;

import java.io.InputStreamReader;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.KlondikeCreator;

/**
 * Class for Main.
 */
public final class Klondike {

  /**
   * Main method of Klondike.
   * @param args user's inputs
   */
  public static void main(String[] args) {
    if (args.length < 1) {
      throw new IllegalArgumentException("No commands");
    }
    switch (args[0]) {
      case "basic": //basic 7 3
        KlondikeCreator creator = new KlondikeCreator();
        KlondikeModel basic = KlondikeCreator.create(KlondikeCreator.GameType.BASIC);
        int numPiles = args.length > 1 ? Integer.parseInt(args[1]) : creator.getNumPile();
        int numDraw = args.length > 2 ? Integer.parseInt(args[2]) : creator.getNumDraw();
        Readable in = new InputStreamReader(System.in);
        Appendable out = System.out;
        KlondikeController controller = new KlondikeTextualController(in, out);
        controller.playGame(basic, basic.getDeck(), true, numPiles, numDraw);
        break;
      case "limited": //limited 3 6 2
        if (args.length < 2) {
          throw new IllegalArgumentException("Need to have a redraw num");
        }
        KlondikeCreator creatorL = new KlondikeCreator();
        creatorL.setNumReDraw(Integer.parseInt(args[1]));
        KlondikeModel limited = KlondikeCreator.create(KlondikeCreator.GameType.LIMITED);
        int numPilesL = args.length > 2 ? Integer.parseInt(args[2]) : creatorL.getNumPile();
        int numDrawL = args.length > 3 ? Integer.parseInt(args[3]) : creatorL.getNumDraw();
        Readable inL = new InputStreamReader(System.in);
        Appendable outL = System.out;
        KlondikeController controllerL = new KlondikeTextualController(inL, outL);
        controllerL.playGame(limited, limited.getDeck(), true, numPilesL, numDrawL);
        break;
      case "whitehead": //whitehead 7 8
        KlondikeCreator creatorW = new KlondikeCreator();
        KlondikeModel whitehead = KlondikeCreator.create(KlondikeCreator.GameType.WHITEHEAD);
        int numPilesW = args.length > 1 ? Integer.parseInt(args[1]) : creatorW.getNumPile();
        int numDrawW = args.length > 2 ? Integer.parseInt(args[2]) : creatorW.getNumDraw();
        Readable inW = new InputStreamReader(System.in);
        Appendable outW = System.out;
        KlondikeController controllerW = new KlondikeTextualController(inW, outW);
        controllerW.playGame(whitehead, whitehead.getDeck(), true, numPilesW, numDrawW);
        break;
      default:
        throw new IllegalArgumentException("Invalid game");
    }
  }
}
