package nl.elsci.nuevavida;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SceneTemplate {
    private String name;
    private String intro;
    private List<String> introActions;
    private String desc;
    private String people;
    private Map<String, Action> actions;
}
