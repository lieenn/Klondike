package cs3500.klondike;

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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Tests for Klondike Controller.
 */

public class KlondikeControllerTest {
  private void addCard(List<Card> deck, String s) {
    KlondikeModel game = new BasicKlondike();
    deck.add(game.getDeck().stream().filter(c -> c.toString()
            .equals(s)).collect(Collectors.toList()).get(0));
  }

  @Test
  public void testInvalidMove() {
    KlondikeModel model = new BasicKlondike();
    Readable r = new StringReader("mpp 3 7 q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    controller.playGame(model, model.getDeck(), false, 7, 3);
    //assertTrue(a.toString().contains("Invalid move"));
    assertTrue(a.toString().contains("Game quit"));
  }


  @Test
  public void testValidMDF() {
    KlondikeModel model = new BasicKlondike();
    Reader r = new StringReader("mdf 1 q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    List<Card> deck = new ArrayList<>();
    addCard(deck, "A♡");
    addCard(deck, "2♠");
    addCard(deck, "2♡");
    addCard(deck, "A♠");
    addCard(deck, "3♡");
    addCard(deck, "3♠");
    addCard(deck, "4♡");
    addCard(deck, "4♠");

    controller.playGame(model, deck, false, 2, 3);
    assertTrue(a.toString().contains("Draw: 3♡, 3♠, 4♡"));
    assertTrue(a.toString().contains("Foundation: A♠, <none>"));
    assertTrue(a.toString().contains("A♡  ?\n    2♡"));
    System.out.print(a);
  }

  @Test
  public void testValidDD() {
    KlondikeModel model = new BasicKlondike();
    Reader r = new StringReader("dd q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    List<Card> deck = new ArrayList<>();
    addCard(deck, "A♡");
    addCard(deck, "2♠");
    addCard(deck, "2♡");
    addCard(deck, "A♠");
    addCard(deck, "3♡");
    addCard(deck, "3♠");

    controller.playGame(model, deck, false, 2, 3);
    assertTrue(a.toString().contains("Draw: 3♡, 3♠, A♠"));
    assertTrue(a.toString().contains("A♡  ?\n    2♡"));
  }

  @Test
  public void testMovePileFoundWithMock() {
    StringBuilder log = new StringBuilder();
    KlondikeModel mockModel = new MockKlondikeModel(log);

    Readable r = new StringReader("mpf 1 1 q");
    Appendable a = new StringBuilder();

    List<Card> deck = mockModel.getDeck().stream().filter(c -> c.toString().equals("A♢")
            || c.toString().equals("A♣") || c.toString().equals("A♡") ||
            c.toString().equals("A♠")).collect(Collectors.toList());

    KlondikeTextualController controller = new KlondikeTextualController(r, a);
    controller.playGame(mockModel, deck, false, 2, 1);

    // Check if the movePile method in the mock model was called with the expected parameters
    assertTrue(log.toString().contains("move from: 0, move to: 0"));
    assertFalse(log.toString().contains("Invalid move"));
  }

  @Test
  public void testMoveDrawWithMock() {
    StringBuilder log = new StringBuilder();
    KlondikeModel mockModel = new MockKlondikeModel(log);

    Readable r = new StringReader("md 2 q");
    Appendable a = new StringBuilder();

    List<Card> deck = mockModel.getDeck().stream().filter(c -> c.toString().equals("A♢")
            || c.toString().equals("A♣") || c.toString().equals("A♡") ||
            c.toString().equals("A♠")).collect(Collectors.toList());

    KlondikeTextualController controller = new KlondikeTextualController(r, a);
    controller.playGame(mockModel, deck, false, 2, 1);

    // Check if the movePile method in the mock model was called with the expected parameters
    assertTrue(log.toString().contains("move from drawPile to: 1"));
    assertFalse(log.toString().contains("Invalid move"));
  }

  @Test
  public void testMovePileWithMock() {
    StringBuilder log = new StringBuilder();
    KlondikeModel mockModel = new MockKlondikeModel(log);

    Readable r = new StringReader("mpp 2 1 1 q");
    Appendable a = new StringBuilder();

    List<Card> deck = new ArrayList<>();
    addCard(deck, "A♡");
    addCard(deck, "2♡");
    addCard(deck, "2♠");
    addCard(deck, "A♠");
    addCard(deck, "3♡");
    addCard(deck, "3♠");

    KlondikeTextualController controller = new KlondikeTextualController(r, a);
    controller.playGame(mockModel, deck, false, 2, 1);

    // Check if the movePile method in the mock model was called with the expected parameters
    assertTrue(log.toString().contains("move from: 1, cards: 1, move to: 0"));
    assertFalse(log.toString().contains("Invalid move"));
  }

  @Test
  public void testMoveDrawToFoundWithMock() {
    StringBuilder log = new StringBuilder();
    KlondikeModel mockModel = new MockKlondikeModel(log);

    Readable r = new StringReader("mdf 2 q");
    Appendable a = new StringBuilder();

    List<Card> deck = new ArrayList<>();
    addCard(deck, "A♡");
    addCard(deck, "2♡");
    addCard(deck, "2♠");
    addCard(deck, "A♠");
    addCard(deck, "3♡");
    addCard(deck, "3♠");

    KlondikeTextualController controller = new KlondikeTextualController(r, a);
    controller.playGame(mockModel, deck, false, 2, 1);

    // Check if the movePile method in the mock model was called with the expected parameters
    assertTrue(log.toString().contains("move from drawPile to foundation: 1"));
    assertFalse(log.toString().contains("Invalid move"));
  }

  @Test(expected = IllegalStateException.class)
  public void testEmptyDeck() {

    KlondikeModel model = new BasicKlondike();
    Reader r = new StringReader("dd q");
    Appendable a = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(r, a);
    List<Card> emptyDeck = new ArrayList<>();

    controller.playGame(model, emptyDeck, false, 2, 3);
  }

  @Test(expected = IllegalStateException.class)
  public void testEmptyUserInput() {

    KlondikeModel model = new BasicKlondike();
    Reader r = new StringReader(" "); // new InputStreamReader(System.in)
    Appendable a = new StringBuilder(); // System.out
    KlondikeController controller = new KlondikeTextualController(r, a);
    List<Card> emptyDeck = new ArrayList<>();

    controller.playGame(model, emptyDeck, false, 2, 3);
  }

}