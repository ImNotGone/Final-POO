package backend.model;

import javafx.scene.paint.Color;

public abstract class Figure {
    private double lineWidth;
    private Color lineColor;
    private Color fillColor;

    public abstract boolean contains(Point point);
    public abstract void moveFigure(double diffX, double diffY);

    public Figure(double lineWidth, Color lineColor, Color fillColor){
        setFigureProperties(lineWidth, lineColor, fillColor);
    }

    public void setFigureProperties(double lineWidth, Color lineColor, Color fillColor) {
        setLineWidth(lineWidth);
        setLineColor(lineColor);
        setFillColor(fillColor);
    }

    public Color getLineColor() {
        return lineColor;
    }

    public Color getfillColor() {
        return fillColor;
    }

    public double getLineThickness() {
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
