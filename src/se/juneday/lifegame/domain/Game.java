package se.juneday.lifegame.domain;

import se.juneday.lifegame.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Game {

  private String title;
  private Situation startSitution;
  private Map<String, Situation> situations;
  private int situationCount;
  private int score;
  private Map<ThingAction, Integer> things;
  private Set<String> thingsNeeded;

  private static final String LOG_TAG = Game.class.getSimpleName();  
  
  public Game(String title, Map<String, Situation> situations,
              Situation startSitution, Map<ThingAction, Integer> things,
              Set<String> thingsNeeded) {
    this.title = title;
    this.situations = situations;
    situations.put("End of game", Situation.endSituation);
    this.startSitution = startSitution;
    situationCount = 0;
    this.things = things;
    if (things == null) {
      this.things = new HashMap<>();
    }
    this.thingsNeeded = thingsNeeded;
  }


  /* test only - there be dragons here */
  public Game(int situationCount, int score, Map<ThingAction, Integer> things) {
    this.title = "Testing game";
    this.situations = null;
    this.things = new HashMap<>();
    this.situations = new HashMap<>();
    situations.put("End of game", Situation.endSituation);
    this.situationCount = situationCount;
    this.score = score;
    this.things = things;
  }
  
  public Situation getSituation(String title) {
    return situations.get(title);
  }

  public Map<String, Situation> situations() {
    return situations;
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

  public Map<ThingAction, Integer> things() {
    return things;
  }

  public void addThing(ThingAction thing) {
    Integer count = things.get(thing);
    if (count == null) {
      Log.v(LOG_TAG, "  Things, adding first " + thing);
      things.put(thing, 1);
    } else {
      Log.v(LOG_TAG, "  Things, adding one more " + thing);
      things.put(thing, count + 1);
    }
  }

  /*  public void dropThing(ThingAction ta) {
      dropThing(ta);
      }
  */
  
  public void dropThing(ThingAction thing) {
    Integer count = things.get(thing);
    if (count != null) {
      if (count == 1) {
        things.remove(thing);
      } else if (count > 0) {
        things.put(thing, count - 1);
      }
    }
  }

  public Set<String> thingsNeeded() {
    return thingsNeeded;
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
