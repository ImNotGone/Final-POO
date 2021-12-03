package backend.model;


import java.awt.*;

public abstract class Figure implements Movable{
    private double lineWidth;
    private Color lineColor;
    private Color fillColor;

    public abstract boolean contains(Point point);
    public abstract boolean isContained(Figure figure);

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

    public Color getFillColor() {
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
