package nl.elsci.nuevavida;

import lombok.Data;

import java.util.List;

@Data
public class ActivityTemplate {
    private String name;
    private List<String> events;
    private Outfit outfit;
    private ResultListener listener;

    public void perform() {
        pickEvent().occur(outfit, listener);
    }

    private Event pickEvent() {
        return new Event(new EventTemplate());
    }
}
