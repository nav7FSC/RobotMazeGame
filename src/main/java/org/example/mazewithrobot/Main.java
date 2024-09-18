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
 * The main class for the Maze with Robot application.
 * This class sets up the JavaFX application, creates the user interface,
 * and manages the interaction between the UI and the Robot.
 */
public class Main extends Application {

    /** The Robot instance that navigates the maze. */
    private Robot robot;

    /** Button to start recording the robot's path. */
    private Button recordButton;

    /** Button to stop recording the robot's path. */
    private Button stopButton;

    /** Button to play back the recorded path. */
    private Button playButton;

    /**
     * The main entry point for the JavaFX application.
     * This method sets up the entire user interface and initializes the robot.
     *
     * @param primaryStage The primary stage for this application.
     */
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
        robotView.setX(50);
        robotView.setY(50);

        // Create a Robot instance and pass the robotView and mazeImage
        robot = new Robot(robotView, mazeImage);

        // Create buttons for recording, stopping, and playing
        recordButton = new Button("Record");
        stopButton = new Button("Stop");
        playButton = new Button("Play");

        // Set actions for the buttons
        recordButton.setOnAction(e -> {
            robot.startRecording();
            updateButtonStates(false, true, false);
        });
        stopButton.setOnAction(e -> {
            robot.stopRecording();
            updateButtonStates(false, false, true);
        });
        playButton.setOnAction(e -> robot.playRecordedPath());

        // Create an HBox to hold the buttons
        HBox buttonBox = new HBox(10, recordButton, stopButton, playButton);

        // Create a VBox to hold the maze pane and button box
        VBox root = new VBox(10, mazePane, buttonBox);

        // Create a scene with the root pane and set its dimensions based on the maze image size
        Scene scene = new Scene(root, mazeImage.getWidth(), mazeImage.getHeight() + 40);

        // Set a key event handler for movement
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:    robot.move(0, -10); break;
                case DOWN:  robot.move(0, 10); break;
                case LEFT:  robot.move(-10, 0); break;
                case RIGHT: robot.move(10, 0); break;
            }
            event.consume(); // Consume the event to prevent it from being handled by other nodes
        });

        // Request focus for the root VBox when clicked
        root.setOnMouseClicked(event -> root.requestFocus());

        // Set the title of the primary stage
        primaryStage.setTitle("Maze with Robot");

        // Set the scene for the primary stage
        primaryStage.setScene(scene);

        // Show the primary stage
        primaryStage.show();

        // Request focus for the root VBox after showing the stage
        Platform.runLater(() -> {
            root.requestFocus();
            updateButtonStates(true, false, false);
        });
    }

    /**
     * Updates the enabled/disabled states of the buttons.
     *
     * @param recordEnabled True if the record button should be enabled, false otherwise.
     * @param stopEnabled True if the stop button should be enabled, false otherwise.
     * @param playEnabled True if the play button should be enabled, false otherwise.
     */
    private void updateButtonStates(boolean recordEnabled, boolean stopEnabled, boolean playEnabled) {
        recordButton.setDisable(!recordEnabled);
        stopButton.setDisable(!stopEnabled);
        playButton.setDisable(!playEnabled);
    }

    /**
     * The main method is the entry point of the Java application.
     * It launches the JavaFX application.
     *
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}