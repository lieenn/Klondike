package cs3500.klondike;

import org.junit.Assert;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Examplar tests that didn't make it to the end ExamplarControllerTests.
 */

public class ExamplarHW2 {

  private void addCard(List<Card> deck, String s) {
    KlondikeModel game = new BasicKlondike();
    deck.add(game.getDeck().stream().filter(c -> c.toString()
            .equals(s)).collect(Collectors.toList()).get(0));
  }

  @Test
  public void testDisCardPile()  {
    KlondikeModel model = new BasicKlondike();
    List<Card> deck = model.getDeck().stream().filter(c -> c.toString().equals("A♢")
            || c.toString().equals("A♣") || c.toString().equals("A♡") ||
            c.toString().equals("A♠")).collect(Collectors.toList());
    Readable r = new StringReader("mpf 1 1 mpf 2 2 mdf 2 dd q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    controller.playGame(model, deck, false, 2, 1);
    assertTrue(a.toString().contains("Invalid move"));
  }

  @Test
  public void testDrawCountAfterMDF() {
    KlondikeModel model = new BasicKlondike();
    Reader r = new StringReader("mdf 1 q"); //mdp 1 mpf 2 2 mpf 2 3 q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    List<Card> deck = new ArrayList<>();
    addCard(deck, "A♣");
    addCard(deck, "A♢");
    addCard(deck, "A♡");
    addCard(deck, "A♠");
    addCard(deck, "2♣");
    addCard(deck, "2♢");
    addCard(deck, "2♡");
    addCard(deck, "2♠");
    controller.playGame(model, deck, false, 2, 3);
    assertTrue(a.toString().contains("Draw: 2♣, 2♢"));
    assertTrue(a.toString().contains("A♣  ?\n    A♡"));
    assertTrue(a.toString().contains("Foundation: A♠, <none>, <none>, <none>"));
    assertTrue(a.toString().contains("Score: 1"));
    assertFalse(a.toString().contains("Invalid move"));
  }


  @Test
  public void testFailMDF() {
    KlondikeModel model = new BasicKlondike();
    Reader r = new StringReader("mdf 1 mdf 1 q"); //mdp 1 mpf 2 2 mpf 2 3 q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    List<Card> deck = model.getDeck().stream().filter(c -> c.toString().equals("A♢")
            || c.toString().equals("A♣") || c.toString().equals("A♡") ||
            c.toString().equals("A♠") || c.toString().equals("2♢")
            || c.toString().equals("2♣") || c.toString().equals("2♡") ||
            c.toString().equals("2♠")).collect(Collectors.toList());
    controller.playGame(model, deck, false, 2, 3);
    assertTrue(a.toString().contains("Invalid move"));
  }


  @Test
  public void testMoveToFound() {
    KlondikeModel model = new BasicKlondike();
    List<Card> deck = new ArrayList<>();
    addCard(deck, "A♣");
    addCard(deck, "A♢");
    addCard(deck, "A♡");
    addCard(deck, "A♠");
    Readable r = new StringReader("mpf 1 1 mpf 2 2 mpf 2 3 q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    controller.playGame(model, deck, false, 2, 1);
    assertTrue(a.toString().contains("Game quit!"));
    assertTrue(a.toString().contains("Foundation: A♣, A♡, A♢"));
    assertTrue(a.toString().contains("Draw: A♠"));
    assertTrue(a.toString().contains("X"));
    assertTrue(a.toString().contains("Score: 3"));
  }

  @Test
  public void testMoveToFoundation() {
    KlondikeModel model = new BasicKlondike();
    Readable r = new StringReader("mpf 6 3 q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    controller.playGame(model, model.getDeck(), false, 7, 3);
    assertTrue(a.toString().contains("Invalid move"));
  }

  @Test
  public void testNoNum() {
    KlondikeModel model = new BasicKlondike();
    Readable r = new StringReader("mdf q");
    Appendable a = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(r, a);
    controller.playGame(model, model.getDeck(), false, 7, 2);
    assertTrue(a.toString().contains("Game quit"));
    System.out.print(a);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayGameWithNullAppendable() {
    KlondikeModel model = new BasicKlondike();
    Readable r = new StringReader("dd q");
    Appendable a = null;  // Null Appendable
    KlondikeTextualController controller = new KlondikeTextualController(r, a);
    controller.playGame(model, model.getDeck(), false, 7, 2);
  }

  @Test
  public void testInvalidCardsMove() {
    KlondikeModel model = new BasicKlondike();
    Readable r = new StringReader("mpp 2 -5 1 q");
    Appendable a = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(r, a);
    controller.playGame(model, model.getDeck(), false, 7, 2);
    assertTrue(a.toString().contains("Invalid move"));
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayGameWithInvalidNumPiles() {
    KlondikeModel model = new BasicKlondike();
    Readable r = new StringReader("dd q");
    Appendable a = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(r, a);
    controller.playGame(model, model.getDeck(), false, -7, 2);
  }

  @Test
  public void testValidCommandMoves() {
    KlondikeModel model = new BasicKlondike();
    List<Card> deck = new ArrayList<>();
    addCard(deck, "A♣");
    addCard(deck, "A♢");
    addCard(deck, "A♡");
    addCard(deck, "A♠");
    Readable r = new StringReader("mpf 1 1 mpf 2 2 mpf 2 3 mdf 4 q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    controller.playGame(model, deck, false, 2, 1);
    assertFalse(a.toString().contains("Invalid move"));
    System.out.print(a);
    assertTrue(a.toString().contains("You win"));
  }


  @Test
  public void testValidGame() {
    KlondikeModel model = new BasicKlondike();
    Readable r = new StringReader("q");
    Appendable a = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(r, a);
    controller.playGame(model, model.getDeck(), false, 7, 2);
    assertFalse(a.toString().isEmpty());
  }

  @Test
  public void testValidMPP2() {
    KlondikeModel model = new BasicKlondike();
    Reader r = new StringReader("mpp 2 1 1 mpp 1 2 2 Q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    List<Card> deck = new ArrayList<>();
    addCard(deck, "2♠");
    addCard(deck, "3♡");
    addCard(deck, "A♡");
    addCard(deck, "A♠");
    addCard(deck, "3♠");
    addCard(deck, "2♡");

    controller.playGame(model, deck, false, 2, 1);
    assertTrue(a.toString().contains("Draw: A♠"));
    assertTrue(a.toString().contains("X 3♡\n    2♠\n    A♡"));
    System.out.print(a);
  }

  @Test
  public void testQuitGame() {
    KlondikeModel model = new BasicKlondike();
    Readable r = new StringReader("Q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    controller.playGame(model, model.getDeck(), false, 7, 3);
    assertTrue(a.toString().contains("Game quit"));
    assertTrue(a.toString().contains("Score: 0"));
  }

  @Test
  public void testTypoCommandMoves() {
    KlondikeModel model = new BasicKlondike();
    List<Card> deck = new ArrayList<>();
    addCard(deck, "A♣");
    addCard(deck, "A♢");
    addCard(deck, "A♡");
    addCard(deck, "A♠");
    Readable r = new StringReader("mdf 1 1 nd 1 q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    controller.playGame(model, deck, false, 2, 1);
    assertTrue(a.toString().contains("Invalid move"));
  }

  @Test
  public void testMovePileInvalid() {
    KlondikeModel model = new BasicKlondike();
    Readable r = new StringReader("mpp 2 5 5 q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    controller.playGame(model, model.getDeck(), false, 7, 3);
    assertTrue(a.toString().contains("Invalid move"));
  }


  @Test
  public void testMovePileOutOfRange() {
    KlondikeModel model = new BasicKlondike();
    Readable r = new StringReader("mpp 2 5 8 q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    controller.playGame(model, model.getDeck(), false, 7, 3);
    Assert.assertTrue(a.toString().contains("Invalid move"));
  }


  @Test
  public void testInvalidCommandMoveToFoundation() {
    KlondikeModel model = new BasicKlondike();
    List<Card> deck = new ArrayList<>();
    addCard(deck, "A♣");
    addCard(deck, "A♢");
    addCard(deck, "A♡");
    addCard(deck, "A♠");
    Readable r = new StringReader("mpf 1 1 2 q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    controller.playGame(model, deck, false, 2, 1);
    assertTrue(a.toString().contains("Invalid move"));
    System.out.print(a);
  }

  @Test
  public void testInvalidCommandMoveToPile() {
    KlondikeModel model = new BasicKlondike();
    List<Card> deck = new ArrayList<>();
    addCard(deck, "A♣");
    addCard(deck, "A♢");
    addCard(deck, "A♡");
    addCard(deck, "A♠");
    Readable r = new StringReader("mpp 1 1 1 q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    controller.playGame(model, deck, false, 2, 1);
    assertTrue(a.toString().contains("Invalid move"));
  }


  @Test
  public void testInvalidCommandMoveDrawToPile() {
    KlondikeModel model = new BasicKlondike();
    List<Card> deck = new ArrayList<>();
    addCard(deck, "A♣");
    addCard(deck, "A♢");
    addCard(deck, "A♡");
    addCard(deck, "A♠");
    Readable r = new StringReader("mdp 1 1 q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    controller.playGame(model, deck, false, 2, 1);
    assertTrue(a.toString().contains("Invalid move"));
    assertTrue(a.toString().contains("Draw: A♠"));
  }
}
