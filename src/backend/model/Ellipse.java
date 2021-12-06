package backend.model;


public class Ellipse extends Figure {

    protected final Point centerPoint;
    protected final double dx, dy;

    public Ellipse(double lineWidth, BackendColor lineColor, BackendColor fillColor, Point centerPoint, double dx, double dy) {
        super(lineWidth, lineColor, fillColor);
        this.centerPoint = centerPoint;
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public String toString() {
        return String.format("Elipse [Centro: %s, DX: %.2f, DY: %.2f]", centerPoint, dx, dy);
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    @Override
    public boolean contains(Point point) {
        return ((Math.pow(point.getX() - centerPoint.getX(), 2)/Math.pow((dx/2), 2)) + (Math.pow(point.getY() - centerPoint.getY(), 2)/Math.pow((dy/2), 2))) <= 1;
    }

    @Override
    public boolean isContained(Figure figure) {
        Point left = new Point(centerPoint.getX() - dx/2, centerPoint.getY());
        Point right = new Point(centerPoint.getX() + dx/2, centerPoint.getY());
        Point bottom = new Point(centerPoint.getX(), centerPoint.getY() + dy/2);
        Point top = new Point(centerPoint.getX(), centerPoint.getY() - dy/2);
        return figure.contains(left) && figure.contains(right) && figure.contains(bottom) && figure.contains(top);
    }

    @Override
    public void move(double diffX, double diffY) {
        centerPoint.move(diffX, diffY);
    }
}