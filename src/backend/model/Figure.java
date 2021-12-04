package backend.model;

// Usamos la clase Color que viene con el SDK de java
import java.awt.*;

public abstract class Figure implements Movable {

    private double lineWidth;
    private Color lineColor;
    private Color fillColor;

    // Permite saber si un punto pertenece a una figura
    public abstract boolean contains(Point point);
    // Permite saber si una figura está contenida en la otra
    public abstract boolean isContained(Figure figure);

    public Figure(double lineWidth, Color lineColor, Color fillColor){
        setFigureProperties(lineWidth, lineColor, fillColor);
    }

    public void setFigureProperties(double lineWidth, Color lineColor, Color fillColor) {
        setLineWidth(lineWidth);
        setLineColor(lineColor);
        setFillColor(fillColor);
    }

    // Getters y setters para las propiedades estéticas de las figuras
    public Color getLineColor() {
        return lineColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public double getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(double lineWidth){
        this.lineWidth = lineWidth;
    }

    public void setLineColor(Color lineColor){
        this.lineColor = lineColor;
    }

    public void setFillColor(Color fillColor){
        this.fillColor = fillColor;
    }
}
