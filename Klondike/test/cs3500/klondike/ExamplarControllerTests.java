package cs3500.klondike;

import org.junit.Assert;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
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
 * Tests for Examplars.
 */

public class ExamplarControllerTests {

  private void addCard(List<Card> deck, String s) {
    KlondikeModel game = new BasicKlondike();
    deck.add(game.getDeck().stream().filter(c -> c.toString()
            .equals(s)).collect(Collectors.toList()).get(0));
  }


  @Test
  public void testMovePileWithInValidCards() {
    KlondikeModel model = new BasicKlondike();
    Readable r = new StringReader("mpp 4 2 5 q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    controller.playGame(model, model.getDeck(), false, 7, 3);
    assertTrue(a.toString().contains("Invalid move"));
  }

  @Test
  public void testMoveDraw() {
    KlondikeModel model = new BasicKlondike();
    Readable r = new StringReader("md 4 q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    controller.playGame(model, model.getDeck(), false, 7, 3);
    assertTrue(a.toString().contains("Invalid move"));
  }

  @Test
  public void testMoveDrawToFoundation() {
    KlondikeModel model = new BasicKlondike();
    Readable r = new StringReader("mdf 2 q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    controller.playGame(model, model.getDeck(), false, 7, 3);
    assertTrue(a.toString().contains("Invalid move"));
  }


  @Test
  public void testInitialGame() {
    KlondikeModel model = new BasicKlondike();
    List<Card> deck = new ArrayList<>();
    addCard(deck, "A♣");
    addCard(deck, "A♢");
    addCard(deck, "A♡");
    addCard(deck, "A♠");
    Readable r = new StringReader("q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    controller.playGame(model, deck, false, 2, 1);
    assertTrue(a.toString().contains("Game quit!"));
    assertTrue(a.toString().contains("State of game when quit:"));
    assertTrue(a.toString().contains("Foundation: <none>, <none>, <none>, <none>"));
    assertTrue(a.toString().contains("Draw: A♠"));
    assertTrue(a.toString().contains("A♣  ?\n    A♡"));
    assertTrue(a.toString().contains("Score: 0"));
    System.out.print(a.toString());
  }


  @Test(expected = IllegalArgumentException.class)
  public void testStartGameWithNullModel() {
    Readable r = new StringReader("mpf 1 1 q");
    Appendable a = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(r, a);
    controller.playGame(null, null, false, 7, 2);
  }

  @Test
  public void testMovePileAndQuit() {
    KlondikeModel model = new BasicKlondike();
    Reader r = new StringReader("mpf 1 1 mdp 1 mpf 2 2 2 mpf 2 3 q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    List<Card> deck = model.getDeck().stream().filter(c -> c.toString().equals("A♢")
            || c.toString().equals("A♣") || c.toString().equals("A♡") ||
            c.toString().equals("A♠") || c.toString().equals("2♢")
            || c.toString().equals("2♣") || c.toString().equals("2♡") ||
            c.toString().equals("2♠")).collect(Collectors.toList());
    controller.playGame(model, deck, false, 3, 1);
    //assertFalse(a.toString().contains("Invalid move"));
    assertTrue(a.toString().contains("Foundation: <none>, <none>, <none>, <none>"));
    assertTrue(a.toString().contains("Score"));
  }

  @Test
  public void testValidMD() {
    KlondikeModel model = new BasicKlondike();
    Reader r = new StringReader("md 2 q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    List<Card> deck = new ArrayList<>();
    addCard(deck, "A♡");
    addCard(deck, "2♠");
    addCard(deck, "2♡");
    addCard(deck, "A♠");

    controller.playGame(model, deck, false, 2, 1);
    assertTrue(a.toString().contains("Draw: "));
    assertTrue(a.toString().contains("A♡  ?\n    2♡\n    A♠"));
  }

  @Test
  public void testValidMPP() {
    KlondikeModel model = new BasicKlondike();
    Reader r = new StringReader("mpp 2 1 1 q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    List<Card> deck = new ArrayList<>();
    addCard(deck, "3♠");
    addCard(deck, "3♡");
    addCard(deck, "2♡");
    addCard(deck, "A♠");
    addCard(deck, "2♠");
    addCard(deck, "A♡");

    controller.playGame(model, deck, false, 2, 1);
    assertTrue(a.toString().contains("Draw: A♠"));
    assertTrue(a.toString().contains("3♠ 3♡\n 2♡"));
  }


  @Test
  public void testInvalidMPP() {
    KlondikeModel model = new BasicKlondike();
    Reader r = new StringReader("mpp yes 2 1 1 q");
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
    assertFalse(a.toString().contains("Invalid move"));
  }

  @Test
  public void testRows() {
    KlondikeModel model = new BasicKlondike();
    Reader r = new StringReader("q"); //mdp 1 mpf 2 2 mpf 2 3 q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    List<Card> deck = model.getDeck();

    int numPiles = 7;
    controller.playGame(model, deck, false, numPiles, 1);
    String[] rows = a.toString().split("\n");
    System.out.print(a.toString());
    Assert.assertEquals((numPiles + 3) * 2 + 2, rows.length);
  }

  @Test
  public void testSize() {
    KlondikeModel model = new BasicKlondike();
    Reader r = new StringReader("q"); //mdp 1 mpf 2 2 mpf 2 3 q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    List<Card> deck = model.getDeck();

    int numPiles = 7;
    controller.playGame(model, deck, false, numPiles, 1);
    String[] rows = a.toString().split("\n");

    for (int i = 0; i < numPiles; i++) {
      Assert.assertEquals(numPiles - i, Arrays.stream(rows[2 + i].split(" "))
              .filter(item -> !item.isEmpty()).count());
    }
  }

  @Test
  public void testRowsAndSizeAfterMDP() {
    KlondikeModel model = new BasicKlondike();
    Reader r = new StringReader("mpp 1 1 2 q"); //mdp 1 mpf 2 2 mpf 2 3 q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    List<Card> deck = new ArrayList<>();
    addCard(deck, "A♠");
    addCard(deck, "A♡");
    addCard(deck, "2♡");
    addCard(deck, "2♠");

    int numPiles = 2;
    controller.playGame(model, deck, false, numPiles, 1);
    String[] rows = a.toString().split("\n");
    Assert.assertTrue(rows[2].contains("A♠"));
    Assert.assertTrue(rows[2].contains("?"));
    Assert.assertTrue(rows[3].contains("2♡"));
    Assert.assertFalse(rows[4].contains("A♠"));
    Assert.assertEquals( 2,
            Arrays.stream(rows[2].split(" ")).filter(item -> !item.isEmpty()).count());
    Assert.assertFalse(rows[7].contains("A♠"));
    Assert.assertTrue(rows[7].contains("X"));
    Assert.assertTrue(rows[7].contains("?"));
    Assert.assertTrue(rows[8].contains("2♡"));
    Assert.assertTrue(rows[9].contains("A♠"));
    Assert.assertEquals( 2,
            Arrays.stream(rows[7].split(" ")).filter(item -> !item.isEmpty()).count());
    Assert.assertEquals( 1,
            Arrays.stream(rows[8].split(" ")).filter(item -> !item.isEmpty()).count());
    Assert.assertEquals( 1,
            Arrays.stream(rows[9].split(" ")).filter(item -> !item.isEmpty()).count());
    System.out.print(a);
  }

  @Test
  public void testInvalidCommandMoveToFoundation() {
    KlondikeModel model = new BasicKlondike();
    List<Card> deck = model.getDeck().stream().filter(c -> c.toString().equals("A♢")
            || c.toString().equals("A♣") || c.toString().equals("A♡") ||
            c.toString().equals("A♠")).collect(Collectors.toList());
    Readable r = new StringReader("mpf 1 1 2 q");
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

  @Test(expected = IllegalStateException.class)
  public void testPlayGame() {
    KlondikeModel model = new BasicKlondike();
    List<Card> deck = new ArrayList<>();
    addCard(deck, "5♡");
    addCard(deck, "10♣");
    addCard(deck, "Q♢");
    addCard(deck, "2♡");
    addCard(deck, "6♡");
    addCard(deck, "4♣");
    addCard(deck, "9♡");
    addCard(deck, "2♠");
    addCard(deck, "K♠");
    addCard(deck, "3♢");
    addCard(deck, "9♠");
    addCard(deck, "7♣");
    addCard(deck, "J♢");
    addCard(deck, "7♢");
    addCard(deck, "9♣");
    addCard(deck, "3♣");
    addCard(deck, "9♢");
    addCard(deck, "4♡");
    addCard(deck, "8♠");
    addCard(deck, "3♠");
    addCard(deck, "2♢");
    addCard(deck, "6♣");
    addCard(deck, "K♣");
    addCard(deck, "A♠");
    addCard(deck, "10♠");
    addCard(deck, "8♣");
    addCard(deck, "5♠");
    addCard(deck, "K♢");
    addCard(deck, "10♡");
    addCard(deck, "Q♠");
    addCard(deck, "8♡");
    addCard(deck, "4♢");
    addCard(deck, "4♠");
    addCard(deck, "A♢");
    addCard(deck, "6♠");
    addCard(deck, "3♡");
    addCard(deck, "Q♣");
    addCard(deck, "8♢");
    addCard(deck, "J♠");
    addCard(deck, "5♢");
    addCard(deck, "K♡");
    addCard(deck, "Q♡");
    addCard(deck, "10♢");
    addCard(deck, "6♢");
    addCard(deck, "7♠");
    addCard(deck, "J♣");
    addCard(deck, "A♣");
    addCard(deck, "2♣");
    addCard(deck, "5♣");
    addCard(deck, "A♡");
    addCard(deck, "7♡");
    addCard(deck, "J♡");
    Readable r = new StringReader("");
    //Readable r = new InputStreamReader(System.in);
    Appendable a = System.out;
    KlondikeController controller = new KlondikeTextualController(r, a);
    controller.playGame(model, deck, false, 7, 3);
  }
}
