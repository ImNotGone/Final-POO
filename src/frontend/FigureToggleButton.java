package frontend;

import backend.model.Figure;
import backend.model.Point;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;


public class FigureToggleButton extends ToggleButton {
    private final FigureBuilder figureBuilder;
    public FigureToggleButton(String name, FigureBuilder figureBuilder) {
        super(name);
        this.figureBuilder = figureBuilder;
    }

    public Figure getFigure(double lineWidth, Color lineColor, Color fillColor, Point startPoint, Point endPoint) {
        return figureBuilder.build(lineWidth, lineColor, fillColor, startPoint, endPoint);
    }
}
