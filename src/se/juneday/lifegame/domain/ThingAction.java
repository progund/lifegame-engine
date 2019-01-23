package se.juneday.lifegame.domain;

import java.lang.reflect.AccessibleObject;

public class ThingAction {

/*    public enum Action {
        TAKE,
        DROP
    } ;
*/
  //  private Action action;
    private String thing;
/*
    public ThingAction(String actionString, String thing) {
        try {
            this.action = Action.valueOf(actionString.toUpperCase());
        } catch (IllegalArgumentException e) {
            // TODO: thrown exception
        }
        this.thing = thing;
    }

    public ThingAction(Action action, String thing) {
        this.action = action;
        this.thing = thing;
    }
*/

    public ThingAction(String thing) {
        this.thing = thing;
    }

/*    public Action action() {
        return action;
    }
*/

    public String thing() {
        return thing;
    }

    @Override
    public String toString() {
        return "ThingAction{" +
  //              "action=" + action +
                ", thing='" + thing + '\'' +
                '}';
    }

}
