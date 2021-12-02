package frontend;

import backend.model.Figure;
import backend.model.Point;

import java.awt.*;


@FunctionalInterface
public interface FigureBuilder {
    Figure build(double lineWidth, Color lineColor, Color fillColor, Point startPoint, Point endPoint);
}
