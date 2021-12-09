package backend;

import backend.model.*;
import java.util.*;

public class CanvasState {

    // Colecciones de figuras necesarias
    // La primera figura es la del fondo, la última es la del frente
    private final List<Figure> figures = new LinkedList<>();
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

    // Devuelve si el punto está dentro de alguna figura seleccionada
    public boolean belongsToASelectedFigure(Point eventPoint) {
        for(Figure selectedFigure : selectedFigures) {
            if (selectedFigure.contains(eventPoint))
                return true;
        }
        return false;
    }

    // Devuelve la figura que está más arriba de las que el punto pertenece (por eso recorremos la lista al revés)
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

    // Selecciona las figuras que se encuentren dentro de la figura de selección recibida
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

    // Cambia el estado de selección de la figura que este más arriba a la que el punto pertenece
    // Por eso la recorremos al revés
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
    public void setSelectedFiguresLineColor(BackendColor lineColor) {
        for(Figure figure : selectedFigures) {
            figure.setLineColor(lineColor);
        }
    }

    // Actualiza el color del relleno de las figuras seleccionadas
    public void setSelectedFiguresFillColor(BackendColor fillColor) {
        for(Figure figure : selectedFigures) {
            figure.setFillColor(fillColor);
        }
    }

    // Devuelve un iterador que recorre al revés, para poder encontrar la figura que
    // este arriba de todas en el canvas más rápido
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
