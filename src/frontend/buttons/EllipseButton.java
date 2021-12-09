package frontend.buttons;

import backend.model.BackendColor;
import backend.model.Figure;
import backend.model.Point;
import frontend.model.DrawableEllipse;
import javafx.scene.canvas.GraphicsContext;

public class EllipseButton extends FigureToggleButton {
    public EllipseButton(String name) {
        super(name);
    }

    @Override
    public Figure build(double lineWidth, BackendColor lineColor, BackendColor fillColor, Point startPoint, Point endPoint, GraphicsContext gc) {
        double dx = Math.abs(startPoint.getX() - endPoint.getX());
        double dy = Math.abs(startPoint.getY() - endPoint.getY());
        double x = (startPoint.getX()+endPoint.getX())/2;
        double y = (startPoint.getY()+endPoint.getY())/2;
        return new DrawableEllipse(lineWidth, lineColor, fillColor, new Point(x, y), dx, dy, gc);
    }
}
