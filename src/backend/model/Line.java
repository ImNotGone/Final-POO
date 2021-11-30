package backend.model;

public class Line extends Figure{
    private final Point start, end;

    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return String.format("Linea [ %s , %s ]", start, end);
    }
}