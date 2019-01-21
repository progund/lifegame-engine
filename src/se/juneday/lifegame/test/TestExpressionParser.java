package se.juneday.lifegame.test;

import se.juneday.lifegame.domain.Game;
import se.juneday.lifegame.engine.LifeGameEngine;
import se.juneday.lifegame.json.ExpressionParser;
import se.juneday.lifegame.json.JParser;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.function.Predicate;

public class TestExpressionParser {

    public static void main(String[] args) {

        LifeGameEngine lengine = new LifeGameEngine("data/univ.json");

        System.out.println("You're in: " + lengine.situation().title());
        lengine.handleExit("Tell 'em to fuck off");

        System.out.println("You're in: " + lengine.situation().title());
        lengine.handleExit("Tell 'em to fuck off");

        System.out.println("You're in: " + lengine.situation().title());
        lengine.handleExit("Tell 'em to fuck off");

        System.out.println("You're in: " + lengine.situation().title());
        lengine.handleExit("Tell 'em to fuck off");

        System.out.println("You're in: " + lengine.situation().title());
        lengine.handleExit("Tell 'em to fuck off");


    }


}
