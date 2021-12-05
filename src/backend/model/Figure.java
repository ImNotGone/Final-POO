package backend.model;

public abstract class Figure implements Movable {

    private double lineWidth;
    private BackendColor lineColor;
    private BackendColor fillColor;

    // Permite saber si un punto pertenece a una figura
    public abstract boolean contains(Point point);
    // Permite saber si una figura está contenida en la otra
    public abstract boolean isContained(Figure figure);

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
