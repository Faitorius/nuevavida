package nl.elsci.nuevavida;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Configuration {
    private List<ActivityTemplate> activities = new ArrayList<>();
    private List<EventTemplate> events = new ArrayList<>();
    private Map<String, SceneTemplate> scenes = new HashMap<>();
    private List<String> files;

    public void merge(Configuration configuration) {
        activities.addAll(configuration.getActivities());
        events.addAll(configuration.getEvents());
        scenes.putAll(configuration.getScenes());
    }
}
