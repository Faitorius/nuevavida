package nl.elsci.nuevavida;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Activity implements ResultListener {
    private String name;
    private List<Event> events = new ArrayList<>();
    private Outfit outfit;
    private OutfitType outfitType;
    private ResultListener listener;
    private ActivityTemplate template;
    private GameData gameData;
    private Result result;

    public void perform(GameData gameData, ResultListener listener) {
        this.gameData = gameData;
        this.result = new Result();
        this.listener = listener;

        //TODO preprocessors

        if (!events.isEmpty()) {
            pickEvent().occur(outfit, this);
        } else {
            listener.listen(result);
        }
    }

    private Event pickEvent() {
        return events.get(0);//TODO
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void listen(Result result) {
        this.result.add(result);

        //TODO postprocessors

        listener.listen(this.result);
    }
}
