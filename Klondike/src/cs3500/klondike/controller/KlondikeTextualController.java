package cs3500.klondike.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Supplier;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.view.KlondikeTextualView;
import cs3500.klondike.view.TextualView;

/**
 * Klondike controller that takes the userInputs and renders the view of the board.
 */

public class KlondikeTextualController implements cs3500.klondike.controller.KlondikeController {
  private final Readable in;
  private final Appendable out;
  private final Map<String, Supplier<ICommand>> commandMap;

  /**
   * Constructors of Klondike controller that produces the view of the board.
   * @param readable input that the user is putting in
   * @param appendable output of the controller (view)
   */
  public KlondikeTextualController(Readable readable, Appendable appendable) {
    if (readable == null || appendable == null) {
      throw new IllegalArgumentException("Invalid inputs");
    }
    this.in = readable;
    this.out = appendable;
    this.commandMap = new HashMap<>();

    this.commandMap.putIfAbsent("mpp", MPP::new);
    this.commandMap.putIfAbsent("md", MD::new);
    this.commandMap.putIfAbsent("mpf", MPF::new);
    this.commandMap.putIfAbsent("mdf", MDF::new);
    this.commandMap.putIfAbsent("dd", DD::new);
  }


  @Override
  public void playGame(KlondikeModel model, List<Card> deck,
                       boolean shuffle, int numPiles, int numDraw) {
    if (model == null) {
      throw new IllegalArgumentException("Invalid model");
    }

    //initializing view
    TextualView view = new KlondikeTextualView(model, this.out);

    //counts how many cards in the given deck
    int totalCards = deck.size();

    try {
      model.startGame(deck, shuffle, numPiles, numDraw);
    } catch (IllegalStateException | IllegalArgumentException e) {
      throw new IllegalStateException("Invalid inputs.");
    }

    view.render();
    IOUtils.writeIO(this.out, String.format("Score: %d\n", model.getScore()));

    Scanner scan = new Scanner(this.in);
    while (scan.hasNext()) {
      String command = scan.next();

      if (command.equalsIgnoreCase("q")) {
        IOUtils.writeIO(this.out, "Game quit!\nState of game when quit:\n");
        view.render();
        IOUtils.writeIO(this.out, String.format("Score: %d\n", model.getScore()));
        return;
      }

      Supplier<ICommand> cmdCreator = this.commandMap.getOrDefault(command, null);
      if (cmdCreator == null) {
        IOUtils.writeIO(this.out, "Invalid move. Play again. " + command + "\n");
        view.render();
        continue;
      } else {
        ICommand cmdToRun = cmdCreator.get();
        if (cmdToRun != null) {
          try {
            boolean runSuccess = cmdToRun.run(scan, model);
            if (!runSuccess) {
              continue;
            }
            view.render();
          } catch (IllegalArgumentException | IllegalStateException e) {
            IOUtils.writeIO(this.out, "Invalid move. Play again. " + command + "\n");
            view.render();
            continue;
          }
        } else {
          IOUtils.writeIO(this.out, "Invalid move. Play again. " + command + "\n");
          view.render();
        }
      }

      if (model.isGameOver()) {
        if (model.getScore() == totalCards) {
          IOUtils.writeIO(this.out, "You win!\n");
        }
        IOUtils.writeIO(this.out, String.format("Game over. Score: %d\n", model.getScore()));
        scan.close();
        return;
      }
    }

    throw new IllegalStateException("Out of inputs");
  }

}
