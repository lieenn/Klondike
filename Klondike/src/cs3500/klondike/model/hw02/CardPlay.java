package cs3500.klondike.model.hw02;

/**
 * Represents a card in a deck of playing cards.
 */
public class CardPlay implements Card {
  private CardRank rank;
  private CardSuit suit;
  private boolean isFaceUp;

  /**
   * Constructors for CardPlay.
   * @param rank the rank of the card (A, 2, 3, etc.)
   * @param suit the suit of the card
   * @param isFaceUp boolean value that returns true if the card is facing up
   */
  public CardPlay(CardRank rank, CardSuit suit, boolean isFaceUp) {
    this.rank = rank;
    this.suit = suit;
    this.isFaceUp = isFaceUp;
  }

  @Override
  public String toString() {
    return this.rank.toString() + this.suit.toString();
  }

  /**
   * Checks if this card is facing up.
   * @return a true if the card is facing up
   */
  public boolean isFaceUp() {
    return this.isFaceUp;
  }

  /**
   * Get this card's rank.
   * @return rank of this card
   */
  public CardRank getRank() {
    return this.rank;
  }

  /**
   * Get this card's suit.
   * @return suit of this card
   */
  public CardSuit getSuit() {
    return this.suit;
  }

  /**
   * Convert the rank of the card into an integer.
   * @return the rank of the card converted into a number
   */
  public int rankToNum() {
    return this.rank.toNum();
  }

  /**
   * Flip the card up if it is currently facing down.
   */
  public void flipCardUp() {
    if (!this.isFaceUp) {
      this.isFaceUp = true;
    }
  }

  /**
   * Flip the card down if it is currently facing up.
   */
  public void flipCardDown() {
    if (this.isFaceUp) {
      this.isFaceUp = false;
    }
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other instanceof CardPlay) {
      CardPlay that = (CardPlay) other;
      return this.rank.equals(that.rank) && this.suit.equals(that.suit);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return 31 * rank.hashCode() + suit.hashCode();
  }
}
