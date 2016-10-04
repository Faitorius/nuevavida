package nl.elsci.nuevavida;

public class DefaultEvent extends Event {
    public DefaultEvent() {
        super(null, null, null, null);
    }

    @Override
    public void occur(Outfit outfit, ResultListener listener) {
        listener.listen(new Result());
    }

    @Override
    public int getWeight() {
        return 1000;
    }
}
