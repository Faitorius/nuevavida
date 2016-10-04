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

    private List<Processor> preProcessors = new ArrayList<>();
    private List<Processor> postProcessors = new ArrayList<>();

    public void perform(GameData gameData, ResultListener listener) {
        this.gameData = gameData;
        this.result = new Result();
        this.listener = listener;

        //TODO preprocessors
        for (Processor processor : preProcessors) {
            String output = processor.process();
            if (output != null && output.length() > 0) {
                result.append("\n" + output);
            }
        }

        if (!events.isEmpty()) {
            pickEvent().occur(outfit, this);
        } else {
            listener.listen(result);
        }
    }

    public void addPreProcessor(Processor processor) {
        preProcessors.add(processor);
    }

    public void addPostProcessor(Processor processor) {
        postProcessors.add(processor);
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
        for (Processor processor : postProcessors) {
            String output = processor.process();
            if (output != null && output.length() > 0) {
                result.append("\n" + output);
            }
        }

        listener.listen(this.result);
    }
}
