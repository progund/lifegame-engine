package se.juneday.lifegame.engine;

import se.juneday.lifegame.domain.Exit;
import se.juneday.lifegame.domain.Game;
import se.juneday.lifegame.domain.Situation;
import se.juneday.lifegame.domain.Suggestion;
import se.juneday.lifegame.json.JParser;

import java.util.List;
import java.util.Map;

public class LifeGameEngine {

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

    public Situation handleExit(String answer) {
        game.incSituationCount();

        System.out.println("handleExit(" + answer + ") ");
        for (Suggestion suggestion : current.suggestions()) {
            System.out.println("handleExit(" + answer + "):   " + suggestion);
            if (answer.equals(suggestion.phrase())) {
                System.out.println("handleExit(" + answer + "):   " + suggestion + " found");
                for (Exit e : suggestion.exits()) {
                    System.out.println("handleExit(" + answer + "):   " + suggestion + "   exit: " + e);
                    if (e.isTrue(game)) {
                        String title = e.exit();
                        System.out.println("handleExit(" + answer + "):   " + suggestion + "   exit: " + e +  "  title: " + title);
                        current = game.getSituation(title);
                        return current;
                    }
                }
            }

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
