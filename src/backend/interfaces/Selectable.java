package backend.interfaces;

import backend.model.Figure;
import backend.model.Point;

public interface Selectable {
    // Permite saber si un punto pertenece a una figura
    boolean contains(Point point);

    // Permite saber si una figura está contenida en la otra
    boolean isContained(Figure figure);
}
