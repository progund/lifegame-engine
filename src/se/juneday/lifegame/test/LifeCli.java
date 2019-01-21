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

        LifeGameEngine engine = new LifeGameEngine("data/univ.json");


        Situation here = engine.situation();

        while(here!=null && here!=Situation.endSituation) {
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

    }

}
