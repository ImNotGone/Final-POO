package frontend.model;

import backend.model.BackendColor;
import backend.model.Point;
import backend.model.Rectangle;
import javafx.scene.canvas.GraphicsContext;

public class DrawableRectangle extends Rectangle {
    GraphicsContext gc;

    public DrawableRectangle(double lineWidth, BackendColor lineColor, BackendColor fillColor, Point topLeft, Point bottomRight, GraphicsContext gc) {
        super(lineWidth, lineColor, fillColor, topLeft, bottomRight);
        this.gc = gc;
    }

    @Override
    public void draw() {
        gc.fillRect(getTopLeft().getX(), getTopLeft().getY(), Math.abs(getTopLeft().getX() - getBottomRight().getX()), Math.abs(getTopLeft().getY() - getBottomRight().getY()));
        gc.strokeRect(getTopLeft().getX(), getTopLeft().getY(), Math.abs(getTopLeft().getX() - getBottomRight().getX()), Math.abs(getTopLeft().getY() - getBottomRight().getY()));
    }
}
