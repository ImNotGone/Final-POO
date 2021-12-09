package frontend.model;

import backend.model.BackendColor;
import backend.model.Ellipse;
import backend.model.Point;
import javafx.scene.canvas.GraphicsContext;

public class DrawableEllipse extends Ellipse {
    private final GraphicsContext gc;

    public DrawableEllipse(double lineWidth, BackendColor lineColor, BackendColor fillColor, Point centerPoint, double dx, double dy, GraphicsContext gc) {
        super(lineWidth, lineColor, fillColor, centerPoint, dx, dy);
        this.gc = gc;
    }

    @Override
    public void draw() {
        gc.fillOval(getCenterPoint().getX() - (getDx()/2), getCenterPoint().getY() - (getDy()/2), getDx(), getDy());
        gc.strokeOval(getCenterPoint().getX() - (getDx()/2), getCenterPoint().getY() - (getDy()/2), getDx(), getDy());
    }
}
