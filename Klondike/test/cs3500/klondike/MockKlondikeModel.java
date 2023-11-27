package cs3500.klondike;


import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Mock model of KlondikeModel, for testing purposes.
 */

public class MockKlondikeModel implements KlondikeModel {
  final StringBuilder log;
  private boolean gameStarted;

  public MockKlondikeModel(StringBuilder log) {
    this.log = Objects.requireNonNull(log);
    this.gameStarted = false;
  }

  @Override
  public List<Card> getDeck() {
    return new ArrayList<Card>() ;
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw)
          throws IllegalArgumentException, IllegalStateException {
    log.append(String.format("numPiles: %d, numDraw: %d", numPiles, numDraw));
    this.getDeck();
    this.gameStarted = true;
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile)
          throws IllegalArgumentException, IllegalStateException {
    log.append(String.format("move from: %d, cards: %d, move to: %d", srcPile, numCards, destPile));
  }

  @Override
  public void moveDraw(int destPile) throws IllegalArgumentException, IllegalStateException {
    log.append(String.format("move from drawPile to: %d", destPile));
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile)
          throws IllegalArgumentException, IllegalStateException {
    log.append(String.format("move from: %d, move to: %d", srcPile, foundationPile));
  }

  @Override
  public void moveDrawToFoundation(int foundationPile)
          throws IllegalArgumentException, IllegalStateException {
    log.append(String.format("move from drawPile to foundation: %d", foundationPile));
  }

  @Override
  public void discardDraw() throws IllegalStateException {
    log.append("discarding the top draw card");
  }

  @Override
  public int getNumRows() throws IllegalStateException {
    return 0;
  }

  @Override
  public int getNumPiles() throws IllegalStateException {
    return 0;
  }

  @Override
  public int getNumDraw() throws IllegalStateException {
    return 0;
  }

  @Override
  public boolean isGameOver() throws IllegalStateException {
    return false;
  }

  @Override
  public int getScore() throws IllegalStateException {
    return 0;
  }

  @Override
  public int getPileHeight(int pileNum)
          throws IllegalArgumentException, IllegalStateException {
    return 0;
  }

  @Override
  public boolean isCardVisible(int pileNum, int card)
          throws IllegalArgumentException, IllegalStateException {
    return false;
  }

  @Override
  public Card getCardAt(int pileNum, int card)
          throws IllegalArgumentException, IllegalStateException {
    return null;
  }

  @Override
  public Card getCardAt(int foundationPile)
          throws IllegalArgumentException, IllegalStateException {
    return null;
  }

  @Override
  public List<Card> getDrawCards()
          throws IllegalStateException {
    return new ArrayList<Card>();
  }

  @Override
  public int getNumFoundations()
          throws IllegalStateException {
    return 0;
  }
}
