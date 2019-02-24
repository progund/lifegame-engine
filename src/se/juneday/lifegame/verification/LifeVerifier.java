package se.juneday.lifegame.verification;

import se.juneday.lifegame.domain.Exit;
import se.juneday.lifegame.domain.Game;
import se.juneday.lifegame.domain.Situation;
import se.juneday.lifegame.domain.Suggestion;
import se.juneday.lifegame.domain.ThingAction;
import se.juneday.lifegame.engine.LifeGameEngine;
import se.juneday.lifegame.domain.InvalidLifeException;
import se.juneday.lifegame.json.JParser;
import se.juneday.lifegame.util.Log;

import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;


public class LifeVerifier {

  private static final String LOG_TAG = LifeVerifier.class.getSimpleName();  
  private LifeGameEngine engine;
  private boolean throwExceptions;
  private int failures;
  private Set<String> missingSituations;
  private Set<String> missingThings;
  
  public void verifySuggestion(Suggestion suggestion) throws LifeVerifierException {
    for (Exit e : suggestion.exits()) {
      if (engine.getSituation(e.exit())==null) {
        failures++;
        if (throwExceptions) {
          throw new LifeVerifierException ("Could not find exit \"" + e.exit() + "\"");
        } else {
          Log.i(LOG_TAG, "      + verifying exit: " + e.exit() + ":  Failed");
          missingSituations.add(e.exit());
        }
      } else {
        Log.i(LOG_TAG, "      + verifying exit: " + e.exit() + ":  Ok");
      }
    }
  }

  public void verifySituation(Situation situation) throws LifeVerifierException {
    for (Suggestion s : situation.suggestions()) {
      Log.i(LOG_TAG, "    + verifying suggestions: " + s.phrase());
      verifySuggestion(s);
    }
  }

  public void verifySituations() throws LifeVerifierException {
    Game game = engine.game();
    for (Map.Entry<String, Situation> s : game.situations().entrySet()) {
      Log.i(LOG_TAG, "  ---------------------- verifying situation: " + s.getValue().title());
      if (engine.gameOver(s.getValue())) {
        Log.i(LOG_TAG, "  + game ove reached");
        return;
      }
      Log.i(LOG_TAG, "  + verifying situation: " + s.getValue().title());
      verifySituation(s.getValue());
    }
  }

  public void verify() throws LifeVerifierException {
    verifySituations();
    verifyThings();
  }

  public int failures() {
    return failures;
  }
  
  public LifeVerifier(String file) throws InvalidLifeException {
    engine = new LifeGameEngine(file);
    missingSituations = new HashSet<>();
    missingThings = new HashSet<>();
  } 

  public Set<String> missingSituations() {
    return missingSituations;    
  }

  public Set<String> missingThings() {
    return missingThings;
  }
  
  public void verifyThing(String thing) {
    for (Situation situation : engine.game().situations().values()) {
      for (ThingAction thingAction : situation.actions()) {
        if (thing.equals(thingAction.thing())) {
          Log.i(LOG_TAG, "  + verifying thing ---  " + thing + ": Ok");
          return ;
        }
      }
    }
    missingThings.add(thing);
    failures++;
    Log.i(LOG_TAG, "  + verifying thing ---  " + thing + ": Failed");
  }
  
  public void verifyThings() {
    for (String thing : engine.game().thingsNeeded()) {
      Log.i(LOG_TAG, "  + verifying thing " + thing);
      verifyThing(thing);
    }
  }
  
  public static void main(String[] args) {

    Log.logLevel(Log.LogLevel.ERROR);
    String fileName;
    if ( args.length > 0 ) {
      fileName = args[0];
    } else {
      fileName = "data/univ-swe.json";
    }

    
    try { 
      LifeVerifier verifier = new LifeVerifier(fileName);

      verifier.verify();
      System.out.println("Verification report");
      System.out.println(" * Failures: " + verifier.failures());
      System.out.println(" * Missing situations: " + verifier.missingSituations());
      System.out.println(" * Missing things:     " + verifier.missingThings());
    } catch (LifeVerifierException | InvalidLifeException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }
    
  }

  
}
