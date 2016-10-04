package nl.elsci.nuevavida;

import lombok.Getter;
import lombok.Setter;
import nl.elsci.nuevavida.processors.MoneyProcessor;
import nl.elsci.nuevavida.processors.StressProcessor;

import javax.el.ELProcessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameData {

    @Getter @Setter
    private Player player = new Player();

    @Getter
    private List<String> flags = new ArrayList<>();
    private int currentWeek;
    private String weekInfo;
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
//TODO        activity.addPostProcessor(new JobPerformanceProcessor(career.getCurrentJob()));
        workActivities.add(activity);

        List<Activity> freeTimeActivities = new ArrayList<>();

        List<Event> freeTimeEvents = new ArrayList<>();
        for (Map.Entry<String, EventTemplate> entry : configuration.getEvents().entrySet()) {
            if (entry.getValue().getActivityTypes().contains("Free time")) {
                freeTimeEvents.add(new Event(entry.getValue(), configuration, elp, viewer));
            }
        }
        freeTimeEvents.add(new DefaultEvent());

        activity = new Activity();
        activity.setName("Relax at home");
        activity.setOutfitType(OutfitType.CASUAL);
        activity.setEvents(freeTimeEvents);
        activity.addPreProcessor(new StressProcessor(-1));

        freeTimeActivities.add(activity);
        activity = new Activity();
        activity.setName("Explore your body");
        activity.setOutfitType(OutfitType.CASUAL);
//TODO        activity.addPreProcessor(new SkillProcessor(Skill.FEMININITY, 4));
        activity.addPreProcessor(new StressProcessor(-2));

        freeTimeActivities.add(activity);
        activity = new Activity();
        activity.setName("Study fashion");
        activity.setOutfitType(OutfitType.CASUAL);
//TODO        activity.addPreProcessor(new SkillProcessor(Skill.FASHION, 4));
        activity.addPreProcessor(new StressProcessor(-2));

        freeTimeActivities.add(activity);

        //TODO dance

        //TODO gym

        List<Activity> goingOutActivities = new ArrayList<>();

        //TODO anniversary

        activity = new Activity();
        activity.setName("Stay at home");
        activity.setOutfitType(OutfitType.CASUAL);
        activity.addPreProcessor(new StressProcessor(-1));

        goingOutActivities.add(activity);
        activity = new Activity();
        activity.setName("Go Clubbing");
        activity.setOutfitType(OutfitType.CLUBBING);
        activity.addPreProcessor(new MoneyProcessor(-10));

        //TODO go to a bar with friends

        //TODO stay in with

        //TODO go out with

        //TODO go to a gig with

        goingOutActivities.add(activity);
        List<Activity> weekendActivities = new ArrayList<>();

        activity = new Activity();
        activity.setName("Relax at home");
        activity.setOutfitType(OutfitType.CASUAL);

        //TODO spend time with friends

        weekendActivities.add(activity);
        activity = new Activity();
        activity.setName("Go Shopping");
        activity.setOutfitType(OutfitType.CASUAL);

        weekendActivities.add(activity);
        activity = new Activity();
        activity.setName("Shop for your flat");
        activity.setOutfitType(OutfitType.CASUAL);

        //TODO visit doctor

        //TODO go to a fashion show

        //TODO compete in dance tournament

        //TODO prenatal class

        //TODO go to the gym

        //TODO befriend someone

        //TODO enjoy a spa weekend

        weekendActivities.add(activity);
        return new WeekStartInfo(workActivities, freeTimeActivities, goingOutActivities, weekendActivities, weekInfo);
    }

    public void incrementWeek() {
        currentWeek++;
        weekInfo = "Week " + currentWeek + "\n";
        weekInfo += player.getWeekUpdate();
    }
}
