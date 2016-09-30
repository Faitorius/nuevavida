package nl.elsci.nuevavida;

import lombok.Data;

import javax.el.ELException;
import javax.el.ELProcessor;
import java.util.ArrayList;
import java.util.List;

@Data
public class GameScene {
    private final SceneTemplate template;
    private ELProcessor elp;
    private List<String> flags = new ArrayList<>();
    private String lastAction;

    public GameScene(SceneTemplate template, ELProcessor elp) {
        this.template = template;
        this.elp = elp;
        elp.defineBean("scene", this);
    }

    public Transition process(Action action) {
        lastAction = action.getName();
        List<Action> actions = getActions(action.getNext());
        Transition transition = new Transition(action.getText(), actions);
        if (actions.isEmpty()) {
            transition.setNextScene(new GameScene(this.getTemplate(), this.getElp()));
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
                    action.setName(eval);
                    actions.add(action);
                }
            }
        }
        return actions;
    }
}
