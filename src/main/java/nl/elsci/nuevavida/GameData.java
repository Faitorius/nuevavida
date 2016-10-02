package nl.elsci.nuevavida;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class GameData {

    @Getter
    private List<String> flags = new ArrayList<>();
    private int currentWeek;
    private String weekInfo;
    private Player player = new Player(); //TODO

    public WeekStartInfo getWeekStartInfo() {
        List<Activity> workActivities = new ArrayList<>();
        Activity activity = new Activity();
        activity.setName("Go to work");
        activity.setOutfitType(OutfitType.BUSINESS);
        activity.getEvents().add(new Event(new EventTemplate()));
        workActivities.add(activity);

        List<Activity> freeTimeActivities = new ArrayList<>();

        activity = new Activity();
        activity.setName("Relax at home");
        activity.setOutfitType(OutfitType.CASUAL);
        activity.getEvents().add(new Event(new EventTemplate()));

        freeTimeActivities.add(activity);
        activity = new Activity();
        activity.setName("Explore your body");
        activity.setOutfitType(OutfitType.CASUAL);
        activity.getEvents().add(new Event(new EventTemplate()));

        freeTimeActivities.add(activity);
        activity = new Activity();
        activity.setName("Study fashion");
        activity.setOutfitType(OutfitType.CASUAL);
        activity.getEvents().add(new Event(new EventTemplate()));

        freeTimeActivities.add(activity);

        List<Activity> goingOutActivities = new ArrayList<>();

        activity = new Activity();
        activity.setName("Stay at home");
        activity.setOutfitType(OutfitType.CASUAL);
        activity.getEvents().add(new Event(new EventTemplate()));

        goingOutActivities.add(activity);
        activity = new Activity();
        activity.setName("Go Clubbing");
        activity.setOutfitType(OutfitType.CLUBBING);
        activity.getEvents().add(new Event(new EventTemplate()));

        goingOutActivities.add(activity);
        List<Activity> weekendActivities = new ArrayList<>();

        activity = new Activity();
        activity.setName("Relax at home");
        activity.setOutfitType(OutfitType.CASUAL);
        activity.getEvents().add(new Event(new EventTemplate()));

        weekendActivities.add(activity);
        activity = new Activity();
        activity.setName("Go Shopping");
        activity.setOutfitType(OutfitType.CASUAL);
        activity.getEvents().add(new Event(new EventTemplate()));

        weekendActivities.add(activity);
        activity = new Activity();
        activity.setName("Shop for your flat");
        activity.setOutfitType(OutfitType.CASUAL);
        activity.getEvents().add(new Event(new EventTemplate()));

        weekendActivities.add(activity);
        return new WeekStartInfo(workActivities, freeTimeActivities, goingOutActivities, weekendActivities, weekInfo);
    }

    public void endWeek() {
        currentWeek++;
        weekInfo = "Week " + currentWeek + "\n";
        weekInfo += player.getWeekUpdate();
    }
}
