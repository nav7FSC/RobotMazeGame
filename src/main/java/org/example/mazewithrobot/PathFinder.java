package org.example.mazewithrobot;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The PathFinder class manages the recording, saving, and loading of paths
 * for the robot in the maze.
 */
public class PathFinder {
    /** List to store the points of the path. */
    private List<Point> path;

    /** The file name used to save and load the path. */
    private static final String PATH_FILE = "correct_path.ser";

    /**
     * Constructs a new PathFinder and loads any existing path.
     */
    public PathFinder() {
        path = new ArrayList<>();
        loadPath();
    }

    /**
     * Adds a new point to the path.
     *
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     */
    public void addPoint(double x, double y) {
        path.add(new Point(x, y));
    }

    /**
     * Returns the current path.
     *
     * @return The list of points representing the path.
     */
    public List<Point> getPath() {
        return path;
    }

    /**
     * Saves the current path to a file.
     */
    public void savePath() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PATH_FILE))) {
            oos.writeObject(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a previously saved path from a file.
     * If the file doesn't exist, the current path remains empty.
     */
    @SuppressWarnings("unchecked")
    private void loadPath() {
        File file = new File(PATH_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                path = (List<Point>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks if there is a recorded path.
     *
     * @return true if there is a recorded path, false otherwise.
     */
    public boolean hasRecordedPath() {
        return !path.isEmpty();
    }

    /**
     * Represents a point in the path with x and y coordinates.
     */
    public static class Point implements Serializable {
        /** The x-coordinate of the point. */
        public double x;
        /** The y-coordinate of the point. */
        public double y;

        /**
         * Constructs a new Point with the given coordinates.
         *
         * @param x The x-coordinate.
         * @param y The y-coordinate.
         */
        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}