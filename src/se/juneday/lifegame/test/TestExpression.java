package se.juneday.lifegame.test;

import se.juneday.lifegame.domain.Game;
import se.juneday.lifegame.domain.ThingAction;
import se.juneday.lifegame.domain.InvalidLifeException;
import se.juneday.lifegame.engine.LifeGameEngine;
import se.juneday.lifegame.json.ExpressionParser;
import se.juneday.lifegame.json.JParser;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.function.Predicate;

import java.util.Map;
import java.util.HashMap;


public class TestExpression {

  

  private static void testInvalidExpression(int situationsCount,
                                            int score,
                                            String expr,
                                            boolean expected,
                                            Map<ThingAction, Integer> things) {
    try {
      testExpression(0, 0, expr,  false, things);
      throw new RuntimeException("Exception \"" + expr + "\" not causing an exception...");
    } catch (RuntimeException e) {
      ;
    }
    System.err.println(" OK (invalidity checked)");
  }
  
  private static void testExpression(int situationsCount,
                                     int score,
                                     String expr,
                                     boolean expected,
                                     Map<ThingAction, Integer> things) {
    try {
      System.err.print(" * \"" + expr + "\"   ===> ");
      Game game = new Game(situationsCount, score, things);
      ExpressionParser parser = new ExpressionParser();
      Predicate predicate = parser.parse(expr);
      
      if (predicate.test(game) != expected) {
        throw new RuntimeException("Expression \"" + expr + "\" not evaluated to " + expected);
      }
      System.err.println(" OK");
      
    } catch (InvalidLifeException e) {
      throw new RuntimeException("Expression \"" + expr + "\" not evaluated to " + expected);
    }
  }
    
  public static void main(String[] args) {

    /*
     * score:
     *     ==
     *     !=
     *     >
     *     <
     */
    testExpression(0, 0, "score == 0", true, null);
    testExpression(0, 0, "score == 10", false, null);
    testExpression(0, 0, "score != 0", false, null);
    testExpression(0, 0, "score > 0",  false, null);
    testExpression(0, 0, "score < 0",  false, null);
    
    testExpression(10, 0, "score == 0", true, null);
    testExpression(10, 0, "score == 10", false, null);
    testExpression(10, 0, "score != 0", false, null);
    testExpression(10, 0, "score > 0",  false, null);
    testExpression(10, 0, "score < 0",  false, null);
    
    testExpression(0, 10, "score == 0", false, null);
    testExpression(0, 10, "score == 10", true, null);
    testExpression(0, 10, "score != 0", true, null);
    testExpression(0, 10, "score > 0",  true, null);
    testExpression(0, 10, "score < 0",  false, null);
    
    /*
     * situations:
     *     ==
     *     !=
     *     >
     *     <
     */
    testExpression(0, 0, "situations == 0", true, null);
    testExpression(0, 0, "situations == 10",false, null);
    testExpression(0, 0, "situations != 0", false, null);
    testExpression(0, 0, "situations > 0",  false, null);
    testExpression(0, 0, "situations < 0",  false, null);
    
    testExpression(10, 0, "situations == 0", false, null);
    testExpression(10, 0, "situations == 10",true, null);
    testExpression(10, 0, "situations != 0", true, null);
    testExpression(10, 0, "situations > 0",  true, null);
    testExpression(10, 0, "situations < 0",  false, null);
    
    testExpression(0, 10, "situations == 0", true, null);
    testExpression(0, 10, "situations == 10",false, null);
    testExpression(0, 10, "situations != 0", false, null);
    testExpression(0, 10, "situations > 0",  false, null);
    testExpression(0, 10, "situations < 0",  false, null);

    /* 
     * Situations and score 
     */
    testExpression(0, 0, "situations == score",  true, null);
    testExpression(0, 10, "situations == score",  false, null);
    testExpression(0, 10, "situations < score",  true, null);
    testExpression(0, 10, "situations != score",  true, null);
    testExpression(0, 10, "situations > score",  false, null);
    testExpression(0, 10, "situations < score",  true, null);
    
    testExpression(10, 0, "situations == score",  false, null);
    testExpression(10, 10, "situations == score",  true, null);
    testExpression(10, 0, "situations < score",  false, null);
    testExpression(10, 0, "situations != score",  true, null);
    testExpression(10, 0, "situations > score",  true, null);
    testExpression(10, 0, "situations < score",  false, null);
    
    
    /* 
     * false expressions
     */
    testInvalidExpression(0, 0, "Maradona", true, null);


    /* 
     * HAS expressions
     */
    Map<ThingAction, Integer> things;
    things = new HashMap<>();
    testExpression(0, 0, "HAS book", false, things);
    testExpression(0, 0, "HASNOT book", true, things);
    testExpression(0, 0, "HAS book AND HAS pen", false, things);
    testExpression(0, 0, "HASNOT book AND HASNOT pen", true, things);
    testExpression(0, 0, "HAS book OR HASNOT pen", true, things);
    things.put(new ThingAction("book"), 1);
    testExpression(0, 0, "HAS book", true, things);
    testExpression(0, 0, "HASNOT book", false, things);
    testExpression(0, 0, "HAS book AND HAS pen", false, things);
    testExpression(0, 0, "HAS book AND HASNOT pen", true, things);
    testExpression(0, 0, "HAS book AND HASNOT pen", true, things);
    things.put(new ThingAction("pen"), 1);
    testExpression(0, 0, "HAS book AND HASNOT pen", false, things);


    
    /* more complex */
    testExpression(10, 10, "situations == 0", false, null);

    testExpression(10, 10, "situations == 0 AND score == 0",   false, null);
    testExpression(10, 10, "situations == 0 AND score == 10",  false, null);
    testExpression(10, 10, "situations == 10 AND score == 0",  false, null);
    testExpression(10, 10, "situations == 10 AND score == 10", true, null);
    
    testExpression(10, 10, "situations > 0 AND score > 0",   true, null);
    testExpression(10, 10, "situations > 0 AND score > 10",  false, null);
    testExpression(10, 10, "situations > 10 AND score > 0",  false, null);
    testExpression(10, 10, "situations > 9 AND score > 9", true, null);
    testExpression(10, 10, "situations > 9 AND score > 10", false, null);
    testExpression(10, 10, "situations > 10 AND score > 9", false, null);
    testExpression(10, 10, "situations > 10 AND score > 10", false, null);
    
    testExpression(10, 10, "situations == 0 OR score == 0",   false, null);
    testExpression(10, 10, "situations == 0 OR score == 10",  true, null);
    testExpression(10, 10, "situations == 10 OR score == 0",  true, null);
    testExpression(10, 10, "situations == 10 OR score == 10", true, null);
    testExpression(10, 10, "situations > 0 OR score > 0",   true, null);
    testExpression(10, 10, "situations > 0 OR score > 10",  true, null);
    testExpression(10, 10, "situations > 10 OR score > 0",  true, null);
    testExpression(10, 10, "situations > 10 OR score > 10 ", false, null);

    things = new HashMap<>();
    testExpression(10, 10, "situations > 5 AND HAS book", false, things);
    testExpression(10, 10, "situations > 5 AND HAS book", false, things);
    testExpression(10, 10, "situations > 5 AND HASNOT book", true, things);
    testExpression(10, 10, "situations > 5 AND HASNOT book AND HASNOT pen", true, things);
    things.put(new ThingAction("book"), 1);
    testExpression(10, 10, "situations > 5 AND HAS book", true, things);
    testExpression(10, 10, "situations > 5 AND HAS book", true, things);
    testExpression(10, 10, "situations > 5 AND HASNOT book", false, things);
    testExpression(10, 10, "situations > 5 AND HAS book AND HASNOT pen", true, things);
    things.put(new ThingAction("pen"), 1);
    testExpression(10, 10, "situations > 5 AND HAS book AND HASNOT pen", false, things);
    testExpression(10, 10, "situations > 5 AND HAS book AND HAS pen", true, things);
    testExpression(10, 10, "situations > 5 AND HAS book AND HAS pen AND score > -1", true, things);
    
  }
  
}
