package cs3500.klondike;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;

/**
 * Tests that didn't make it to Examplar.
 */

public class ExamplarHW4 {
  @Test(expected = IllegalArgumentException.class)
  public void testLimitedNullDeck() {
    KlondikeModel model = new LimitedDrawKlondike(0);
    List<Card> deck = null;
    model.startGame(deck, false, 2, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLimitedEmptyDeck() {
    KlondikeModel model = new LimitedDrawKlondike(0);
    List<Card> deck = new ArrayList<>();
    model.startGame(deck, false, 2, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLimitedNegDraw() {
    KlondikeModel model = new LimitedDrawKlondike(0);
    List<Card> deck = model.getDeck();
    model.startGame(deck, false, 2, -2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLimitedZeroDraw() {
    KlondikeModel model = new LimitedDrawKlondike(0);
    List<Card> deck = model.getDeck();
    model.startGame(deck, false, 2, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLimitedNegRedraw() {
    KlondikeModel model = new LimitedDrawKlondike(-1);
    List<Card> deck = model.getDeck();
    model.startGame(deck, false, 2, 3);
  }

}
