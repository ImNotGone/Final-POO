package frontend;

import backend.CanvasState;
import backend.model.*;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
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
	FigureToggleButton rectangleButton = new FigureToggleButton("Rectángulo", (lineWidth, lineColor, fillColor, startPoint, endPoint) -> new Rectangle(lineWidth, lineColor, fillColor, startPoint, endPoint));
	FigureToggleButton circleButton = new FigureToggleButton("Círculo", (lineWidth, lineColor, fillColor, startPoint, endPoint) -> new Circle(lineWidth, lineColor, fillColor, startPoint, Math.abs(endPoint.getX() - startPoint.getX())));
	FigureToggleButton squareButton = new FigureToggleButton("Cuadrado", (lineWidth, lineColor, fillColor, startPoint, endPoint) -> new Square(lineWidth, lineColor, fillColor, startPoint, Math.abs(startPoint.getX() - endPoint.getX())));
	FigureToggleButton ellipseButton = new FigureToggleButton("Elipse", (lineWidth, lineColor, fillColor, startPoint, endPoint) ->
	{double dx = Math.abs(startPoint.getX() - endPoint.getX());
		double dy = Math.abs(startPoint.getY() - endPoint.getY());
		double x = (startPoint.getX()+endPoint.getX())/2;
		double y = (startPoint.getY()+endPoint.getY())/2;
		return new Ellipse(lineWidth, lineColor, fillColor, new Point(x, y),dx,dy);});
	FigureToggleButton lineButton = new FigureToggleButton("Línea", (lineWidth, lineColor, fillColor, startPoint, endPoint) -> new Line(lineWidth, lineColor, fillColor, startPoint, endPoint));

	// Sliders
	Slider lineWidthSlider = new Slider(1, 50, 25);

	// Line Label
	Label lineWidthLabel = new Label("Borde");

	// Fill Label
	Label fillLabel = new Label("Relleno");

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
		FigureToggleButton[] figureToggleButtonArr = {rectangleButton, circleButton, squareButton, ellipseButton, lineButton};
		ToggleGroup tools = new ToggleGroup();
		for (ToggleButton tool : toolsArr) {
			tool.setMinWidth(90);
			tool.setToggleGroup(tools);
			tool.setCursor(Cursor.HAND);
		}

		lineWidthSlider.setShowTickMarks(true);
		lineWidthSlider.setBlockIncrement(1f);
		lineWidthSlider.setShowTickLabels(true);
		
		VBox buttonsBox = new VBox(10);
		// Buttons
		buttonsBox.getChildren().addAll(toolsArr);

		// Line options
		buttonsBox.getChildren().add(lineWidthLabel);
		buttonsBox.getChildren().add(lineWidthSlider);
		buttonsBox.getChildren().add(lineColorPicker);

		// Fill options
		buttonsBox.getChildren().add(fillLabel);
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
			for (FigureToggleButton figureButton : figureToggleButtonArr) {
				if(figureButton.isSelected()) {
					canvasState.addFigure(figureButton.getFigure(lineWidthSlider.getValue(), lineColorPicker.getValue(), fillColorPicker.getValue(), startPoint, endPoint));
				}
			}
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
