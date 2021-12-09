package frontend.model;

import backend.model.BackendColor;
import backend.model.Line;
import backend.model.Point;
import javafx.scene.canvas.GraphicsContext;

public class DrawableLine extends Line {
    GraphicsContext gc;

    public DrawableLine(double lineWidth, BackendColor lineColor, BackendColor fillColor, Point start, Point end, GraphicsContext gc) {
        super(lineWidth, lineColor, fillColor, start, end);
        this.gc = gc;
    }

    @Override
    public void draw() {
        gc.strokeLine(getStart().getX(), getStart().getY(), getEnd().getX(), getEnd().getY());
    }
}
