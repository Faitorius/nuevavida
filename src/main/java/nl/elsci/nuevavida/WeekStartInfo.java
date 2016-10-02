package nl.elsci.nuevavida;

import lombok.Data;

import java.util.List;

@Data
public class WeekStartInfo {
    private final List<Activity> workActivities;
    private final List<Activity> freeTimeActivities;
    private final List<Activity> goingOutActivities;
    private final List<Activity> weekendActivities;
    private final String weekInfo;
}
