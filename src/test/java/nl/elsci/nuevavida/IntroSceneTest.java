package nl.elsci.nuevavida;

import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.el.ELException;
import javax.el.ELProcessor;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class IntroSceneTest {

    private Configuration configuration;
    private ELProcessor elp;
    private SceneTemplate template;

    private GameScene scene;

    public IntroSceneTest() {
        Yaml yaml = new Yaml(new Constructor(Configuration.class));
        configuration = yaml.loadAs(IntroSceneTest.class.getClass().getResourceAsStream("/intro.yml"), Configuration.class);
        template = configuration.getScenes().get(0);

        elp = new ELProcessor();
        elp.defineBean("configuration", configuration);
    }

    @Before
    public void setUp() throws Exception {

        Player player = new Player();
        player.addTrait("BITCHY");
        elp.defineBean("player", player);
        elp.defineBean("gameData", new GameData());

        scene = new GameScene(template, elp);
        elp.defineBean("scene", scene);
    }

    @Test
    public void testStartTransition() {
        Transition transition = process();
        assertThat(eval(transition.getText()), startsWith("A few years from now:"));
        assertThat(eval(template.getPeople()), is("You're here. I would describe you, but let's wait until I've found out a little more first."));
        assertThat(eval(template.getDesc()), is("You're at home."));
        assertThat(transition.getActions().size(), is(1));
        assertThat(transition.getActions().get(0).getName(), is("Check your email"));
    }

    @Test
    public void testCheckYourEmailTransition() {
        Transition transition = process("Check your email");
        assertThat(eval(transition.getText()), startsWith("You open up your email, hoping to at least have some vaguely interesting spam."));
        assertThat(eval(template.getPeople()), is("You're here. I would describe you, but let's wait until I've found out a little more first."));
        assertThat(eval(template.getDesc()), is("You're at home."));
        assertThat(transition.getActions().size(), is(1));
        assertThat(transition.getActions().get(0).getName(), is("Read the email"));
    }

    @Test
    public void testReadTheEmailTransition() {
        Transition transition = process("Check your email", "Read the email");
        assertThat(eval(transition.getText()), startsWith("You open up the message and see that it's an invitation from Ivy to come visit her."));
        assertThat(eval(template.getPeople()), is("You're here. I would describe you, but let's wait until I've found out a little more first."));
        assertThat(eval(template.getDesc()), is("You're at home."));
        assertThat(transition.getActions().size(), is(1));
        assertThat(transition.getActions().get(0).getName(), is("Consider her offer"));
    }

    @Test
    public void testConsiderHerOfferTransition() {
        Transition transition = process("Check your email", "Read the email", "Consider her offer");
        assertThat(eval(transition.getText()), startsWith("You pause to consider her offer, but you're distracted by the doorbell."));
        assertThat(eval(template.getPeople()), endsWith("There's also a pair of criminal-looking thugs here, but let's not lower ourselves to detailed descriptions of such scum."));
        assertThat(eval(template.getDesc()), is("You're at home, confronted with some rather unpleasant visitors."));
        assertThat(transition.getActions().size(), is(1));
        assertThat(transition.getActions().get(0).getName(), is("You don't know"));
    }

    @Test
    public void testYouDontKnowTransition() {
        Transition transition = process("Check your email", "Read the email", "Consider her offer", "You don't know");
        assertThat(eval(transition.getText()), startsWith("You tell the thugs that you've no idea where your housemate has gone."));
        assertThat(eval(template.getPeople()), endsWith("There's also a pair of criminal-looking thugs here, but let's not lower ourselves to detailed descriptions of such scum."));
        assertThat(eval(template.getDesc()), is("You're at home, confronted with some rather unpleasant visitors."));
        assertThat(transition.getActions().size(), is(1));
        assertThat(transition.getActions().get(0).getName(), is("Explain yourself"));
    }

    @Test
    public void testExplainYourselfTransition() {
        Transition transition = process("Check your email", "Read the email", "Consider her offer", "You don't know", "Explain yourself");
        assertThat(eval(transition.getText()), startsWith("You try to explain how this is none of your business,"));
        assertThat(eval(template.getPeople()), endsWith("There's also a pair of criminal-looking thugs here, but let's not lower ourselves to detailed descriptions of such scum."));
        assertThat(eval(template.getDesc()), is("You're at home, confronted with some rather unpleasant visitors."));
        assertThat(transition.getActions().size(), is(4));
        assertThat(transition.getActions().get(0).getName(), is("Escape!"));
        assertThat(transition.getActions().get(1).getName(), is("Offer sexual favours"));
        assertThat(transition.getActions().get(2).getName(), is("Beg"));
        assertThat(transition.getActions().get(3).getName(), is("Be murdered"));
    }

    private Transition process(String... actions) {
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

    private String eval(String text) {
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
