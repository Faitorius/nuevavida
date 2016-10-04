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

        // TODO friends
        update += "\n\n";

        if (stressChange != 0) {
            stress += stressChange;
            stress = Math.max(stress, 0);
            stress = Math.min(stress, 100);
            if (stressChange > 0) {
                update += "Last week you suffered " + stressChange + " points of stress and your stress total is now " + stress;
            } else {
                update += "Last week your stress changed by " + stressChange + " and your stress total is now " + stress;
            }
            stressChange = 0;
        } else {
            update += "Your stress remains unchanged this week at " + stress;
        }
        update += "\n";

        //TODO processCum
        //TODO increase cycle

        //TODO fertility info

        return update;
    }

    public void earnMoney(int amount) {
        //TODO
    }
}
