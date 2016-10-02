package nl.elsci.nuevavida;

import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
public class EventTemplate {
    private String name;
    private String scene;
    private String weight;
    private List<String> activityTypes;
}
