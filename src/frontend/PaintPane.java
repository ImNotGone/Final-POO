package frontend;

import backend.CanvasState;
import backend.model.*;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class PaintPane extends BorderPane {

	// BackEnd
	CanvasState canvasState;

	// Canvas y relacionados
	Canvas canvas = new Canvas(800, 600);
	GraphicsContext gc = canvas.getGraphicsContext2D();

	// Botones Barra Izquierda
	ToggleButton selectionButton = new ToggleButton("Seleccionar");
	ToggleButton rectangleButton = new ToggleButton("Rectángulo");
	ToggleButton circleButton = new ToggleButton("Círculo");
	ToggleButton squareButton = new ToggleButton("Cuadrado");
	ToggleButton ellipseButton = new ToggleButton("Elipse");
	ToggleButton lineButton = new ToggleButton("Línea");

	// Sliders
	Slider lineWidthSlider = new Slider(1, 50, 25);

	// ColorPicker
	ColorPicker lineColorPicker = new ColorPicker(Color.BLACK);
	ColorPicker fillColorPicker = new ColorPicker(Color.YELLOW);

	// Dibujar una figura
	Point startPoint;

	// Seleccionar una figura
	Figure selectedFigure;

	// StatusBar
	StatusPane statusPane;

	public PaintPane(CanvasState canvasState, StatusPane statusPane) {
		this.canvasState = canvasState;
		this.statusPane = statusPane;
		ToggleButton[] toolsArr = {selectionButton, rectangleButton, circleButton, squareButton, ellipseButton, lineButton};
		ToggleGroup tools = new ToggleGroup();
		for (ToggleButton tool : toolsArr) {
			tool.setMinWidth(90);
			tool.setToggleGroup(tools);
			tool.setCursor(Cursor.HAND);
		}

		lineWidthSlider.setShowTickMarks(true);
		lineWidthSlider.setBlockIncrement(1f);

		
		VBox buttonsBox = new VBox(10);
		// Buttons
		buttonsBox.getChildren().addAll(toolsArr);
		// Slider
		buttonsBox.getChildren().add(lineWidthSlider);
		// ColorPickers
		buttonsBox.getChildren().add(lineColorPicker);
		buttonsBox.getChildren().add(fillColorPicker);
		buttonsBox.setPadding(new Insets(5));
		buttonsBox.setStyle("-fx-background-color: #999");
		buttonsBox.setPrefWidth(100);

		canvas.setOnMousePressed(event -> {
			startPoint = new Point(event.getX(), event.getY());
		});
		canvas.setOnMouseReleased(event -> {
			Point endPoint = new Point(event.getX(), event.getY());
			if(startPoint == null) {
				return ;
			}
			if((endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY()) && !lineButton.isSelected()) {
				return ;
			}
			Figure newFigure = null;
			if(rectangleButton.isSelected()) {
				newFigure = new Rectangle(lineWidthSlider.getValue(), lineColorPicker.getValue(), fillColorPicker.getValue(), startPoint, endPoint);
			} else if(circleButton.isSelected()) {
				double circleRadius = Math.abs(endPoint.getX() - startPoint.getX());
				newFigure = new Circle(lineWidthSlider.getValue(), lineColorPicker.getValue(), fillColorPicker.getValue(), startPoint, circleRadius);
			} else if(squareButton.isSelected()) {
				newFigure = new Square(lineWidthSlider.getValue(), lineColorPicker.getValue(), fillColorPicker.getValue(), startPoint, Math.abs(startPoint.getX() - endPoint.getX()));
			} else if(ellipseButton.isSelected()) {
				double dx = Math.abs(startPoint.getX() - endPoint.getX());
				double dy = Math.abs(startPoint.getY() - endPoint.getY());
				double x = (startPoint.getX()+endPoint.getX())/2;
				double y = (startPoint.getY()+endPoint.getY())/2;
				newFigure = new Ellipse(lineWidthSlider.getValue(), lineColorPicker.getValue(), fillColorPicker.getValue(), new Point(x, y),dx,dy);
			} else if(lineButton.isSelected()) {
				newFigure = new Line(lineWidthSlider.getValue(), lineColorPicker.getValue(), fillColorPicker.getValue(), startPoint, endPoint);
			} else {
				return ;
			}
			canvasState.addFigure(newFigure);
			startPoint = null;
			redrawCanvas();
		});
		canvas.setOnMouseMoved(event -> {
			Point eventPoint = new Point(event.getX(), event.getY());
			boolean found = false;
			StringBuilder label = new StringBuilder();
			for(Figure figure : canvasState.figures()) {
				if(figureBelongs(figure, eventPoint)) {
					found = true;
					label.append(figure.toString());
				}
			}
			if(found) {
				statusPane.updateStatus(label.toString());
			} else {
				statusPane.updateStatus(eventPoint.toString());
			}
		});

		canvas.setOnMouseClicked(event -> {
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				boolean found = false;
				StringBuilder label = new StringBuilder("Se seleccionó: ");
				for (Figure figure : canvasState.figures()) {
					if(figureBelongs(figure, eventPoint)) {
						found = true;
						selectedFigure = figure;
						label.append(figure.toString());
					}
				}
				if (found) {
					statusPane.updateStatus(label.toString());
				} else {
					selectedFigure = null;
					statusPane.updateStatus("Ninguna figura encontrada");
				}
				redrawCanvas();
			}
		});
		canvas.setOnMouseDragged(event -> {
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				double diffX = (eventPoint.getX() - startPoint.getX()) / 100;
				double diffY = (eventPoint.getY() - startPoint.getY()) / 100;
				if(selectedFigure != null) {
					selectedFigure.moveFigure(diffX, diffY);
				}
				redrawCanvas();
			}
		});
		setLeft(buttonsBox);
		setRight(canvas);
	}

	void redrawCanvas() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for(Figure figure : canvasState.figures()) {
			if (figure == selectedFigure) {
				gc.setStroke(Color.RED);
			} else {
				gc.setStroke(figure.getLineColor());
			}
			gc.setLineWidth(figure.getLineThickness());
			gc.setFill(figure.getfillColor());
			if (figure instanceof Rectangle) {
				Rectangle rectangle = (Rectangle) figure;
				gc.fillRect(rectangle.getTopLeft().getX(), rectangle.getTopLeft().getY(),
						Math.abs(rectangle.getTopLeft().getX() - rectangle.getBottomRight().getX()), Math.abs(rectangle.getTopLeft().getY() - rectangle.getBottomRight().getY()));
				gc.strokeRect(rectangle.getTopLeft().getX(), rectangle.getTopLeft().getY(),
						Math.abs(rectangle.getTopLeft().getX() - rectangle.getBottomRight().getX()), Math.abs(rectangle.getTopLeft().getY() - rectangle.getBottomRight().getY()));
			} else if (figure instanceof Ellipse) {
				Ellipse ellipse = (Ellipse) figure;
				double dx = ellipse.getDx();
				double dy = ellipse.getDy();
				gc.fillOval(ellipse.getCenterPoint().getX() - (dx/2), ellipse.getCenterPoint().getY() - (dy/2), dx, dy);
				gc.strokeOval(ellipse.getCenterPoint().getX() - (dx/2), ellipse.getCenterPoint().getY() - (dy/2), dx, dy);
			} else if (figure instanceof Line) {
				Line line = (Line) figure;
				gc.strokeLine(line.getStart().getX(), line.getStart().getY(), line.getEnd().getX(), line.getEnd().getY());
			}
		}
	}

	boolean figureBelongs(Figure figure, Point eventPoint) {
		return figure.contains(eventPoint);
	}

}
