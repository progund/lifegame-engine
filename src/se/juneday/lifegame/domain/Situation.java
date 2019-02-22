package se.juneday.lifegame.domain;

import java.util.List;

public class Situation {

  private String title;
  private String description;
  private String question;
  private List<Suggestion> suggestions;
  private List<ThingAction> actions ;

  

    
    public static Situation endSituation = new Situation("End of game", "Your life is complete, you've made it.", null, null, null);

    public Situation(String title, String description, String question, List<Suggestion> suggestions, List<ThingAction> actions) {
        this.title = title;
        this.description = description;
        this.question = question;
        this.suggestions = suggestions;
        this.actions = actions;
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    public String question() {
        return question;
    }

    public List<Suggestion> suggestions() {
        return suggestions;
    }

    public List<ThingAction> actions() {
        return actions;
    }
/*
    public void removeActionThing(ThingAction t) {
        actions.remove(t);
    }

    public void addActionThing(ThingAction t) {
        actions.add(t);
    }
*/


    @Override
    public String toString() {
        return "Situation{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", question='" + question + '\'' +
                ", actions='" + actions + '\'' +
                ", suggestions=" + suggestions+
                '}';
    }

}
