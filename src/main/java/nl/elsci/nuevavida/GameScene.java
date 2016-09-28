package nl.elsci.nuevavida;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;

@Data
public class GameScene {
    private String name;
    private final SceneTemplate template;

    public GameScene(SceneTemplate template) {
        this.template = template;
    }

    public Transition process(Action selectedItem) {
        //TODO
        Transition transition = new Transition("Done!", new ArrayList<>());
        transition.setNextScene(this);
        return transition;
    }

    public Transition getStartTransition() {
        Action action1 = new Action();
        action1.setName("Apply for the job");
        action1.setDesc("Send in your CV");
        Action action2 = new Action();
        action2.setName("Apply for the second job");
        action2.setDesc("Send in your other CV");

        return new Transition("A few years from now:\n" +
                "\n" +
                "The last few years have not been the best for you. You're single, you've mostly lost contact with your friends and family, and you don't even have much of a job to speak of. \n" +
                "You just haven't been able to dig yourself out of this rut, and you've become more and more miserable about your life, so that now you mostly just doze through your dead-end job and doze the evenings away mindlessly clicking on the internet. \n" +
                "\n" +
                "Just as things seem most bleak you get an email from Ivy. She's a woman you met about six months ago when she came to your hometown on holiday. Not a girlfriend or anything like that, but probably the only friend you still talk to regularly, even if she doesn't live close enough to visit.\n" +
                "She sends you a link to a job that's opened up near where she lives. A job offer with much better pay and prospects than your current one, and one that might even match your skills. It's also one that means moving far away from your home, but it's not like you had anything keeping you there. ", Arrays.asList(action1, action2));
    }
}
