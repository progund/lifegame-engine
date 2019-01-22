package se.juneday.lifegame.engine;

import se.juneday.lifegame.domain.*;
import se.juneday.lifegame.json.JParser;
import se.juneday.lifegame.util.Log;

import java.util.List;
import java.util.Map;

public class LifeGameEngine {

    private static final String LOG_TAG = LifeGameEngine.class.getSimpleName();
    private Game game;
    private Situation current;

    public LifeGameEngine(String file) {
        JParser jParser = new JParser();
        this.game = jParser.game("data/univ.json");
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


    public Situation handleExit(String answer) {
        game.incSituationCount();

        Log.d(LOG_TAG,"handleExit(" + answer + ") ");
        for (Suggestion suggestion : current.suggestions()) {
            Log.d(LOG_TAG,"handleExit(" + answer + "):   " + suggestion);
            if (answer.equals(suggestion.phrase())) {
                Log.d(LOG_TAG,"handleExit(" + answer + "):   " + suggestion.phrase()+ " found");
                for (Exit e : suggestion.exits()) {
                    Log.d(LOG_TAG,"handleExit(" + answer + "):   " + suggestion.phrase()+ "       exit: " + e);
                    System.out.println("handleExit(" + answer + "):   " + suggestion.phrase()+ "       exit: " + e);
                    if (e.isTrue(game)) {
                        Log.d(LOG_TAG,"handleExit(" + answer + "):   " + suggestion.phrase() + "       exit: " + e);
                        String title = e.exit();
                        Log.d(LOG_TAG,"handleExit(" + answer + "):   " + suggestion.phrase() + "       exit: " + e +  "  title: " + title + " RETURNING");

                        // Change situation
                        current = game.getSituation(title);

                        for (ThingAction action : current.actions()) {
                            Log.i(LOG_TAG," * action: " + action);
                            switch (action.action()) {
                                case TAKE:
                                    Log.i(LOG_TAG," * TAKE: " + action.thing());
                                    game.addThing(action.thing());
                                    break;
                                case DROP:
                                    Log.i(LOG_TAG," * DROP: " + action.thing());
                                    game.dropThing(action.thing());
                                    break;
                            }
                        }



                        Log.d(LOG_TAG,"handleExit(" + answer + "):   " + current);
                        Log.d(LOG_TAG,"handleExit(" + answer + "):   " + current.title());
                        if (current.title().equals("End of game")) {
                            Log.d(LOG_TAG,"You win!!");
                            current = Situation.endSituation;
                        }
                        return current;
                    }
                }
                Log.d(LOG_TAG,"handleExit(" + answer + "):   " + suggestion.phrase() + " handled");
            }

        }
        if (current.title().equals("End of game")) {
            Log.d(LOG_TAG,"You win!!");
            current = Situation.endSituation;
        }
        return current;
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

    public Map<String, Integer> things() {
        return game.things();
    }

    @Override
    public String toString() {
        return "LifeGameEngine{" +
                "game=" + game +
                ", current=" + current +
                '}';
    }
}
