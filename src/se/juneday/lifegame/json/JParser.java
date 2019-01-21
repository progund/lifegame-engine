package se.juneday.lifegame.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;
import se.juneday.lifegame.domain.Exit;
import se.juneday.lifegame.domain.Game;
import se.juneday.lifegame.domain.Situation;
import se.juneday.lifegame.domain.Suggestion;
import se.juneday.lifegame.util.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public final static String SITUATION_SUGGESTION_EXITS_SITUATION = "situation";
    private static final String LOG_TAG =JParser.class.getSimpleName() ;

    public List<Exit> exits(JSONArray jsonExits) {
        return null;
    }


    private String readJsonString(JSONObject json, String key, String defaultValue) {
        try {
            return json.getString(key);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    public Map<String, Situation> situations(JSONArray jsonSituations) {
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

            // for each suggestion
            List<Suggestion> suggestionList = new ArrayList<>();
            for (int j = 0; j < exits.length(); j++) {
                JSONObject suggestionJson = exits.getJSONObject(j);
                List<Exit> exitList = new ArrayList<>();

                // read suggestion phrase
                String phrase = readJsonString(suggestionJson, SITUATION_SUGGESTION_PHRASE, "");

                // "exit" (one single exit)
                String exitStr = readJsonString(suggestionJson, SITUATION_SUGGESTION_EXIT, null);
                if (exitStr != null) {
                    exitList.add(new Exit(g -> true, exitStr));
                } else {

                    // "exits" (multiple exits depending on calculations)
                    JSONArray exitsJsonArray = suggestionJson.getJSONArray(SITUATION_SUGGESTION_EXITS);
                    // for each exits
                    for (int k = 0; k < exitsJsonArray.length(); k++) {
                        JSONObject exitJson = exitsJsonArray.getJSONObject(j);

                        // expression to get to next situation
                        String expr = exitJson.getString(SITUATION_SUGGESTION_EXITS_EXPR);
                        // next situation
                        String exit = exitJson.getString(SITUATION_SUGGESTION_EXITS_SITUATION);

                        exitList.add(new Exit(ep.parse(expr), exit));
                    }
                }
                Suggestion suggestion = new Suggestion(phrase, exitList);
                suggestionList.add(suggestion);
            }
            Situation situation = new Situation(title, sDescription, sQuestion, suggestionList);
            situations.put(title, situation);
        }
        return situations;
    }

    public Game game(JSONObject jsondata) {
        String title = jsondata.getString(GAME_TITLE);
        String startSituationString = jsondata.getString(GAME_START);
        JSONArray jsonSituations = jsondata.getJSONArray(GAME_SITUATIONS);
        Map<String, Situation> situations = situations(jsonSituations);
        Situation startSituation = situations.get(startSituationString);
        return new Game(title, situations, startSituation);
    }

    public Game game(String jsonFile) {
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
