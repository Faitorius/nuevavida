package nl.elsci.nuevavida;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;

import javax.el.ELException;
import javax.el.ELProcessor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class NuevaVidaGame extends Application implements ResultListener {

    private GameData gameData;

    private Stage primaryStage;

    private Scene weekPlannerScene;

    private Scene sceneViewer;

    private Configuration configuration;
    private ComboBox<Activity> dateActivityComboBox;
    private ComboBox<Activity> workActivityComboBox;
    private ComboBox<Activity> freeTimeActivityComboBox;
    private ComboBox<Activity> weekendActivityComboBox;

    private ELProcessor elp;
    private TextArea weekInfoText;

    private ComboBox<String> workOutfitComboBox;
    private GameSceneController gameSceneController;

    public NuevaVidaGame() {
        Yaml yaml = new Yaml(new Constructor(Configuration.class));
        configuration = yaml.loadAs(NuevaVidaGame.class.getClass().getResourceAsStream("/config.yml"), Configuration.class);
        for (String file : configuration.getFiles()) {
            log.debug("loading " + file);
            try {
                configuration.merge(yaml.loadAs(NuevaVidaGame.class.getClass().getResourceAsStream("/" + file), Configuration.class));
            } catch (YAMLException e) {
                e.printStackTrace();
            }
        }
        log.debug(configuration.toString());

        elp = new ELProcessor();
        Player player = new Player();
        player.addTrait("BITCHY");
        elp.defineBean("player", player);
        elp.defineBean("configuration", configuration);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Nuevavida");

        weekPlannerScene = createWeekPlanner();
        sceneViewer = createSceneViewer();

        gameData = new GameData(configuration, elp, gameSceneController);
        elp.defineBean("gameData", gameData);

        Scheduler intro = new FullFemaleIntro(gameSceneController);
        intro.setListener(this);

//        viewWeekPlanner(gameData.getWeekStartInfo());

        primaryStage.show();
    }

    private Scene createSceneViewer() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(NuevaVidaGame.class.getClass().getResource("/view/GameSceneViewer.fxml"));

        Parent parent = fxmlLoader.load();
        gameSceneController = fxmlLoader.getController();
        gameSceneController.setMain(this);
        gameSceneController.setConfiguration(configuration);
        gameSceneController.setElp(elp);

        return new Scene(parent);
    }

    private void viewWeekPlanner(WeekStartInfo weekStartInfo) {

        workActivityComboBox.getItems().setAll(weekStartInfo.getWorkActivities());
        workActivityComboBox.getSelectionModel().select(0);

        freeTimeActivityComboBox.getItems().setAll(weekStartInfo.getFreeTimeActivities());
        freeTimeActivityComboBox.getSelectionModel().select(0);

        dateActivityComboBox.getItems().setAll(weekStartInfo.getGoingOutActivities());
        dateActivityComboBox.getSelectionModel().select(0);

        weekendActivityComboBox.getItems().setAll(weekStartInfo.getWeekendActivities());
        weekendActivityComboBox.getSelectionModel().select(0);

        weekInfoText.setText(weekStartInfo.getWeekInfo());

        primaryStage.setScene(weekPlannerScene);
    }

    public String eval(String text) {
        String eval;
        try {
            eval = (String) elp.eval(text);
            log.debug("parsed something");
        } catch (ELException e) {
            eval = text;
            log.debug("parse error, using plain text", e);
        }
        return eval;
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
        workOutfitComboBox = createOutfitComboBox();
        grid.add(workOutfitComboBox, 2, 0);

        freeTimeActivityComboBox = new ComboBox<>();
        freeTimeActivityComboBox.setPrefWidth(150);
        Label freeTimeLabel = new Label("Free Time Activity:");
        freeTimeLabel.setPrefWidth(175);
        freeTimeLabel.setLabelFor(freeTimeActivityComboBox);

        grid.add(freeTimeLabel, 0, 1);
        grid.add(freeTimeActivityComboBox, 1, 1);
        grid.add(createOutfitComboBox(), 2, 1);

        dateActivityComboBox = new ComboBox<>();
        dateActivityComboBox.setPrefWidth(150);
        Label dateLabel = new Label("Weekend Evening Activity:");
        dateLabel.setPrefWidth(175);
        dateLabel.setLabelFor(dateActivityComboBox);

        grid.add(dateLabel, 0, 2);
        grid.add(dateActivityComboBox, 1, 2);
        grid.add(createOutfitComboBox(), 2, 2);

        weekendActivityComboBox = new ComboBox<>();
        weekendActivityComboBox.setPrefWidth(150);
        Label weekendLabel = new Label("Weekend Activity:");
        weekendLabel.setPrefWidth(175);
        weekendLabel.setLabelFor(weekendActivityComboBox);

        grid.add(weekendLabel, 0, 3);
        grid.add(weekendActivityComboBox, 1, 3);
        grid.add(createOutfitComboBox(), 2, 3);

        Button startWeekButton = new Button("Start Week");
        startWeekButton.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        startWeekButton.setOnAction(event -> {
            List<Activity> activities = new ArrayList<>();
            Activity activity = workActivityComboBox.getSelectionModel().getSelectedItem();
//TODO            activity.setOutfit(workOutfitComboBox.getSelectionModel().getSelectedItem());
            activities.add(activity);
            activities.add(freeTimeActivityComboBox.getSelectionModel().getSelectedItem());
            activities.add(dateActivityComboBox.getSelectionModel().getSelectedItem());
            activities.add(weekendActivityComboBox.getSelectionModel().getSelectedItem());
            new WeekRunner(gameData, activities).setListener(this);
        });

        FlowPane flowPane = new FlowPane(Orientation.VERTICAL);
        flowPane.setVgap(10);
        flowPane.setPrefSize(700, 250);
        flowPane.getChildren().add(startWeekButton);
        flowPane.getChildren().add(grid);

        flowPane.setAlignment(Pos.CENTER);

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20));

        borderPane.setTop(flowPane);

        weekInfoText = new TextArea();
        weekInfoText.setEditable(false);
        weekInfoText.setFont(Font.font("Tahoma", 16));
        weekInfoText.setWrapText(true);

        borderPane.setCenter(weekInfoText);

        return new Scene(borderPane, 1000, 710);
    }

    private ComboBox<String> createOutfitComboBox() {
        ComboBox<String> freeTimeOutfitComboBox = new ComboBox<>();
        freeTimeOutfitComboBox.getItems().add("Random outfit");
        freeTimeOutfitComboBox.getSelectionModel().select(0);
        freeTimeOutfitComboBox.setPrefWidth(300);
        return freeTimeOutfitComboBox;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void listen(Result result) {
        log.debug("going to next week!");
        gameData.incrementWeek();
        viewWeekPlanner(gameData.getWeekStartInfo());
    }

    public static void startGame(Player player, int gameLength) {
        //TODO create outfits
        //TODO create NPCs
        //TODO create gameData
        //TODO set money
    }

    public void showSceneViewer() {
        primaryStage.setScene(sceneViewer);
    }

    interface FemaleChargenListener {
        void template(FemaleTemplate template);
    }
}
