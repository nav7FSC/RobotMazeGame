package org.example.mazewithrobot;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.*;

public class Car {
    private Rectangle body;
    private Rectangle roof;
    private Circle wheel1, wheel2;
    private Rectangle headlight;
    private double x, y;
    private double width, height;
    private static final double SCALE = 0.20;
    private static final int STEP_SIZE = 10;
    private static final int SOLVE_SPEED = 100;
    private static final int CAR_SIZE = 20;
    private static final int MIN_OPENING_WIDTH = 5;
    private static final int EXIT_RANGE = 35;

    private Image mazeImage;
    private Color pathColor;
    private Stack<Point> path;
    private Set<Point> visited;
    private Point exitPoint;
    private boolean isSolving;

    public Car(double x, double y, Image mazeImage) {
        this.x = x;
        this.y = y;
        this.width = 100 * SCALE;
        this.height = 40 * SCALE;
        this.mazeImage = mazeImage;
        initializeCarParts();
        this.pathColor = getPathColor();
        this.path = new Stack<>();
        this.visited = new HashSet<>();
        findExit();
    }

    private void initializeCarParts() {
        body = new Rectangle(x, y, width, height);
        body.setFill(Color.DARKBLUE);
        roof = new Rectangle(x + 20 * SCALE, y - 20 * SCALE, 60 * SCALE, 20 * SCALE);
        roof.setFill(Color.BLUE);
        wheel1 = new Circle(x + 20 * SCALE, y + 40 * SCALE, 10 * SCALE);
        wheel1.setFill(Color.BLACK);
        wheel2 = new Circle(x + 80 * SCALE, y + 40 * SCALE, 10 * SCALE);
        wheel2.setFill(Color.BLACK);
        headlight = new Rectangle(x + 85 * SCALE, y + 10 * SCALE, 10 * SCALE, 5 * SCALE);
        headlight.setFill(Color.YELLOW);
    }

    public void move(int deltaX, int deltaY) {
        double newX = x + deltaX;
        double newY = y + deltaY;
        if (isValidMove(newX, newY)) {
            x = newX;
            y = newY;
            updateCarPosition();
        }
    }

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

    private void moveTo(Point p) {
        x = p.x;
        y = p.y;
        updateCarPosition();
    }

    private boolean isAtExit() {
        return Math.abs(x - exitPoint.x) < EXIT_RANGE && Math.abs(y - exitPoint.y) < EXIT_RANGE;
    }

    private boolean isValidMove(double newX, double newY) {
        if (newX < 0 || newX > mazeImage.getWidth() - CAR_SIZE ||
                newY < 0 || newY > mazeImage.getHeight() - CAR_SIZE) {
            return false;
        }
        return isPathAvailable(newX, newY) &&
                isPathAvailable(newX + CAR_SIZE, newY) &&
                isPathAvailable(newX, newY + CAR_SIZE) &&
                isPathAvailable(newX + CAR_SIZE, newY + CAR_SIZE);
    }

    private boolean isPathAvailable(double x, double y) {
        PixelReader pixelReader = mazeImage.getPixelReader();
        if (x < 0 || x >= mazeImage.getWidth() || y < 0 || y >= mazeImage.getHeight()) {
            return false;
        }
        Color color = pixelReader.getColor((int) x, (int) y);
        return color.equals(pathColor);
    }

    private Color getPathColor() {
        PixelReader pixelReader = mazeImage.getPixelReader();
        return pixelReader.getColor((int) x, (int) y);
    }

    private void updateCarPosition() {
        body.setX(x);
        body.setY(y);
        roof.setX(x + 20 * SCALE);
        roof.setY(y - 20 * SCALE);
        wheel1.setCenterX(x + 20 * SCALE);
        wheel1.setCenterY(y + 40 * SCALE);
        wheel2.setCenterX(x + 80 * SCALE);
        wheel2.setCenterY(y + 40 * SCALE);
        headlight.setX(x + 85 * SCALE);
        headlight.setY(y + 10 * SCALE);
    }

    private void findExit() {
        PixelReader pixelReader = mazeImage.getPixelReader();
        int width = (int) mazeImage.getWidth();
        int height = (int) mazeImage.getHeight();
        List<Point> openings = new ArrayList<>();

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

        Point carPosition = new Point(x, y);
        Point entrance = findClosestPoint(carPosition, openings);
        openings.remove(entrance);
        System.out.println("Entrance (closest to car): " + entrance);
        exitPoint = findFurthestPoint(carPosition, openings);
        System.out.println("Exit point (furthest from car): " + exitPoint);
    }

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

        if (openingWidth >= MIN_OPENING_WIDTH && isConnectedToPath(pixelReader, openingStart, fixed, isHorizontal)) {
            int openingMiddle = openingStart + openingWidth / 2;
            Point opening = isHorizontal ? new Point(openingMiddle, fixed) : new Point(fixed, openingMiddle);
            openings.add(opening);
            System.out.println("Opening found at end of " + borderName + " border: " + opening);
        }

        return openings;
    }

    private boolean isConnectedToPath(PixelReader pixelReader, int start, int fixed, boolean isHorizontal) {
        int checkDepth = 5;
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

    private Point findClosestPoint(Point reference, List<Point> points) {
        return points.stream()
                .min(Comparator.comparingDouble(p -> distance(reference, p)))
                .orElseThrow(() -> new IllegalStateException("No points to compare"));
    }

    private Point findFurthestPoint(Point reference, List<Point> points) {
        return points.stream()
                .max(Comparator.comparingDouble(p -> distance(reference, p)))
                .orElseThrow(() -> new IllegalStateException("No points to compare"));
    }

    private double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    public Rectangle getBody() { return body; }
    public Rectangle getRoof() { return roof; }
    public Circle getWheel1() { return wheel1; }
    public Circle getWheel2() { return wheel2; }
    public Rectangle getHeadlight() { return headlight; }

    public double getX() { return x; }
    public double getY() { return y; }

    private static class Point {
        double x, y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "Point{" + "x=" + x + ", y=" + y + '}';
        }
    }
}