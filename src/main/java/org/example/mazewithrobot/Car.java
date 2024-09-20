package org.example.mazewithrobot;


import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class Car {
    private Rectangle body;
    private Rectangle roof;
    private Circle wheel1, wheel2;
    private Rectangle headlight;
    private Rotate rotate;

    public Car(double x, double y) {
        // Scale factor
        double scale = 0.25;  // Adjust this value as needed

        // Create the main body of the car (scaled)
        body = new Rectangle(x, y, 100 * scale, 40 * scale);
        body.setFill(Color.DARKBLUE);

        // Create the roof of the car (scaled)
        roof = new Rectangle(x + 20 * scale, y - 20 * scale, 60 * scale, 20 * scale);
        roof.setFill(Color.BLUE);

        // Create the wheels (scaled)
        wheel1 = new Circle(x + 20 * scale, y + 40 * scale, 10 * scale);
        wheel1.setFill(Color.BLACK);
        wheel2 = new Circle(x + 80 * scale, y + 40 * scale, 10 * scale);
        wheel2.setFill(Color.BLACK);

        // Create the headlights (scaled)
        headlight = new Rectangle(x + 85 * scale, y + 10 * scale, 10 * scale, 5 * scale);
        headlight.setFill(Color.YELLOW);

        // Add rotation transformation for the car
        rotate = new Rotate(0, x + (50 * scale), y + (20 * scale)); // Rotate around the center of the body
        body.getTransforms().add(rotate);
        roof.getTransforms().add(rotate);
        wheel1.getTransforms().add(rotate);
        wheel2.getTransforms().add(rotate);
        headlight.getTransforms().add(rotate);
    }

    public Rectangle getBody() {
        return body;
    }

    public Rectangle getRoof() {
        return roof;
    }

    public Circle getWheel1() {
        return wheel1;
    }

    public Circle getWheel2() {
        return wheel2;
    }

    public Rectangle getHeadlight() {
        return headlight;
    }


    // Method to move the car
    public void move(double dx, double dy) {
        body.setLayoutX(body.getLayoutX() + dx);
        body.setLayoutY(body.getLayoutY() + dy);

        roof.setLayoutX(roof.getLayoutX() + dx);
        roof.setLayoutY(roof.getLayoutY() + dy);

        wheel1.setLayoutX(wheel1.getLayoutX() + dx);
        wheel1.setLayoutY(wheel1.getLayoutY() + dy);

        wheel2.setLayoutX(wheel2.getLayoutX() + dx);
        wheel2.setLayoutY(wheel2.getLayoutY() + dy);

        headlight.setLayoutX(headlight.getLayoutX() + dx);
        headlight.setLayoutY(headlight.getLayoutY() + dy);


    }

    // Method to rotate the car
    public void rotate(double angle) {
        rotate.setAngle(angle);
    }
}
