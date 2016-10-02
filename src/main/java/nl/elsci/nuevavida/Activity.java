package nl.elsci.nuevavida;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Activity {
    private String name;
    private List<Event> events = new ArrayList<>();
    private Outfit outfit;
    private OutfitType outfitType;
    private ResultListener listener;
    private ActivityTemplate template;

    public void perform() {
        pickEvent().occur(outfit, listener);
    }

    private Event pickEvent() {
        return new Event(new EventTemplate());//TODO
    }

    @Override
    public String toString() {
        return name;
    }
}
