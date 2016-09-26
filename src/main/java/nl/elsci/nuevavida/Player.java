package nl.elsci.nuevavida;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int arousal = 15;
    private List<String> traits = new ArrayList<>();

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
}
