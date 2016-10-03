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
    private Configuration configuration;
    private ELProcessor elp;

    public Event(EventTemplate eventTemplate, Configuration configuration, ELProcessor elp) {
        this.eventTemplate = eventTemplate;
        this.configuration = configuration;
        this.elp = elp;
    }

    public void occur(Outfit outfit, ResultListener listener) {
        Player player = new Player();
        List npcs = new ArrayList();
//        Scene scene = new Scene(player, npcs, this);
//        viewScene(scene, listener);

        GameScene scene = new GameScene(this, configuration.getScenes().get(eventTemplate.getScene()), elp);
        viewScene(scene, listener);

//        listener.listen(result);//TODO
    }

    private void viewScene(GameScene scene, ResultListener listener) {
        this.listener = listener;
        NuevaVidaGame.instance.viewGameScene(scene);
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
        listener = null;//TODO ??
        result = null;//TODO ??
    }
}
