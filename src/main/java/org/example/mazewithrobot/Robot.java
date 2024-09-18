package org.example.mazewithrobot;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.List;

/**
 * Represents a robot that can navigate through a maze.
 * The robot can move, record its path, and play back a recorded path.
 */
public class Robot {
    /** The visual representation of the robot. */
    private ImageView robotView;

    /** The current X-coordinate of the robot. */
    private double x;

    /** The current Y-coordinate of the robot. */
    private double y;

    /** The image of the maze the robot is navigating. */
    private Image mazeImage;

    /** Handles path recording and retrieval. */
    private PathFinder pathFinder;

    /** Indicates whether the robot is currently recording its path. */
    private boolean isRecording;

    /** Indicates whether the robot is currently playing back a recorded path. */
    private boolean isPlaying;

    /**
     * Constructs a new Robot with the given view and maze image.
     *
     * @param robotView The ImageView representing the robot.
     * @param mazeImage The Image of the maze.
     */
    public Robot(ImageView robotView, Image mazeImage) {
        this.robotView = robotView;
        this.x = robotView.getX();
        this.y = robotView.getY();
        this.mazeImage = mazeImage;
        this.pathFinder = new PathFinder();
        this.isRecording = false;
        this.isPlaying = false;
    }

    /**
     * Moves the robot by the specified delta if the path is available.
     *
     * @param deltaX The change in X-coordinate.
     * @param deltaY The change in Y-coordinate.
     */
    public void move(int deltaX, int deltaY) {
        double newX = x + deltaX;
        double newY = y + deltaY;

        if (isPathAvailable(newX, newY)) {
            x = newX;
            y = newY;
            updateRobotPosition();

            if (isRecording) {
                pathFinder.addPoint(x, y);
            }
        }
    }

    /**
     * Starts recording the robot's path.
     */
    public void startRecording() {
        isRecording = true;
        pathFinder = new PathFinder();
        pathFinder.addPoint(x, y);
    }

    /**
     * Stops recording the robot's path and saves it.
     */
    public void stopRecording() {
        isRecording = false;
        pathFinder.savePath();
    }

    /**
     * Plays back the recorded path if one exists and the robot is not already playing.
     */
    public void playRecordedPath() {
        if (isPlaying || !pathFinder.hasRecordedPath()) return;
        isPlaying = true;

        List<PathFinder.Point> path = pathFinder.getPath();
        Timeline timeline = new Timeline();

        for (int i = 0; i < path.size(); i++) {
            PathFinder.Point point = path.get(i);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(i * 100), e -> {
                x = point.x;
                y = point.y;
                updateRobotPosition();
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.setOnFinished(e -> isPlaying = false);
        timeline.play();
    }

    /**
     * Checks if the given coordinates are a valid path in the maze.
     *
     * @param x The X-coordinate to check.
     * @param y The Y-coordinate to check.
     * @return true if the coordinates represent a valid path, false otherwise.
     */
    private boolean isPathAvailable(double x, double y) {
        PixelReader pixelReader = mazeImage.getPixelReader();

        if (x < 0 || x >= mazeImage.getWidth() || y < 0 || y >= mazeImage.getHeight()) {
            return false;
        }

        Color color = pixelReader.getColor((int) x, (int) y);

        return color.equals(Color.WHITE);
    }

    /**
     * Updates the visual position of the robot.
     */
    private void updateRobotPosition() {
        robotView.setX(x);
        robotView.setY(y);
    }

    /**
     * Gets the current X-coordinate of the robot.
     *
     * @return The current X-coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the current Y-coordinate of the robot.
     *
     * @return The current Y-coordinate.
     */
    public double getY() {
        return y;
    }
}