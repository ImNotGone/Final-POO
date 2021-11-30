package backend;

import backend.model.Figure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CanvasState {

    private final List<Figure> list = new ArrayList<>();

    public boolean addFigure(Figure figure) {
        return list.add(figure);
    }

    public Iterable<Figure> figures() {
        return new ArrayList<>(list);
    }

    public boolean removeAll(Collection<Figure> figures) {
        return list.removeAll(figures);
    }

    public boolean addAll(Collection<Figure> figures) {
        return list.addAll(figures);
    }
}
