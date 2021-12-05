package frontend;

import backend.model.BackendColor;
import backend.model.Figure;
import backend.model.Point;
import javafx.scene.control.ToggleButton;


// Para que cada botón pueda construir la figura que le corresponde
// Recibe cómo construir una figura en su constructor
public class FigureToggleButton extends ToggleButton {

    private final FigureBuilder figureBuilder;

    public FigureToggleButton(String name, FigureBuilder figureBuilder) {
        super(name);
        this.figureBuilder = figureBuilder;
    }

    public Figure buildFigure(double lineWidth, BackendColor lineColor, BackendColor fillColor, Point startPoint, Point endPoint) {
        return figureBuilder.build(lineWidth, lineColor, fillColor, startPoint, endPoint);
    }
}
