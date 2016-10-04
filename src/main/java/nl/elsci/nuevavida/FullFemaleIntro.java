package nl.elsci.nuevavida;

public class FullFemaleIntro implements Scheduler, ResultListener, NuevaVidaGame.FemaleChargenListener {

    private int step = 0;
    private Result result = new Result();
    private ResultListener listener;
    private GameSceneViewer viewer;

    public FullFemaleIntro(GameSceneViewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void setListener(ResultListener listener) {
        this.listener = listener;
        viewer.viewGameScene("Intro");
    }

    @Override
    public void listen(Result result) {
        step++;
        this.result.add(result);

        if (step == 1) {
            if (result.hasFlag("END_GAME")) {
                //TODO restart game
            } else {
                //TODO add skip intro
                //TODO add secret template
                template(new FemaleTemplate());//TODO show female chargen screen
            }
        } else if (step == 2) {
            //TODO go shopping
        } else {
            listener.listen(result);
        }
    }

    @Override
    public void template(FemaleTemplate template) {
        Player player = template.createPlayer();
        //TODO outfit

        // TODO start game
        NuevaVidaGame.startGame(player, 52);

        viewer.viewGameScene("Intro 2");
    }
}
