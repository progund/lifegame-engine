package se.juneday.lifegame.json;

import se.juneday.lifegame.domain.Game;
import se.juneday.lifegame.domain.ThingAction;
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

    public final static String POINTS = "points";
    public final static String SITUATIONS = "situations";
    public final static String HAS = "has";
    private static final String LOG_TAG = ExpressionParser.class.getSimpleName();

    private Set<String> logicalOperators;
    private Set<String> compareOperators;
    private Set<String> gameExpressions;


    public ExpressionParser() {
        logicalOperators = new HashSet<>();
        compareOperators = new HashSet<>();
        gameExpressions = new HashSet<>();
        logicalOperators.addAll(Arrays.asList(new String[]{AND, OR}));
        compareOperators.addAll(Arrays.asList(new String[]{GT, LT, EQ, NE}));
        gameExpressions.addAll(Arrays.asList(new String[]{POINTS, SITUATIONS}));
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
  
  private String gameExpressionToJava(String expr) {
        if (expr.equals(POINTS)) {
            return "g.points()";
        } else {
            return "g.situationCount()";
        }
    }

  private boolean isEmpty(SimpleExpr se) {
    return ( se.op1 == null && se.expr == null && se.op2 == null);
  }

    private void invalidateSimpleExpr(SimpleExpr se) {
      se.op1 = null;
      se.expr = null;
      se.op2 = null;
    }
  
  private  Predicate<Game> createPointsGTPredicate(SimpleExpr se) {
    switch (se.op2) {
    case POINTS:
      return g -> g.score() > g.score();
    case SITUATIONS:
      return g -> g.score() > g.situationCount();
    default:
      return g -> {
        Log.d(LOG_TAG," +++++++++++ score "
              + g.score() + " < value "
              + Integer.parseInt(se.op2) + " ==> "
              + (g.score() < Integer.parseInt(se.op2)) );
        return g.score() < Integer.parseInt(se.op2);
      };
    }
  }
  
  private  Predicate<Game> createPointsPredicate(SimpleExpr se) {
    switch (se.expr) {
    case GT:
      return createPointsGTPredicate(se);
    case LT:
    case EQ:
    case NE:
    default:
      return null;
    }
  }

  private  Predicate<Game> createSituationsLTPredicate(SimpleExpr se) {
    switch (se.op2) {
    case POINTS:
      return g -> g.situationCount() < g.score();
    case SITUATIONS:
      return g -> g.situationCount() < g.situationCount();
    default:
      return g -> {
        Log.d(LOG_TAG," +++++++++++ situation "
              + g.situationCount() + " < value "
              + Integer.parseInt(se.op2) + " ==> "
              + (g.situationCount() < Integer.parseInt(se.op2)) );
        return g.situationCount() < Integer.parseInt(se.op2);
      };
    }
  }

  private  Predicate<Game> createSituationsGTPredicate(SimpleExpr se) {
    switch (se.op2) {
    case POINTS:
      return g -> g.situationCount() > g.score();
    case SITUATIONS:
      return g -> g.situationCount() > g.situationCount();
    default:
      return g -> {
        Log.d(LOG_TAG," +++++++++++ situation "
              + g.situationCount() + " > value "
              + Integer.parseInt(se.op2) + " ==> "
              + (g.situationCount() > Integer.parseInt(se.op2)) );
        return g.situationCount() > Integer.parseInt(se.op2);
      };
    }
  }

  private  Predicate<Game> createSituationsEQPredicate(SimpleExpr se) {
    switch (se.op2) {
    case POINTS:
      return g -> g.situationCount() == g.score();
    case SITUATIONS:
      return g -> g.situationCount() == g.situationCount();
    default:
      return g -> {
        Log.d(LOG_TAG," +++++++++++ situation "
              + g.situationCount() + " == value "
              + Integer.parseInt(se.op2) + " ==> "
              + (g.situationCount() == Integer.parseInt(se.op2)) );
        return g.situationCount() == Integer.parseInt(se.op2);
      };
    }
  }


  private  Predicate<Game> createSituationsNEPredicate(SimpleExpr se) {
    switch (se.op2) {
    case POINTS:
      return g -> g.situationCount() != g.score();
    case SITUATIONS:
      return g -> g.situationCount() != g.situationCount();
    default:
      return g -> {
        Log.d(LOG_TAG," +++++++++++ situation "
              + g.situationCount() + " != value "
              + Integer.parseInt(se.op2) + " ==> "
              + (g.situationCount() != Integer.parseInt(se.op2)) );
        return g.situationCount() != Integer.parseInt(se.op2);
      };
    }
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
        if (!validateSimpleExpr(se)) {
            // TODO: throw exception
            Log.d(LOG_TAG,"ERROR in expression: " + se);
            return null;
        }

        switch (se.op1) {
        case POINTS:
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
            Log.d(LOG_TAG,"ERROR in expression: " + se);
            return null;
        }

        Log.d(LOG_TAG,"createPredicate(" + se + ") ==> " + "new java.util.function.Predicate(function(g) { return " + gameExpressionToJava(se.expr) + se.expr + se.op2 + ";})");
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        try {
          String funcStr = "new java.util.function.Predicate(function(g) { System.out.println(\" ---------------------- hej ----------------------- \") ; return " + gameExpressionToJava(se.expr) + se.expr + se.op2 + ";})";
            Log.d(LOG_TAG,"createPredicate(" + se + ") ==> " + funcStr);
            return (Predicate<Game>) engine.eval(funcStr);
        } catch (ScriptException e) {
            e.printStackTrace();
        }


//        Predicate<Game> predicate = a -> a.score() > 2;

        return null;
    }
  */

  
    private boolean validateSimpleExpr(SimpleExpr se) {
        if ((isNumeric(se.op1) && gameExpressions.contains(se.op2)) ||
                (isNumeric(se.op2) && gameExpressions.contains(se.op1))) {
            if (compareOperators.contains(se.expr)) {
                return true;
            }
        }
        return false;
    }

  public Predicate<Game> addPredicate(Predicate<Game> predicate, String op, String expr)  {
    if (op.equals(AND)) {
      return predicate.and(parseSimple(expr));
    } else if (op.equals(OR)) {
      return predicate.or(parseSimple(expr));
    } else {
      Log.d(LOG_TAG,"Invalid operator::            " + op + "   <---------------------- ERROR --------------");
      return null;
    }
    
  }
  
    public Predicate<Game> parse(String exprString) {
      Predicate<Game> predicate = g -> true;
      String[] expressions = exprString.split(" ");
      StringBuilder sb = new StringBuilder();
      String savedOp = AND;
      for (String e : expressions) {
        if (logicalOperators.contains(e)) {
          predicate = addPredicate(predicate, savedOp, sb.toString());
          savedOp = e;
          sb = new StringBuilder();
        } else {
          sb.append(" " + e);
        }
      }
      predicate = addPredicate(predicate, savedOp, sb.toString());
      return predicate;
    }
  
    public Predicate<Game> parseSimple(String exprString) {

      String[] expressions = exprString.split(" ");
      List<String> simpleExpression;

      String logicalOperator = "";
      SimpleExpr se = new SimpleExpr();
      for (String e : expressions) {
        if (validateSimpleExpr(se)) {
          Log.d(LOG_TAG,"Valid expression::" + se.op1 + " " + se.expr + " " + se.op2);
          return createPredicate(se);
        } else {
          Log.d(LOG_TAG,"Invalid expression:" + se.op1 + " " + se.expr + " " + se.op2);
        }
        //   Log.d(LOG_TAG," Check: " + e);
        if (e.equals("")) {
          Log.d(LOG_TAG," * empty string");
        } else if (compareOperators.contains(e)) {
          Log.d(LOG_TAG," * compareoperator: " + e);
          Log.d(LOG_TAG," * game: " + e);
          if (se.op1 == null || se.op2 != null) {
            Log.d(LOG_TAG," * INVALID SYNTAX");
          } else {
            se.expr = e;
          }
        } else if (gameExpressions.contains(e)) {
          Log.d(LOG_TAG," * game expr: " + e);
          if (se.op1 != null) {
            se.op2 = e;
          } else {
            se.op1 = e;
          }
        } else if (isNumeric(e)) {
          Log.d(LOG_TAG," * number: " + e);
          if (se.op1 != null) {
            se.op2 = e;
          } else {
            se.op1 = e;
          }
        } else {
          Log.d(LOG_TAG," * UNKNOWN: \"" + e + "\"");
        }
      }
      if (validateSimpleExpr(se)) {
        Log.d(LOG_TAG,"Valid expression::" + se.op1 + " " + se.expr + " " + se.op2);
        return createPredicate(se);
      }
      return null;
    }

}
