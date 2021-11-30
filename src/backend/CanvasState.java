package backend;

import backend.model.Figure;

import java.util.ArrayList;
import java.util.List;

public class CanvasState {

    private final List<Figure> list = new ArrayList<>();

    public boolean addFigure(Figure figure) {
        return list.add(figure);
    }

    public Iterable<Figure> figures() {
        return new ArrayList<>(list);
    }

    public boolean remove(Figure figure) {
        return list.remove(figure);
    }
}
