package frontend.buttons;

import javafx.scene.control.ToggleButton;


// Para que cada botón pueda construir la figura que le corresponde
// Recibe cómo construir una figura en su constructor
public abstract class FigureToggleButton extends ToggleButton implements FigureBuilder {
    public FigureToggleButton(String name) {
        super(name);
    }
}
