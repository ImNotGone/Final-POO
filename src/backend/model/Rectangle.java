package backend.model;


import java.awt.*;

public class Rectangle extends Figure {

    private final Point topLeft, bottomRight;

    public Rectangle(double lineWidth, Color lineColor, Color fillColor, Point topLeft, Point bottomRight) {
        super(lineWidth, lineColor, fillColor);
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }


    @Override
    public String toString() {
        return String.format("Rectángulo [ %s , %s ]", topLeft, bottomRight);
    }

    @Override
    public boolean contains(Point point) {
        return point.getX() > topLeft.getX() && point.getX() < bottomRight.getX() && point.getY() > topLeft.getY() && point.getY() < bottomRight.getY();
    }

    @Override
    public boolean isContained(Figure figure) {
        return figure.contains(topLeft) && figure.contains(bottomRight);
    }

    @Override
    public void moveFigure(double diffX, double diffY) {
        topLeft.move(diffX, diffY);
        bottomRight.move(diffX, diffY);
    }
}
