package org.example.mazewithrobot;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.*;

/**
 * Represents a robot that can navigate and solve a maze.
 * This class handles the robot's movement, maze solving algorithm, and interaction with the maze image.
 */
public class Robot {
    /** The ImageView representing the robot in the UI. */
    private ImageView robotView;

    /** The current x-coordinate of the robot. */
    private double x;

    /** The current y-coordinate of the robot. */
    private double y;

    /** The Image object of the maze. */
    private Image mazeImage;

    /** Flag indicating whether the robot is currently solving the maze. */
    private boolean isSolving;

    /** The size of each step the robot takes. */
    private static final int STEP_SIZE = 10;

    /** The speed at which the robot solves the maze (in milliseconds). */
    private static final int SOLVE_SPEED = 100;

    /** The size of the robot in pixels. */
    private static final int ROBOT_SIZE = 20;

    /** The minimum width of an opening in the maze. */
    private static final int MIN_OPENING_WIDTH = 5;

    /** The range within which the robot is considered to have reached the exit. */
    private static final int EXIT_RANGE = 35;

    /** The stack representing the path taken by the robot. */
    private Stack<Point> path;

    /** The set of points visited by the robot. */
    private Set<Point> visited;

    /** The color of the path in the maze. */
    private Color pathColor;

    /** The exit point of the maze. */
    private Point exitPoint;

    /**
     * Constructs a new Robot instance.
     *
     * @param robotView The ImageView representing the robot in the UI.
     * @param mazeImage The Image object of the maze.
     */
    public Robot(ImageView robotView, Image mazeImage) {
        this.robotView = robotView;
        this.x = robotView.getX();
        this.y = robotView.getY();
        this.mazeImage = mazeImage;
        this.isSolving = false;
        this.path = new Stack<>();
        this.visited = new HashSet<>();
        this.pathColor = getPathColor();
        findExit();
    }

    /**
     * Finds the exit point of the maze.
     * This method scans the borders of the maze image to locate openings,
     * determines the entrance (closest to the robot's starting position),
     * and sets the exit point (furthest from the robot's starting position).
     */
    private void findExit() {
        PixelReader pixelReader = mazeImage.getPixelReader();
        int width = (int) mazeImage.getWidth();
        int height = (int) mazeImage.getHeight();
        List<Point> openings = new ArrayList<>();

        // Check all borders for openings
        openings.addAll(findOpeningsOnBorder(pixelReader, 0, width, 0, true, "Top"));
        openings.addAll(findOpeningsOnBorder(pixelReader, 0, width, height - 1, true, "Bottom"));
        openings.addAll(findOpeningsOnBorder(pixelReader, 0, height, 0, false, "Left"));
        openings.addAll(findOpeningsOnBorder(pixelReader, 0, height, width - 1, false, "Right"));

        System.out.println("Total openings found: " + openings.size());
        for (Point opening : openings) {
            System.out.println("Opening: " + opening);
        }

        if (openings.size() < 2) {
            throw new IllegalStateException("Maze must have at least two openings, found: " + openings.size());
        }

        // Determine entrance and exit
        Point robotPosition = new Point(x, y);
        Point entrance = findClosestPoint(robotPosition, openings);
        openings.remove(entrance);
        System.out.println("Entrance (closest to robot): " + entrance);
        exitPoint = findFurthestPoint(robotPosition, openings);
        System.out.println("Exit point (furthest from robot): " + exitPoint);
    }

    /**
     * Finds openings on a specified border of the maze.
     *
     * @param pixelReader The PixelReader for the maze image.
     * @param start The starting coordinate for the search.
     * @param end The ending coordinate for the search.
     * @param fixed The fixed coordinate (for the non-searching dimension).
     * @param isHorizontal True if searching a horizontal border, false for vertical.
     * @param borderName The name of the border being searched (for logging).
     * @return A list of Points representing openings on the border.
     */
    private List<Point> findOpeningsOnBorder(PixelReader pixelReader, int start, int end, int fixed, boolean isHorizontal, String borderName) {
        List<Point> openings = new ArrayList<>();
        int openingStart = -1;
        int openingWidth = 0;

        for (int i = start; i < end; i++) {
            int x = isHorizontal ? i : fixed;
            int y = isHorizontal ? fixed : i;
            Color color = pixelReader.getColor(x, y);

            if (color.equals(pathColor)) {
                if (openingStart == -1) {
                    openingStart = i;
                }
                openingWidth++;
            } else {
                if (openingWidth >= MIN_OPENING_WIDTH && isConnectedToPath(pixelReader, openingStart, fixed, isHorizontal)) {
                    int openingMiddle = openingStart + openingWidth / 2;
                    Point opening = isHorizontal ? new Point(openingMiddle, y) : new Point(x, openingMiddle);
                    openings.add(opening);
                    System.out.println("Opening found on " + borderName + " border: " + opening);
                }
                openingStart = -1;
                openingWidth = 0;
            }
        }

        // Check if an opening ends at the border
        if (openingWidth >= MIN_OPENING_WIDTH && isConnectedToPath(pixelReader, openingStart, fixed, isHorizontal)) {
            int openingMiddle = openingStart + openingWidth / 2;
            Point opening = isHorizontal ? new Point(openingMiddle, fixed) : new Point(fixed, openingMiddle);
            openings.add(opening);
            System.out.println("Opening found at end of " + borderName + " border: " + opening);
        }

        return openings;
    }

    /**
     * Checks if a potential opening is connected to the maze path.
     *
     * @param pixelReader The PixelReader for the maze image.
     * @param start The starting coordinate of the potential opening.
     * @param fixed The fixed coordinate (for the non-searching dimension).
     * @param isHorizontal True if checking a horizontal opening, false for vertical.
     * @return True if the opening is connected to the maze path, false otherwise.
     */
    private boolean isConnectedToPath(PixelReader pixelReader, int start, int fixed, boolean isHorizontal) {
        int checkDepth = 5; // Check 5 pixels deep into the maze
        for (int i = 1; i <= checkDepth; i++) {
            int x = isHorizontal ? start : fixed + (fixed == 0 ? i : -i);
            int y = isHorizontal ? fixed + (fixed == 0 ? i : -i) : start;
            if (x < 0 || x >= mazeImage.getWidth() || y < 0 || y >= mazeImage.getHeight()) {
                return false;
            }
            if (pixelReader.getColor(x, y).equals(pathColor)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the point closest to a reference point from a list of points.
     *
     * @param reference The reference point.
     * @param points The list of points to search.
     * @return The point closest to the reference point.
     */
    private Point findClosestPoint(Point reference, List<Point> points) {
        return points.stream()
                .min(Comparator.comparingDouble(p -> distance(reference, p)))
                .orElseThrow(() -> new IllegalStateException("No points to compare"));
    }

    /**
     * Finds the point furthest from a reference point from a list of points.
     *
     * @param reference The reference point.
     * @param points The list of points to search.
     * @return The point furthest from the reference point.
     */
    private Point findFurthestPoint(Point reference, List<Point> points) {
        return points.stream()
                .max(Comparator.comparingDouble(p -> distance(reference, p)))
                .orElseThrow(() -> new IllegalStateException("No points to compare"));
    }

    /**
     * Calculates the Euclidean distance between two points.
     *
     * @param p1 The first point.
     * @param p2 The second point.
     * @return The distance between the two points.
     */
    private double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    /**
     * Moves the robot by the specified delta values if the move is valid.
     *
     * @param deltaX The change in x-coordinate.
     * @param deltaY The change in y-coordinate.
     */
    public void move(int deltaX, int deltaY) {
        double newX = x + deltaX;
        double newY = y + deltaY;
        if (isValidMove(newX, newY)) {
            x = newX;
            y = newY;
            updateRobotPosition();
        }
    }

    /**
     * Initiates the maze-solving process.
     * This method sets up a timeline to step through the maze at regular intervals.
     */
    public void solveMaze() {
        if (isSolving) return;
        isSolving = true;
        path.push(new Point(x, y));
        visited.add(new Point(x, y));
        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(SOLVE_SPEED), event -> {
            if (!isAtExit()) {
                step();
            } else {
                isSolving = false;
                timeline.stop();
                System.out.println("Exit reached at (" + x + ", " + y + ")!");
                System.out.println("Distance from exact exit: " +
                        Math.sqrt(Math.pow(x - exitPoint.x, 2) + Math.pow(y - exitPoint.y, 2)));
            }
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Performs a single step in the maze-solving process.
     * This method is called repeatedly by the timeline until the exit is reached.
     */
    private void step() {
        if (path.isEmpty()) {
            isSolving = false;
            return;
        }
        Point current = path.peek();
        List<Point> neighbors = getUnvisitedNeighbors(current);
        if (!neighbors.isEmpty()) {
            Point next = neighbors.get(0);
            path.push(next);
            visited.add(next);
            moveTo(next);
        } else {
            path.pop();
            if (!path.isEmpty()) {
                moveTo(path.peek());
            }
        }
    }

    /**
     * Gets the unvisited neighboring points of a given point.
     *
     * @param p The point to check neighbors for.
     * @return A list of unvisited neighboring points.
     */
    private List<Point> getUnvisitedNeighbors(Point p) {
        List<Point> neighbors = new ArrayList<>();
        int[][] directions = {{0, -STEP_SIZE}, {STEP_SIZE, 0}, {0, STEP_SIZE}, {-STEP_SIZE, 0}};
        for (int[] dir : directions) {
            Point neighbor = new Point(p.x + dir[0], p.y + dir[1]);
            if (isValidMove(neighbor.x, neighbor.y) && !visited.contains(neighbor)) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    /**
     * Moves the robot to a specific point.
     *
     * @param p The point to move the robot to.
     */
    private void moveTo(Point p) {
        x = p.x;
        y = p.y;
        updateRobotPosition();
    }

    /**
     * Checks if the robot is at the exit of the maze.
     *
     * @return True if the robot is within the exit range, false otherwise.
     */
    private boolean isAtExit() {
        return Math.abs(x - exitPoint.x) < EXIT_RANGE && Math.abs(y - exitPoint.y) < EXIT_RANGE;
    }

    /**
     * Checks if a move to the specified coordinates is valid.
     *
     * @param newX The new x-coordinate.
     * @param newY The new y-coordinate.
     * @return True if the move is valid, false otherwise.
     */
    private boolean isValidMove(double newX, double newY) {
        if (newX < 0 || newX > mazeImage.getWidth() - ROBOT_SIZE ||
                newY < 0 || newY > mazeImage.getHeight() - ROBOT_SIZE) {
            return false;
        }
        return isPathAvailable(newX, newY) &&
                isPathAvailable(newX + ROBOT_SIZE, newY) &&
                isPathAvailable(newX, newY + ROBOT_SIZE) &&
                isPathAvailable(newX + ROBOT_SIZE, newY + ROBOT_SIZE);
    }

    /**
     * Checks if a path is available at the specified coordinates.
     *
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @return True if a path is available, false otherwise.
     */
    private boolean isPathAvailable(double x, double y) {
        PixelReader pixelReader = mazeImage.getPixelReader();
        if (x < 0 || x >= mazeImage.getWidth() || y < 0 || y >= mazeImage.getHeight()) {
            return false;
        }
        Color color = pixelReader.getColor((int) x, (int) y);
        return color.equals(pathColor);
    }

    /**
     * Gets the path color from the robot's starting position.
     *
     * @return The Color object representing the path color.
     */
    private Color getPathColor() {
        PixelReader pixelReader = mazeImage.getPixelReader();
        return pixelReader.getColor((int) x, (int) y);
    }

    /**
     * Updates the robot's position in the UI.
     */
    private void updateRobotPosition() {
        robotView.setX(x);
        robotView.setY(y);
    }
    /**
     * Gets the current x-coordinate of the robot.
     *
     * @return The current x-coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the current y-coordinate of the robot.
     *
     * @return The current y-coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Represents a point in 2D space.
     */
    private static class Point {
        /** The x-coordinate of the point. */
        double x;
        /** The y-coordinate of the point. */
        double y;

        /**
         * Constructs a new Point.
         *
         * @param x The x-coordinate.
         * @param y The y-coordinate.
         */
        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Checks if this Point is equal to another object.
         *
         * @param o The object to compare with.
         * @return true if the objects are equal, false otherwise.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;
        }

        /**
         * Generates a hash code for this Point.
         *
         * @return The hash code.
         */
        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        /**
         * Returns a string representation of this Point.
         *
         * @return A string representation of the Point.
         */
        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}