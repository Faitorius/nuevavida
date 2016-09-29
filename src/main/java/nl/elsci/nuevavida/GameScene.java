package nl.elsci.nuevavida;

import lombok.Data;

import javax.el.ELException;
import javax.el.ELProcessor;
import java.util.ArrayList;
import java.util.List;

@Data
public class GameScene {
    private String name;
    private final SceneTemplate template;
    private ELProcessor elp;
    private List<String> flags = new ArrayList<>();

    public GameScene(SceneTemplate template, ELProcessor elp) {
        this.template = template;
        this.elp = elp;
    }

    public Transition process(Action action) {
        List<Action> actions = getActions(action.getNext());
        Transition transition = new Transition(action.getText(), actions);
        if (action.getNext() == null || action.getNext().isEmpty()) {
            transition.setNextScene(this);
        }
        return transition;
    }

    public Transition getStartTransition() {

        List<Action> actions = getActions(template.getIntroActions());

        return new Transition(template.getIntro(), actions);
    }

    private List<Action> getActions(Iterable<String> actionStrings) {
        List<Action> actions = new ArrayList<>();
        if (actionStrings != null) {
            for (String actionName : actionStrings) {
                String eval;
                try {
                    eval = (String) elp.eval(actionName);
                } catch(ELException e) {
                    eval = actionName;
                }
                Action action = template.getActions().get(eval);
                if (action != null) {
                    action.setName(actionName);
                    actions.add(action);
                }
            }
        }
        return actions;
    }
}
