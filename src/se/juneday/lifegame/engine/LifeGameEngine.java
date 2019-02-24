package se.juneday.lifegame.engine;

import se.juneday.lifegame.domain.*;
import se.juneday.lifegame.json.JParser;
import se.juneday.lifegame.util.Log;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LifeGameEngine {

    private static final String LOG_TAG = LifeGameEngine.class.getSimpleName();
    private Game game;
    private Situation current;

    public LifeGameEngine(String file) {
        JParser jParser = new JParser();
        this.game = jParser.game(file);
        current = game.startSitution();
    }

    public Situation getSituation(String title) {
        return game.getSituation(title);
    }

    public Situation situation() {
        return current;
    }


    public boolean gameOver() {
        return current.title().equals("End of game");
    }


    public boolean gameOver(Situation situation) {
        return situation.title().equals("End of game");
    }


    public Situation handleExit(String answer) {
      game.incSituationCount();

        Log.i(LOG_TAG, "handleExit(" + answer + ") ");
        for (Suggestion suggestion : current.suggestions()) {
            Log.i(LOG_TAG, "handleExit(" + answer + "):   " + suggestion);

            if (answer.equals(suggestion.phrase())) {
                Log.d(LOG_TAG, "handleExit(" + answer + "):   " + suggestion.phrase() + " found");

                for (Exit e : suggestion.exits()) {
                    Log.i(LOG_TAG,"handleExit(" + answer + "):   " + suggestion.phrase()+ "       exit: " + e);
                    Log.i(LOG_TAG,"handleExit:    situation count: " + situationCount());
                    if (e.isTrue(game)) {
                        Log.d(LOG_TAG, "handleExit(" + answer + "):   " + suggestion.phrase() + "       exit: " + e);
                        String title = e.exit();
                        Log.d(LOG_TAG, "handleExit(" + answer + "):   " + suggestion.phrase() + "       exit: " + e + "  title: " + title + " RETURNING");
                        
                        // Change situation

                        Situation savedCurrent = current;
                        current = game.getSituation(title);
                        if (current!=null) {
                          Log.d(LOG_TAG, "handleExit(" + answer + "):   " + current.title());
                        } else {
                          Log.e(LOG_TAG, "current is null from: " + title);
                        }
                        //                        Log.i(LOG_TAG, " current: " + current);

                        /************************************************************
                         * Leave this to the ui instead 
                        List<ThingAction> actions = current.actions();
                        Iterator<ThingAction> iter = actions.iterator();

                        // TODO: why does this NOT work without the size check?
                        while (actions.size() > 0 && iter.hasNext()) {
                            ThingAction action = iter.next();
                            game.addThing(action);
                            actions.remove(action);
                            Log.i(LOG_TAG, "  Things:"           + game.things() + " / " + actions);
                        }
                        ****************************************************************/

                        
                        Log.d(LOG_TAG, "handleExit(" + answer + "):   " + current);
                        if (current.title().equals("End of game")) {
                            Log.d(LOG_TAG, "You win!!");
                            current = Situation.endSituation;
                        }
                        return current;
                    }
                }
                Log.d(LOG_TAG, "handleExit(" + answer + "):   " + suggestion.phrase() + " handled");
            }

        }
        if (current.title().equals("End of game")) {
            Log.d(LOG_TAG, "You win!!");
            current = Situation.endSituation;
        }
        Log.e(LOG_TAG, "Odd, we seem to have gotten an answer not suggested");
        return null;
    }

    public int situationCount() {
        return game.situationCount();
    }

    public int score() {
        return game.score();
    }

    public void incScore(int amount) {
        game.incScore(amount);
    }

    public void decScore(int amount) {
        game.decScore(amount);
    }

  public Map<ThingAction, Integer> things() {
      return game.things();
    }

    public Game game() {
        return game;
    }

  public void removeActionThing(ThingAction action) {
    game.dropThing(action);
    current.addActionThing(action);
  }
  
  public void addActionThing(ThingAction ta) {
    game.addThing(ta);
    current.removeActionThing(ta);
  }

  @Override
  public String toString() {
    return "LifeGameEngine{" +
      "game=" + game +
      ", current=" + current +
      '}';
  }
}
