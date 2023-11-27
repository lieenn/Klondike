package cs3500.klondike;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.KlondikeCreator;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;
import cs3500.klondike.model.hw04.WhiteheadKlondike;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test for new versions of klondike.
 */

public class KlondikeExtendedModelTest {

  private boolean isEveryCardFacingUp(KlondikeModel model) {
    for (int col = 0; col < model.getNumPiles(); col++) {
      for (int row = col; row < col + 1; row++) {
        if (!model.isCardVisible(col, row)) {
          return false;
        }
      }
    }
    return true;
  }

  private void addCardLimited(List<Card> deck, String s, int redraw) {
    KlondikeModel game = new LimitedDrawKlondike(redraw);
    deck.add(game.getDeck().stream().filter(c -> c.toString()
            .equals(s)).collect(Collectors.toList()).get(0));
  }

  private void addCardWhitehead(List<Card> deck, String s) {
    KlondikeModel game = new WhiteheadKlondike();
    deck.add(game.getDeck().stream().filter(c -> c.toString()
            .equals(s)).collect(Collectors.toList()).get(0));
  }

  @Test
  public void isEveryCardInCascadeFacingUp() {
    KlondikeModel model = new WhiteheadKlondike();
    List<Card> deck = model.getDeck();
    model.startGame(deck, true, 7, 3);
    //System.out.print(new KlondikeTextualView(model));
    assertTrue(isEveryCardFacingUp(model));
  }

  @Test
  public void testMoveCardToEmptyPile() {
    KlondikeModel model = new WhiteheadKlondike();
    List<Card> deck = new ArrayList<>();
    addCardWhitehead(deck, "A♡");
    addCardWhitehead(deck, "2♡");
    addCardWhitehead(deck, "2♠");
    addCardWhitehead(deck, "A♠");
    addCardWhitehead(deck, "3♠");
    addCardWhitehead(deck, "3♡");
    model.startGame(deck, false, 2, 1);
    assertEquals(1, model.getPileHeight(0));
    model.moveToFoundation(0, 0);
    assertEquals(0, model.getPileHeight(0));
    model.movePile(1,1,0);
    assertEquals(1, model.getPileHeight(0));
  }

  @Test
  public void testWhiteheadKlondikeMPF() {
    KlondikeModel model = new WhiteheadKlondike();
    List<Card> deck = new ArrayList<>();
    addCardWhitehead(deck, "A♡");
    addCardWhitehead(deck, "2♡");
    addCardWhitehead(deck, "2♠");
    addCardWhitehead(deck, "A♠");
    addCardWhitehead(deck, "3♠");
    addCardWhitehead(deck, "3♡");
    model.startGame(deck, false, 2, 1);
    assertEquals(1, model.getPileHeight(0));
    model.moveToFoundation(0, 0);
    assertEquals(0, model.getPileHeight(0));
    model.movePile(1,1,0);
    assertEquals(1, model.getPileHeight(0));
    try {
      model.moveToFoundation(0, 0);
    } catch (IllegalStateException e) {
      //suits doesn't match
    }
    assertEquals(1, model.getPileHeight(0));
    assertEquals(1, model.getPileHeight(1));
    model.moveToFoundation(1, 0);
    assertEquals(0, model.getPileHeight(1));

  }

  @Test
  public void testSetAndGetNumPile() {
    KlondikeCreator klondikeCreator = new KlondikeCreator();

    int numPiles = 5;
    klondikeCreator.setNumPile(numPiles);

    assertEquals(numPiles, klondikeCreator.getNumPile());
  }

  @Test
  public void testSetAndGetNumDraw() {
    KlondikeCreator klondikeCreator = new KlondikeCreator();

    int numDraws = 4;
    klondikeCreator.setNumDraw(numDraws);

    assertEquals(numDraws, klondikeCreator.getNumDraw());
  }

  @Test
  public void testSetAndGetNumRedraw() {
    KlondikeCreator klondikeCreator = new KlondikeCreator();
    int numRedraws = 2;
    klondikeCreator.setNumReDraw(numRedraws);

    assertEquals(numRedraws, klondikeCreator.getNumRedraw());
  }

  @Test
  public void testCombineSettings() {
    KlondikeCreator klondikeCreator = new KlondikeCreator();

    klondikeCreator.setNumPile(5)
            .setNumDraw(2)
            .setNumReDraw(1);

    // Check if the properties were set correctly
    assertEquals(5, klondikeCreator.getNumPile());
    assertEquals(2, klondikeCreator.getNumDraw());
    assertEquals(1, klondikeCreator.getNumRedraw());
  }

}
