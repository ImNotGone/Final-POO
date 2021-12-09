package frontend.buttons;

import backend.model.BackendColor;
import backend.model.Figure;
import backend.model.Point;
import frontend.model.DrawableLine;
import javafx.scene.canvas.GraphicsContext;

public class LineButton extends FigureToggleButton {
    public LineButton(String name) {
        super(name);
    }

    @Override
    public Figure build(double lineWidth, BackendColor lineColor, BackendColor fillColor, Point startPoint, Point endPoint, GraphicsContext gc) {
        return new DrawableLine(lineWidth, lineColor, fillColor, startPoint, endPoint, gc);
    }
}
