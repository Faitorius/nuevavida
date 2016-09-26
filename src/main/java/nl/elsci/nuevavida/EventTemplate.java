package nl.elsci.nuevavida;

import lombok.Data;

import java.util.List;

@Data
public class EventTemplate {
    private String name;
    private List<String> scenes;
    private String weight;

    public String getWeight() {
        return weight;
    }
}
