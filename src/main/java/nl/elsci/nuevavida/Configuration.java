package nl.elsci.nuevavida;

import lombok.Data;

import java.util.List;

@Data
public class Configuration {
    List<ActivityTemplate> activities;
    List<EventTemplate> events;
    List<SceneTemplate> scenes;
}
