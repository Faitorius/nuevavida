package nl.elsci.nuevavida;

import lombok.Data;

import javax.el.ELProcessor;
import java.util.ArrayList;
import java.util.List;

@Data
public class Event implements ResultListener {
    private String name;
    private List<String> scenes;
    private ResultListener listener;
    private Result result = new Result();
    private EventTemplate eventTemplate;

    public Event(EventTemplate eventTemplate) {
        this.eventTemplate = eventTemplate;
    }

    public void occur(Outfit outfit, ResultListener listener) {
        Player player = new Player();
        List npcs = new ArrayList();
//        Scene scene = new Scene(player, npcs, this);
//        viewScene(scene, listener);
    }

    private void viewScene(Scene scene, ResultListener listener) {
        this.listener = listener;

    }

    public int getWeight() {

        ELProcessor elp = new ELProcessor();
        elp.defineBean("player", new Player());
        Object name = elp.eval("player.arousal > 10");

        System.out.println(name.getClass());
        System.out.println(name.toString());


        return 1000;
    }

    @Override
    public void listen(Result sceneResult) {
        listener.listen(sceneResult);
        listener.listen(result);
    }
}
