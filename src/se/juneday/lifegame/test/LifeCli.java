package se.juneday.lifegame.test;

import se.juneday.lifegame.domain.Exit;
import se.juneday.lifegame.domain.Situation;
import se.juneday.lifegame.domain.Suggestion;
import se.juneday.lifegame.engine.LifeGameEngine;
import se.juneday.lifegame.json.JParser;
import se.juneday.lifegame.util.Log;

import java.util.Map;
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

        LifeGameEngine engine = new LifeGameEngine("data/univ.json");


        Situation here = engine.situation();

        while( here!=null
               &&
               !engine.gameOver() ) {
            Log.i(LOG_TAG, " Round information");
            Log.i(LOG_TAG, "  Score:           " + engine.score());
            Log.i(LOG_TAG, "  Situation count: " + engine.situationCount());
            Log.i(LOG_TAG, "  Things:"           + engine.things());
            println("You're in: " + here.title());
            println(here.description());
            println(here.question());
            int idx=0;
            println("Suggestions:");
            for (Suggestion s : here.suggestions()) {
                println("  " + idx++ + ". " + s.phrase());
            }
            print("What index do you want to go for?: ");
            String input = read();
            try {
                int menuIndex = Integer.parseInt(input);
                String phrase = here.suggestions().get(menuIndex).phrase();
                Log.d(LOG_TAG, "You said: " + input + " => " + menuIndex + " => " + phrase );
                engine.handleExit(phrase);
                here = engine.situation();
            } catch (NumberFormatException e) {
                println("Your input " + input + " was invalid. Choose again");
            }
        }
        println("");
        println("");
        println("  You've won!");
        println("");
        println("");

    }

}
