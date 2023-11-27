package cs3500.klondike.view;

import java.io.IOException;
import java.util.List;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * A simple text-based rendering of the Klondike game.
 */
public class KlondikeTextualView implements TextualView {
  private final KlondikeModel model;
  private final Appendable appendable;

  public KlondikeTextualView(KlondikeModel model, Appendable a) {
    this.model = model;
    this.appendable = a;
  }

  public KlondikeTextualView(KlondikeModel model) {
    this.model = model;
    this.appendable = System.out;
  }

  /**
   * Return the view of the current state of the board.
   * @return String that may be used to display the board.
   */
  public String toString() {
    StringBuilder view = new StringBuilder();

    //View drawPile
    view.append("Draw: ");
    List<Card> drawCards = model.getDrawCards();
    for (Card card : drawCards) {
      view.append(card.toString()).append(", ");
    }

    // Remove the trailing comma and space if there are draw cards
    if (!drawCards.isEmpty()) {
      view.delete(view.length() - 2, view.length());
    }
    view.append("\n");


    //View foundationPiles
    view.append("Foundation: ");
    for (int pile = 0; pile < model.getNumFoundations(); pile++) {
      Card foundationCard = model.getCardAt(pile);
      if (foundationCard == null) {
        view.append("<none>");
      } else {
        view.append(foundationCard.toString());
      }
      if (pile < model.getNumFoundations() - 1) {
        view.append(", ");
      }
    }
    view.append("\n");

    //View cascadePile

    for (int row = 0; row < model.getNumRows(); row++) {
      for (int pile = 0; pile < model.getNumPiles(); pile++) {
        try {
          if (model.getPileHeight(pile) == 0 && row == 0) {
            view.append("  X");
            continue;
          }
          if (!model.isCardVisible(pile, row)) {
            view.append("  ?");
          } else {
            Card card = model.getCardAt(pile, row);
            view.append(String.format("%3s", card.toString()));
          }
        } catch (IllegalArgumentException e) {
          view.append("   "); // Handle null card
        }
      }
      view.append("\n");
    }
    return view.toString();
  }

  @Override
  public void render() {
    try {
      this.appendable.append(this.toString());
    } catch (IOException e) {
      throw new IllegalArgumentException("Unable to render");
    }
  }
}
