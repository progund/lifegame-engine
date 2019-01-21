package se.juneday.lifegame.domain;

import java.util.List;

public class Situation {

    private String title;
    private String description;
    private String question;
    private List<Suggestion> suggestions;

    public Situation(String title, String description, String question, List<Suggestion> suggestions) {
        this.title = title;
        this.description = description;
        this.question = question;
        this.suggestions = suggestions;
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

    @Override
    public String toString() {
        return "Situation{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", question='" + question + '\'' +
                ", suggestions=" + suggestions+
                '}';
    }
}
