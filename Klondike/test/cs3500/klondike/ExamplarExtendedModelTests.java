package cs3500.klondike;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;
import cs3500.klondike.model.hw04.WhiteheadKlondike;
import cs3500.klondike.view.KlondikeTextualView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for examplar.
 */

public class ExamplarExtendedModelTests {

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
  public void testLimitedBaseCase() {
    KlondikeModel model = new LimitedDrawKlondike(1);
    List<Card> deck = new ArrayList<>();
    addCardLimited(deck, "A♡", 1);
    addCardLimited(deck, "2♠", 1);
    addCardLimited(deck, "2♡", 1);
    addCardLimited(deck, "A♠", 1);
    addCardLimited(deck, "3♠", 1);
    addCardLimited(deck, "3♡", 1);

    model.startGame(deck, false, 2, 3);
    assertEquals(3, model.getDrawCards().size()); // A♠ 3♠ 3♡ || 1 redraw left
    assertEquals("A♠", model.getDrawCards().get(0).toString());
    assertEquals("3♠", model.getDrawCards().get(1).toString());
    assertEquals("3♡", model.getDrawCards().get(2).toString());
    model.discardDraw();
    assertEquals(3, model.getDrawCards().size()); // 3♠ 3♡ A♠
    assertEquals("3♠", model.getDrawCards().get(0).toString());
    assertEquals("3♡", model.getDrawCards().get(1).toString());
    assertEquals("A♠", model.getDrawCards().get(2).toString());
    model.discardDraw();
    assertEquals(3, model.getDrawCards().size()); // 3♡ A♠ 3♠
    assertEquals("3♡", model.getDrawCards().get(0).toString());
    assertEquals("A♠", model.getDrawCards().get(1).toString());
    assertEquals("3♠", model.getDrawCards().get(2).toString());
    model.discardDraw();
    assertEquals(3, model.getDrawCards().size()); // A♠ 3♠ 3♡ || 0 redraw left
    assertEquals("A♠", model.getDrawCards().get(0).toString());
    assertEquals("3♠", model.getDrawCards().get(1).toString());
    assertEquals("3♡", model.getDrawCards().get(2).toString());
    model.discardDraw();
    assertEquals(2, model.getDrawCards().size()); // 3♠ 3♡
    assertEquals("3♠", model.getDrawCards().get(0).toString());
    assertEquals("3♡", model.getDrawCards().get(1).toString());
    //assertEquals("A♠", model.getDrawCards().get(2).toString());
    model.discardDraw();
    assertEquals(1, model.getDrawCards().size()); // 3♡
    assertEquals("3♡", model.getDrawCards().get(0).toString());
    model.discardDraw();
    assertEquals(0, model.getDrawCards().size()); //
  }

  @Test
  public void testAllCardsFaceUp() {
    KlondikeModel model = new WhiteheadKlondike();
    List<Card> deck = model.getDeck();
    model.startGame(deck, false, 2, 1);
    assertTrue(model.isCardVisible(0,0));
    assertTrue(model.isCardVisible(1,0));
    assertTrue(model.isCardVisible(1,1));
  }

  @Test(expected = IllegalStateException.class)
  public void testLimitedBaseCaseException() {
    KlondikeModel model = new LimitedDrawKlondike(0);
    List<Card> deck = new ArrayList<>();
    addCardLimited(deck, "A♡", 1);
    addCardLimited(deck, "2♠", 1);
    addCardLimited(deck, "2♡", 1);
    addCardLimited(deck, "A♠", 1);
    addCardLimited(deck, "3♠", 1);
    addCardLimited(deck, "3♡", 1);

    model.startGame(deck, false, 2, 3);
    assertEquals(3, model.getDrawCards().size()); // A♠ 3♠ 3♡ || 0 redraw left
    assertEquals("A♠", model.getDrawCards().get(0).toString());
    assertEquals("3♠", model.getDrawCards().get(1).toString());
    assertEquals("3♡", model.getDrawCards().get(2).toString());
    model.discardDraw();
    assertEquals(2, model.getDrawCards().size()); // 3♠ 3♡
    assertEquals("3♠", model.getDrawCards().get(0).toString());
    assertEquals("3♡", model.getDrawCards().get(1).toString());
    //assertEquals("A♠", model.getDrawCards().get(2).toString());
    model.discardDraw();
    assertEquals(1, model.getDrawCards().size()); // 3♡
    assertEquals("3♡", model.getDrawCards().get(0).toString());
    model.discardDraw();
    assertEquals(0, model.getDrawCards().size()); //
    model.discardDraw();
  }

  @Test
  public void moveToSameSuit() {
    KlondikeModel model = new WhiteheadKlondike();
    List<Card> deck = new ArrayList<>();
    addCardWhitehead(deck, "A♡");
    addCardWhitehead(deck, "2♠");
    addCardWhitehead(deck, "A♠");
    addCardWhitehead(deck, "2♡");
    addCardWhitehead(deck, "3♠");
    addCardWhitehead(deck, "3♡");
    addCardWhitehead(deck, "4♠");
    addCardWhitehead(deck, "4♡");
    model.startGame(deck, false, 3, 1);
    assertEquals(2, model.getPileHeight(1));
    model.movePile(0,1,1);
    assertEquals(3, model.getPileHeight(1));
    assertEquals(3, model.getPileHeight(2));
    model.movePile(1,2,2);
    assertEquals(5, model.getPileHeight(2));
  }

  @Test
  public void moveToDiffColor() {
    KlondikeModel model = new WhiteheadKlondike();
    List<Card> deck = new ArrayList<>();
    addCardWhitehead(deck, "A♡");
    addCardWhitehead(deck, "2♡");
    addCardWhitehead(deck, "2♠");
    addCardWhitehead(deck, "A♠");
    addCardWhitehead(deck, "3♠");
    addCardWhitehead(deck, "3♡");
    model.startGame(deck, false, 2, 1);
    assertEquals(2, model.getPileHeight(1));
    try {
      model.movePile(0, 1, 1);
    } catch (IllegalStateException e) {
      //can't move to different color
    }
    assertEquals(2, model.getPileHeight(1));
  }

  @Test
  public void moveToSameColorThenMoveMultipleToDiffSuit() {
    KlondikeModel model = new WhiteheadKlondike();
    List<Card> deck = new ArrayList<>();
    addCardWhitehead(deck, "A♡");
    addCardWhitehead(deck, "2♡");
    addCardWhitehead(deck, "A♢");
    addCardWhitehead(deck, "2♢");
    addCardWhitehead(deck, "3♢");
    addCardWhitehead(deck, "3♡");
    addCardWhitehead(deck, "4♢");
    addCardWhitehead(deck, "4♡");
    model.startGame(deck, false, 3, 1);
    assertEquals(2, model.getPileHeight(1));
    model.movePile(0,1,1);
    assertEquals(3, model.getPileHeight(1));
    assertEquals(3, model.getPileHeight(2));
    System.out.print(new KlondikeTextualView(model));
    try {
      model.movePile(1, 2, 2);
    } catch (IllegalStateException e) {
      //can't move build with different suits
    }
    assertEquals(3, model.getPileHeight(2));
  }

  @Test
  public void moveToSameColorThenMoveMultipleToSameSuitThenMoveToEmpty() {
    KlondikeModel model = new WhiteheadKlondike();
    List<Card> deck = new ArrayList<>();
    addCardWhitehead(deck, "A♡");
    addCardWhitehead(deck, "A♢");
    addCardWhitehead(deck, "2♢");
    addCardWhitehead(deck, "2♡");
    addCardWhitehead(deck, "3♢");
    addCardWhitehead(deck, "3♡");
    addCardWhitehead(deck, "4♢");
    addCardWhitehead(deck, "4♡");
    model.startGame(deck, false, 3, 1);
    assertEquals(2, model.getPileHeight(1));
    model.movePile(0,1,1);
    assertEquals(3, model.getPileHeight(1));
    assertEquals(3, model.getPileHeight(2));
    model.movePile(1, 2, 2);
    assertEquals(5, model.getPileHeight(2));
    assertEquals(0, model.getPileHeight(0));
    model.movePile(2, 3, 0);
  }

  //TODO: move to same color, but would throw exception when move to same color but different suit
  //TODO: move a correct build but not same suit to an empty pile
  //TODO: move a correct build and same suit to an empty pile
}
