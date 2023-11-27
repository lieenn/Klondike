package cs3500.klondike.model.hw04;

import java.util.HashMap;
import java.util.Map;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;

/**
 * Limited draw version of Klondike.
 */
public class LimitedDrawKlondike extends BasicKlondike {
  private final int numRedraw;
  private final Map<Card, Integer> discardedCard;

  /**
   * Limited version of Klondike.
   * @param numTimesRedrawAllowed number of redraw the user is allowed to make
   */
  public LimitedDrawKlondike(int numTimesRedrawAllowed) {
    super();

    if (numTimesRedrawAllowed < 0) {
      throw new IllegalArgumentException("Number of redraws cannot be negative");
    }
    this.numRedraw = numTimesRedrawAllowed;

    this.discardedCard = new HashMap<Card, Integer>();
  }

  @Override
  protected void discardDrawHelper() {

    Card discard = drawPile.get(0);
    if (numRedraw == 0) {
      drawPile.remove(0);
      super.draw();
      return;
    }
    if (discardedCard.get(discard) == null) {
      discardedCard.put(discard, numRedraw - 1);
      super.discardDrawHelper();
      return;
    }
    if (discardedCard.get(discard) != 0) {
      discardedCard.put(discard, discardedCard.get(discard) - 1);
      super.discardDrawHelper();
    }
    else {
      discardedCard.remove(discard);
      drawPile.remove(0);
      super.draw();
    }
  }
}
