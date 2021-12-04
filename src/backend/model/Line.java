package backend.model;


import java.awt.*;

public class Line extends Figure {

    private final Point start, end;

    public Line(double lineWidth, Color lineColor, Color fillColor, Point start, Point end) {
        super(lineWidth, lineColor, fillColor);
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return String.format("Linea [ %s , %s ]", start, end);
    }

    @Override
    public boolean contains(Point point) {
        return false;
    }

    @Override
    public boolean isContained(Figure figure) {
        return figure.contains(start) && figure.contains(end);
    }

    @Override
    public void move(double diffX, double diffY) {
        start.move(diffX, diffY);
        end.move(diffX, diffY);
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }
}
