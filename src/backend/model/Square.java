package backend.model;


import java.awt.*;

public class Square extends Rectangle{
    public Square(double lineWidth, Color lineColor, Color fillColor, Point topLeft, double side) {
        super(lineWidth, lineColor, fillColor, topLeft, new Point(topLeft.getX() + side, topLeft.getY() + side));
    }

    @Override
    public String toString() {
        return String.format("Cuadrado [ %s , %s ]", super.getTopLeft(), super.getBottomRight());
    }

}
