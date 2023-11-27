package cs3500.klondike;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.CardPlay;
import cs3500.klondike.model.hw02.CardRank;
import cs3500.klondike.model.hw02.CardSuit;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.view.KlondikeTextualView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for KlondikeModels.
 */
public class KlondikeModelTest {
  Card kingClubDown = new CardPlay(CardRank.King, CardSuit.Club, false);
  Card aceSpadeUp = new CardPlay(CardRank.Ace, CardSuit.Spade, true);
  Card twoHeartDown = new CardPlay(CardRank.Two, CardSuit.Heart, false);
  Card twoHeartUp = new CardPlay(CardRank.Two, CardSuit.Heart, true);
  Card jackDiamondUp = new CardPlay(CardRank.Jack, CardSuit.Diamond, true);

  BasicKlondike game;
  List<Card> deck;

  Card newCard1S;
  Card newCard1H;
  Card newCard2S;
  Card newCard2H;
  Card newCard3S;
  Card newCard3H;
  Card newCard4S;
  Card newCard4H;
  Card newCard5S;
  Card newCard5H;
  List<Card> newDeck;

  private void init() {
    this.game = new BasicKlondike();
    this.deck = this.game.getDeck();

    this.newCard1S = new CardPlay(CardRank.Ace, CardSuit.Spade, false);
    this.newCard1H = new CardPlay(CardRank.Ace, CardSuit.Heart, false);
    this.newCard2S = new CardPlay(CardRank.Two, CardSuit.Spade, false);
    this.newCard2H = new CardPlay(CardRank.Two, CardSuit.Heart, false);
    this.newCard3S = new CardPlay(CardRank.Three, CardSuit.Spade, false);
    this.newCard3H = new CardPlay(CardRank.Three, CardSuit.Heart, false);
    this.newCard4S = new CardPlay(CardRank.Four, CardSuit.Spade, false);
    this.newCard4H = new CardPlay(CardRank.Four, CardSuit.Heart, false);
    this.newCard5S = new CardPlay(CardRank.Five, CardSuit.Spade, false);
    this.newCard5H = new CardPlay(CardRank.Five, CardSuit.Heart, false);
    this.newDeck = new ArrayList<Card>();
    newDeck.add(newCard1S);
    newDeck.add(newCard3S);
    newDeck.add(newCard2H);
    newDeck.add(newCard4H);
    newDeck.add(newCard5S);
    newDeck.add(newCard1H);
    newDeck.add(newCard3H);
    newDeck.add(newCard2S);
    newDeck.add(newCard4S);
    newDeck.add(newCard5H);
  }

  private void addCard(List<Card> deck, String s) {
    KlondikeModel game = new BasicKlondike();
    deck.add(game.getDeck().stream().filter(c -> c.toString()
            .equals(s)).collect(Collectors.toList()).get(0));
  }

  @Test
  public void testCardToString() {
    assertEquals("K♣", kingClubDown.toString());
    assertEquals("A♠", aceSpadeUp.toString());
  }

  // Testing to see if there are 52 card in the deck every card is present
  @Test
  public void testgetDeck() {
    assertNull(deck);
    init();
    // count how many cards in the deck
    assertEquals(52, deck.size());

    // check to see all cards are in the deck
    for (CardSuit suit : CardSuit.values()) {
      for (CardRank rank: CardRank.values()) {
        assertTrue(deck.contains(new CardPlay(rank, suit, false)));
      }
    }
  }

  @Test
  public void testEquals() {
    assertEquals(twoHeartDown, twoHeartUp);
    assertEquals(twoHeartDown, twoHeartDown);
    assertNotEquals(jackDiamondUp, twoHeartUp);
    assertNotEquals(jackDiamondUp, twoHeartDown);
  }

  @Test
  public void testStartGame() {
    BasicKlondike game = new BasicKlondike();
    List<Card> deck = game.getDeck();
    game.startGame(deck, false, 7, 2);
    KlondikeTextualView view = new KlondikeTextualView(game);
    assertEquals(7, game.getNumPiles());
    assertEquals(2, game.getNumDraw());
    assertEquals(4, game.getNumFoundations());
    System.out.print(view);
  }


  @Test
  public void testMovePile() {
    init();
    game.startGame(newDeck, false,2,1);
    KlondikeTextualView view = new KlondikeTextualView(game);
    Card actualCard = game.getCardAt(0,0); // row 0, column 0
    Card expectedCard = new CardPlay(CardRank.Ace, CardSuit.Spade, true);
    assertEquals(expectedCard, actualCard);
    assertEquals(1, game.getPileHeight(0));
    assertEquals(2, game.getPileHeight(1));
    System.out.print(view.toString());
    game.movePile(0,1,1);
    System.out.print(view.toString());
    assertEquals(0, game.getPileHeight(0));
    assertEquals(3, game.getPileHeight(1));
    try {
      game.getCardAt(0, 0); // row 0, column 0;
    }
    catch (IllegalArgumentException e) {
      //there is no card
    }
    Card actualCard2 = game.getCardAt(1,2); // row 2, column 1;
    Assert.assertEquals(newCard1S, actualCard2);
    try {
      game.movePile(1,1,0);
    }
    catch (IllegalStateException e) {
      //move is not valid
    }
  }

  @Test
  //test 3 methods: moveDraw, discardDraw, getDrawCards
  public void testMoveDrawAndDiscardDrawAndGetDrawCards() {
    init();
    game.startGame(newDeck, false,2,1);
    KlondikeTextualView view = new KlondikeTextualView(game);
    assertEquals(newCard4H, game.getDrawCards().get(0)); //
    System.out.print(view.toString());

    try {
      game.moveDraw(0);
    }
    catch (IllegalStateException e) {
      //not a valid move
    }
    System.out.print(view.toString());
    game.discardDraw();
    System.out.print(view.toString());
    assertEquals(newCard5S, game.getDrawCards().get(0));
    game.movePile(0,1,1);
    System.out.print(view.toString());
    try {
      game.getCardAt(0, 0); // row 0, column 0;
    }
    catch (IllegalArgumentException e) {
      //There is no card
    }

    game.moveDraw(0);
    assertEquals(newCard5S, game.getCardAt(0, 0));
    assertTrue(game.isCardVisible(0,0));
  }

  @Test
  public void testDiscardDraw() {
    init();
    List<Card> deck10 = new ArrayList<>();
    deck10.add(new CardPlay(CardRank.Ace, CardSuit.Club, false));
    deck10.add(new CardPlay(CardRank.Ace, CardSuit.Diamond,false));
    deck10.add(new CardPlay(CardRank.Two, CardSuit.Club,false));
    deck10.add(new CardPlay(CardRank.Two, CardSuit.Diamond,false));
    deck10.add(new CardPlay(CardRank.Three, CardSuit.Club,false));
    deck10.add(new CardPlay(CardRank.Three, CardSuit.Diamond,false));
    game.startGame(deck10, false, 2, 1);
    KlondikeTextualView view = new KlondikeTextualView(game);
    System.out.print(view);
    game.moveToFoundation(0,0);
    System.out.print(view);
    game.discardDraw();
    System.out.print(view);
    game.moveDraw(0);
    System.out.print(view);
    game.moveToFoundation(1, 0);
    System.out.print(view);
    try {
      game.moveToFoundation(0, 1);
    }
    catch (IllegalStateException e) {
      //not a valid move
    }
    game.moveToFoundation(1, 1);
    game.discardDraw();
    game.discardDraw();
    System.out.print(view);
    game.moveDraw(1);
    System.out.print(view);
    assertEquals(1, game.getPileHeight(0));
    assertEquals("3♣", game.getCardAt(0,0).toString());
    game.moveDraw(0);
    System.out.print(view);
    try {
      game.moveDraw(0);
    }
    catch (IllegalStateException e) {
      //no draw cards
    }
    game.moveToFoundation(0, 1);
    assertFalse(game.isGameOver());
    game.moveToFoundation(1, 1);
    game.moveToFoundation(0, 0);
    System.out.print(view);
    assertTrue(game.isGameOver());
  }



  @Test
  //test 3 methods: moveToFoundation, getCardAt, getNumFoundation
  public void testMoveToFoundationAndGetCardAtAndGetNumFoundation() {
    init();
    game.startGame(newDeck, false, 2, 1);
    assertEquals(2, game.getNumFoundations());
    assertEquals(newCard1S, game.getCardAt(0,0));
    try {
      game.getCardAt(0); // row 0, column 0;
    }
    catch (IllegalArgumentException e) {
      //There is no card in that foundation pile
    }
    game.moveToFoundation(0, 0);
    assertEquals(newCard1S, game.getCardAt(0));
    assertEquals(1, game.getScore());
    assertEquals(newCard2H, game.getCardAt(1,1));
    try {
      game.moveToFoundation(1, 0);
    }
    catch (IllegalStateException e) {
      //Suits doesn't match
    }
  }

  @Test
  public void testMoveDrawToFoundation() {
    init();
    game.startGame(newDeck, false, 2, 1);
    assertEquals(newCard4H, game.getDrawCards().get(0)); //
    game.discardDraw();
    assertEquals(newCard5S, game.getDrawCards().get(0));
    game.discardDraw();
    assertEquals(newCard1H, game.getDrawCards().get(0));
    assertTrue(newCard1H.isFaceUp());
    game.moveDrawToFoundation(1);
    assertEquals(newCard1H, game.getCardAt(1));
    assertEquals(1, game.getScore());
    game.moveToFoundation(0, 0);
    assertEquals(newCard1S, game.getCardAt(0));
    assertEquals(2, game.getScore());
  }

  @Test
  public void testGetPileHeight() {
    init();
    game.startGame(deck, false,4,1);
    assertEquals(1, game.getPileHeight(0));
    assertEquals(2, game.getPileHeight(1));
    assertEquals(3, game.getPileHeight(2));
    assertEquals(4, game.getPileHeight(3));
  }

  @Test
  public void testGetCardAtAndIsCardVisible() {
    init();
    game.startGame(deck, false,4,1);
    KlondikeTextualView view = new KlondikeTextualView(game);

    Card actualCard = game.getCardAt(0,0);
    Card expectedCard = new CardPlay(CardRank.Ace, CardSuit.Spade, true);
    assertEquals(expectedCard, actualCard);

    assertTrue(game.isCardVisible(0,0));
    assertFalse(game.isCardVisible(1,0));
    assertTrue(game.isCardVisible(1,1));
    assertFalse(game.isCardVisible(2,0));

    try {
      game.getCardAt(1,0);
    }
    catch (IllegalArgumentException e) {
      //card is not visible
    }

    try {
      game.getCardAt(2,0);
    }
    catch (IllegalArgumentException e) {
      //card is not visible
    }

    Card actualCard4 = game.getCardAt(1,1);
    Card expectedCard4 = new CardPlay(CardRank.Five, CardSuit.Spade, true);
    assertEquals(expectedCard4, actualCard4);
  }

  @Test
  public void testGetNumRowsAndGetNumPilesAndGetNumDraw() {
    init();
    game.startGame(deck, false,2,1);
    assertEquals(2, game.getNumRows());
    assertEquals(2, game.getNumPiles());
    assertEquals(1, game.getNumDraw());
  }


  /*
  @Test
  public void testValid() {
    init();
    Collections.shuffle(deck);
    Map<CardSuit, List<Card>> map = game.isDeckValidHelper(deck);
    for (CardSuit suit : map.keySet()) {
      map.get(suit).sort(Comparator.comparing(Card::getRank));
    }
    Assert.assertTrue(game.isDeckValid(deck));
    List<Card> deck2 = new ArrayList<>();
    deck2.add(new CardPlay(CardRank.Ace, CardSuit.Spade, true));
    deck2.add(new CardPlay(CardRank.Ace, CardSuit.Spade, true));
    deck2.add(new CardPlay(CardRank.Ace, CardSuit.Spade, true));
    deck2.add(new CardPlay(CardRank.Ace, CardSuit.Heart, true));
    deck2.add(new CardPlay(CardRank.Ace, CardSuit.Heart, true));
    Assert.assertTrue(new BasicKlondike().isDeckValid(deck2));
    List<Card> deck3 = new ArrayList<>();
    deck3.add(new CardPlay(CardRank.Ace, CardSuit.Spade, true));
    deck3.add(new CardPlay(CardRank.Two, CardSuit.Spade, true));
    deck3.add(new CardPlay(CardRank.Three, CardSuit.Spade, true));
    deck3.add(new CardPlay(CardRank.Ace, CardSuit.Heart, true));
    deck3.add(new CardPlay(CardRank.Two, CardSuit.Heart, true));
    Assert.assertFalse(new BasicKlondike().isDeckValid(deck3));
    List<Card> deck4 = new ArrayList<>();
    deck4.add(new CardPlay(CardRank.Ace, CardSuit.Spade, true));
    deck4.add(new CardPlay(CardRank.Two, CardSuit.Spade, true));
    deck4.add(new CardPlay(CardRank.Three, CardSuit.Spade, true));
    deck4.add(new CardPlay(CardRank.Ace, CardSuit.Heart, true));
    deck4.add(new CardPlay(CardRank.Two, CardSuit.Heart, true));
    deck4.add(new CardPlay(CardRank.Three, CardSuit.Heart, true));
    Assert.assertTrue(new BasicKlondike().isDeckValid(deck4));
  }
   */

  @Test
  public void testToString() {
    init();
    game.startGame(deck, false, 4, 1);
    KlondikeTextualView view = new KlondikeTextualView(game);

    // Define the expected output based on the game state
    String expectedOutput = "Draw: J♠\n" +
            "Foundation: <none>, <none>, <none>, <none>\n" +
            " A♠  ?  ?  ?\n" +
            "    5♠  ?  ?\n" +
            "       8♠  ?\n" +
            "         10♠\n" +
            "\n";

    // Check if the toString() method produces the expected output
    assertEquals(expectedOutput, view.toString());
  }

  /*
  @Test
  public void testIsPileMoveAvailable() {
    BasicKlondike game = new BasicKlondike();
    List<Card> deck = new ArrayList<Card>();
    deck.add(new CardPlay(CardRank.Two, CardSuit.Spade, false));
    deck.add(new CardPlay(CardRank.Three, CardSuit.Spade, false));
    deck.add(new CardPlay(CardRank.Ace, CardSuit.Spade, false));
    deck.add(new CardPlay(CardRank.Ace, CardSuit.Heart, false));
    deck.add(new CardPlay(CardRank.Two, CardSuit.Heart, false));
    deck.add(new CardPlay(CardRank.Three, CardSuit.Heart, false));

    game.startGame(deck, false, 3, 1);
    assertTrue(game.isPileMoveAvailable(1, 1));
    assertTrue(game.canPileMoveToPile());
    game.movePile(1, 1, 0);
    assertTrue(game.canPileMoveToPile());
    game.movePile(0, 2, 2);
    assertFalse(game.isPileMoveAvailable(2, 3));
    assertFalse(game.canPileMoveToPile());
    //testing empty srcPile
    //assertFalse(game.isPileMoveAvailable(0, 3));
  }
   */

  @Test
  public void testGameOver() {
    List<Card> deck4 = new ArrayList<>();
    deck4.add(new CardPlay(CardRank.Two, CardSuit.Spade, false));
    deck4.add(new CardPlay(CardRank.Three, CardSuit.Spade, false));
    deck4.add(new CardPlay(CardRank.Ace, CardSuit.Spade, false));
    deck4.add(new CardPlay(CardRank.Ace, CardSuit.Heart, false));
    deck4.add(new CardPlay(CardRank.Two, CardSuit.Heart, false));
    deck4.add(new CardPlay(CardRank.Three, CardSuit.Heart, false));
    //Assert.assertTrue(new BasicKlondike().isDeckValid(deck4));
    BasicKlondike game = new BasicKlondike();
    game.startGame(deck4, false, 2, 1);
    Assert.assertFalse(game.isGameOver());
    System.out.print(new KlondikeTextualView(game));
    game.moveDrawToFoundation(1);
    System.out.print(new KlondikeTextualView(game));
    Assert.assertFalse(game.isGameOver());
    game.moveToFoundation(1,0);
    System.out.print(new KlondikeTextualView(game));
    //Assert.assertFalse(game.isGameOver());
    game.moveToFoundation(0,0);
    System.out.print(new KlondikeTextualView(game));
    Assert.assertFalse(game.isGameOver());
    //game.moveToFoundation(1,0);
    System.out.print(new KlondikeTextualView(game));
    Assert.assertFalse(game.isGameOver());
    game.moveDrawToFoundation(1);
    System.out.print(new KlondikeTextualView(game));
    Assert.assertFalse(game.isGameOver());
    //game.moveDrawToFoundation(1);
    System.out.print(new KlondikeTextualView(game));
    //Assert.assertTrue(game.isGameOver());
  }

  @Test
  public void testFailedGame() {
    List<Card> deck4 = new ArrayList<>();
    deck4.add(new CardPlay(CardRank.Ace, CardSuit.Spade, false));
    deck4.add(new CardPlay(CardRank.Two, CardSuit.Spade, false));
    deck4.add(new CardPlay(CardRank.Three, CardSuit.Spade, false));
    deck4.add(new CardPlay(CardRank.Ace, CardSuit.Heart, false));
    deck4.add(new CardPlay(CardRank.Two, CardSuit.Heart, false));
    deck4.add(new CardPlay(CardRank.Three, CardSuit.Heart, false));
    //Assert.assertTrue(new BasicKlondike().isDeckValid(deck4));
    BasicKlondike game = new BasicKlondike();
    game.startGame(deck4, false, 2, 1);
    Assert.assertFalse(game.isGameOver());
    System.out.print(new KlondikeTextualView(game));
    game.moveDrawToFoundation(1);
    game.moveToFoundation(0, 0);
    System.out.print(new KlondikeTextualView(game));
    Assert.assertFalse(game.isGameOver());
    game.moveDrawToFoundation(1);
    game.moveDrawToFoundation(1);
    System.out.print(new KlondikeTextualView(game));
    Assert.assertTrue(game.isGameOver());
  }

  @Test
  public void testGameWMoreDraw() {
    BasicKlondike game = new BasicKlondike();
    List<Card> deck = game.getDeck();
    game.startGame(deck, false, 7, 3);
    Assert.assertEquals(new CardPlay(CardRank.Three, CardSuit.Club, true),
            game.getDrawCards().get(0));
    game.moveToFoundation(0, 3);
    Assert.assertFalse(game.isGameOver());
    game.moveToFoundation(2, 2);
    Assert.assertFalse(game.isGameOver());
    game.movePile(5, 1, 0);
    Assert.assertFalse(game.isGameOver());
    game.movePile(2, 1, 4);
    Assert.assertFalse(game.isGameOver());
    game.discardDraw();
    Assert.assertFalse(game.isGameOver());
    game.discardDraw();
    Assert.assertFalse(game.isGameOver());
    game.moveDraw(3);
    game.discardDraw();
    Assert.assertFalse(game.isGameOver());
    game.discardDraw();
    Assert.assertFalse(game.isGameOver());
    game.discardDraw();
    Assert.assertFalse(game.isGameOver());
    game.discardDraw();
    Assert.assertFalse(game.isGameOver());
    game.moveDraw(5);
    Assert.assertFalse(game.isGameOver());
    game.discardDraw();
    Assert.assertFalse(game.isGameOver());
    game.moveDraw(0);
    Assert.assertFalse(game.isGameOver());
    game.movePile(5, 2, 0);
    Assert.assertFalse(game.isGameOver());
    game.movePile(5, 1, 4);
    Assert.assertFalse(game.isGameOver());
    game.movePile(2, 1, 5);
    Assert.assertFalse(game.isGameOver());
    game.movePile(5, 2, 3);
    Assert.assertFalse(game.isGameOver());
    game.moveDraw(2);
    game.moveDrawToFoundation(0);
    Assert.assertFalse(game.isGameOver());
    game.moveDrawToFoundation(0);
    Assert.assertFalse(game.isGameOver());
    game.moveDrawToFoundation(0);
    Assert.assertFalse(game.isGameOver());
    game.moveDrawToFoundation(0);
    Assert.assertFalse(game.isGameOver());
    game.moveDrawToFoundation(0);
    Assert.assertFalse(game.isGameOver());
    game.moveDrawToFoundation(0);
    Assert.assertFalse(game.isGameOver());
    game.moveDrawToFoundation(0);
    Assert.assertFalse(game.isGameOver());
    game.moveDrawToFoundation(0);
    Assert.assertFalse(game.isGameOver());
    game.moveDraw(0);
    Assert.assertFalse(game.isGameOver());
    game.movePile(1, 1, 0);
    Assert.assertFalse(game.isGameOver());
    game.moveToFoundation(1, 3);
    Assert.assertFalse(game.isGameOver());
    game.discardDraw();
    Assert.assertFalse(game.isGameOver());
    game.moveDraw(5);
    Assert.assertFalse(game.isGameOver());
    game.moveDraw(2);
    Assert.assertFalse(game.isGameOver());
    game.moveDraw(1);
    Assert.assertFalse(game.isGameOver());
    game.movePile(5, 2, 1);
    Assert.assertFalse(game.isGameOver());
    game.discardDraw();
    Assert.assertFalse(game.isGameOver());
    game.discardDraw();
    Assert.assertFalse(game.isGameOver());
    game.discardDraw();
    Assert.assertFalse(game.isGameOver());
    game.moveDraw(4);
    Assert.assertFalse(game.isGameOver());
    game.getCardAt(3, 3);
    Assert.assertFalse(game.isGameOver());
    game.movePile(3, 4, 4);
    Assert.assertFalse(game.isGameOver());
    game.moveToFoundation(3, 2);
    Assert.assertFalse(game.isGameOver());
    game.moveToFoundation(4, 3);
    Assert.assertFalse(game.isGameOver());
    game.movePile(3, 1, 1);
    Assert.assertFalse(game.isGameOver());
    game.moveToFoundation(3, 3);
    Assert.assertFalse(game.isGameOver());
    game.discardDraw();
    Assert.assertFalse(game.isGameOver());
    game.discardDraw();
    Assert.assertFalse(game.isGameOver());
    game.moveDraw(2);
    Assert.assertFalse(game.isGameOver());
    game.discardDraw();
    Assert.assertFalse(game.isGameOver());
    game.moveDraw(4);
    Assert.assertFalse(game.isGameOver());
    game.movePile(4, 8, 2);
    Assert.assertFalse(game.isGameOver());
    game.movePile(4, 1,0);
    Assert.assertFalse(game.isGameOver());
    game.movePile(6, 1, 4);
    Assert.assertFalse(game.isGameOver());
    game.moveToFoundation(6, 1);
    Assert.assertFalse(game.isGameOver());
    game.moveToFoundation(4, 1);
    Assert.assertFalse(game.isGameOver());
    game.moveToFoundation(2, 1);
    Assert.assertFalse(game.isGameOver());
    game.moveDrawToFoundation(1);
    Assert.assertFalse(game.isGameOver());
    game.moveToFoundation(4, 2);
    Assert.assertFalse(game.isGameOver());
    game.moveToFoundation(2, 2);
    Assert.assertFalse(game.isGameOver());
    game.moveToFoundation(2, 1);
    Assert.assertFalse(game.isGameOver());
    game.movePile(4, 1, 6);
    Assert.assertFalse(game.isGameOver());
    game.moveToFoundation(4, 3);
    Assert.assertFalse(game.isGameOver());
    game.moveToFoundation(5, 3);
    Assert.assertFalse(game.isGameOver());
    game.moveDrawToFoundation(1);
    Assert.assertFalse(game.isGameOver());
    game.discardDraw();
    Assert.assertFalse(game.isGameOver());
    game.discardDraw();
    Assert.assertFalse(game.isGameOver());
    game.moveDraw(6);
    System.out.print(new KlondikeTextualView(game));
    Assert.assertFalse(game.isGameOver());
    game.discardDraw();
    Assert.assertFalse(game.isGameOver());
    game.moveDraw(6);
    Assert.assertTrue(game.isGameOver());
    game.discardDraw();
    System.out.print(new KlondikeTextualView(game));
    Assert.assertTrue(game.isGameOver());
  }

  @Test
  public void testDeckValid() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.getDeck();
    deck.addAll(model.getDeck());
    Assert.assertEquals(104, deck.size());
    deck = deck.stream().filter(c -> c.getSuit() == CardSuit.Spade
            || c.getSuit() == CardSuit.Heart).collect(Collectors.toList());
    addCard(deck, "A♢");
    addCard(deck, "2♢");
    addCard(deck, "3♢");
    addCard(deck, "4♢");
    addCard(deck, "5♢");
    addCard(deck, "6♢");
    addCard(deck, "7♢");
    addCard(deck, "8♢");
    addCard(deck, "9♢");
    addCard(deck, "10♢");
    addCard(deck, "J♢");
    addCard(deck, "Q♢");
    addCard(deck, "K♢");
    //Assert.assertEquals(65, deck.size());
    //Assert.assertTrue(model.isDeckValid(deck));
  }

  @Test
  public void testValidDeal() {
    BasicKlondike model = new BasicKlondike();
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
    model.startGame(deck, false, 2, 3);
    Assert.assertEquals(9, deck.size());
    //Assert.assertTrue(model.isDeckValid(deck));
  }
}
