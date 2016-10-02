package nl.elsci.nuevavida;

import org.mockito.Mockito;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;

import javax.el.ELException;
import javax.el.ELProcessor;
import java.util.Arrays;

public abstract class AbstractSceneTest {
    private final Configuration configuration;
    protected final ELProcessor elp;
    protected SceneTemplate template;
    private GameScene scene;

    public AbstractSceneTest(String name) {
        Yaml yaml = new Yaml(new Constructor(Configuration.class));

        configuration = yaml.loadAs(AbstractSceneTest.class.getClass().getResourceAsStream("/config.yml"), Configuration.class);
        for (String file : configuration.getFiles()) {
            try {
                configuration.merge(yaml.loadAs(AbstractSceneTest.class.getClass().getResourceAsStream("/" + file), Configuration.class));
            } catch (YAMLException e) {
                e.printStackTrace();
            }
        }

        template = configuration.getScenes().get(name);

        elp = new ELProcessor();
        elp.defineBean("configuration", configuration);
    }

    public void setUp() {
        ResultListener listener = Mockito.mock(ResultListener.class);
        scene = new GameScene(listener, template, elp);
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
