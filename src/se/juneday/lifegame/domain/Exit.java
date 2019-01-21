package se.juneday.lifegame.domain;

import se.juneday.lifegame.engine.LifeGameEngine;

import java.util.function.Predicate;

public class Exit {

    private Predicate<Game> predicate;
    private String exitSituation;

    public Exit(Predicate<Game> predicate, String exitSituation) {
        this.predicate = predicate;
        this.exitSituation = exitSituation;
    }

    public String exit() {
        return exitSituation;
    }

    public Boolean isTrue(Game game) {
        return predicate.test(game);
    }


    @Override
    public String toString() {
        return "Exit{" +
                "predicate=" + predicate +
                ", exitSituation='" + exitSituation + '\'' +
                '}';
    }
}
