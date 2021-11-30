package backend.model;

public class Ellipse extends Figure {
    protected final Point centerPoint;
    protected final double dx, dy;

    public Ellipse(Point centerPoint, double dx, double dy) {
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
        return ((Math.pow(point.getX() - centerPoint.getX(), 2)/Math.pow(dx, 2)) + (Math.pow(point.getY() - centerPoint.getY(), 2)/Math.pow(dy, 2))) <= 1;
    }

    @Override
    public void moveFigure(double diffX, double diffY) {
        centerPoint.move(diffX, diffY);
    }
}