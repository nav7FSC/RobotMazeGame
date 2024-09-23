package org.example.mazewithrobot;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;


public class CarMain extends Application {


    private Car car;
    private Image mazeImage;
    private ImageView mazeView;
    private boolean isSolving = false;
    private static final int STEP_SIZE = 10;


    @Override
    public void start(Stage primaryStage) {
        mazeImage = new Image(getClass().getResourceAsStream("/maze.png"));
        mazeView = new ImageView(mazeImage);


        car = new Car(10, 260, mazeImage);


        Pane mazePane = new Pane();
        mazePane.getChildren().addAll(mazeView, car.getBody(), car.getRoof(), car.getWheel1(), car.getWheel2(), car.getHeadlight());


        Button solveButton = new Button("Solve Maze");
        solveButton.setOnAction(e -> {
            if (!isSolving) {
                isSolving = true;
                car.solveMaze();
                solveButton.setDisable(true);
            }
        });


        VBox root = new VBox(10, mazePane, solveButton);
        Scene scene = new Scene(root, mazeImage.getWidth(), mazeImage.getHeight() + 40);


        scene.setOnKeyPressed(event -> {
            if (!isSolving) {
                switch (event.getCode()) {
                    case UP:    car.move(0, -STEP_SIZE); break;
                    case DOWN:  car.move(0, STEP_SIZE); break;
                    case LEFT:  car.move(-STEP_SIZE, 0); break;
                    case RIGHT: car.move(STEP_SIZE, 0); break;
                }
            }
            event.consume();
        });


        root.setOnMouseClicked(event -> root.requestFocus());


        primaryStage.setTitle("Car in Maze");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        Platform.runLater(() -> root.requestFocus());
    }


    public static void main(String[] args) {
        launch(args);
    }
}