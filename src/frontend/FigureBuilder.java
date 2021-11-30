package frontend;

import backend.model.Figure;
import backend.model.Point;
import javafx.scene.paint.Color;


@FunctionalInterface
public interface FigureBuilder {
    public Figure build(double lineWidth, Color lineColor, Color fillColor, Point startPoint, Point endPoint);
}
