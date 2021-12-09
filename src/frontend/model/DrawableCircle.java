package frontend.model;

import backend.model.BackendColor;
import backend.model.Circle;
import backend.model.Point;
import javafx.scene.canvas.GraphicsContext;

public class DrawableCircle extends Circle {
    GraphicsContext gc;

    public DrawableCircle(double lineWidth, BackendColor lineColor, BackendColor fillColor, Point centerPoint, double radius, GraphicsContext gc) {
        super(lineWidth, lineColor, fillColor, centerPoint, radius);
        this.gc = gc;
    }

    @Override
    public void draw() {
        gc.fillOval(getCenterPoint().getX() - (getDx()/2), getCenterPoint().getY() - (getDy()/2), getDx(), getDy());
        gc.strokeOval(getCenterPoint().getX() - (getDx()/2), getCenterPoint().getY() - (getDy()/2), getDx(), getDy());
    }
}
