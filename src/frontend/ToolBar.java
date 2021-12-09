package frontend;

import backend.model.*;
import frontend.buttons.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ToolBar extends VBox {

    /* ========== Declaración de macros ========== */
    private static final int MIN_LINE_WIDTH = 1;
    private static final int MAX_LINE_WIDTH = 50;
    private static final int DEFAULT_LINE_WIDTH = 3;
    private static final Color LINE_COLOR = Color.BLACK;
    private static final Color FILL_COLOR = Color.YELLOW;
    private static final String SELECTION_BUTTON_LABEL = "Seleccionar";
    private static final String CIRCLE_BUTTON_LABEL = "Círculo";
    private static final String SQUARE_BUTTON_LABEL = "Cuadrado";
    private static final String RECTANGLE_BUTTON_LABEL = "Rectángulo";
    private static final String ELLIPSE_BUTTON_LABEL = "Elipse";
    private static final String LINE_BUTTON_LABEL = "Línea";
    private static final String DELETE_BUTTON_LABEL = "Borrar";
    private static final String BACK_BUTTON_LABEL = "Al fondo";
    private static final String FORWARD_BUTTON_LABEL = "Al frente";
    private static final String LINE_OPTIONS_LABEL = "Borde";
    private static final String FILL_OPTIONS_LABEL = "Relleno";

    /* ========== Creación de botones ========== */
    private final ToggleButton selectionButton = new ToggleButton(SELECTION_BUTTON_LABEL);
    private final FigureToggleButton rectangleButton = new RectangleButton(RECTANGLE_BUTTON_LABEL);
    private final FigureToggleButton circleButton = new CircleButton(CIRCLE_BUTTON_LABEL);
    private final FigureToggleButton ellipseButton = new EllipseButton(ELLIPSE_BUTTON_LABEL);
    private final FigureToggleButton squareButton = new SquareButton(SQUARE_BUTTON_LABEL);
    private final FigureToggleButton lineButton = new LineButton(LINE_BUTTON_LABEL);
    private final ToggleButton deleteButton = new ToggleButton(DELETE_BUTTON_LABEL);
    private final ToggleButton backButton = new ToggleButton(BACK_BUTTON_LABEL);
    private final ToggleButton forwardButton = new ToggleButton(FORWARD_BUTTON_LABEL);

    private final ToggleButton[] buttonsArr = {selectionButton, rectangleButton, circleButton, squareButton, ellipseButton, lineButton, deleteButton, backButton, forwardButton};
    private final FigureToggleButton[] figureButtonsArr = {rectangleButton, circleButton, squareButton, ellipseButton, lineButton};

    // Slider
    private final Slider lineWidthSlider = new Slider(MIN_LINE_WIDTH, MAX_LINE_WIDTH, DEFAULT_LINE_WIDTH);

    // Line Label
    private final Label lineWidthLabel = new Label(LINE_OPTIONS_LABEL);

    // Fill Label
    private final Label fillLabel = new Label(FILL_OPTIONS_LABEL);

    // ColorPicker
    private final ColorPicker lineColorPicker = new ColorPicker(LINE_COLOR);
    private final ColorPicker fillColorPicker = new ColorPicker(FILL_COLOR);

    private final ToggleGroup tools = new ToggleGroup();

    /* ========== Se agregan todos los botones a la toolbar ========== */
    public ToolBar() {
        for (ToggleButton tool : buttonsArr) {
            tool.setMinWidth(90);
            tool.setToggleGroup(tools);
            tool.setCursor(Cursor.HAND);
        }

        lineWidthSlider.setShowTickMarks(true);
        lineWidthSlider.setBlockIncrement(1f);
        lineWidthSlider.setShowTickLabels(true);

        getChildren().addAll(buttonsArr);

        // Line options
        getChildren().add(lineWidthLabel);
        getChildren().add(lineWidthSlider);
        getChildren().add(lineColorPicker);

        // Fill options
        getChildren().add(fillLabel);
        getChildren().add(fillColorPicker);

        setSpacing(10);
        setPadding(new Insets(5));
        setStyle("-fx-background-color: #999");
        setPrefWidth(100);
    }

    /* ========== Métodos auxiliares ========== */
    // La barra de herramientas recibe por setters las acciones que debe realizar cada botón

    // Permite configurar el setOnAction para todos los botones de figuras
    public void setFigureButtonAction(EventHandler<ActionEvent> action) {
        for (FigureToggleButton figureToggleButton : figureButtonsArr) {
            figureToggleButton.setOnAction(action);
        }
    }

    // Configuration de setOnAction para forwardButton
    public void setForwardAction(EventHandler<ActionEvent> action){
        forwardButton.setOnAction(action);
    }

    // Configuración de setOnAction para backButton
    public void setBackAction(EventHandler<ActionEvent> action){
        backButton.setOnAction(action);
    }

    // Configuración de setOnAction para deleteButton
    public void setDeleteAction(EventHandler<ActionEvent> action){
        deleteButton.setOnAction(action);
    }

    // Configuración de setOnAction para fillColorPicker
    public void setFillColorPickerAction(EventHandler<ActionEvent> action){
        fillColorPicker.setOnAction(action);
    }

    // Configuración de setOnAction para lineColorPicker
    public void setLineColorPickerAction(EventHandler<ActionEvent> action){
        lineColorPicker.setOnAction(action);
    }

    // Configuración de setOnMouseDragged y setOnMouseClicked para el lineWidthSlider
    public void setLineWidthSliderAction(EventHandler<? super MouseEvent> action) { lineWidthSlider.setOnMouseDragged(action); lineWidthSlider.setOnMouseClicked(action);}

    // Devuelve el array con los botones de figuras
    public FigureToggleButton[] getFigureButtons() {
        return figureButtonsArr;
    }

    // Devuelve el array con todos los botones
    public ToggleButton[] getButtons() {
        return buttonsArr;
    }

    // Devuelve el color actual del lineColorPicker
    public Color getLineColor() {
        return lineColorPicker.getValue();
    }

    // Devuelve el color actual del fillColorPicker
    public Color getFillColor() {
        return fillColorPicker.getValue();
    }

    // Devuelve el valor actual del lineWidthSlider
    public double getLineWidth(){
        return lineWidthSlider.getValue();
    }

    // Cambia el botón actual, por el botón de selección
    public void changeToSelect() {
        selectionButton.setSelected(true);
        selectionButton.requestFocus();
    }

    // Devuelve si el botón de selección está activo
    public boolean isSelectionButtonSelected(){
        return selectionButton.isSelected();
    }

    // Devuelve si el botón de línea está activo
    public boolean isLineButtonSelected() {
        return lineButton.isSelected();
    }
}
