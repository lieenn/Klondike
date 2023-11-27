package cs3500.klondike.model.hw02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of the basic version of Klondike.
 * Changes made: different structure type for cascadePiles and foundationPiles
 */

public class BasicKlondike implements cs3500.klondike.model.hw02.KlondikeModel {
  private List<Card> deck;
  private int numPile;
  private int numDraw;

  private boolean hasGameStarted;

  protected List<List<Card>> cascadePiles;
  protected List<Card> drawPile;
  private List<List<Card>> foundationPiles;

  public BasicKlondike() {
    this.hasGameStarted = false;
  }

  @Override
  public List<Card> getDeck() {
    List<Card> deck = new ArrayList<Card>();
    for (CardSuit suit : CardSuit.values()) {
      for (CardRank rank : CardRank.values()) {
        deck.add(new CardPlay(rank, suit, false));
      }
    }
    return deck;
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw)
          throws IllegalArgumentException, IllegalStateException {
    if (deck == null) {
      throw new IllegalArgumentException("Not a valid deck");
    }
    if (deck.isEmpty()) {
      throw new IllegalArgumentException("Deck is empty");
    }
    if (hasGameStarted) {
      throw new IllegalStateException("Game has already started");
    }
    if (numPiles < 1 || numDraw < 1) {
      throw new IllegalArgumentException("Not a valid number");
    }
    if (deck.contains(null)) {
      throw new IllegalArgumentException("The deck contains null cards");
    }
    if (!this.isDeckValid(deck)) {
      throw new IllegalArgumentException("Deck is not valid");
    }
    if (!this.hasEnoughCardsForCascade(deck, numPiles)) {
      throw new IllegalArgumentException("There is not enough cards");
    }
    if (shuffle) {
      Collections.shuffle(deck);
    }

    this.numPile = numPiles;
    this.numDraw = numDraw;
    this.cascadePiles = deal(deck, numPiles, numDraw);
    this.deck = deck;

    //Make foundationPiles based on how many Aces is in the deck
    int numAce = this.countAces(this.deck);
    this.foundationPiles = new ArrayList<>();
    for (int pile = 0; pile < numAce; pile++) {
      this.foundationPiles.add(new ArrayList<>());
    }
    this.hasGameStarted = true;
  }

  private boolean hasEnoughCardsForCascade(List<Card> deck, int numPiles) {
    int minCardsRequired = (numPiles * (1 + numPiles)) / 2; // Minimum cards needed for the cascades
    return deck.size() >= minCardsRequired;
  }

  //Helper method that counts how many aces are there in the pile
  private int countAces(List<Card> deck) {
    int count = 0;
    for (Card card : deck) {
      if (card.getRank() == CardRank.Ace) {
        count++;
      }
    }
    return count;
  }

  /*
  A deck should have:
   - have an ace for every suit -> check for Ace in every suit
   - have consecutive number of cards for each suit -> search for each rank ->
   - each suits must have the same number of cards
   - two different suits of colors
   */

  // checks if the given deck is valid
  private boolean isDeckValid(List<Card> deck) {
    Map<CardSuit, List<Card>> deckmap = isDeckValidHelper(deck);//returns a map by suit:rank sorted

    //checks how many cards each suit has
    Set<CardRank> deckSet = deckmap.get(deckmap.keySet().iterator().next()).stream()
            .map(Card::getRank).collect(Collectors.toSet());
    int runlength = 0;
    for (CardRank rank: CardRank.values()) {
      if (deckSet.contains(rank)) {
        runlength++;
        continue;
      }
      break;
    }

    if (runlength == 0) {
      return false;
    }
    if ((runlength * countAces(deck)) != deck.size()) {
      return false;
    }
    //checks if the runs are all the same size while accounting for duplicate suit runs
    int[] suitCounts = deckmap.values().stream().mapToInt(List::size).toArray();
    int finalRunLength = runlength;
    if (!Arrays.stream(suitCounts).allMatch(l -> l % finalRunLength == 0)) {
      return false;
    }

    // final test to see if all runs have same cards as other runs
    for (CardSuit suit : deckmap.keySet()) {
      Set<Card> suitRun = new HashSet<>(deckmap.get(suit));
      if (!suitRun.stream().allMatch(c -> deckSet.contains(c.getRank()))) {
        return false;
      }
    }
    return true;
  }

  //Returns a map where the key is suit and value is a sorted array of cards of that suit
  private Map<CardSuit, List<Card>> isDeckValidHelper(List<Card> deck) {
    Map<CardSuit, List<Card>> map = deck.stream().collect(Collectors.groupingBy(Card::getSuit));
    for (CardSuit suit : map.keySet()) {
      map.get(suit).sort(Comparator.comparing(Card::getRank));
    }
    return map;
  }

  //TODO: Override in Whitehead
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
    // Flip the card at the end of every column
    for (int index = 0; index < numPiles; index++) {
      dealtCards.get(index).get(index).flipCardUp();
    }

    drawPile(deck, currentCard, numDraw);
    return dealtCards;
  }

  protected void drawPile(List<Card> leftOverDeck, int currentCard, int numDraw) {
    // Add the remaining cards to the drawPile as a List
    this.drawPile = leftOverDeck.subList(currentCard, leftOverDeck.size());
    // Flip the given number of draw cards up
    if (!drawPile.isEmpty()) {
      for (int index = 0; index < numDraw; index++) {
        this.drawPile.get(index).flipCardUp();
      }
    }
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile)
          throws IllegalStateException {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    if (srcPile < 0 || srcPile > numPile - 1 || destPile < 0
            || destPile > numPile - 1 || srcPile == destPile) {
      throw new IllegalArgumentException("Invalid number");
    }
    if (numCards > getPileHeight(srcPile) || numCards <= 0) {
      throw new IllegalArgumentException("Not enough cards to move");
    }

    int firstCardIndex = getPileHeight(srcPile) - numCards;
    Card topCard = this.getCardAt(srcPile,firstCardIndex);

    if (!this.isValidMoveToPile(topCard, destPile)) {
      throw new IllegalStateException("Not a valid move");
    }

    // Check for face-down cards in the source pile being moved
    if (!isMovingPileValid(srcPile, numCards)) {
      throw new IllegalStateException("Moving pile is invalid");
    }

    //moving pile to pile
    movePilesHelper(srcPile, numCards, destPile);

    //flip the last card in the srcPile up if its facing down
    flipLastCard(srcPile);
  }

  private void flipLastCard(int srcPile) {
    if (getPileHeight(srcPile) > 0) {
      int lastCardIndex = getPileHeight(srcPile) - 1;
      Card lastCard = cascadePiles.get(srcPile).get(lastCardIndex);
      if (!lastCard.isFaceUp()) {
        lastCard.flipCardUp();
      }
    }
  }

  protected boolean isMovingPileValid(int srcPile, int numCards) {
    int firstCardIndex = getPileHeight(srcPile) - numCards;
    int lastCardIndex = getPileHeight(srcPile);
    for (int cardIndex = firstCardIndex; cardIndex < lastCardIndex; cardIndex++) {
      if (!this.isCardVisible(srcPile, cardIndex)) {
        return false;
      }
    }
    return true;
  }

  private void movePilesHelper(int srcPile, int numCards, int destPile) {
    List<Card> destCards = this.cascadePiles.get(destPile);
    List<Card> srcCards = this.cascadePiles.get(srcPile);

    int firstCardIndex = getPileHeight(srcPile) - numCards;
    int lastCardIndex = getPileHeight(srcPile);

    //get a list of cards to be moved to another pile
    List<Card> removedCards = new ArrayList<>(srcCards.subList(firstCardIndex, lastCardIndex));

    //remove the cards from the old pile
    srcCards.subList(firstCardIndex, srcCards.size()).clear();
    destCards.addAll(removedCards);
  }

  //TODO: Override in Whitehead
  protected boolean isValidMoveToPile(Card card, int destPile) {
    //if the given destPile is empty
    //check if the card to be moved to the pile is the highest in the deck
    if (getPileHeight(destPile) == 0) {
      return (this.getHighestCardRank(this.deck) == card.getRank());
    }
    //compare the last card int the pile to the given card
    Card bottomCard = getCardAt(destPile, getPileHeight(destPile) - 1);
    return isValidMoveHelper(card, bottomCard, false);
  }

  protected boolean isValidMoveHelper(Card card1, Card card2, boolean sameColor) {
    if (card1.rankToNum() != card2.rankToNum() - 1) {
      return false;
    }
    if (sameColor) {
      if (card1.getSuit() == CardSuit.Club || card1.getSuit() == CardSuit.Spade) {
        return (card2.getSuit() == CardSuit.Club || card2.getSuit() == CardSuit.Spade);
      } else {
        return (card2.getSuit() == CardSuit.Heart || card2.getSuit() == CardSuit.Diamond);
      }
    }
    else {
      if (card1.getSuit() == CardSuit.Club || card1.getSuit() == CardSuit.Spade) {
        return (card2.getSuit() == CardSuit.Heart || card2.getSuit() == CardSuit.Diamond);
      } else {
        return (card2.getSuit() == CardSuit.Club || card2.getSuit() == CardSuit.Spade);
      }
    }
  }

  private boolean isValidMoveToFoundation(Card card, int foundPile) {
    if (this.foundationPiles.get(foundPile).isEmpty()) {
      return (card.getRank() == CardRank.Ace);
    }

    Card topCard = getCardAt(foundPile);
    return card.getRank().toNum() == topCard.getRank().toNum() + 1
            && card.getSuit() == topCard.getSuit();
  }

  private CardRank getHighestCardRank(List<Card> deck) {
    CardRank highestRank = CardRank.Ace;
    for (Card card : deck) {
      CardRank rank = card.getRank();
      if (rank.toNum() > highestRank.toNum()) {
        highestRank = rank;
      }
    }
    return highestRank;
  }

  @Override
  public void moveDraw(int destPile) throws IllegalStateException {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started yet.");
    }
    if (destPile < 0 || destPile > (numPile - 1)) {
      throw new IllegalArgumentException("Invalid destination pile number.");
    }
    if (drawPile.isEmpty()) {
      throw new IllegalStateException("No draw cards available.");
    }
    if (!this.isValidMoveToPile(drawPile.get(0), destPile)) {
      throw new IllegalStateException("Not a valid move");
    }

    //remove the first card in the draw pile
    Card removedCard = drawPile.remove(0);

    //deck.remove(removedCard);  // Remove the card from the overall deck???

    //adds the removed cards to the cascadePile
    this.cascadePiles.get(destPile).add(removedCard);

    // Flip the next card in the drawPile if there are still draw cards
    draw();
  }

  protected void draw() {
    for (int i = 0; i < numDraw; i++) {
      try {
        drawPile.get(i).flipCardUp();
      } catch (IndexOutOfBoundsException e) {
        return;
      }
    }
  }

  /*
  private void draw() {
    if (drawPile.isEmpty()) {
      return;
    }
    if (drawPile.size() >= numDraw && !drawPile.get(0).isFaceUp()) {
      for (int draw = 0; draw < numDraw; draw++) {
        drawPile.get(draw).flipCardUp();
      }
    } else if (!drawPile.get(0).isFaceUp()) {
      for (Card card : drawPile) {
        card.flipCardUp();
      }
    }
  }
   */

  @Override
  public void moveToFoundation(int srcPile, int foundationPile)
          throws IllegalStateException {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started yet.");
    }
    if (srcPile < 0 || srcPile > (numPile - 1)
            || foundationPile < 0 || foundationPile > (foundationPiles.size() - 1)) {
      throw new IllegalArgumentException("Invalid pile numbers.");
    }
    if (getPileHeight(srcPile) == 0) {
      throw new IllegalStateException("Source pile is empty.");
    }
    Card removedCard = this.getCardAt(srcPile, getPileHeight(srcPile) - 1);
    if (!this.isValidMoveToFoundation(removedCard, foundationPile)) {
      throw new IllegalStateException("Not a valid move");
    }
    //remove card from cascade then add to the top of foundation
    Card removed = this.cascadePiles.get(srcPile).remove(getPileHeight(srcPile) - 1);
    this.foundationPiles.get(foundationPile).add(0, removed);
    //flip the last card in the srcPile up if its facing down
    if (getPileHeight(srcPile) > 0) {
      int lastCardIndex = getPileHeight(srcPile) - 1;
      Card lastCard = this.cascadePiles.get(srcPile).get(lastCardIndex);
      if (!lastCard.isFaceUp()) {
        lastCard.flipCardUp();
      }
    }
  }

  @Override
  public void moveDrawToFoundation(int foundationPile) throws IllegalStateException {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started yet.");
    }
    // Validate the foundation pile number
    if (foundationPile < 0 || foundationPile > foundationPiles.size() - 1) {
      throw new IllegalArgumentException("Invalid foundation pile number.");
    }
    // Check if there are any draw cards
    if (drawPile.isEmpty()) {
      throw new IllegalStateException("No draw cards available.");
    }
    //Card removedCard = drawPile.remove(0);
    Card removedCard = drawPile.get(0);
    if (!this.isValidMoveToFoundation(removedCard, foundationPile)) {
      throw new IllegalStateException("Not a valid move");
    }

    //remove card from cascade then add to the top of foundation
    Card removed = drawPile.remove(0);
    this.foundationPiles.get(foundationPile).add(0, removed);

    //flip the next card in the drawPile if there are still draw cards
    draw();
  }

  @Override
  public void discardDraw() throws IllegalStateException {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started yet.");
    }
    if (drawPile.isEmpty()) {
      throw new IllegalStateException("No draw cards available to discard.");
    }
    discardDrawHelper();
  }

  protected void discardDrawHelper() {
    Card topCard = drawPile.remove(0);
    drawPile.add(topCard);
    topCard.flipCardDown();
    draw();
  }

  @Override
  public int getNumRows() {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    int longestPile = 0;
    for (int pile = 0; pile < numPile; pile++) {
      if (this.getPileHeight(pile) > longestPile ) {
        longestPile = this.getPileHeight(pile);
      }
    }
    return longestPile;
  }

  @Override
  public int getNumPiles() {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    if (cascadePiles == null) {
      return 0;
    }
    return cascadePiles.size();
  }

  @Override
  public int getNumDraw() {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    int visibleCardCount = 0;
    for (Card card : this.drawPile) {
      if (card != null && card.isFaceUp()) {
        visibleCardCount++;
      }
    }
    return visibleCardCount;
  }


  @Override
  public boolean isGameOver() throws IllegalStateException {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started yet.");
    }
    return !this.canDrawMoveToPile() && !this.canPileMoveToFoundation()
            && !this.canDrawMoveToFoundation() && !this.canPileMoveToPile();
  }

  //for drawPile -> cascadePile
  private boolean canDrawMoveToPile() {
    if (!drawPile.isEmpty()) {
      for (int pile = 0; pile < numPile; pile++) {
        for (Card card : drawPile) {
          if (this.isValidMoveToPile(card, pile)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  //for cascadePile -> foundationPile
  private boolean canPileMoveToFoundation() {
    for (int pile = 0; pile < numPile; pile++) {
      if (getPileHeight(pile) == 0) {
        continue;
      }
      for (int found = 0; found < this.getNumFoundations(); found++) {
        Card lastCard = getCardAt(pile, getPileHeight(pile) - 1);
        if (this.isValidMoveToFoundation(lastCard, found)) {
          return true;
        }
      }
    }
    return false;
  }

  //for drawPile -> foundationPile
  //checks if any card in the drawPile can be moved to foundationPile
  private boolean canDrawMoveToFoundation() {
    if (drawPile.isEmpty()) {
      return false;
    }
    for (Card card : drawPile) {
      for (int found = 0; found < this.getNumFoundations(); found++) {
        if (this.isValidMoveToFoundation(card, found)) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean canPileMoveToPile() {
    for (int col = 0; col < numPile; col++) {
      for (int row = 0; row < getPileHeight(col); row++) {
        if (!isCardVisible(col, row)) {
          break;
        }
        if (getCardAt(col, row).isFaceUp()) {
          if (isPileMoveAvailable(col, getPileHeight(col) - row)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  //TODO: change
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
    for (int piles = 0; piles < numPile; piles++) {
      //checking for every pile in the deck
      if (src == piles) {
        continue;
      }
      if (getPileHeight(piles) > 0) {
        Card bottomCard = this.getCardAt(piles, (getPileHeight(piles) - 1));
        if (cardOnTop.getRank() != bottomCard.getRank()) {
          return isValidMoveHelper(base, bottomCard, false);
        }
      }
    }
    return false;
  }

  @Override
  public int getScore() throws IllegalStateException {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started yet.");
    }
    int score = 0;
    for (int pile = 0; pile < this.getNumFoundations(); pile++) {
      List<Card> foundPile = foundationPiles.get(pile);
      if (foundPile.isEmpty()) {
        score += 0;
        continue;
      }
      score += getCardAt(pile).rankToNum();
    }
    return score;
  }

  @Override
  public int getPileHeight(int pileNum) throws IllegalStateException {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    if (pileNum < 0 || pileNum > (numPile - 1)) {
      throw new IllegalArgumentException("Invalid pile number");
    }
    List<Card> pile = this.cascadePiles.get(pileNum);
    return pile.size();
  }

  /*
  public int getFoundationHeight(int pileNum) {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    if (pileNum < 0 || pileNum > numPile + 13) {
      throw new IllegalArgumentException("Invalid pile number");
    }
    Card[] fPile = this.getFoundation(pileNum);
    return this.countCards(fPile);
  }
   */

  @Override
  public boolean isCardVisible(int pileNum, int card)
          throws IllegalArgumentException, IllegalStateException {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started yet.");
    }
    if (pileNum < 0 || pileNum > (numPile - 1)
            || card < 0 || card > this.getPileHeight(pileNum) - 1) {
      throw new IllegalArgumentException("Invalid coordinates.");
    }
    if (cascadePiles.get(pileNum).get(card) == null) {
      throw new IllegalArgumentException("There is no card.");
    }
    return cascadePiles.get(pileNum).get(card).isFaceUp();
  }

  @Override
  public Card getCardAt(int pileNum, int card) throws IllegalStateException {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started yet.");
    }
    if (pileNum < 0 || pileNum > this.getNumPiles() - 1
            || card < 0 || card > this.getPileHeight(pileNum) - 1) {
      throw new IllegalArgumentException("Invalid coordinates.");
    }
    if (cascadePiles.get(pileNum).get(card) == null) {
      throw new IllegalArgumentException("There is no card");
    }
    if (!isCardVisible(pileNum, card)) {
      throw new IllegalArgumentException("Card is not visible.");
    }
    return cascadePiles.get(pileNum).get(card);
  }

  @Override
  public Card getCardAt(int foundationPile) throws IllegalStateException {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started yet.");
    }
    if (foundationPile < 0 || foundationPile > foundationPiles.size() - 1) {
      throw new IllegalArgumentException("Invalid foundation pile number.");
    }
    if (!this.foundationPiles.get(foundationPile).isEmpty()) {
      return this.foundationPiles.get(foundationPile).get(0);
    }
    return null;
  }

  @Override
  public List<Card> getDrawCards() throws IllegalStateException {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    List<Card> visibleDrawCards = new ArrayList<>();
    for (Card card : drawPile) {
      if (card.isFaceUp()) {
        visibleDrawCards.add(card);
      }
    }
    return visibleDrawCards;
  }

  @Override
  public int getNumFoundations() throws IllegalStateException {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    //calculating number of columns in the first row
    return foundationPiles.size();
  }
}
