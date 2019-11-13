package se.juneday.lifegame.json;

import se.juneday.lifegame.domain.Game;
import se.juneday.lifegame.domain.ThingAction;
import se.juneday.lifegame.domain.InvalidLifeException;
import se.juneday.lifegame.util.Log;

// import javax.script.ScriptEngine;
// import javax.script.ScriptEngineManager;
// import javax.script.ScriptException;

import java.io.PipedInputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class ExpressionParser {

  public final static String AND = "AND";
  public final static String OR = "OR";
  public final static String LOG_OP_REGEX = AND + "//" + OR;

  public final static String GT = ">";
  public final static String LT = "<";
  public final static String EQ = "==";
  public final static String NE = "!=";

  public final static String SCORE = "score";
  public final static String SITUATIONS = "situations";
  public final static String HAS = "HAS";
  public final static String HASNOT = "HASNOT";
  private static final String LOG_TAG = ExpressionParser.class.getSimpleName();

  private Set<String> logicalOperators;
  private Set<String> compareOperators;
  private Set<String> gameExpressions;

  private Set<String> thingsNeeded;
  private Set<String> thingsExisting;


  public ExpressionParser() {
    logicalOperators = new HashSet<>();
    compareOperators = new HashSet<>();
    gameExpressions = new HashSet<>();
    logicalOperators.addAll(Arrays.asList(new String[]{AND, OR}));
    compareOperators.addAll(Arrays.asList(new String[]{GT, LT, EQ, NE}));
    gameExpressions.addAll(Arrays.asList(new String[]{SCORE, SITUATIONS}));

    thingsNeeded = new HashSet<>();
  }

  private static boolean isNumeric(String str) {
    try {
      Integer.parseInt(str);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  private class SimpleExpr {
    String op1;
    String expr;
    String op2;
    
    public String toString() {
      return op1 + " " + expr + " " + op2;
    }

  }

  /*
    private boolean isEmpty(ThingAction ta) {
    return ( ta.action() == null && ta.thing() == null);
    }
  */
  
  // private String gameExpressionToJava(String expr) {
  //   if (expr.equals(SCORE)) {
  //     return "g.points()";
  //   } else {
  //     return "g.situationCount()";
  //   }
  // }

  private boolean isEmpty(SimpleExpr se) {
    return ( se.op1 == null && se.expr == null && se.op2 == null);
  }

  private void invalidateSimpleExpr(SimpleExpr se) {
    se.op1 = null;
    se.expr = null;
    se.op2 = null;
  }
  
  private  Predicate<Game> createPointsGTPredicate(SimpleExpr se) {
    //    System.err.print(" ==================== : " + se + " || " + se.op2);
    switch (se.op1) {
    case SCORE:
      //System.err.println(" ==================== ");
      return g -> g.score() > Integer.parseInt(se.op2);
    case SITUATIONS:
      return g -> g.score() > g.situationCount();
    default:
      return g -> {
        Log.v(LOG_TAG," +++++++++++ score "
              + g.score() + " > value "
              + Integer.parseInt(se.op2) + " ==> "
              + (g.score() > Integer.parseInt(se.op2)) );
        return g.score() > Integer.parseInt(se.op2);
      };
    }
  }
  
  private  Predicate<Game> createPointsLTPredicate(SimpleExpr se) {
    switch (se.op2) {
    case SCORE:
      return g -> g.score() < g.score();
    case SITUATIONS:
      return g -> g.score() < g.situationCount();
    default:
      return g -> g.score() < Integer.parseInt(se.op2);
    }
  }
  
  private Predicate<Game> createPointsEQPredicate(SimpleExpr se) {
    switch (se.op2) {
    case SCORE:
      return g -> g.score() == g.score();
    case SITUATIONS:
      return g -> g.score() == g.situationCount();
    default:
      return g -> g.score() == Integer.parseInt(se.op2);
    }
  }
  
  private Predicate<Game> createPointsNEPredicate(SimpleExpr se) {
    switch (se.op2) {
    case SCORE:
      return g -> g.score() != g.score();
    case SITUATIONS:
      return g -> g.score() != g.situationCount();
    default:
      return g -> g.score() != Integer.parseInt(se.op2);
    }
  }
  
  private  Predicate<Game> createPointsPredicate(SimpleExpr se) {
    switch (se.expr) {
    case GT:
      return createPointsGTPredicate(se);
    case LT:
      return createPointsLTPredicate(se);
    case EQ:
      return createPointsEQPredicate(se);
    case NE:
      return createPointsNEPredicate(se);
    default:
      return null;
    }
  }

  private  Predicate<Game> createSituationsLTPredicate(SimpleExpr se) {
    //    System.err.println(" --- [" + se + "] ----- " + se.op2);
    switch (se.op2) {
    case SCORE:
      return g -> g.situationCount() < g.score();
    case SITUATIONS:
      return g -> g.situationCount() < Integer.parseInt(se.op2);
    default:
      return g -> {
        Log.v(LOG_TAG," +++++++++++ situation "
              + g.situationCount() + " < value "
              + Integer.parseInt(se.op2) + " ==> "
              + (g.situationCount() < Integer.parseInt(se.op2)) );
        return g.situationCount() < Integer.parseInt(se.op2);
      };
    }
  }

  private  Predicate<Game> createSituationsGTPredicate(SimpleExpr se) {
    switch (se.op2) {
    case SCORE:
      return g -> g.situationCount() > g.score();
    case SITUATIONS:
      return g -> g.situationCount() > g.situationCount();
    default:
      return g -> {
        Log.v(LOG_TAG," +++++++++++ situation "
              + g.situationCount() + " > value "
              + Integer.parseInt(se.op2) + " ==> "
              + (g.situationCount() > Integer.parseInt(se.op2)) );
        return g.situationCount() > Integer.parseInt(se.op2);
      };
    }
  }

  private  Predicate<Game> createSituationsEQPredicate(SimpleExpr se) {
    switch (se.op2) {
    case SCORE:
      return g -> g.situationCount() == g.score();
    case SITUATIONS:
      return g -> g.situationCount() == g.situationCount();
    default:
      return g -> {
        Log.v(LOG_TAG," +++++++++++ situation "
              + g.situationCount() + " == value "
              + Integer.parseInt(se.op2) + " ==> "
              + (g.situationCount() == Integer.parseInt(se.op2)) );
        return g.situationCount() == Integer.parseInt(se.op2);
      };
    }
  }


  private  Predicate<Game> createSituationsNEPredicate(SimpleExpr se) {
    switch (se.op2) {
    case SCORE:
      return g -> g.situationCount() != g.score();
    case SITUATIONS:
      return g -> g.situationCount() != g.situationCount();
    default:
      return g -> {
        Log.v(LOG_TAG," +++++++++++ situation "
              + g.situationCount() + " != value "
              + Integer.parseInt(se.op2) + " ==> "
              + (g.situationCount() != Integer.parseInt(se.op2)) );
        return g.situationCount() != Integer.parseInt(se.op2);
      };
    }
  }


  private  Predicate<Game>  createHasPredicate(SimpleExpr se) {
    // TODO: properly implement the below
    return g -> { for (ThingAction a : g.things().keySet()) { if ( a.thing().equals(se.op2)) { return true;} } ; return false; } ;
  }
  
  private  Predicate<Game>  createHasNotPredicate(SimpleExpr se) {
    // TODO: properly implement the below
    return g -> { for (ThingAction a : g.things().keySet()) { if ( a.thing().equals(se.op2)) { return false;} } ; return true; } ;
  }
  
  private  Predicate<Game>  createSituationsPredicate(SimpleExpr se) {
    switch (se.expr) {
    case GT:
      return createSituationsGTPredicate(se);
    case LT:
      return createSituationsLTPredicate(se);
    case EQ:
      return createSituationsEQPredicate(se);
    case NE:
      return createSituationsNEPredicate(se);
    default:
      return null;
    }
  }


  
  private Predicate<Game> createPredicate(SimpleExpr se) {
    //    System.err.println(" createPredicate(" + se +")");
    if (!validateSimpleExpr(se)) {
      // TODO: throw exception
      Log.v(LOG_TAG,"ERROR in expression: " + se);
      return null;
    }

    Log.v(LOG_TAG,"Create predicate from: " + se);

    switch (se.expr) {
    case HAS:
      return createHasPredicate(se);
    case HASNOT:
      return createHasNotPredicate(se);
    }

    switch (se.op1) {
    case SCORE:
      return createPointsPredicate(se);
    case SITUATIONS:
      return createSituationsPredicate(se);
    default:
      //return createValuePredicate(se);
      // TODO: specify that value is not allowed first .... or add corresponding methods
      return null;
    }
        
  }

  /*
    private Predicate<Game> createPredicateOld(SimpleExpr se) {
    if (!validateSimpleExpr(se)) {
    // TODO: throw exception
    Log.v(LOG_TAG,"ERROR in expression: " + se);
    return null;
    }

    Log.v(LOG_TAG,"createPredicate(" + se + ") ==> " + "new java.util.function.Predicate(function(g) { return " + gameExpressionToJava(se.expr) + se.expr + se.op2 + ";})");
    ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
    try {
    String funcStr = "new java.util.function.Predicate(function(g) { System.out.println(\" ---------------------- hej ----------------------- \") ; return " + gameExpressionToJava(se.expr) + se.expr + se.op2 + ";})";
    Log.v(LOG_TAG,"createPredicate(" + se + ") ==> " + funcStr);
    return (Predicate<Game>) engine.eval(funcStr);
    } catch (ScriptException e) {
    e.printStackTrace();
    }


    //        Predicate<Game> predicate = a -> a.score() > 2;

    return null;
    }
  */

  protected Set<String> thingsNeeded() {
    return thingsNeeded;
  }
  
  private boolean validateHasExpression(SimpleExpr se) {
    if ( (se.op1 == null) && (HAS.equals(se.expr)) && (se.op2!=null) ) {
      thingsNeeded.add(se.op2);
      return true;
    } else {
      return false;
    }
  }
  
  private boolean validateHasNotExpression(SimpleExpr se) {
    if ( (se.op1 == null) && (HASNOT.equals(se.expr)) && (se.op2!=null) ) {
      thingsNeeded.add(se.op2);
      return true;
    } else {
      return false;
    }
  }
  
  private boolean validateSimpleExpr(SimpleExpr se) {
    // System.err.println(" validateSimpleExpr: " + se.op1);
    // System.err.println(" validateSimpleExpr: " + se.op2);
    // System.err.print(" validateSimpleExpr: " + gameExpressions);
    // System.err.print("  " + gameExpressions.contains(se.op1));
    // System.err.print(" | " + gameExpressions.contains(se.op2) + "  :: ");
    if ((isNumeric(se.op1) && gameExpressions.contains(se.op2)) ||
        (isNumeric(se.op2) && gameExpressions.contains(se.op1)) ||
        gameExpressions.contains(se.op1) && gameExpressions.contains(se.op2)) {
      if (compareOperators.contains(se.expr)) {
        return true;
      }
    }
    if (validateHasExpression(se)) {
      return true;
    }
    if (validateHasNotExpression(se)) {
      return true;
    }
    Log.v(LOG_TAG, "Invalid or incomplete Simple Expression: " + se);
    return false;
  }

  public Predicate<Game> addPredicate(Predicate<Game> predicate, String op, String expr)
    throws InvalidLifeException{
    if (op.equals(AND)) {
      return predicate.and(parseSimple(expr));
    } else if (op.equals(OR)) {
      return predicate.or(parseSimple(expr));
    } else {
      Log.v(LOG_TAG,"Invalid operator::            " + op + "   <---------------------- ERROR --------------");
      return null;
    }
    
  }
  
  public Predicate<Game> parse(String exprString) throws InvalidLifeException{
    Predicate<Game> predicate = g -> true;
    if (exprString.trim().equals("default")) {
      return predicate;
    }
    String[] expressions = exprString.split(" ");
    StringBuilder sb = new StringBuilder();
    String savedOp = AND;
    for (String e : expressions) {
      Log.v(LOG_TAG, "Parsing: " + e);
      if (logicalOperators.contains(e)) {
        predicate = addPredicate(predicate, savedOp, sb.toString());
        savedOp = e;
        sb = new StringBuilder();
      } else {
        sb.append(" " + e);
      }
    }
    String expr = sb.toString();
    if (! expr.equals("")) {
      predicate = addPredicate(predicate, savedOp, expr);
    } else {
      throw new InvalidLifeException("Could not parse expression: \"" + exprString + "\"");
    }
    return predicate;
  }
  
  public Predicate<Game> parseSimple(String exprString) throws InvalidLifeException {

    String[] expressions = exprString.split(" ");
    List<String> simpleExpression;


    String logicalOperator = "";
    SimpleExpr se = new SimpleExpr();
    for (String e : expressions) {
      if (validateSimpleExpr(se)) {
        Log.v(LOG_TAG,"Valid expression::" + se.op1 + " " + se.expr + " " + se.op2);
        return createPredicate(se);
      } else {
        Log.v(LOG_TAG,"Invalid expression:" + se.op1 + " " + se.expr + " " + se.op2);
      }
      //   Log.v(LOG_TAG," Check: " + e);
      if (e.equals("")) {
        Log.v(LOG_TAG," * empty string");
      } else if (compareOperators.contains(e)) {
        Log.v(LOG_TAG," * compareoperator: " + e);
        Log.v(LOG_TAG," * game: " + e);
        if (se.op1 == null || se.op2 != null) {
          Log.v(LOG_TAG," * INVALID SYNTAX");
        } else {
          se.expr = e;
        }
      } else if (HAS.equals(e)) {
        se.expr = e;
      } else if (HASNOT.equals(e)) {
        se.expr = e;
      } else if (gameExpressions.contains(e)) {
        Log.v(LOG_TAG," * game expr: " + e);
        if (se.op1 != null) {
          se.op2 = e;
        } else {
          se.op1 = e;
        }
      } else if (isNumeric(e)) {
        Log.v(LOG_TAG," * number: " + e);
        if (se.op1 != null) {
          se.op2 = e;
        } else {
          se.op1 = e;
        }
      } else {
        // Assume HAS or HASNOT
        se.op1 = null;
        se.op2 = e;
      }
    }
    if (validateSimpleExpr(se)) {
      Log.v(LOG_TAG,"Valid expression::" + se.op1 + " " + se.expr + " " + se.op2);
      return createPredicate(se);
    }
    if (logicalOperator==null || (logicalOperator.equals("")) ) {
      throw new InvalidLifeException("Failed parsing expression: \"" + exprString + "\"");
    } else {
      throw new InvalidLifeException("Failed parsing expression: \"" + exprString + "\" (current logical operator: " + logicalOperator + ")");
    }        
        
  }

}
