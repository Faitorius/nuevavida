package nl.elsci.nuevavida;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.el.ELProcessor;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Application {
    public static void main(String[] args) throws FileNotFoundException {
        Yaml yaml = new Yaml(new Constructor(Configuration.class));
        Configuration configuration = yaml.loadAs(new FileReader("src/main/resources/test.yaml"), Configuration.class);
        System.out.println(configuration);

        ELProcessor elp = new ELProcessor();
        Player player = new Player();
        player.addTrait("BITCHY");
        elp.defineBean("player", player);
        elp.defineBean("configuration", configuration);
        elp.defineBean("gameData", new GameData());
        Integer arousal = (Integer) elp.eval("player.arousal");
        System.out.println(elp.eval(configuration.getEvents().get(0).getWeight()));
        System.out.println(elp.eval("configuration.activities[0].events.contains('defaultEvent')"));

        for (String s : configuration.getScenes().get(1).getActions().get(1).getNext()) {
            String eval = (String) elp.eval(s);
            if (eval.length() > 0) {
                System.out.println(eval);
            }
        }
        System.out.println(player.getArousal());
        System.out.println(elp.eval(configuration.getScenes().get(1).getActions().get(3).getText()));
        System.out.println(player.getArousal());

/*
        System.out.println("Possible activities:");
        int i = 1;
        for (ActivityTemplate activity : configuration.activities) {
            System.out.println(i++ + ": " + activity.getName());
        }
        System.out.println("Choose an activity: ");
        Scanner scanner = new Scanner(System.in);
        i = scanner.nextInt();
        ActivityTemplate activity = configuration.activities.get(i - 1);
        System.out.println("Picked activity '"+activity.getName()+"'");
        activity.perform();
*/
    }
}
