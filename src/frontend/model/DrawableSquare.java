package frontend.model;

import backend.model.BackendColor;
import backend.model.Point;
import backend.model.Square;
import javafx.scene.canvas.GraphicsContext;

public class DrawableSquare extends Square {
    private final GraphicsContext gc;

    public DrawableSquare(double lineWidth, BackendColor lineColor, BackendColor fillColor, Point topLeft, double side, GraphicsContext gc) {
        super(lineWidth, lineColor, fillColor, topLeft, side);
        this.gc = gc;
    }

    @Override
    public void draw() {
        gc.fillRect(getTopLeft().getX(), getTopLeft().getY(), Math.abs(getTopLeft().getX() - getBottomRight().getX()), Math.abs(getTopLeft().getY() - getBottomRight().getY()));
        gc.strokeRect(getTopLeft().getX(), getTopLeft().getY(), Math.abs(getTopLeft().getX() - getBottomRight().getX()), Math.abs(getTopLeft().getY() - getBottomRight().getY()));
    }
}
