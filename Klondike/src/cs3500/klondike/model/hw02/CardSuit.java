package cs3500.klondike.model.hw02;

/**
 * Represents the suits of a playing card.
 */
public enum CardSuit {
  Spade("♠"),
  Heart("♡"),
  Club("♣"),
  Diamond("♢");


  private final String symbol;

  CardSuit(String symbol) {
    this.symbol = symbol;
  }

  public String toString() {
    return this.symbol;
  }
}
