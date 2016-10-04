package nl.elsci.nuevavida;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Iterator;

@Slf4j
public class WeekRunner implements Scheduler, ResultListener {

    private GameData gameData;
    private final Iterator<Activity> activities;
    private ResultListener listener;
    private Result result = new Result();

    public WeekRunner(GameData gameData, Collection<Activity> activities) {
        this.gameData = gameData;
        this.activities = activities.iterator();
    }

    @Override
    public void setListener(ResultListener listener) {
        this.listener = listener;
        run();
    }

    private void run() {
        if (activities.hasNext()) {
            activities.next().perform(gameData, this);
        } else {
            listener.listen(result);
        }
    }

    @Override
    public void listen(Result result) {
        this.result.add(result);
        this.result.append("\n\n");
        run();
    }
}
