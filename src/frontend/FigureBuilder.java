package frontend;

import backend.model.Figure;
import backend.model.Point;

import java.awt.*;

// Construye una figura a partir de los datos que recibe
@FunctionalInterface
public interface FigureBuilder {
    Figure build(double lineWidth, Color lineColor, Color fillColor, Point startPoint, Point endPoint);
}
