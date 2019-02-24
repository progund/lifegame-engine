package se.juneday.lifegame.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;
import se.juneday.lifegame.domain.*;
import se.juneday.lifegame.util.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

public class JParser {

  public final static String GAME_TITLE = "title";
  public final static String GAME_SITUATIONS = "situations";
  public final static String GAME_START = "start";

  public final static String SITUATION_TITLE = "title";
  public final static String SITUATION_DESCRIPTION = "description";
  public final static String SITUATION_QUESTION = "question";

  public final static String SITUATION_SUGGESTIONS = "suggestions";

  public final static String SITUATION_SUGGESTION_PHRASE = "phrase";
  public final static String SITUATION_SUGGESTION_EXIT = "exit";
  public final static String SITUATION_SUGGESTION_EXITS = "exits";
  public final static String SITUATION_SUGGESTION_EXITS_EXPR = "expr";
  public final static String SITUATION_SUGGESTION_SCORE = "score";
  public final static String SITUATION_SUGGESTION_EXITS_SITUATION = "situation";
  private static final String LOG_TAG =JParser.class.getSimpleName() ;

  public final static String SITUATION_THINGS = "things";
  public final static String SITUATION_THINGS_ACTION = "action";
  public final static String SITUATION_THINGS_THING = "thing";
  public final static String SITUATION_THINGS_DROP = "drop";
  public final static String SITUATION_THINGS_TAKE = "take";
  public final static String SITUATION_THINGS_HAS = "has";
  /*    private Set<String> thingExpressions;
        thingExpressions.addAll(Arrays.asList(new String[]{TAKE, THING, HAS}));
  */

  private Set<String> thingsNeeded;

  public List<Exit> exits(JSONArray jsonExits) {
    return null;
  }


  private String readJsonString(JSONObject json, String key, String defaultValue) {
    Log.d(LOG_TAG, " readJsonString()" + json + " key: " + key);
    try {
      return json.getString(key);
    } catch (JSONException e) {
      return defaultValue;
    }
  }

  private int readJsonInt(JSONObject json, String key, int defaultValue) {
    Log.d(LOG_TAG, " readJsonInt()" + json + " key: " + key);
    try {
      return json.getInt(key);
    } catch (JSONException e) {
      return defaultValue;
    }
  }

  public Map<String, Situation> situations(JSONArray jsonSituations) throws InvalidLifeException{
    ExpressionParser ep = new ExpressionParser();
    Map<String, Situation> situations = new HashMap<>();

    // for each situation
    for (int i = 0; i < jsonSituations.length(); i++) {
      JSONObject jsonSituation = jsonSituations.getJSONObject(i);
      String title = jsonSituation.getString(SITUATION_TITLE);
      String sDescription = jsonSituation.getString(SITUATION_DESCRIPTION);
      String sQuestion = jsonSituation.getString(SITUATION_QUESTION);
      JSONArray exits = jsonSituation.getJSONArray(SITUATION_SUGGESTIONS);

      Log.d(LOG_TAG, "exits: " + exits);
      List<ThingAction> actionList = new ArrayList<>();

      // "things"
      try {
        JSONArray thingsJsonArray = jsonSituation.getJSONArray(SITUATION_THINGS);
        for (int k = 0; k < thingsJsonArray.length(); k++) {
          JSONObject thingJson = thingsJsonArray.getJSONObject(k);
          //                    String action = thingJson.getString(SITUATION_THINGS_ACTION);
          String thing = thingJson.getString(SITUATION_THINGS_THING);
          actionList.add(new ThingAction(thing));
        }
      } catch (JSONException e) {
        Log.d(LOG_TAG, "no things in " + title);
      }

      // for each suggestion
      List<Suggestion> suggestionList = new ArrayList<>();
      for (int j = 0; j < exits.length(); j++) {
        JSONObject suggestionJson = exits.getJSONObject(j);
        List<Exit> exitList = new ArrayList<>();

        
        // read suggestion phrase
        String phrase = readJsonString(suggestionJson, SITUATION_SUGGESTION_PHRASE, "");
        int situationScore = readJsonInt(suggestionJson, SITUATION_SUGGESTION_SCORE, 0);
        System.out.println("Read score: " + situationScore + " from " + suggestionJson);
        
        
        // "exit" (one single exit)
        Log.d(LOG_TAG, " trying to find exit in " + title + " => "+ suggestionJson);
        String exitStr = null;
        String expr = null;
        try {
          //                    JSONObject exitJson = null;
          exitStr = readJsonString(suggestionJson, SITUATION_SUGGESTION_EXIT, null);
          //String exitStr = readJsonString(exitJson, SITUATION_SUGGESTION_PHRASE, null);
          expr = readJsonString(suggestionJson,SITUATION_SUGGESTION_EXITS_EXPR, null);
          Log.d(LOG_TAG, " trying to find exit in " + title + " => "+ exitStr+ "   <-----");
          // exitStr = readJsonString(exitJson, SITUATION_SUGGESTION_EXITS_SITUATION, null);
          
        } catch (JSONException e) {
          Log.d(LOG_TAG, "Exception: ");
          //e.printStackTrace();
        }
        if (exitStr != null) {
          exitList.add(new Exit(g -> true, exitStr));
          // } else {
          //     System.out.println(" exit: " + expr + " " + exitStr);
          //     exitList.add(new Exit(ep.parse(expr), exitStr));
        } else {
          Log.d(LOG_TAG, " trying to find exits in " + suggestionJson);
          // "exits" (multiple exits depending on calculations)
          JSONArray exitsJsonArray = suggestionJson.getJSONArray(SITUATION_SUGGESTION_EXITS);

          // for each exits
          for (int k = 0; k < exitsJsonArray.length(); k++) {
            JSONObject exitObject = exitsJsonArray.getJSONObject(k);
            System.out.println("Adding expr: " + expr + " | " + k  + " | from " + exitObject + "  exit: " + exitsJsonArray);

            // expression to get to next situation
            expr = exitObject.getString(SITUATION_SUGGESTION_EXITS_EXPR);
            // next situation
            String exit = exitObject.getString(SITUATION_SUGGESTION_EXITS_SITUATION);
            String explanation = readJsonString(exitObject, "explanation", null);
            
            exitList.add(new Exit(ep.parse(expr), exit, explanation));
          }
        }
        Suggestion suggestion = new Suggestion(phrase, exitList, situationScore);
        suggestionList.add(suggestion);
      }
      Situation situation = new Situation(title, sDescription, sQuestion, suggestionList, actionList);
      situations.put(title, situation);
    }
    thingsNeeded = ep.thingsNeeded();
    return situations;
  }
  
  public Game game(JSONObject jsondata) throws InvalidLifeException {
    String title = jsondata.getString(GAME_TITLE);
    String startSituationString = jsondata.getString(GAME_START);
    JSONArray jsonSituations = jsondata.getJSONArray(GAME_SITUATIONS);
    Map<String, Situation> situations = situations(jsonSituations);
    Situation startSituation = situations.get(startSituationString);
    return new Game(title, situations, startSituation, null, thingsNeeded);
  }

  public Game game(String jsonFile) throws InvalidLifeException {
    String data = null;
    try {
      data = new String(Files.readAllBytes(Paths.get(jsonFile)));
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    return game(new JSONObject(data));
  }

}
