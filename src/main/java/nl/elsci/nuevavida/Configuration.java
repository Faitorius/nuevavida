package nl.elsci.nuevavida;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Configuration {
    private List<ActivityTemplate> activities = new ArrayList<>();
    private Map<String, EventTemplate> events = new HashMap<>();
    private Map<String, SceneTemplate> scenes = new HashMap<>();
    private List<String> files;

    public void merge(Configuration configuration) {
        activities.addAll(configuration.getActivities());
        events.putAll(configuration.getEvents());
        scenes.putAll(configuration.getScenes());
    }
}
