package backend.model;

public abstract class Figure {

    public abstract boolean contains(Point point);

    public abstract void moveFigure(double diffX, double diffY);
}
