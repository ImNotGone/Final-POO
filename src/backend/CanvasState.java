package backend;

import backend.model.Figure;
import backend.model.Point;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CanvasState {

    private final List<Figure> figures = new ArrayList<>();
    private List<Figure> selectedFigures = new ArrayList<>();

    public void addFigure(Figure figure) {
        figures.add(figure);
    }

    public void addAll(Collection<Figure> figures) {
        this.figures.addAll(figures);
    }

    public void addAllToStart(Collection<Figure> figures) {
        this.figures.addAll(0, figures);
    }

    public void removeAll(Collection<Figure> figures) {
        this.figures.removeAll(figures);
    }

    public List<Figure> figures() {
        return new ArrayList<>(figures);
    }

    public List<Figure> selectedFigures() {
        return new ArrayList<>(selectedFigures);
    }

    public void resetSelectedFigures() {
        selectedFigures = new ArrayList<>();
    }

    public boolean checkPointInSelected(Point eventPoint) {
        boolean found = false;
        for(Figure selectedFigure : selectedFigures) {
            if (figureBelongs(selectedFigure, eventPoint))
                found = true;
        }
        return found;
    }

    public boolean selectFigures(Figure selectionFigure) {
        boolean found = false;
        for (Figure figure : figures) {
            if(figureContains(figure, selectionFigure)) {
                found = true;
                selectedFigures.add(figure);
            }
        }
        return found;
    }

    public void selectFigure(Figure figure) {
        selectedFigures.add(figure);
    }

    private boolean figureBelongs(Figure figure, Point eventPoint) {
        return figure.contains(eventPoint);
    }

    private boolean figureContains(Figure figure, Figure figureContainer) {
        return figure.isContained(figureContainer);
    }
}
