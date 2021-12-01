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

import java.util.*;


public class PaintPane extends BorderPane {

	// UI Size
	final int WIDTH = 800;
	final int HEIGHT = 600;

	// Slider defaults
	final int MIN_LINE_WIDTH = 1;
	final int MAX_LINE_WIDTH = 50;
	final int DEFAULT_LINE_WIDTH = 5;

	// BackEnd
	CanvasState canvasState;

	// Canvas y relacionados
	Canvas canvas = new Canvas(WIDTH, HEIGHT);
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
	
	ToggleButton deleteButton = new ToggleButton("Borrar");
	ToggleButton backButton = new ToggleButton("Al fondo");
	ToggleButton fowardButton = new ToggleButton("Al frente");

	// Sliders
	Slider lineWidthSlider = new Slider(MIN_LINE_WIDTH, MAX_LINE_WIDTH, DEFAULT_LINE_WIDTH);

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
	List<Figure> selectedFigures = new ArrayList<>();

	// StatusBar
	StatusPane statusPane;

	public PaintPane(CanvasState canvasState, StatusPane statusPane) {
		this.canvasState = canvasState;
		this.statusPane = statusPane;
		ToggleButton[] toolsArr = {selectionButton, rectangleButton, circleButton, squareButton, ellipseButton, lineButton, deleteButton, backButton, fowardButton};
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

		selectionButton.setOnAction(event -> {canvasState.resetSelectedFigures(); redrawCanvas();});
		squareButton.setOnAction(event -> {canvasState.resetSelectedFigures(); redrawCanvas();});
		rectangleButton.setOnAction(event -> {canvasState.resetSelectedFigures(); redrawCanvas();});
		circleButton.setOnAction(event -> {canvasState.resetSelectedFigures(); redrawCanvas();});
		ellipseButton.setOnAction(event -> {canvasState.resetSelectedFigures(); redrawCanvas();});
		lineButton.setOnAction(event -> {canvasState.resetSelectedFigures(); redrawCanvas();});
		deleteButton.setOnAction(event -> {canvasState.removeAll(canvasState.selectedFigures()); redrawCanvas(); canvasState.resetSelectedFigures();});
		fowardButton.setOnAction(event -> {canvasState.removeAll(canvasState.selectedFigures()); canvasState.addAll(canvasState.selectedFigures()); canvasState.resetSelectedFigures(); redrawCanvas();});
		backButton.setOnAction(event -> {canvasState.removeAll(canvasState.selectedFigures()); canvasState.addAllToStart(canvasState.selectedFigures()); ;canvasState.resetSelectedFigures(); redrawCanvas();});
		canvas.setOnMousePressed(event -> startPoint = new Point(event.getX(), event.getY()));
		lineWidthSlider.setOnMouseDragged((event) -> {
			for(Figure figure : canvasState.selectedFigures()) {
				figure.setLineWidth(lineWidthSlider.getValue());
			}
			redrawCanvas();
		});
		lineColorPicker.setOnAction((event) -> {
			for(Figure figure : canvasState.selectedFigures()) {
				figure.setLineColor(lineColorPicker.getValue());
			}
			//selectedFigures = new ArrayList<>();
			redrawCanvas();
		});
		fillColorPicker.setOnAction((event) -> {
			for(Figure figure : canvasState.selectedFigures()) {
				figure.setFillColor(fillColorPicker.getValue());
			}
			//selectedFigures = new ArrayList<>();
			redrawCanvas();
		});

		canvas.setOnMouseReleased(event -> {
			Point endPoint = new Point(event.getX(), event.getY());
			if(startPoint == null) {
				return ;
			}

			// Si se movio el mouse y hay figuras seleccionadas, me fijo caigo so
			if (!canvasState.selectedFigures().isEmpty() && !event.isStillSincePress()) {
				if (!checkPointInSelected(endPoint))
					canvasState.resetSelectedFigures();
			} else if((endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY()) && !lineButton.isSelected()) {
				return;
			} else if (selectionButton.isSelected() && canvasState.selectedFigures().isEmpty()) {
				Figure selectionRectangle = new Rectangle(DEFAULT_LINE_WIDTH, Color.RED, Color.TRANSPARENT, startPoint, endPoint);
				selectFigures(selectionRectangle);
			}
			for (FigureToggleButton figureButton : figureToggleButtonArr) {
				if(figureButton.isSelected()) {
					Figure newFigure = figureButton.getFigure(lineWidthSlider.getValue(), lineColorPicker.getValue(), fillColorPicker.getValue(), startPoint, endPoint);
					canvasState.addFigure(newFigure);
					// en vez de dibujar todas las figuras, dibujo la nueva arriba de todas
					drawFigure(newFigure);
					startPoint = null;
					// para que no se dibuje todas las figuras denuevo
					return;
				}
			}
			startPoint = null;
			redrawCanvas();
		});

		canvas.setOnMouseMoved(event -> {
			if (!canvasState.selectedFigures().isEmpty())
				return;
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
			if(selectionButton.isSelected() && event.isStillSincePress()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				StringBuilder label = new StringBuilder("Se seleccionó: ");
				List<Figure> aux = canvasState.figures();
				Collections.reverse(aux);
				for (Figure figure : aux) {
					if(figureBelongs(figure, eventPoint)) {
						canvasState.selectFigure(figure);
						label.append(figure);
						statusPane.updateStatus(label.toString());
						redrawCanvas();
						return;
					}
				}
				canvasState.resetSelectedFigures();
				statusPane.updateStatus("Ninguna figura encontrada");
				redrawCanvas();
			}
		});
		canvas.setOnMouseDragged(event -> {
			// si el usuario hace click derecho afuera mientras mantiene el click izquierdo
			// cuando vuelve a la pantalla se rompe
			if(startPoint == null) {
				return ;
			}
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				double diffX = (eventPoint.getX() - startPoint.getX()) / 100;
				double diffY = (eventPoint.getY() - startPoint.getY()) / 100;
				if(!canvasState.selectedFigures().isEmpty()) {
					for(Figure selectedFigure : canvasState.selectedFigures())
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
			drawFigure(figure);
		}
	}

	// horrible el code
	void drawFigure(Figure figure) {
		if (canvasState.selectedFigures().contains(figure)) {
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

	void selectFigures(Figure selectionFigure) {
		StringBuilder label = new StringBuilder("Se seleccionó: ");
		if (canvasState.selectFigures(selectionFigure)) {
			for (Figure selectedFigure : canvasState.selectedFigures()) {
				label.append(selectedFigure.toString());
			}
			statusPane.updateStatus(label.toString());
		} else {
			canvasState.resetSelectedFigures();
			statusPane.updateStatus("Ninguna figura encontrada");
		}
	}

	boolean checkPointInSelected(Point eventPoint) {
		boolean found = false;
		for(Figure selectedFigure : canvasState.selectedFigures()) {
			if (figureBelongs(selectedFigure, eventPoint))
				found = true;
		}
		return found;
	}

	boolean figureBelongs(Figure figure, Point eventPoint) {
		return figure.contains(eventPoint);
	}

	boolean figureContains(Figure figure, Figure figureContainer) {
		return figure.isContained(figureContainer);
	}


}
