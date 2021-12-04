package frontend;

import backend.model.Figure;
import backend.model.Point;
import javafx.scene.control.ToggleButton;

import java.awt.*;

// Para que cada boton pueda construir la figura que le corresponde
// Recibe cómo construir una figura en su constructor
public class FigureToggleButton extends ToggleButton {

    private final FigureBuilder figureBuilder;

    public FigureToggleButton(String name, FigureBuilder figureBuilder) {
        super(name);
        this.figureBuilder = figureBuilder;
    }

    public Figure buildFigure(double lineWidth, Color lineColor, Color fillColor, Point startPoint, Point endPoint) {
        return figureBuilder.build(lineWidth, lineColor, fillColor, startPoint, endPoint);
    }
}
