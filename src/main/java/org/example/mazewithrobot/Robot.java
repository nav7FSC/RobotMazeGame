package org.example.mazewithrobot;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Robot {
    private ImageView robotView; // Reference to the robot's ImageView for visual representation
    private double x; // Current X position of the robot in the maze
    private double y; // Current Y position of the robot in the maze
    private Image mazeImage; // Store reference to the maze image for path validation

    // Constructor that initializes the robot's ImageView and maze image reference
    public Robot(ImageView robotView, Image mazeImage) {
        this.robotView = robotView; // Assign the provided ImageView to the robotView variable
        this.x = robotView.getX(); // Initialize X position from the ImageView's current position
        this.y = robotView.getY(); // Initialize Y position from the ImageView's current position
        this.mazeImage = mazeImage; // Initialize maze image reference for path checking
    }

    // Method to move the robot by specified delta values (change in X and Y)
    public void move(int deltaX, int deltaY) {
        double newX = x + deltaX; // Calculate new X position based on movement input
        double newY = y + deltaY; // Calculate new Y position based on movement input

        // Check if the new position is a valid path in the maze before updating
        if (isPathAvailable(newX, newY)) {
            x = newX; // Update X position if valid
            y = newY; // Update Y position if valid
            updateRobotPosition(); // Update visual position of the robot in the scene
        }
    }

    // Method to check if the specified (x, y) coordinates are a valid path in the maze
    private boolean isPathAvailable(double x, double y) {
        PixelReader pixelReader = mazeImage.getPixelReader(); // Get pixel reader from maze image

        // Check if coordinates are within bounds of the maze image dimensions
        if (x < 0 || x >= mazeImage.getWidth() || y < 0 || y >= mazeImage.getHeight()) {
            return false; // Return false if out of bounds, indicating an invalid path
        }

        Color color = pixelReader.getColor((int)x, (int)y); // Get color of the pixel at specified coordinates

        // Check if color indicates a valid path (e.g., white)
        return color.equals(Color.WHITE); // Return true if it's a valid path, false otherwise
    }

    // Method to update the robot's position in its ImageView based on current coordinates
    private void updateRobotPosition() {
        robotView.setX(x); // Set X position in ImageView based on current coordinates
        robotView.setY(y); // Set Y position in ImageView based on current coordinates
    }

    // Getter method for current X position
    public double getX() {
        return x;
    }

    // Getter method for current Y position
    public double getY() {
        return y;
    }
}