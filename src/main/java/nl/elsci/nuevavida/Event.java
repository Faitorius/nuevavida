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
    private GameSceneViewer viewer;

    public Event(EventTemplate eventTemplate, Configuration configuration, ELProcessor elp, GameSceneViewer viewer) {
        this.eventTemplate = eventTemplate;
        this.configuration = configuration;
        this.elp = elp;
        this.viewer = viewer;
    }

    public void occur(Outfit outfit, ResultListener listener) {
//        Player player = new Player();
//        List npcs = new ArrayList();
//        Scene scene = new Scene(player, npcs, this);
//        viewScene(scene, listener);

        GameScene scene = new GameScene(this, configuration.getScenes().get(eventTemplate.getScene()), elp);
        viewScene(scene, listener);

//        listener.listen(result);//TODO
    }

    private void viewScene(GameScene scene, ResultListener listener) {
        this.listener = listener;
        viewer.viewGameScene(scene);
    }

    public int getWeight() {
        return (int) (long) elp.eval(eventTemplate.getWeight());
    }

    @Override
    public void listen(Result sceneResult) {
        listener.listen(sceneResult);
        listener.listen(result);
        listener = null;//TODO ??
        result = null;//TODO ??
    }
}
