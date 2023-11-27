package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Whitehead variant of Klondike.
 */
public class WhiteheadKlondike extends BasicKlondike {
  public WhiteheadKlondike() {
    super();
  }

  /*
  All cards in cascade pile should be facing up and be visible
   */

  @Override
  protected List<List<Card>> deal(List<Card> deck, int numPiles, int numDraw) {

    List<List<Card>> dealtCards = new ArrayList<>();
    for (int column = 0; column < numPiles; column++) {
      dealtCards.add(new ArrayList<>());
    }
    //Dealing cascade
    int currentCard = 0;
    for (int column = 0; column < numPiles; column++) {
      for (int row = column; row < numPiles; row++) {
        dealtCards.get(row).add(deck.get(currentCard++));
      }
    }

    // Flip the card up in the cascade pile
    for (int column = 0; column < numPiles; column++) {
      dealtCards.get(column).forEach(Card::flipCardUp);
    }

    drawPile(deck, currentCard, numDraw);
    return dealtCards;
  }


  /*
  - builds must be same-colored
  - all the moved cards must be of the same suit.
  - empty cascadePile can take any value
   */

  @Override
  protected boolean isValidMoveToPile(Card card, int destPile) {
    if (getPileHeight(destPile) == 0) {
      return true;
    }
    //compare the last card int the pile to the given card
    Card bottomCard = getCardAt(destPile, getPileHeight(destPile) - 1);
    return isValidMoveHelper(card, bottomCard, true);
  }

  @Override
  protected boolean isMovingPileValid(int srcPile, int numCards) {
    int firstCardIndex = getPileHeight(srcPile) - numCards;
    int lastCardIndex = getPileHeight(srcPile);

    List<Card> srcCards = this.cascadePiles.get(srcPile);

    List<Card> cardsToMove = new ArrayList<>(srcCards.subList(firstCardIndex, lastCardIndex));

    String rank = cardsToMove.get(0).getSuit().toString();

    for (Card card : cardsToMove) {
      if (!card.getSuit().toString().equals(rank)) {
        return false;
      }
    }
    return true;
  }

  @Override
  protected boolean isPileMoveAvailable(int src, int num) {
    //base card is the first card of the pile that can be moved
    Card base = this.getCardAt(src, (getPileHeight(src) - num));
    //setting the base card as the card on the top of the pile by default
    Card cardOnTop = base;
    //if the there is a card on top of the base card,
    //the cardOnTop of the pile would change to the card above the base card
    if (getPileHeight(src) - num - 1 >= 0) {
      cardOnTop = cascadePiles.get(src).get(getPileHeight(src) - num - 1);
    }
    for (int piles = 0; piles < getNumPiles(); piles++) {
      //checking for every pile in the deck
      if (src == piles) {
        continue;
      }
      if (getPileHeight(piles) > 0) {
        Card bottomCard = this.getCardAt(piles, (getPileHeight(piles) - 1));
        if (cardOnTop.getRank() != bottomCard.getRank()) {
          return isValidMoveHelper(base, bottomCard, true);
        }
      }
    }
    return false;
  }
}