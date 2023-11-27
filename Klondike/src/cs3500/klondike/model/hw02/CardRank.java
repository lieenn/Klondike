package cs3500.klondike.model.hw02;

/**
 * Represents the possible ranks of a playing card.
 */
public enum CardRank {
  Ace("A"),
  Two("2"),
  Three("3"),
  Four("4"),
  Five("5"),
  Six("6"),
  Seven("7"),
  Eight("8"),
  Nine("9"),
  Ten("10"),
  Jack("J"),
  Queen("Q"),
  King("K");

  private final String symbol;
  CardRank(String symbol) {
    this.symbol = symbol;
  }

  public String toString() {
    return this.symbol;
  }

  public int toNum() {
    return ordinal() + 1;
  }

}
