package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * Builder class for Klondike.
 */

public class KlondikeCreator {
  private static int numPile;
  private static int numDraw;
  private static int numRedraw;

  /**
   * Initiate default fields for Klondike.
   */
  public KlondikeCreator() {
    numPile = 7;
    numDraw = 3;
    numRedraw = 2;
  }

  /**
   * Enumerates different types of Klondike.
   */
  public enum GameType {
    BASIC, LIMITED, WHITEHEAD;
  }

  public KlondikeCreator setNumPile(int numPiles) {
    numPile = numPiles;
    return this;
  }
  
  public int getNumPile() {
    return numPile;
  }

  public KlondikeCreator setNumDraw(int numDraws) {
    numDraw = numDraws;
    return this;
  }

  public int getNumDraw() {
    return numDraw;
  }

  public KlondikeCreator setNumReDraw(int numRedraws) {
    numRedraw = numRedraws;
    return this;
  }

  public int getNumRedraw() {
    return numRedraw;
  }

  /**
   * Creates an instance of KlondikeModel.
   * @param game Type of Klondike
   * @return KlondikeModel
   */
  public static KlondikeModel create(GameType game) {
    KlondikeModel model = null;

    if (game == GameType.BASIC) {
      return new BasicKlondike();
    }
    if (game == GameType.LIMITED) {
      return new LimitedDrawKlondike(numRedraw);
    }
    if (game == GameType.WHITEHEAD) {
      return new WhiteheadKlondike();
    }
    else {
      throw new IllegalArgumentException("Invalid game type");
    }
  }
}
