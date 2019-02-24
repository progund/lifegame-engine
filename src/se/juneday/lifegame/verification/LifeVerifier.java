package se.juneday.lifegame.verification;

import se.juneday.lifegame.domain.Exit;
import se.juneday.lifegame.domain.Game;
import se.juneday.lifegame.domain.Situation;
import se.juneday.lifegame.domain.Suggestion;
import se.juneday.lifegame.domain.ThingAction;
import se.juneday.lifegame.engine.LifeGameEngine;
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
  private Set<String> missingRooms;
  
  public void verifySuggestion(Suggestion suggestion) throws LifeVerifierException {
    for (Exit e : suggestion.exits()) {
      if (engine.getSituation(e.exit())==null) {
        failures++;
        if (throwExceptions) {
          throw new LifeVerifierException ("Could not find exit \"" + e.exit() + "\"");
        } else {
          Log.i(LOG_TAG, "      + verifying exit: " + e.exit() + ":  Failed");
          missingRooms.add(e.exit());
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

  public void verifySituations(Game game) throws LifeVerifierException {
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
    verifySituations(engine.game());
  }

  public int failures() {
    return failures;
  }
  
  public LifeVerifier(String file) {
    engine = new LifeGameEngine(file);
    missingRooms = new HashSet<>();
  } 

  public Set<String> missingRooms() {
    return missingRooms;    
  }
  
  public static void main(String[] args) {

    Log.logLevel(Log.LogLevel.ERROR);
    String fileName;
    if ( args.length > 0 ) {
      fileName = args[0];
    } else {
      fileName = "data/univ-swe.json";
    }

    
    Log.logLevel(Log.LogLevel.INFO);
    LifeVerifier verifier = new LifeVerifier(fileName);

    try { 
      verifier.verify();
      System.out.println(verifier.failures() + " failures");
      System.out.println(verifier.missingRooms());
    } catch (LifeVerifierException e) {
      e.printStackTrace();
    }
    
  }

  
}
