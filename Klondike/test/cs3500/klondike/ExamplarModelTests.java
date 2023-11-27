package cs3500.klondike;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * Tests for Examplars.
 */
public class ExamplarModelTests {
  KlondikeModel game;
  List<Card> deck;

  private void init() {
    this.game = new BasicKlondike();
    this.deck = this.game.getDeck();
  }

  private void addCard(List<Card> deck, String s) {
    KlondikeModel game = new BasicKlondike();
    deck.add(game.getDeck().stream().filter(c -> c.toString()
            .equals(s)).collect(Collectors.toList()).get(0));
  }

  // Tests for movePile //////////////////////////////////////////////////////////////////////////


  //chaffs 11 & 12
  @Test(expected = IllegalArgumentException.class)
  public void testMoveToSamePile() {
    init();
    game.startGame(game.getDeck(), false, 5, 1);
    game.movePile(1, 1, 1);
  }

  //chaffs 10, 11, 12
  @Test(expected = IllegalStateException.class)
  public void testMoveFirstToSecond() {
    init();
    game.startGame(deck, false, 7, 1);
    game.movePile(0,1, 1);
  }

  //chaff 10 and 12
  @Test(expected = IllegalStateException.class)
  public void testMoveFirstToAndBack() {
    init();
    game.startGame(deck, false, 7, 1);
    game.movePile(0,1, 6);
    game.movePile(6,1, 0);
  }

  //chaff 13
  @Test(expected = IllegalArgumentException.class)
  public void testMoveTwoToSecondPile() {
    init();
    game.startGame(deck, false, 7, 1);
    game.movePile(0,2, 1);
  }


  @Test
  public void testCheckForCardDuplicationWhenMovePile() {
    KlondikeModel game = new BasicKlondike();
    List<Card> deck = game.getDeck().stream().filter(c ->
            c.toString().equals("A♢") ||
                    c.toString().equals("A♣") ||
                    c.toString().equals("A♡") ||
                    c.toString().equals("A♠")).collect(Collectors.toList());
    game.startGame(deck, false, 2, 1);
    Assert.assertEquals(1, game.getPileHeight(0));
    game.moveToFoundation(0, 0);
    Assert.assertEquals(0, game.getPileHeight(0));
    Assert.assertEquals(1, game.getScore());
    game.moveToFoundation(1, 1);
    Assert.assertEquals(2, game.getScore());
  }

  //2 3 4 5 6 7 8 9 10 11 12 13
  @Test
  public void testLegal() {
    KlondikeModel game = new BasicKlondike();
    List<Card> deck = new ArrayList<>();
    addCard(deck,"3♠");// 3♠ A♠
    addCard(deck,"A♠");// 2♢ A♢
    addCard(deck,"A♢");
    addCard(deck,"2♢");//1st draw
    addCard(deck,"A♣");
    addCard(deck,"2♣");
    addCard(deck,"2♠");
    addCard(deck,"3♣");
    addCard(deck,"3♢");
    Assert.assertEquals(9, deck.size());
    game.startGame(deck, false, 2, 1);
    Assert.assertEquals(1, game.getDrawCards().size());
    Assert.assertEquals("2♢", game.getDrawCards().get(0).toString());
    game.moveDraw(0);
    Assert.assertEquals("A♣", game.getDrawCards().get(0).toString());
    game.moveDrawToFoundation(0);//clover pile 0
    Assert.assertEquals("2♣", game.getDrawCards().get(0).toString());
    game.moveToFoundation(1,1);//diamonds pile 1
    game.moveDrawToFoundation(0);
    Assert.assertEquals("2♠", game.getDrawCards().get(0).toString());
    game.movePile(1,1,0);
    game.moveToFoundation(0, 2);//spades pile 2
    game.moveToFoundation(0, 1);
    game.moveDrawToFoundation(2);
    game.moveToFoundation(0, 2);
    Assert.assertEquals("3♣", game.getDrawCards().get(0).toString());
    game.discardDraw();
    Assert.assertEquals("3♢", game.getDrawCards().get(0).toString());
    game.moveDrawToFoundation(1);
    Assert.assertEquals("3♣", game.getDrawCards().get(0).toString());
    game.moveDrawToFoundation(0);
    Assert.assertEquals(9, game.getScore());
    Assert.assertTrue(game.isGameOver());
  }

  @Test
  public void testDiscard() {
    KlondikeModel game = new BasicKlondike();
    List<Card> deck = new ArrayList<>();
    addCard(deck, "3♠");
    addCard(deck, "A♠");
    addCard(deck, "A♢");
    addCard(deck, "2♢");
    addCard(deck, "A♣");
    addCard(deck, "2♣");
    addCard(deck, "2♠");//draw start
    addCard(deck, "3♣");
    addCard(deck, "3♢");
    Assert.assertEquals(9, deck.size());
    game.startGame(deck, false, 3, 3);
    Assert.assertEquals("2♠", game.getDrawCards().get(0).toString());
    game.discardDraw();
    Assert.assertEquals("3♣", game.getDrawCards().get(0).toString());
    game.discardDraw();
    Assert.assertEquals("3♢", game.getDrawCards().get(0).toString());
    game.discardDraw();
    Assert.assertEquals("2♠", game.getDrawCards().get(0).toString());
  }

  @Test
  public void testMultPile() {
    KlondikeModel game = new BasicKlondike();
    List<Card> deck = new ArrayList<>();
    addCard(deck, "2♢");//2♢ 3♣
    addCard(deck, "3♣");//   A♠
    addCard(deck, "A♠");
    addCard(deck, "2♠");//first draw
    addCard(deck, "3♠");
    addCard(deck, "A♢");
    addCard(deck, "3♢");
    addCard(deck, "2♣");
    addCard(deck, "A♣");
    Assert.assertEquals(9, deck.size());
    game.startGame(deck, false, 2, 1);
    game.movePile(1, 1, 0);
    game.movePile(0, 2, 1);
    Assert.assertEquals("2♠", game.getDrawCards().get(0).toString());
    game.moveToFoundation(1,0);
    game.moveDrawToFoundation(0);
    game.moveDrawToFoundation(0);
    game.moveDrawToFoundation(1);
    game.moveToFoundation(1,1);
    game.moveDrawToFoundation(1);
    game.discardDraw();
    game.moveDrawToFoundation(2);
    game.moveDrawToFoundation(2);
    game.moveToFoundation(1,2);
    Assert.assertTrue(game.isGameOver());
  }
  ////////////////////////////////////////////////////////////////////////////////////////////////


  // Tests for moveDraw //////////////////////////////////////////////////////////////////////////


  // 0 7 8 9
  @Test(expected = IllegalStateException.class)
  public void testMovePileToFound() {
    init();
    game.startGame(deck, false, 7, 1);
    game.moveToFoundation(1, 0);
  }

  // 5
  @Test(expected = IllegalStateException.class)
  public void testMoveDrawToFound() {
    init();
    game.startGame(deck, false, 7, 1);
    game.moveDrawToFoundation(0);
  }

  // 6
  @Test(expected = IllegalStateException.class)
  public void testMoveDrawToPile() {
    init();
    game.startGame(deck, false, 7, 1);
    game.discardDraw();
    game.moveDraw(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveDrawToFoundInvalid() {
    init();
    game.startGame(deck, false, 7, 1);
    game.moveDrawToFoundation(-5);
  }

  @Test(expected = IllegalStateException.class)
  public void testDiscardThenDrawToFoundation() {
    init();
    game.startGame(game.getDeck(), false, 7, 1);
    game.discardDraw();
    game.moveDrawToFoundation(1);
  }

  @Test
  public void testDrawCards() {
    KlondikeModel game = new BasicKlondike();
    List<Card> deck = new ArrayList<>();
    addCard(deck,"A♠");
    addCard(deck,"A♢");
    addCard(deck,"2♢");
    addCard(deck,"2♠");
    addCard(deck,"3♢");
    addCard(deck,"3♠");
    game.startGame(deck, false, 2, 1);
    Assert.assertEquals(1, game.getNumDraw());
    Assert.assertEquals("2♠", game.getDrawCards().get(0).toString());
    game.moveToFoundation(0, 0);
    game.moveDrawToFoundation(0);
    Assert.assertEquals("3♢", game.getDrawCards().get(0).toString());
    game.discardDraw();
    Assert.assertEquals("3♠", game.getDrawCards().get(0).toString());
    game.moveDrawToFoundation(0);
    Assert.assertEquals("3♢", game.getDrawCards().get(0).toString());
    Assert.assertEquals(1, game.getDrawCards().size());

  }

  @Test
  public void testKingPile() {
    KlondikeModel game = new BasicKlondike();
    List<Card> deck = new ArrayList<>();
    addCard(deck,"A♢");
    addCard(deck,"A♠");
    addCard(deck,"2♢");

    addCard(deck,"K♠");
    addCard(deck,"K♢");
    addCard(deck,"2♠");
    addCard(deck,"3♢");
    addCard(deck,"3♠");
    addCard(deck,"4♢");
    addCard(deck,"4♠");
    addCard(deck,"5♢");
    addCard(deck,"5♠");
    addCard(deck,"6♢");
    addCard(deck,"6♠");
    addCard(deck,"7♢");
    addCard(deck,"7♠");
    addCard(deck,"8♢");
    addCard(deck,"8♠");
    addCard(deck,"9♢");
    addCard(deck,"9♠");
    addCard(deck,"10♢");
    addCard(deck,"10♠");
    addCard(deck,"J♢");
    addCard(deck,"J♠");
    addCard(deck,"Q♢");
    addCard(deck,"Q♠");
    Assert.assertEquals(26, deck.size());
    game.startGame(deck, false, 2, 1);
    Assert.assertEquals("K♠", game.getDrawCards().get(0).toString());
    game.moveToFoundation(0,0);
    game.moveToFoundation(1,0);
    game.moveToFoundation(1,1);
    game.moveDraw(0);
    Assert.assertEquals("K♢", game.getDrawCards().get(0).toString());
    Assert.assertEquals("K♠", game.getCardAt(0, 0).toString());
    game.movePile(0, 1, 1);
    Assert.assertEquals("K♠", game.getCardAt(1, 0).toString());
    Assert.assertEquals("2♢", game.getCardAt(0).toString());
  }

  @Test
  public void testMovePile() {
    KlondikeModel game = new BasicKlondike();
    List<Card> deck = new ArrayList<>();
    addCard(deck,"A♠");
    addCard(deck,"A♢");
    addCard(deck,"2♠");
    addCard(deck,"2♢");
    addCard(deck,"3♢");
    addCard(deck,"3♠");
    addCard(deck,"4♢");
    addCard(deck,"4♠");
    game.startGame(deck, false, 3, 1);
    Assert.assertEquals("4♢", game.getDrawCards().get(0).toString());
    game.movePile(1,1,2); //moving 2♢ under 3♠
    game.movePile(0,1,2); //moving A♠ under 2♢
    Assert.assertEquals(5, game.getPileHeight(2));
    Assert.assertEquals(0, game.getPileHeight(0));
  }
}
