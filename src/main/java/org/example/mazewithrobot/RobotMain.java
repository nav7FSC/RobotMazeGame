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

/**
 * Main class for the Maze with Robot application.
 * Handles the primary stage, loading the maze and robot images, and managing
 * interactions between buttons and robot movement.
 */
public class RobotMain extends Application {

    private Robot robot;
    private Button solveButton;
    private Button carButton;
    private Button maze2Button;

    /**
     * Initializes and displays the primary stage for the application.
     * Loads the maze and robot images, sets up button actions, and enables
     * movement control for the robot.
     *
     * @param primaryStage the main window of the application
     */
    @Override
    public void start(Stage primaryStage) {
        // Load the maze image and display it
        Image mazeImage = new Image(getClass().getResourceAsStream("/maze.png"));
        ImageView mazeView = new ImageView(mazeImage);

        // Load the robot image and display it
        Image robotImage = new Image(getClass().getResourceAsStream("/robot.png"));
        ImageView robotView = new ImageView(robotImage);

        // Create a pane to hold the maze and robot images
        Pane mazePane = new Pane();
        mazePane.getChildren().addAll(mazeView, robotView);

        // Set the robot's initial position on the maze
        robotView.setX(10);
        robotView.setY(260);

        // Initialize the robot and associate it with the maze
        robot = new Robot(robotView, mazeImage);

        // Button to trigger maze-solving
        solveButton = new Button("Solve Maze");
        solveButton.setOnAction(e -> {
            robot.solveMaze();
            solveButton.setDisable(true); // Disable button after maze-solving starts
        });

        // Button to switch to the car simulation
        carButton = new Button("Car");
        carButton.setOnAction(e -> {
            CarMain carMain = new CarMain();
            Stage carStage = new Stage();
            try {
                carMain.start(carStage); // Open CarMain in a new window
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Button to switch to the second maze
        maze2Button = new Button("Maze2");
        maze2Button.setOnAction(e -> {
            Maze2Main maze2Main = new Maze2Main();
            Stage maze2Stage = new Stage();
            try {
                maze2Main.start(maze2Stage); // Open Maze2Main in a new window
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Layout for buttons
        HBox buttonBox = new HBox(10, solveButton, carButton, maze2Button);

        // Layout for the main view, including the maze and buttons
        VBox root = new VBox(10, mazePane, buttonBox);

        // Create the scene and scale its dimensions by 20%
        Scene scene = new Scene(root, mazeImage.getWidth() * 1.3, (mazeImage.getHeight() + 60) * 1.2);

        // Handle keyboard input for robot movement
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:    robot.move(0, -10); break;
                case DOWN:  robot.move(0, 10); break;
                case LEFT:  robot.move(-10, 0); break;
                case RIGHT: robot.move(10, 0); break;
            }
            event.consume();
        });

        // Request focus on the scene when mouse clicked
        root.setOnMouseClicked(event -> root.requestFocus());

        // Set the title and display the primary stage
        primaryStage.setTitle("Maze with Robot");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);

        // Request focus for the root element
        Platform.runLater(() -> root.requestFocus());
    }

    /**
     * Main entry point for launching the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}