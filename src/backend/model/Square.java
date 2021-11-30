package backend.model;

public class Square extends Rectangle{
    public Square(Point topLeft, double side) {
        super(topLeft, new Point(topLeft.getX() + side, topLeft.getY() - side));
    }

    @Override
    public String toString() {
        return String.format("Cuadrado [ %s , %s ]", super.getTopLeft(), super.getBottomRight());
    }

}
