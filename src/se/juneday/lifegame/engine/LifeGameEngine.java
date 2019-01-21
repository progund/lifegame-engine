package se.juneday.lifegame.engine;

import se.juneday.lifegame.domain.Exit;
import se.juneday.lifegame.domain.Game;
import se.juneday.lifegame.domain.Situation;
import se.juneday.lifegame.domain.Suggestion;
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
                    if (e.isTrue(game)) {
                        Log.d(LOG_TAG,"handleExit(" + answer + "):   " + suggestion.phrase() + "       exit: " + e);
                        String title = e.exit();
                        Log.d(LOG_TAG,"handleExit(" + answer + "):   " + suggestion.phrase() + "       exit: " + e +  "  title: " + title + " RETURNING");
                        current = game.getSituation(title);
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

}
