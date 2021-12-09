package backend.model;

import backend.interfaces.*;

public abstract class Figure implements Movable, Selectable, Coloreable, Drawable {

    private double lineWidth;
    private BackendColor lineColor;
    private BackendColor fillColor;

    public Figure(double lineWidth, BackendColor lineColor, BackendColor fillColor){
        setFigureProperties(lineWidth, lineColor, fillColor);
    }

    public void setFigureProperties(double lineWidth, BackendColor lineColor, BackendColor fillColor) {
        setLineWidth(lineWidth);
        setLineColor(lineColor);
        setFillColor(fillColor);
    }

    // Getters y setters para las propiedades estéticas de las figuras
    public BackendColor getLineColor() {
        return lineColor;
    }

    public BackendColor getFillColor() {
        return fillColor;
    }

    public double getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(double lineWidth){
        this.lineWidth = lineWidth;
    }

    public void setLineColor(BackendColor lineColor){
        this.lineColor = lineColor;
    }

    public void setFillColor(BackendColor fillColor){
        this.fillColor = fillColor;
    }
}
