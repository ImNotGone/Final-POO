package backend.model;

import javafx.scene.paint.Color;

public abstract class Figure {
    private double lineThickness;
    private Color lineColor;
    private Color backgroundColor;

    public abstract boolean contains(Point point);
    public abstract void moveFigure(double diffX, double diffY);

    public void setFigureProperties(double lineThickness, Color lineColor, Color backgroundColor) {
        setLineThickness(lineThickness);
        setLineColor(lineColor);
        setBackgroundColor(backgroundColor);
    }

    public Color getLineColor() {
        return lineColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public double getLineThickness() {
        return lineThickness;
    }

    public void setLineThickness(double lineThickness){
        this.lineThickness = lineThickness;
    }

    public void setLineColor(Color lineColor){
        this.lineColor = lineColor;
    }

    public void setBackgroundColor(Color backgroundColor){
        this.backgroundColor = backgroundColor;
    }
}
