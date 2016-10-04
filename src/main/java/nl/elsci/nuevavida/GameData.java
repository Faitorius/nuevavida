package nl.elsci.nuevavida;

import lombok.Getter;

import javax.el.ELProcessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameData {

    @Getter
    private List<String> flags = new ArrayList<>();
    private int currentWeek;
    private String weekInfo;
    private Player player = new Player(); //TODO
    private Configuration configuration;
    private ELProcessor elp;
    private GameSceneViewer viewer;

    public GameData(Configuration configuration, ELProcessor elp, GameSceneViewer viewer) {
        this.configuration = configuration;
        this.elp = elp;
        this.viewer = viewer;
    }

    public WeekStartInfo getWeekStartInfo() {
        List<Activity> workActivities = new ArrayList<>();
        Activity activity = new Activity();
        activity.setName("Go to work");
        activity.setOutfitType(OutfitType.BUSINESS);
//        activity.getEvents().add(new Event(new EventTemplate(), configuration, elp));
        workActivities.add(activity);

        List<Activity> freeTimeActivities = new ArrayList<>();

        List<Event> freeTimeEvents = new ArrayList<>();
        for (Map.Entry<String, EventTemplate> entry : configuration.getEvents().entrySet()) {
            if (entry.getValue().getActivityTypes().contains("Free time")) {
                freeTimeEvents.add(new Event(entry.getValue(), configuration, elp, viewer));
            }
        }

        activity = new Activity();
        activity.setName("Relax at home");
        activity.setOutfitType(OutfitType.CASUAL);
        activity.setEvents(freeTimeEvents);

        freeTimeActivities.add(activity);
        activity = new Activity();
        activity.setName("Explore your body");
        activity.setOutfitType(OutfitType.CASUAL);
//        activity.getEvents().add(new Event(new EventTemplate(), configuration, elp));

        freeTimeActivities.add(activity);
        activity = new Activity();
        activity.setName("Study fashion");
        activity.setOutfitType(OutfitType.CASUAL);
//        activity.getEvents().add(new Event(new EventTemplate(), configuration, elp));

        freeTimeActivities.add(activity);

        List<Activity> goingOutActivities = new ArrayList<>();

        activity = new Activity();
        activity.setName("Stay at home");
        activity.setOutfitType(OutfitType.CASUAL);
//        activity.getEvents().add(new Event(new EventTemplate(), configuration, elp));

        goingOutActivities.add(activity);
        activity = new Activity();
        activity.setName("Go Clubbing");
        activity.setOutfitType(OutfitType.CLUBBING);
//        activity.getEvents().add(new Event(new EventTemplate(), configuration, elp));

        goingOutActivities.add(activity);
        List<Activity> weekendActivities = new ArrayList<>();

        activity = new Activity();
        activity.setName("Relax at home");
        activity.setOutfitType(OutfitType.CASUAL);
//        activity.getEvents().add(new Event(new EventTemplate(), configuration, elp));

        weekendActivities.add(activity);
        activity = new Activity();
        activity.setName("Go Shopping");
        activity.setOutfitType(OutfitType.CASUAL);
//        activity.getEvents().add(new Event(new EventTemplate(), configuration, elp));

        weekendActivities.add(activity);
        activity = new Activity();
        activity.setName("Shop for your flat");
        activity.setOutfitType(OutfitType.CASUAL);
//        activity.getEvents().add(new Event(new EventTemplate(), configuration, elp));

        weekendActivities.add(activity);
        return new WeekStartInfo(workActivities, freeTimeActivities, goingOutActivities, weekendActivities, weekInfo);
    }

    public void incrementWeek() {
        currentWeek++;
        weekInfo = "Week " + currentWeek + "\n";
        weekInfo += player.getWeekUpdate();
    }
}
