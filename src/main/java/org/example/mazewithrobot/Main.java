package org.example.mazewithrobot;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Load the maze image using a relative path
        Image mazeImage = new Image(getClass().getResourceAsStream("/maze.png"));
        ImageView mazeView = new ImageView(mazeImage);

        // Load the robot image using a relative path
        Image robotImage = new Image(getClass().getResourceAsStream("/robot.png"));
        ImageView robotView = new ImageView(robotImage);

        // Create a Pane to hold the maze and robot images
        Pane root = new Pane();
        root.getChildren().addAll(mazeView, robotView); // Add both images to the pane

        // Set the initial position of the robot
        robotView.setX(50); // Set starting X position
        robotView.setY(50); // Set starting Y position

        // Create a scene with the root pane and set its dimensions based on the maze image size
        Scene scene = new Scene(root, mazeImage.getWidth(), mazeImage.getHeight());

        // Create a Robot instance and pass the robotView and mazeImage
        Robot robot = new Robot(robotView, mazeImage);

        // Set a key event handler for movement
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:    robot.move(0, -10); break; // Move the robot up by 10 pixels
                case DOWN:  robot.move(0, 10); break; // Move the robot down by 10 pixels
                case LEFT:  robot.move(-10, 0); break; // Move the robot left by 10 pixels
                case RIGHT: robot.move(10, 0); break; // Move the robot right by 10 pixels
            }
        });

        // Set the title of the primary stage
        primaryStage.setTitle("Maze with Robot");

        // Set the scene for the primary stage
        primaryStage.setScene(scene);

        // Show the primary stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}