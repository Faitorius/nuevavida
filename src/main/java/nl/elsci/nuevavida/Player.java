package nl.elsci.nuevavida;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int arousal = 15;
    private List<String> traits = new ArrayList<>();
    private int stress;
    private int stressChange;

    public int getArousal() {
        return arousal;
    }

    public void setArousal(int arousal) {
        this.arousal = arousal;
    }

    public List<String> getTraits() {
        return traits;
    }

    public void addTrait(String trait) {
        traits.add(trait);
    }

    public void incStress(int stress){}

    public String getWeekUpdate() {
        String update = "";

        // TODO children

        if (stressChange != 0) {
            // TODO stress
        } else {
            update += "Your stress remains unchanged this week at " + stress;
        }
        update += "\n";

        //TODO processCum
        //TODO increase cycle

        //TODO fertility info

        return update;
    }
}
