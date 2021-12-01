package backend.model;

import javafx.scene.paint.Color;

public class Circle extends Ellipse {

    public Circle(double lineWidth, Color lineColor, Color fillColor, Point centerPoint, double radius) {
        super(lineWidth, lineColor, fillColor, centerPoint, radius*2, radius*2);
    }

    @Override
    public String toString() {
        return String.format("Círculo [Centro: %s, Radio: %.2f]", centerPoint, dx/2);
    }

}
