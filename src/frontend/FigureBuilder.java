package frontend;

import backend.model.BackendColor;
import backend.model.Figure;
import backend.model.Point;


// Construye una figura a partir de los datos que recibe
@FunctionalInterface
public interface FigureBuilder {
    Figure build(double lineWidth, BackendColor lineColor, BackendColor fillColor, Point startPoint, Point endPoint);
}
