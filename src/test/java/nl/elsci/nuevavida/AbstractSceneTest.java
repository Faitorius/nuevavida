package nl.elsci.nuevavida;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.el.ELException;
import javax.el.ELProcessor;
import java.util.Arrays;

public abstract class AbstractSceneTest {
    private Configuration configuration;
    protected ELProcessor elp;
    protected SceneTemplate template;
    private GameScene scene;

    public AbstractSceneTest(String name, int index) {
        Yaml yaml = new Yaml(new Constructor(Configuration.class));
        configuration = yaml.loadAs(AbstractSceneTest.class.getClass().getResourceAsStream("/" + name + ".yml"), Configuration.class);

        template = configuration.getScenes().get(index);

        elp = new ELProcessor();
        elp.defineBean("configuration", configuration);
    }

    public void setUp() {
        scene = new GameScene(template, elp);
        elp.defineBean("scene", scene);
    }

    protected Transition process(String... actions) {
        return process(scene.getStartTransition(), actions);
    }

    private Transition process(Transition transition, String... actions) {
        if (actions.length > 0) {
            eval(transition.getText());
            for (Action action : transition.getActions()) {
                if (action.getName().equals(actions[0])) {
                    return process(scene.process(action), Arrays.copyOfRange(actions, 1, actions.length));
                }
            }
            throw new RuntimeException("Unknown action: " + actions[0]);
        } else {
            return transition;
        }
    }

    protected String eval(String text) {
        String eval;
        try {
            eval = (String) elp.eval(text);
        } catch (ELException e) {
//            e.printStackTrace();
            eval = text;
        }
        return eval;
    }

}
