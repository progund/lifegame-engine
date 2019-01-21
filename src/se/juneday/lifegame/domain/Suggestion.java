package se.juneday.lifegame.domain;

import java.util.List;

public class Suggestion {

    private String phrase;
    private List<Exit> exits;

    public Suggestion(String phrase, List<Exit> exits) {
        this.phrase = phrase;
        this.exits = exits;
    }

    public String phrase() {
        return phrase;
    }

    public List<Exit> exits() {
        return exits;
    }

    @Override
    public String toString() {
        return "Suggestion{" +
                "phrase='" + phrase + '\'' +
                ", exits=" + exits +
                '}';
    }
}
