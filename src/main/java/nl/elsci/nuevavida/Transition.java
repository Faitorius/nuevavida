package nl.elsci.nuevavida;

import java.util.List;

public class Transition {
    private final String text;
    private final List<Action> actions;
    private GameScene nextScene;

    public Transition(String text, List<Action> actions) {
        this.text = text;
        this.actions = actions;
    }

    public List<Action> getActions() {
        return actions;
    }

    public String getText() {
        return text;
    }

    public GameScene getNextScene() {
        return nextScene;
    }

    public void setNextScene(GameScene nextScene) {
        this.nextScene = nextScene;
    }
}
