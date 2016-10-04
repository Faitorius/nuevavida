package nl.elsci.nuevavida;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import nl.elsci.nuevavida.processors.Processor;

import java.util.*;

@Slf4j
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

    private Collection<Processor> preProcessors = new ArrayList<>();
    private Collection<Processor> postProcessors = new ArrayList<>();

    public void perform(GameData gameData, ResultListener listener) {
        this.gameData = gameData;
        this.result = new Result();
        this.listener = listener;

        for (Processor processor : preProcessors) {
            String output = processor.process(gameData);
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
        Map<Integer, Event> map = new HashMap<>();
        int totalWeight = 0;
        for (Event event : events) {
            map.put(event.getWeight(), event);
            totalWeight += event.getWeight();
        }

        log.debug("picking event, totalweight: " + totalWeight);
        int rnd = new Random().nextInt(totalWeight);

        log.debug("random nr: " + rnd);

        int weightCounter = 0;
        for (Map.Entry<Integer, Event> integerEventEntry : map.entrySet()) {
            weightCounter += integerEventEntry.getKey();
            if (rnd < weightCounter) {
                return integerEventEntry.getValue();
            }
        }

        return new DefaultEvent();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void listen(Result result) {
        this.result.add(result);

        for (Processor processor : postProcessors) {
            String output = processor.process(gameData);
            if (output != null && output.length() > 0) {
                result.append("\n" + output);
            }
        }

        listener.listen(this.result);
    }
}
