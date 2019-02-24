package se.juneday.lifegame.domain;

import se.juneday.lifegame.engine.LifeGameEngine;
import se.juneday.lifegame.util.Log;

import java.util.function.Predicate;

public class Exit {

    private static final String LOG_TAG = Exit.class.getSimpleName() ;
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
        Log.i(LOG_TAG, "isTrue(): " + predicate + "  ===> " + predicate.test(game) + "   exit situation" + exitSituation );
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
