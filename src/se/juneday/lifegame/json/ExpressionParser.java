package se.juneday.lifegame.json;

import se.juneday.lifegame.domain.Game;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

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

    public final static String GT = ">";
    public final static String LT = ">=";
    public final static String EQ = "==";
    public final static String NE = "!=";

    public final static String POINTS = "points";
    public final static String SITUATIONS = "situations";

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

        ;
    }

    private String gameExpressionToJava(String expr) {
        if (expr.equals(POINTS)) {
            return "g.points()";
        } else {
            return "g.situationCount()";
        }
    }

    private void invalidateSimpleExpr(SimpleExpr se) {
        se.op1 = null;
        se.expr = null;
        se.op2 = null;
    }

    private Predicate<Game> createPredicate(SimpleExpr se) {
        if (!validateSimpleExpr(se)) {
            // TODO: throw exception
            System.out.println("ERROR in expression: " + se);
        }

        System.out.println("createPredicate(" + se + ") ==> " + "new java.util.function.Predicate(function(g) { return " + gameExpressionToJava(se.expr) + se.expr + se.op2 + ";})");
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        try {
            System.out.println("createPredicate(" + se + ") ==> " + "new java.util.function.Predicate(function(g) { return " + gameExpressionToJava(se.expr) + se.expr + se.op2 + ";})");
            return (Predicate<Game>) engine.eval("new java.util.function.Predicate(function(g) { return " + gameExpressionToJava(se.expr) + se.expr + se.op2 + ";})");
        } catch (ScriptException e) {
            e.printStackTrace();
        }


//        Predicate<Game> predicate = a -> a.score() > 2;

        return null;
    }

    private boolean validateSimpleExpr(SimpleExpr se) {
        if ((isNumeric(se.op1) && gameExpressions.contains(se.op2)) ||
                (isNumeric(se.op2) && gameExpressions.contains(se.op1))) {
            if (compareOperators.contains(se.expr)) {
                return true;
            }
        }
        return false;
    }

    public Predicate<Game> parse(String exprString) {
        String[] expressions = exprString.split(" ");
        List<String> simpleExpression;

        Predicate<Game> predicate = g -> true;
        String logicalOperator = "";
        SimpleExpr se = new SimpleExpr();
        for (String e : expressions) {
            if (validateSimpleExpr(se)) {
                System.out.println("Valid expression:" + se.op1 + " " + se.expr + " " + se.op2);
                return createPredicate(se);
            } else {
                System.out.println("Invalid expression:" + se.op1 + " " + se.expr + " " + se.op2);
            }
            //   System.out.println(" Check: " + e);
            if (e.equals("")) {
                System.out.println(" * empty string");
            } else if (logicalOperators.contains(e)) {
                System.out.println(" * logical operator: " + e);
                if (validateSimpleExpr(se)) {
                    System.out.println("Adding logical expression: " + e + "  " + se);
                } else {
                    // TODO: throw exception
                    System.out.println("Invalid expression");
                }

            } else if (compareOperators.contains(e)) {
                System.out.println(" * compareoperator: " + e);
                System.out.println(" * game: " + e);
                if (se.op1 == null || se.op2 != null) {
                    System.out.println(" * INVALID SYNTAX");
                } else {
                    se.expr = e;
                }
            } else if (gameExpressions.contains(e)) {
                System.out.println(" * game expr: " + e);
                if (se.op1 != null) {
                    se.op2 = e;
                } else {
                    se.op1 = e;
                }
            } else if (isNumeric(e)) {
                System.out.println(" * number: " + e);
                if (se.op1 != null) {
                    se.op2 = e;
                } else {
                    se.op1 = e;
                }
            } else {
                System.out.println(" * UNKNOWN: \"" + e + "\"");
            }
        }
        if (validateSimpleExpr(se)) {
            System.out.println("Valid expression:" + se.op1 + " " + se.expr + " " + se.op2);
            return createPredicate(se);
        }
        return null;
    }

}
