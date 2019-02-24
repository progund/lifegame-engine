package se.juneday.lifegame.test;

import se.juneday.lifegame.domain.Game;
import se.juneday.lifegame.domain.InvalidLifeException;
import se.juneday.lifegame.engine.LifeGameEngine;
import se.juneday.lifegame.json.ExpressionParser;
import se.juneday.lifegame.json.JParser;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.function.Predicate;

public class TestExpressionParser {

    public static void main(String[] args) {

      LifeGameEngine lengine;
      try { 
        lengine = new LifeGameEngine("data/univ.json");
        System.out.println();
      } catch (InvalidLifeException e) {
        System.out.println("\n\n\n ---=== FAILURE ===---\n");
        System.out.println(e.getCause());
        System.exit(1);
        return;
      }


        System.out.println("You're in: " + lengine.situation().title());
        lengine.handleExit("Tell 'em to fuck off");

        System.out.println("You're in: " + lengine.situation().title());
        lengine.handleExit("Tell 'em to fuck off");

        System.out.println("You're in: " + lengine.situation().title());
        lengine.handleExit("Go");

        System.out.println("You're in: " + lengine.situation().title());
        lengine.handleExit("Listen to what they say and follow instruction");

        System.out.println("You're in: " + lengine.situation().title());
        lengine.handleExit("Tell 'em to fuck off");

        System.out.println("You're in: " + lengine.situation().title());
        lengine.handleExit("Go");

        System.out.println("You're in: " + lengine.situation().title());

    }


}
