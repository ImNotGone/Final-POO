package backend.model;



public class Circle extends Ellipse {

    public Circle(double lineWidth, BackendColor lineColor, BackendColor fillColor, Point centerPoint, double radius) {
        super(lineWidth, lineColor, fillColor, centerPoint, radius*2, radius*2);
    }

    @Override
    public String toString() {
        return String.format("Círculo [Centro: %s, Radio: %.2f]", getCenterPoint(), getDx()/2);
    }
}
