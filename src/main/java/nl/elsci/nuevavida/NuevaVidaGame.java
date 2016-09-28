package nl.elsci.nuevavida;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class NuevaVidaGame extends Application {

    private static final Logger log = LoggerFactory.getLogger(NuevaVidaGame.class);

    private Stage primaryStage;

    private Scene weekPlannerScene;

    private Scene sceneViewer;
    private TextArea sceneText;
    private ObservableList<Action> actionList;
    private MultipleSelectionModel<Action> selectionModel;

    private GameScene currentGameScene;
    private Action finishedAction;
    private Transition transition;

    private Configuration configuration;
    private ComboBox<Activity> dateActivityComboBox;
    private ComboBox<Activity> workActivityComboBox;
    private ComboBox<Activity> freeTimeActivityComboBox;
    private ComboBox<Activity> weekendActivityComboBox;

    public NuevaVidaGame() {

        Yaml yaml = new Yaml(new Constructor(Configuration.class));
        try {
            configuration = yaml.loadAs(new FileReader("src/main/resources/test.yaml"), Configuration.class);
        } catch (FileNotFoundException e) {
            log.error("Unable to load configuration", e);
            configuration = new Configuration();
        }

        finishedAction = new Action();
        finishedAction.setName("Finished");
        finishedAction.setDesc("End the scene and move on to the next event, or to the next week if this is this week's last event.");
   }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Nuevavida");

        weekPlannerScene = createWeekPlanner();
        sceneViewer = createSceneViewer();

        GameScene gameScene = new GameScene(configuration.getScenes().get(0));
//        viewGameScene(gameScene);
        viewWeekPlanner(new WeekStartInfo());

        primaryStage.show();
    }

    private Scene createSceneViewer() {

        log.debug("creating sceneviewer scene");

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
                    viewWeekPlanner(new WeekStartInfo());//TODO
                }
            } else if (selectedItem != null) {
                doTransition(currentGameScene.process(selectedItem));
            }
        });

        bottomGrid.add(actionButton, 2, 0);

        return new Scene(pane, 800, 600);
    }

    private void viewWeekPlanner(WeekStartInfo weekStartInfo) {
        //TODO

        primaryStage.setScene(weekPlannerScene);
    }

    public void viewGameScene(GameScene scene) {

        log.debug("switching to scene " + scene.getName());

        currentGameScene = scene;
        sceneText.clear();
        doTransition(scene.getStartTransition());
        primaryStage.setScene(sceneViewer);
    }

    private void doTransition(Transition transition) {
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

    private Scene createWeekPlanner() {

        GridPane grid = new GridPane();
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(25);
        grid.getColumnConstraints().add(columnConstraints);
        columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(25);
        grid.getColumnConstraints().add(columnConstraints);
        columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(50);
        grid.getColumnConstraints().add(columnConstraints);
        grid.setVgap(10);
        grid.setHgap(10);

        workActivityComboBox = new ComboBox<>();
        workActivityComboBox.setPrefWidth(150);
        Label workLabel = new Label("Work Activity:");
        workLabel.setPrefWidth(175);
        workLabel.setLabelFor(workActivityComboBox);

        grid.add(workLabel, 0, 0);
        grid.add(workActivityComboBox, 1, 0);
        grid.add(createOutfitComboBox(), 2, 0);

        freeTimeActivityComboBox = new ComboBox<>();
        freeTimeActivityComboBox.setPrefWidth(150);
        Label freeTimeLabel = new Label("Free Time Activity:");
        freeTimeLabel.setPrefWidth(175);
        freeTimeLabel.setLabelFor(freeTimeActivityComboBox);

        grid.add(freeTimeLabel, 0, 1);
        grid.add(freeTimeActivityComboBox, 1, 1);
        grid.add(createOutfitComboBox(), 2, 1);

        weekendActivityComboBox = new ComboBox<>();
        weekendActivityComboBox.setPrefWidth(150);
        Label weekendLabel = new Label("Weekend Activity:");
        weekendLabel.setPrefWidth(175);
        weekendLabel.setLabelFor(weekendActivityComboBox);

        grid.add(weekendLabel, 0, 2);
        grid.add(weekendActivityComboBox, 1, 2);
        grid.add(createOutfitComboBox(), 2, 2);

        dateActivityComboBox = new ComboBox<>();
        dateActivityComboBox.setPrefWidth(150);
        Label dateLabel = new Label("Weekend Evening Activity:");
        dateLabel.setPrefWidth(175);
        dateLabel.setLabelFor(dateActivityComboBox);

        grid.add(dateLabel, 0, 3);
        grid.add(dateActivityComboBox, 1, 3);
        grid.add(createOutfitComboBox(), 2, 3);

        Button startWeekButton = new Button("Start Week");
        startWeekButton.setFont(new Font("Tahoma", 14));
        startWeekButton.setOnAction(event -> {/*todo*/});

        FlowPane flowPane = new FlowPane(Orientation.VERTICAL);
        flowPane.setVgap(10);
        flowPane.setPrefSize(700, 250);
        flowPane.getChildren().add(startWeekButton);
        flowPane.getChildren().add(grid);

        flowPane.setAlignment(Pos.CENTER);

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20));

        borderPane.setTop(flowPane);

        TextArea weekInfoText = new TextArea("Week 2\n\n\n" +
                "Your stress remains unchanged this week at 0");
        weekInfoText.setEditable(false);
        borderPane.setCenter(weekInfoText);

        return new Scene(borderPane, 800, 600);
    }

    private ComboBox<String> createOutfitComboBox() {
        ComboBox<String> freeTimeOutfitComboBox = new ComboBox<>();
        freeTimeOutfitComboBox.getItems().add("Random outfit");
        freeTimeOutfitComboBox.getSelectionModel().select(0);
        freeTimeOutfitComboBox.setPrefWidth(300);
        return freeTimeOutfitComboBox;
    }

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
