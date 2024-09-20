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

    @Override
    public void start(Stage primaryStage) {
        // Load the maze image
        mazeImage = new Image(getClass().getResourceAsStream("/maze.png"));
        mazeView = new ImageView(mazeImage);

        // Create the Car instance
        car = new Car(10, 260); // Set initial position

        // Create a Pane to hold the maze and car images
        Pane mazePane = new Pane();
        mazePane.getChildren().addAll(mazeView, car.getBody(), car.getRoof(), car.getWheel1(), car.getWheel2(), car.getHeadlight());

        // Button to start solving the maze
        Button solveButton = new Button("Solve Maze");
        solveButton.setOnAction(e -> {
            if (!isSolving) {
                isSolving = true;
                solveMaze(); // Start solving the maze
                solveButton.setDisable(true);
            }
        });

        // Set up the scene
        VBox root = new VBox(10, mazePane, solveButton);
        Scene scene = new Scene(root, mazeImage.getWidth(), mazeImage.getHeight() + 40);

        // Key event handler for car movement and rotation
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:    car.move(0, -10); car.rotate(-90); break;
                case DOWN:  car.move(0, 10); car.rotate(90); break;
                case LEFT:  car.move(-10, 0); car.rotate(180); break;
                case RIGHT: car.move(10, 0); car.rotate(0); break;
            }
            event.consume(); // Consume the event
        });

        // Request focus for the root VBox when clicked
        root.setOnMouseClicked(event -> root.requestFocus());

        // Set up the stage
        primaryStage.setTitle("Car in Maze");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        Platform.runLater(() -> root.requestFocus());
    }

    private void solveMaze() {
        // Implement the logic for the car to navigate the maze
        // This would involve using similar logic to what you have in the Robot class
        // You'll need to create an automatic movement method based on pathfinding

        // Example: Call move methods based on the path found using pixel values
        // Replace this with actual pathfinding logic
        new Thread(() -> {
            while (isSolving) {
                // Replace with your logic to move the car towards the exit
                // This is a placeholder to simulate movement
                Platform.runLater(() -> {
                    // Example of moving the car
                    car.move(10, 0); // Move right
                    // Check for exit condition and set isSolving to false when done
                });

                try {
                    Thread.sleep(100); // Adjust speed as necessary
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}