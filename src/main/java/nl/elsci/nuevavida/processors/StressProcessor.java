package nl.elsci.nuevavida.processors;

import nl.elsci.nuevavida.GameData;

public class StressProcessor implements Processor {
    private int stress;

    public StressProcessor(int stress) {
        this.stress = stress;
    }

    @Override
    public String process(GameData gameData) {
        gameData.getPlayer().incStress(stress);
        return null;//TODO
    }
}
