package nl.elsci.nuevavida;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class IntroSceneTest extends AbstractSceneTest {

    public IntroSceneTest() {
        super("Intro");
    }

    @Before
    public void setUp() {
        super.setUp();

        Player player = new Player();
        player.addTrait("BITCHY");
        elp.defineBean("player", player);
        elp.defineBean("gameData", new GameData());
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
}
