package backend.model;

public class Line extends Figure {
    private final Point start, end;

    public Line(Point start, Point end) {
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
    public void moveFigure(double diffX, double diffY) {
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
