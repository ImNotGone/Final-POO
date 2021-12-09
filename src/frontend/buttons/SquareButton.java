package frontend.buttons;

import backend.model.BackendColor;
import backend.model.Figure;
import backend.model.Point;
import frontend.model.DrawableSquare;
import javafx.scene.canvas.GraphicsContext;

public class SquareButton extends FigureToggleButton{
    public SquareButton(String name) {
        super(name);
    }

    @Override
    public Figure build(double lineWidth, BackendColor lineColor, BackendColor fillColor, Point startPoint, Point endPoint, GraphicsContext gc) {
        double side = Math.abs(startPoint.getX() - endPoint.getX());
        return new DrawableSquare(lineWidth, lineColor, fillColor, startPoint, side, gc);
    }
}
