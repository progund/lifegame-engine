package se.juneday.lifegame.domain;

import java.util.List;
import java.util.Map;

public class Game {

    private String title;
    private Situation startSitution;
    private Map<String, Situation> situations;
    private int situationCount;
    private int score;

    public Game(String title, Map<String, Situation> situations, Situation startSitution) {
        this.title = title;
        this.situations = situations;
        situations.put("End of game", Situation.endSituation);
        this.startSitution = startSitution;
        situationCount = 0;
    }

    public Situation getSituation(String title) {
        return situations.get(title);
    }

    public Situation startSitution() {
        return startSitution;
    }

    public int situationCount() {
        return situationCount;
    }

    public void incSituationCount() {
        situationCount++;
    }

    public int score() {
        return score;
    }

    public int incScore(int amount) {
        return score += amount;
    }

    public int decScore(int amount) {
        return score -= amount;
    }

}
