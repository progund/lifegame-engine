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

public class TestExpression {

  

  private static void testInvalidExpression(int situationsCount,
                                     int score,
                                     String expr,
                                     boolean expected) {
    try {
      testExpression(0, 0, expr,  false);
      throw new RuntimeException("Exception \"" + expr + "\" not causing an exception...");
    } catch (RuntimeException e) {
      ;
    }
    System.err.println(" OK (invalidity checked)");
  }
  
  private static void testExpression(int situationsCount,
                                     int score,
                                     String expr,
                                     boolean expected) {
    try {
      System.err.print(" * \"" + expr + "\"   ===> ");
      Game game = new Game(situationsCount, score);
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
    testExpression(0, 0, "score == 0", true);
    testExpression(0, 0, "score == 10", false);
    testExpression(0, 0, "score != 0", false);
    testExpression(0, 0, "score > 0",  false);
    testExpression(0, 0, "score < 0",  false);
    
    testExpression(10, 0, "score == 0", true);
    testExpression(10, 0, "score == 10", false);
    testExpression(10, 0, "score != 0", false);
    testExpression(10, 0, "score > 0",  false);
    testExpression(10, 0, "score < 0",  false);
    
    testExpression(0, 10, "score == 0", false);
    testExpression(0, 10, "score == 10", true);
    testExpression(0, 10, "score != 0", true);
    testExpression(0, 10, "score > 0",  true);
    testExpression(0, 10, "score < 0",  false);
    
    /*
     * situations:
     *     ==
     *     !=
     *     >
     *     <
     */
    testExpression(0, 0, "situations == 0", true);
    testExpression(0, 0, "situations == 10",false);
    testExpression(0, 0, "situations != 0", false);
    testExpression(0, 0, "situations > 0",  false);
    testExpression(0, 0, "situations < 0",  false);
    
    testExpression(10, 0, "situations == 0", false);
    testExpression(10, 0, "situations == 10",true);
    testExpression(10, 0, "situations != 0", true);
    testExpression(10, 0, "situations > 0",  true);
    testExpression(10, 0, "situations < 0",  false);
    
    testExpression(0, 10, "situations == 0", true);
    testExpression(0, 10, "situations == 10",false);
    testExpression(0, 10, "situations != 0", false);
    testExpression(0, 10, "situations > 0",  false);
    testExpression(0, 10, "situations < 0",  false);

    /* 
     * Situations and score 
     */
    testExpression(0, 0, "situations == score",  true);
    testExpression(0, 10, "situations == score",  false);
    testExpression(0, 10, "situations < score",  true);
    testExpression(0, 10, "situations != score",  true);
    testExpression(0, 10, "situations > score",  false);
    testExpression(0, 10, "situations < score",  true);
    
    testExpression(10, 0, "situations == score",  false);
    testExpression(10, 10, "situations == score",  true);
    testExpression(10, 0, "situations < score",  false);
    testExpression(10, 0, "situations != score",  true);
    testExpression(10, 0, "situations > score",  true);
    testExpression(10, 0, "situations < score",  false);
    
    
    /* 
     * false expressions
     */
    testInvalidExpression(0, 0, "Maradona", true);


    /* more complex */
    testExpression(10, 10, "situations == 0", false);

    testExpression(10, 10, "situations == 0 AND score == 0",   false);
    testExpression(10, 10, "situations == 0 AND score == 10",  false);
    testExpression(10, 10, "situations == 10 AND score == 0",  false);
    testExpression(10, 10, "situations == 10 AND score == 10", true);
    
    testExpression(10, 10, "situations > 0 AND score > 0",   true);
    testExpression(10, 10, "situations > 0 AND score > 10",  false);
    testExpression(10, 10, "situations > 10 AND score > 0",  false);
    testExpression(10, 10, "situations > 9 AND score > 9", true);
    testExpression(10, 10, "situations > 9 AND score > 10", false);
    testExpression(10, 10, "situations > 10 AND score > 9", false);
    testExpression(10, 10, "situations > 10 AND score > 10", false);
    
    testExpression(10, 10, "situations == 0 OR score == 0",   false);
    testExpression(10, 10, "situations == 0 OR score == 10",  true);
    testExpression(10, 10, "situations == 10 OR score == 0",  true);
    testExpression(10, 10, "situations == 10 OR score == 10", true);
    testExpression(10, 10, "situations > 0 OR score > 0",   true);
    testExpression(10, 10, "situations > 0 OR score > 10",  true);
    testExpression(10, 10, "situations > 10 OR score > 0",  true);
    testExpression(10, 10, "situations > 10 OR score > 10 ", false);
    
  }
  
}
