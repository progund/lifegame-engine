package se.juneday.lifegame.domain;

import java.util.List;

public class Suggestion {

  private String phrase;
  private List<Exit> exits;
  private int score;

  public Suggestion(String phrase, List<Exit> exits, int score) {
    this.phrase = phrase;
    this.exits = exits;
    this.score = score;
  }

  public Suggestion(String phrase, List<Exit> exits) {
    this.phrase = phrase;
    this.exits = exits;
  }

  public String phrase() {
    return phrase;
  }

  public List<Exit> exits() {
    return exits;
  }

  public int score() {
    return score;
  }

  @Override
  public String toString() {
    return "Suggestion{" +
      "phrase='" + phrase + '\'' +
      ", exits=" + exits +
      '}';
  }
}
