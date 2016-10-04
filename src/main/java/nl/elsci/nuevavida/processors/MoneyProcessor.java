package nl.elsci.nuevavida.processors;

import nl.elsci.nuevavida.GameData;

public class MoneyProcessor implements Processor {

    private int amount;

    public MoneyProcessor(int amount) {
        this.amount = amount;
    }

    @Override
    public String process(GameData gameData) {
        gameData.getPlayer().earnMoney(amount);
        return "You "+ (amount < 0 ? "spend " : "make") + " $" + Math.abs(amount);
    }
}
