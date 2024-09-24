package org.example.mazewithrobot;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RobotMain extends Application {

    private Robot robot;
    private Button solveButton;
    private Button carButton;
    private Button maze2Button; // New button for Maze2

    @Override
    public void start(Stage primaryStage) {
        // Load the maze image using a relative path
        Image mazeImage = new Image(getClass().getResourceAsStream("/maze.png"));
        ImageView mazeView = new ImageView(mazeImage);

        // Load the robot image using a relative path
        Image robotImage = new Image(getClass().getResourceAsStream("/robot.png"));
        ImageView robotView = new ImageView(robotImage);

        // Create a Pane to hold the maze and robot images
        Pane mazePane = new Pane();
        mazePane.getChildren().addAll(mazeView, robotView);

        // Set the initial position of the robot
        robotView.setX(10);
        robotView.setY(260);

        // Create a Robot instance and pass the robotView and mazeImage
        robot = new Robot(robotView, mazeImage);

        // Create button for solving the maze
        solveButton = new Button("Solve Maze");
        solveButton.setOnAction(e -> {
            robot.solveMaze();
            solveButton.setDisable(true); // Disable button while solving
        });

        // Create button to switch to the CarMain class
        carButton = new Button("Car");
        carButton.setOnAction(e -> {
            CarMain carMain = new CarMain();
            Stage carStage = new Stage();
            try {
                carMain.start(carStage); // Open CarMain in a new window (Stage)
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Create button to switch to Maze2Main class
        maze2Button = new Button("Maze2");
        maze2Button.setOnAction(e -> {
            Maze2Main maze2Main = new Maze2Main();
            Stage maze2Stage = new Stage();
            try {
                maze2Main.start(maze2Stage); // Open Maze2Main in a new window (Stage)
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Create an HBox to hold the buttons
        HBox buttonBox = new HBox(10, solveButton, carButton, maze2Button);

        // Create a VBox to hold the maze pane and button box
        VBox root = new VBox(10, mazePane, buttonBox);

        // Create a scene with the root pane and set its dimensions based on the maze image size
        Scene scene = new Scene(root, mazeImage.getWidth() * 1.3, (mazeImage.getHeight() + 60) * 1.2);

        // Set a key event handler for movement (optional if you want manual control)
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:    robot.move(0, -10); break;
                case DOWN:  robot.move(0, 10); break;
                case LEFT:  robot.move(-10, 0); break;
                case RIGHT: robot.move(10, 0); break;
            }
            event.consume();
        });

        // Request focus for the root VBox when clicked
        root.setOnMouseClicked(event -> root.requestFocus());

        // Set the title of the primary stage
        primaryStage.setTitle("Maze with Robot");

        // Set the scene for the primary stage
        primaryStage.setScene(scene);

        // Show the primary stage
        primaryStage.show();
        primaryStage.setResizable(false);

        // Request focus for the root VBox after showing the stage
        Platform.runLater(() -> root.requestFocus());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
