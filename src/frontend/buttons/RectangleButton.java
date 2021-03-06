package frontend.buttons;

import backend.model.BackendColor;
import backend.model.Figure;
import backend.model.Point;
import frontend.model.DrawableRectangle;
import javafx.scene.canvas.GraphicsContext;

public class RectangleButton extends FigureToggleButton {
    public RectangleButton(String name) {
        super(name);
    }

    @Override
    public Figure build(double lineWidth, BackendColor lineColor, BackendColor fillColor, Point startPoint, Point endPoint, GraphicsContext gc) {
        return new DrawableRectangle(lineWidth, lineColor, fillColor, startPoint, endPoint, gc);
    }
}
