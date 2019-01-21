package se.juneday.lifegame.test;

import se.juneday.lifegame.domain.Exit;
import se.juneday.lifegame.domain.Situation;
import se.juneday.lifegame.domain.Suggestion;
import se.juneday.lifegame.engine.LifeGameEngine;
import se.juneday.lifegame.json.JParser;

import java.util.Map;
import java.util.Scanner;


public class LifeCli {

    private static Scanner input = new Scanner(System.in);

    public static void println(String s) {
        System.out.println(s);
    }

    public static String read() {
        return input.next();
    }

    public static void main(String[] args) {

        LifeGameEngine engine = new LifeGameEngine("data/univ.json");


        Situation here = engine.situation();

        while(here!=null) {
            println("You're in: " + here.title());
            println(here.question());
            int idx=1;
            for (Suggestion s : here.suggestions()) {
                println(idx++ +"." + s.phrase());
            }
            String input = read();
            println("You said: " + input);
            engine.handleExit(input);
            here = engine.situation();
        }

    }

}
