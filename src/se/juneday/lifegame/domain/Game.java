package se.juneday.lifegame.domain;

import se.juneday.lifegame.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    private String title;
    private Situation startSitution;
    private Map<String, Situation> situations;
    private int situationCount;
    private int score;
    private Map<String, Integer> things;

    public Game(String title, Map<String, Situation> situations,
                Situation startSitution, Map<String, Integer> things) {
        this.title = title;
        this.situations = situations;
        situations.put("End of game", Situation.endSituation);
        this.startSitution = startSitution;
        situationCount = 0;
        this.things = things;
        if (things == null) {
            this.things = new HashMap<>();
        }
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

    public Map<String, Integer> things() {
        return things;
    }

    public void addThing(ThingAction thing) {
        Integer count = things.get(thing.thing());
        if (count == null) {
            things.put(thing.thing(), 1);
        } else {
            things.put(thing.thing(), count + 1);
        }
    }

    public void dropThing(String thing) {
        Integer count = things.get(thing);
        if (count != null) {
            if (count == 1) {
                things.remove(thing);
            } else if (count > 0) {
                things.put(thing, count - 1);
            }
        }
    }

    @Override
    public String toString() {
        return "Game{" +
                "title='" + title + '\'' +
                ", startSitution=" + startSitution +
                ", situations=" + situations +
                ", situationCount=" + situationCount +
                ", score=" + score +
                ", things=" + things +
                '}';
    }
}
