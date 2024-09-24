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

/**
 * The CarMain class is responsible for displaying a maze and a car that moves within it.
 * It allows manual car movement using the arrow keys and provides an option to auto-solve the maze.
 */
public class CarMain extends Application {

    private Car car;
    private Image mazeImage;
    private ImageView mazeView;
    private boolean isSolving = false;  // Track if the maze-solving is active
    private static final int STEP_SIZE = 10;  // Define the step size for car movement

    /**
     * Initializes and displays the maze with the car.
     * Supports manual car movement and an auto-solve function for the maze.
     *
     * @param primaryStage the main window for the application
     */
    @Override
    public void start(Stage primaryStage) {
        // Load the maze image
        mazeImage = new Image(getClass().getResourceAsStream("/maze.png"));
        mazeView = new ImageView(mazeImage);

        // Create a Car object and place it initially in the maze
        car = new Car(10, 270, mazeImage);

        // Create a Pane to hold the maze image and car components
        Pane mazePane = new Pane();
        mazePane.getChildren().addAll(mazeView, car.getBody(), car.getRoof(), car.getWheel1(), car.getWheel2(), car.getHeadlight());

        // Button to solve the maze
        Button solveButton = new Button("Solve Maze");
        solveButton.setOnAction(e -> {
            if (!isSolving) {  // Ensure that solving occurs only once
                isSolving = true;
                car.solveMaze();  // Trigger the maze-solving function
                solveButton.setDisable(true);  // Disable the button after starting the solve process
            }
        });

        // Layout to hold the maze and the solve button
        VBox root = new VBox(10, mazePane, solveButton);
        Scene scene = new Scene(root, mazeImage.getWidth(), mazeImage.getHeight() + 40);

        // Handle key events for manual car movement and rotation
        scene.setOnKeyPressed(event -> {
            if (!isSolving) {  // Allow manual control only if maze-solving is not active
                switch (event.getCode()) {
                    case UP:
                        car.move(0, -STEP_SIZE);  // Move up
                        car.rotate(-90);  // Rotate to face upwards
                        break;
                    case DOWN:
                        car.move(0, STEP_SIZE);  // Move down
                        car.rotate(90);  // Rotate to face downwards
                        break;
                    case LEFT:
                        car.move(-STEP_SIZE, 0);  // Move left
                        car.rotate(180);  // Rotate to face left
                        break;
                    case RIGHT:
                        car.move(STEP_SIZE, 0);  // Move right
                        car.rotate(0);  // Rotate to face right
                        break;
                }
            }
            event.consume();
        });

        // Request focus when the user clicks on the root pane
        root.setOnMouseClicked(event -> root.requestFocus());

        // Set the stage title and display the window
        primaryStage.setTitle("Car in Maze");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        // Request focus after showing the stage
        Platform.runLater(() -> root.requestFocus());
    }

    /**
     * Main entry point to launch the CarMain application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}