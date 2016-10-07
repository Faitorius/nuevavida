package nl.elsci.nuevavida;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import lombok.extern.slf4j.Slf4j;

import javax.el.ELProcessor;

@Slf4j
public class GameSceneController implements GameSceneViewer {

    @FXML
    public TextArea descText;
    @FXML
    private TextArea sceneText;
    @FXML
    private ListView<Action> actionList;

    private NuevaVidaGame nuevaVidaGame;

    private GameScene currentGameScene;
    private final Action finishedAction;
    private final Action proceedAction;
    private Action lastAction;

    private ObservableList<Action> observableList;
    private MultipleSelectionModel<Action> selectionModel;

    private GameScene nextGameScene;
    private Configuration configuration;
    private ELProcessor elp;

    public GameSceneController() {
        finishedAction = new Action();
        finishedAction.setName("Finished");
        finishedAction.setDesc("End the scene and move on to the next event, or to the next week if this is this week's last event.");

        proceedAction = new Action();
        proceedAction.setName("Proceed");
        proceedAction.setDesc("Proceed to the next scene");
    }

    @FXML
    public void initialize() {
        observableList = actionList.getItems();
        selectionModel = actionList.getSelectionModel();

        actionList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                descText.clear();
            } else {
                descText.setText(newValue.getDesc());
            }
        });

        actionList.setCellFactory(param -> {
            ListCell<Action> cell = new ListCell<Action>() {
                @Override
                protected void updateItem(Action item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText((observableList.indexOf(item) + 1) + ": " + item.toString());
                    }
                }
            };
            cell.setOnMouseClicked(event -> {
                if (cell.isEmpty()) {
                    event.consume();
                }
            });
            return cell;
        });

        actionList.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                event.consume();
                performSelectedAction();
            }
        });
        actionList.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                event.consume();
                performSelectedAction();
            } else {
                try {
                    int i = Integer.parseInt(event.getText());
                    Action action = observableList.get(i - 1);
                    if (action != null) {
                        selectionModel.select(action);
                        event.consume();
                        performSelectedAction();
                    }
                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    //not a valid number, continue
                }
            }
        });
    }

    private void clear() {
        sceneText.clear();
        actionList.getItems().clear();
    }

    private void appendText(String txt) {
        String text = nuevaVidaGame.eval(txt);
        if (sceneText.getText().length() > 0) {
            sceneText.appendText("\n_______________________________________\n\n");
        }
        sceneText.appendText(text.trim());
    }

    public void setMain(NuevaVidaGame nuevaVidaGame) {
        this.nuevaVidaGame = nuevaVidaGame;
    }

    private void doTransition(Transition transition) {
        appendText(transition.getText());

        if (transition.getNextScene() != null) {
            observableList.setAll(proceedAction);
            nextGameScene = transition.getNextScene();
        } else if (transition.getActions().size() == 0) {
            observableList.setAll(finishedAction);
        } else {
            observableList.setAll(transition.getActions());
        }
        if (observableList.contains(lastAction)) {
            selectionModel.select(lastAction);
        } else {
            selectionModel.select(0);
        }
        actionList.requestFocus();
    }

    @Override
    public void viewGameScene(String name) {
        viewGameScene(new GameScene(nuevaVidaGame, configuration.getScenes().get(name), elp));
    }

    @Override
    public void viewGameScene(GameScene scene) {
        log.debug("switching to new scene");

        currentGameScene = scene;
        clear();
        doTransition(scene.getStartTransition());
        nuevaVidaGame.showSceneViewer();
    }


    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setElp(ELProcessor elp) {
        this.elp = elp;
    }

    public void describeScene(ActionEvent actionEvent) {
        appendText(currentGameScene.getTemplate().getDesc());
    }

    public void describePeople(ActionEvent actionEvent) {
        appendText(currentGameScene.getTemplate().getPeople());
    }

    public void onActionButtonAction(ActionEvent actionEvent) {
        performSelectedAction();
    }

    private void performSelectedAction() {
        lastAction = selectionModel.getSelectedItem();
        if (lastAction == finishedAction) {
            clear();
            GameScene scene = this.currentGameScene;
            currentGameScene = null;
            scene.notifyListener();
        } else if (lastAction == proceedAction && nextGameScene != null) {
            GameScene scene = nextGameScene;
            nextGameScene = null;
            viewGameScene(scene);
        } else if (lastAction != null) {
            doTransition(currentGameScene.process(lastAction));
        }
    }
}
