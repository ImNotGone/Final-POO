package backend;

import backend.model.Figure;
import backend.model.Point;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CanvasState {

    private final List<Figure> figures = new ArrayList<>();
    private List<Figure> selectedFigures = new ArrayList<>();

    public void addFigure(Figure figure) {
        figures.add(figure);
    }

    public void deleteSelected() {
        figures.removeAll(selectedFigures);
        resetSelectedFigures();
    }

    public List<Figure> figures() {
        return new ArrayList<>(figures);
    }

    public List<Figure> selectedFigures() {
        return new ArrayList<>(selectedFigures);
    }

    public boolean isSelectedFiguresEmpty() {
        return selectedFigures.isEmpty();
    }

    public void resetSelectedFigures() {
        selectedFigures = new ArrayList<>();
    }

    public boolean checkPointIsInAnySelectedFigure(Point eventPoint) {
        for(Figure selectedFigure : selectedFigures) {
            if (figureBelongs(selectedFigure, eventPoint))
                return true;
        }
        return false;
    }

    public Figure getFigureOnPoint(Point eventPoint) {
        List<Figure> reversedFigures = new ArrayList<>(figures);
        Collections.reverse(reversedFigures);
        //todo mejorar esto de aca arriba xd
        for(Figure figure : reversedFigures) {
            if(figure.contains(eventPoint))
                return figure;
        }
        return null;
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

    public boolean selectFigure(Point selectionPoint) {
        List<Figure> reversedFigures = new ArrayList<>(figures);
        Collections.reverse(reversedFigures);
        //todo mejorar esto de aca arriba xd
        for (Figure figure : reversedFigures) {
            if(figureBelongs(figure, selectionPoint)) {
                selectedFigures.add(figure);
                return true;
            }
        }
        return false;
    }

    public void moveSelectedForward() {
        figures.removeAll(selectedFigures);
        figures.addAll(selectedFigures);
    }

    public void moveSelectedBack() {
        figures.removeAll(selectedFigures);
        figures.addAll(0, selectedFigures);
    }

    public void moveSelectedFigures(double diffX, double diffY) {
        for(Figure selectedFigure : selectedFigures)
            selectedFigure.moveFigure(diffX, diffY);
    }

    public void setSelectedFigureLineWidth(double lineWidth) {
        for(Figure figure : selectedFigures) {
            figure.setLineWidth(lineWidth);
        }
    }

    public void setSelectedFiguresLineColor(Color lineColor) {
        for(Figure figure : selectedFigures) {
            figure.setLineColor(lineColor);
        }
    }

    public void setSelectedFiguresFillColor(Color fillColor) {
        for(Figure figure : selectedFigures) {
            figure.setFillColor(fillColor);
        }
    }

    private boolean figureBelongs(Figure figure, Point eventPoint) {
        return figure.contains(eventPoint);
    }

    private boolean figureContains(Figure figure, Figure figureContainer) {
        return figure.isContained(figureContainer);
    }
}
