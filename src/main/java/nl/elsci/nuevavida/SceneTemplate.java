package nl.elsci.nuevavida;

import lombok.Data;

import java.util.List;

@Data
public class SceneTemplate {
    private String name;
    private String desc;
    private String people;
    private List<Action> actions;
}
