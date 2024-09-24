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
 * The Maze2Main class handles the second maze where the car moves within the maze.
 * It supports manual car movement and auto-solving of the maze.
 */
public class Maze2Main extends Application {

    private Car car;
    private Image mazeImage;
    private ImageView mazeView;
    private boolean isSolving = false;  // Flag to check if the maze-solving is active
    private static final int STEP_SIZE = 10;  // Step size for car movement

    /**
     * Initializes and displays the second maze with a car object.
     * Supports manual car movement and a "Solve Maze" button to automatically
     * solve the maze.
     *
     * @param primaryStage the main window of the application
     */
    @Override
    public void start(Stage primaryStage) {
        // Load the second maze image
        mazeImage = new Image(getClass().getResourceAsStream("/maze2.png"));
        mazeView = new ImageView(mazeImage);

        // Create a Car object and place it initially in the maze
        car = new Car(10, 260, mazeImage);

        // Create a Pane to hold the maze image and car components
        Pane mazePane = new Pane();
        mazePane.getChildren().addAll(mazeView, car.getBody(), car.getRoof(), car.getWheel1(), car.getWheel2(), car.getHeadlight());

        // Button to solve the maze
        Button solveButton = new Button("Solve Maze");
        solveButton.setOnAction(e -> {
            if (!isSolving) {
                isSolving = true;
                car.solveMaze();  // Auto-solve the maze
                solveButton.setDisable(true);  // Disable the button after starting solving
            }
        });

        // Layout to hold the maze and solve button
        VBox root = new VBox(10, mazePane, solveButton);
        Scene scene = new Scene(root, mazeImage.getWidth() * 1.2, (mazeImage.getHeight() + 40) * 1.2);

        // Handle key events for manual car movement
        scene.setOnKeyPressed(event -> {
            if (!isSolving) {  // Allow manual control only if maze-solving is not active
                switch (event.getCode()) {
                    case UP:    car.move(0, -STEP_SIZE); break;
                    case DOWN:  car.move(0, STEP_SIZE); break;
                    case LEFT:  car.move(-STEP_SIZE, 0); break;
                    case RIGHT: car.move(STEP_SIZE, 0); break;
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
     * Main entry point to launch the Maze2Main application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}