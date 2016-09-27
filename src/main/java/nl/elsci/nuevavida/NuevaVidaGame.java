package nl.elsci.nuevavida;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class NuevaVidaGame extends Application {
    private Stage primaryStage;
    private Scene weekPlannerScene;
    private Scene sceneViewerScene;

    private GameScene currentGameScene;
    private ObservableList<Action> actionList;
    private TextArea sceneText;
    private Action finishedAction;
    private Transition transition;
    private MultipleSelectionModel<Action> selectionModel;

//    private Configuration configuration;

/*
    public NuevaVidaGame(Configuration configuration) {
        this.configuration = configuration;
    }
*/

    public NuevaVidaGame() {
        finishedAction = new Action();
        finishedAction.setName("Finished");
        finishedAction.setDesc("End the scene and move on to the next event, or to the next week if this is this week's last event.");
   }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        primaryStage.setTitle("Nuevavida");

//        weekPlannerScene = createFirstScene();
        sceneViewerScene = createSceneViewerScene();

        GameScene gameScene = new GameScene();

        viewGameScene(gameScene);

        primaryStage.show();
    }

    private Scene createSceneViewerScene() {

        BorderPane pane = new BorderPane();
        sceneText = new TextArea();
        sceneText.setEditable(false);
        sceneText.setWrapText(true);
        pane.setCenter(sceneText);
        GridPane bottomGrid = new GridPane();
        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        columnConstraints1.setPercentWidth(30);
        bottomGrid.getColumnConstraints().add(columnConstraints1);
        ColumnConstraints columnConstraints2 = new ColumnConstraints();
        columnConstraints2.setPercentWidth(50);
        bottomGrid.getColumnConstraints().add(columnConstraints2);
        pane.setBottom(bottomGrid);

        TextArea descTextArea = new TextArea();
        descTextArea.setEditable(false);
        descTextArea.setWrapText(true);

        actionList = FXCollections.observableArrayList();
        ListView<Action> listView = new ListView<>(actionList);
        selectionModel = listView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener(($1, $2, newValue) -> {
            if (newValue != null) {
                descTextArea.setText(newValue.getDesc());
            } else {
                descTextArea.clear();
            }
        });
        listView.setPrefHeight(100);
        listView.setPrefWidth(100);
        bottomGrid.add(listView, 0, 0);
        bottomGrid.add(descTextArea, 1, 0);
        Button actionButton = new Button("Take Action");
        actionButton.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 20));
        actionButton.setPrefSize(200, 200);
        actionButton.setOnAction(event -> {
            Action selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem == finishedAction) {
                if (transition.getNextScene() != null) {
                    viewGameScene(transition.getNextScene());
                } else {
                    viewWeekPlanner();
                }
            } else if (selectedItem != null) {
                doTransition(currentGameScene.process(selectedItem));
            }
        });

        bottomGrid.add(actionButton, 2, 0);

        return new Scene(pane, 800, 600);
    }

    private void viewWeekPlanner() {
        //TODO
    }

    public void viewGameScene(GameScene scene) {
        currentGameScene = scene;
        sceneText.clear();
        doTransition(scene.getStartTransition());
        primaryStage.setScene(sceneViewerScene);
    }

    public void doTransition(Transition transition) {
        this.transition = transition;
        if (sceneText.getText().length() > 0) {
            sceneText.appendText("_________________________\n\n");
        }
        sceneText.appendText(transition.getText());
        actionList.setAll(transition.getActions());
        if (actionList.size() == 0) {
            actionList.add(finishedAction);
        }
        if (actionList.size() == 1) {
            selectionModel.select(0);
        }
    }

/*
    private Scene createFirstScene() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Welcome");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        btn.setOnAction(event -> {
            actiontarget.setFill(Color.FIREBRICK);
            actiontarget.setText("Sign in button pressed");
        });

        return new Scene(grid, 300, 275);
    }
*/

    public static void main(String[] args) throws FileNotFoundException {
        launch(args);
/*
        Yaml yaml = new Yaml(new Constructor(Configuration.class));
        Configuration configuration = yaml.loadAs(new FileReader("src/main/resources/test.yaml"), Configuration.class);
        System.out.println(configuration);
        new NuevaVidaGame(configuration);

        ELProcessor elp = new ELProcessor();
        Player player = new Player();
        player.addTrait("BITCHY");
        elp.defineBean("player", player);
        elp.defineBean("configuration", configuration);
        elp.defineBean("gameData", new GameData());
        Integer arousal = (Integer) elp.eval("player.arousal");
        System.out.println(elp.eval(configuration.getEvents().get(0).getWeight()));
        System.out.println(elp.eval("configuration.activities[0].events.contains('defaultEvent')"));

        for (String s : configuration.getScenes().get(1).getActions().get(1).getNext()) {
            String eval = (String) elp.eval(s);
            if (eval.length() > 0) {
                System.out.println(eval);
            }
        }
        System.out.println(player.getArousal());
        System.out.println(elp.eval(configuration.getScenes().get(1).getActions().get(3).getText()));
        System.out.println(player.getArousal());
*/

/*
        System.out.println("Possible activities:");
        int i = 1;
        for (ActivityTemplate activity : configuration.activities) {
            System.out.println(i++ + ": " + activity.getName());
        }
        System.out.println("Choose an activity: ");
        Scanner scanner = new Scanner(System.in);
        i = scanner.nextInt();
        ActivityTemplate activity = configuration.activities.get(i - 1);
        System.out.println("Picked activity '"+activity.getName()+"'");
        activity.perform();
*/
    }
}
