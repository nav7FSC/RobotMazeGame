/**
 * Package declaration for the maze with robot application.
 */
package org.example.mazewithrobot;

// Import statements for required JavaFX classes
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
 * Extends JavaFX Application class to create the GUI.
 */
public class Main extends Application {

    /** The robot object that will navigate the maze. */
    private Robot robot;

    /** Button to trigger the maze-solving algorithm. */
    private Button solveButton;

    /**
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
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
        robotView.setX(10);
        robotView.setY(260);

        // Create a Robot instance and pass the robotView and mazeImage
        robot = new Robot(robotView, mazeImage);

        // Create button for solving the maze
        solveButton = new Button("Solve Maze");

        // Set action for the solve button
        solveButton.setOnAction(e -> {
            robot.solveMaze(); // Start solving the maze when button is clicked
            solveButton.setDisable(true); // Disable button while solving
        });

        // Create an HBox to hold the button
        HBox buttonBox = new HBox(10, solveButton);

        // Create a VBox to hold the maze pane and button box
        VBox root = new VBox(10, mazePane, buttonBox);

        // Create a scene with the root pane and set its dimensions based on the maze image size
        Scene scene = new Scene(root, mazeImage.getWidth(), mazeImage.getHeight() + 40);

        // Set a key event handler for movement (optional if you want manual control)
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
        primaryStage.setResizable(false);

        // Request focus for the root VBox after showing the stage
        Platform.runLater(() -> root.requestFocus());
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}