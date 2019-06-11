package se.juneday.lifegame.test;

import se.juneday.lifegame.domain.Exit;
import se.juneday.lifegame.domain.InvalidLifeException;
import se.juneday.lifegame.domain.Situation;
import se.juneday.lifegame.domain.Suggestion;
import se.juneday.lifegame.domain.ThingAction;
import se.juneday.lifegame.engine.LifeGameEngine;
import se.juneday.lifegame.verification.LifeVerifier;
import se.juneday.lifegame.verification.LifeVerifierException;
import se.juneday.lifegame.json.JParser;
import se.juneday.lifegame.util.Log;

import java.util.Map;
import java.util.ArrayList;
import java.util.Scanner;


public class LifeCli {

  private static final String LOG_TAG = LifeCli.class.getSimpleName();
  private static Scanner input = new Scanner(System.in);

  public static void println(String s) {
    System.out.println(s);
  }

  public static void print(String s) {
    System.out.print(s);
  }

  public static String read() {
    return input.next();
  }

  public static void main(String[] args) {

    Log.logLevel(Log.LogLevel.INFO);
    // Log.includeTag("LifeGame");
    LifeGameEngine engine;
    String file = "";
    try { 
      if ( args.length > 0 ) {
        file = args[0];
      } else {
        file = "data/univ-swe.json";
      }
      engine = new LifeGameEngine(file);
    } catch (InvalidLifeException e) {
      println("\n\n\n ---=== FAILURE ===---\n");
      println("Could not even parse life in: " + file);
      System.exit(1);
      return;
    }

    try { 
      LifeVerifier verifier = new LifeVerifier(file);
      verifier.verify();
      System.out.println("Verification report");
      System.out.println(" * Failures: " + verifier.failures());
      System.out.println(" * Missing situations: " + verifier.missingSituations());
      System.out.println(" * Missing things:     " + verifier.missingThings());
      System.out.println(" * Missing exits:      " + verifier.missingExits());
      if (verifier.failures()!=0) {
        //System.exit(1);
      }
    } catch (LifeVerifierException | InvalidLifeException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
      System.exit(1);
    }
    
    Situation here = engine.situation();

    while( here!=null
           &&
           !engine.gameOver() ) {

      println("\n\n\n ---=== " + here.title() + " ===---\n");
      println(here.description() + "\n");
      Log.d(LOG_TAG, " Round information");
      Log.d(LOG_TAG, "  Score:              " + engine.score());
      Log.d(LOG_TAG, "  Situation count:    " + engine.situationCount());
      Log.d(LOG_TAG, "  Things in the room: " + engine.situation().actions());
      Log.d(LOG_TAG, "  Things in your bag: " + engine.things());
      println(here.question());
      int idx=0;
      for (Suggestion s : here.suggestions()) {
        println("  " + idx++ + ". " + s.phrase());
      }
            
      ArrayList<ThingAction> actions = new ArrayList<>(engine.things().keySet());
      for (ThingAction ta : actions) {
        println("  " + idx++ + ". drop " + ta);
      }
            
      for (ThingAction ta : engine.situation().actions()) {
        println("  " + idx++ + ". take " + ta);
      }
            
      print("What's next?  ");
      String input = read();
      int menuIndex = 0 ;
      try {
        menuIndex = Integer.parseInt(input);
        System.out.println("Your choice: " + menuIndex);

        //                println("Test " + here.suggestions().size() + "  " + engine.things().entrySet().size());

        if (menuIndex < here.suggestions().size()) {
          String phrase = here.suggestions().get(menuIndex).phrase();
          Log.d(LOG_TAG, "You said: " + input + " => " + menuIndex + " => " + phrase );
          engine.handleExit(phrase);

          if ( engine.explanation() != null ) {
            println("\n -- Explanation: " + engine.explanation());
          }
      
          
          here = engine.situation();
        } else if ( menuIndex < (here.suggestions().size() + engine.things().entrySet().size())) {
          int dropIndex = menuIndex - here.suggestions().size() ;
          Log.d(LOG_TAG, "Drop from user: index:   " + dropIndex);
          Log.d(LOG_TAG, "Drop from user: index:   " + dropIndex);
          Log.d(LOG_TAG, "Drop from user: actions: " + engine.situation().actions().size());
          Log.d(LOG_TAG, "Drop from user: actions: " + engine.situation().actions());
          Log.d(LOG_TAG, "Drop from user: things:  " + engine.things().size());
          Log.d(LOG_TAG, "Drop from user: things:  " + engine.things());
          Log.d(LOG_TAG, "Drop from user: actions: " + actions.size());
          Log.d(LOG_TAG, "Drop from user: actions: " + actions);
          Log.d(LOG_TAG, "Drop from user: --"        + actions.get(dropIndex));

          engine.removeActionThing(actions.get(dropIndex));
        } else {
          Log.d(LOG_TAG, "Pick up from room: ");
          int takeIndex = menuIndex - (here.suggestions().size() + engine.things().entrySet().size());
          // add to user, remove from room
          engine.addActionThing(engine.situation().actions().get(takeIndex));
        }
      } catch (NumberFormatException | IndexOutOfBoundsException e) {
        e.printStackTrace();
        println("Your input " + input + " was invalid. Choose again");
        println(" * " + menuIndex + " < " + here.suggestions().size());
        println(" * " + menuIndex + " < " + here.suggestions().size() +  " + " + engine.things().entrySet().size());
      }
      
      if (engine.explanation() != null ) {
        println("\n\n\n" + engine.explanation() + "\n\n\n");
      }
      
    }
    println("");
    println("");
    println("  You've won!");
    println("");
    println("");

  }

}
