package backend;

import backend.model.Figure;
import backend.model.Point;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CanvasState {

    // Colecciones de figuras necesarias
    // La primer figura es la del fondo, la ultima es la del frente
    private final List<Figure> figures = new ArrayList<>();
    private List<Figure> selectedFigures = new ArrayList<>();

    // Agrega una figura a la lista de figuras
    public void addFigure(Figure figure) {
        figures.add(figure);
    }

    // Elimina las figuras seleccionadas de la lista de figuras
    // resetea la lista de figuras seleccionadas
    public void deleteSelected() {
        figures.removeAll(selectedFigures);
        resetSelectedFigures();
    }

    // Devuelve una copia de la lista de figuras
    public List<Figure> figures() {
        return new ArrayList<>(figures);
    }

    // Devuelve una copia de la lista de figuras seleccionadas
    public List<Figure> selectedFigures() {
        return new ArrayList<>(selectedFigures);
    }

    // Devuelve si hay figuras seleccionadas
    public boolean isSelectedFiguresEmpty() {
        return selectedFigures.isEmpty();
    }

    // Elimina todas las figuras seleccionadas
    public void resetSelectedFigures() {
        selectedFigures = new ArrayList<>();
    }

    // Devuelve si el punto esta dentro de alguna figura seleccionada
    public boolean belongsToASelectedFigure(Point eventPoint) {
        for(Figure selectedFigure : selectedFigures) {
            if (selectedFigure.contains(eventPoint))
                return true;
        }
        return false;
    }

    // Devuelve la figura que esta mas arriba de las que el punto pertenece (por eso recorremos la lista al reves)
    // Si no encuentra ninguna devuelve null
    public Figure getFigureOnPoint(Point eventPoint) {
        Iterator<Figure> reversedFigures = reverseFigureIterator();
        Figure figure;
        while (reversedFigures.hasNext()) {
            figure = reversedFigures.next();
            if(figure.contains(eventPoint))
                return figure;
        }
        return null;
    }

    // Selecciona las figuras que se encuentren dentro de la figura de eseleccion recibida
    // Devuelve si agrego alguna figura o no
    public boolean selectFigures(Figure selectionFigure) {
        boolean found = false;
        for (Figure figure : figures) {
            if(figure.isContained(selectionFigure)) {
                found = true;
                selectedFigures.add(figure);
            }
        }
        return found;
    }

    // Cambia el estado de seleccion de la figura que este mas arriba a la que el punto pertenece
    // Por eso la recorremos al reves
    public boolean toggleSelectionFigure(Point selectionPoint) {

        Iterator<Figure> reversedFigures = reverseFigureIterator();
        Figure figure;
        while (reversedFigures.hasNext()) {
            figure = reversedFigures.next();
            if(figure.contains(selectionPoint)) {
                if (selectedFigures.contains(figure)) {
                    selectedFigures.remove(figure);
                } else {
                    selectedFigures.add(figure);
                }
                return true;
            }
        }
        return false;
    }


    // Mueve las figuras seleccionadas adelante
    public void moveSelectedForward() {
        figures.removeAll(selectedFigures);
        figures.addAll(selectedFigures);
    }

    // Mueve las figuras seleccionadas al fondo
    public void moveSelectedBack() {
        figures.removeAll(selectedFigures);
        figures.addAll(0, selectedFigures);
    }

    // Mueve las figuras seleccionadas acorde a la diferencia en x e y recibida
    public void moveSelectedFigures(double diffX, double diffY) {
        for(Figure selectedFigure : selectedFigures)
            selectedFigure.move(diffX, diffY);
    }

    // Actualiza el espesor del outline de las figuras seleccionadas
    public void setSelectedFigureLineWidth(double lineWidth) {
        for(Figure figure : selectedFigures) {
            figure.setLineWidth(lineWidth);
        }
    }

    // Actualiza el color del outline de las figuras seleccionadas
    public void setSelectedFiguresLineColor(Color lineColor) {
        for(Figure figure : selectedFigures) {
            figure.setLineColor(lineColor);
        }
    }

    // Actualiza el color del relleno de las figuras seleccionadas
    public void setSelectedFiguresFillColor(Color fillColor) {
        for(Figure figure : selectedFigures) {
            figure.setFillColor(fillColor);
        }
    }

    // Devuelve un iterador que reccore al reves, para poder encontrar la figura que
    // este arriba de todas en el canvas mas rapido
    private Iterator<Figure> reverseFigureIterator() {
        return new Iterator<>() {
            private int index = figures.size() - 1;
            @Override
            public boolean hasNext() {
                return index >= 0;
            }

            @Override
            public Figure next() {
                if(!hasNext())
                    throw new NoSuchElementException("No existe el elemento");
                Figure out = figures.get(index);
                index--;
                return out;
            }
        };
    }
}
