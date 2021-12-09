package frontend.buttons;

import backend.model.BackendColor;
import backend.model.Circle;
import backend.model.Figure;
import backend.model.Point;
import frontend.model.DrawableCircle;
import javafx.scene.canvas.GraphicsContext;

public class CircleButton extends FigureToggleButton {
    public CircleButton(String name) {
        super(name);
    }

    @Override
    public Figure build(double lineWidth, BackendColor lineColor, BackendColor fillColor, Point startPoint, Point endPoint, GraphicsContext gc) {
        double radius = Math.abs(endPoint.getX() - startPoint.getX());
        return new DrawableCircle(lineWidth, lineColor, fillColor, startPoint, radius, gc);
    }
}
