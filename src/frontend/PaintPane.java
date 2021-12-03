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

	// UI Size
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	// Slider defaults
	private static final int MIN_LINE_WIDTH = 1;
	private static final int MAX_LINE_WIDTH = 50;
	private static final int DEFAULT_LINE_WIDTH = 5;

	// BackEnd
	private final CanvasState canvasState;

	// Canvas y relacionados
	private final Canvas canvas = new Canvas(WIDTH, HEIGHT);
	private final GraphicsContext gc = canvas.getGraphicsContext2D();

	// Botones Barra Izquierda
	private final ToggleButton selectionButton = new ToggleButton("Seleccionar");
	private final FigureToggleButton circleButton = new FigureToggleButton("Círculo", (lineWidth, lineColor, fillColor, startPoint, endPoint) -> new Circle(lineWidth, lineColor, fillColor, startPoint, Math.abs(endPoint.getX() - startPoint.getX())));
	private final FigureToggleButton squareButton = new FigureToggleButton("Cuadrado", (lineWidth, lineColor, fillColor, startPoint, endPoint) -> new Square(lineWidth, lineColor, fillColor, startPoint, Math.abs(startPoint.getX() - endPoint.getX())));
	private final FigureToggleButton rectangleButton = new FigureToggleButton("Rectángulo", (lineWidth, lineColor, fillColor, startPoint, endPoint) -> new Rectangle(lineWidth, lineColor, fillColor, startPoint, endPoint));
	private final FigureToggleButton ellipseButton = new FigureToggleButton("Elipse", (lineWidth, lineColor, fillColor, startPoint, endPoint) ->
	{double dx = Math.abs(startPoint.getX() - endPoint.getX());
		double dy = Math.abs(startPoint.getY() - endPoint.getY());
		double x = (startPoint.getX()+endPoint.getX())/2;
		double y = (startPoint.getY()+endPoint.getY())/2;
		return new Ellipse(lineWidth, lineColor, fillColor, new Point(x, y),dx,dy);});
	private final FigureToggleButton lineButton = new FigureToggleButton("Línea", (lineWidth, lineColor, fillColor, startPoint, endPoint) -> new Line(lineWidth, lineColor, fillColor, startPoint, endPoint));

	private final ToggleButton deleteButton = new ToggleButton("Borrar");
	private final ToggleButton backButton = new ToggleButton("Al fondo");
	private final ToggleButton fowardButton = new ToggleButton("Al frente");

	// Sliders
	private final Slider lineWidthSlider = new Slider(MIN_LINE_WIDTH, MAX_LINE_WIDTH, DEFAULT_LINE_WIDTH);

	// Line Label
	private final Label lineWidthLabel = new Label("Borde");

	// Fill Label
	private final Label fillLabel = new Label("Relleno");

	// ColorPicker
	private final ColorPicker lineColorPicker = new ColorPicker(Color.BLACK);
	private final ColorPicker fillColorPicker = new ColorPicker(Color.YELLOW);

	// Dibujar una figura
	private Point startPoint;

	// StatusBar
	private StatusPane statusPane;

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
		deleteButton.setOnAction(event -> {canvasState.deleteSelected(); redrawCanvas(); changeToSelect(deleteButton);});
		fowardButton.setOnAction(event -> {canvasState.moveSelectedForward(); redrawCanvas(); changeToSelect(fowardButton);});
		backButton.setOnAction(event -> {canvasState.moveSelectedBack(); redrawCanvas(); changeToSelect(backButton);});
		canvas.setOnMousePressed(event -> startPoint = new Point(event.getX(), event.getY()));
		lineWidthSlider.setOnMouseDragged((event) -> {
			canvasState.setSelectedFigureLineWidth(lineWidthSlider.getValue());
			redrawCanvas();
		});
		lineColorPicker.setOnAction((event) -> {
			canvasState.setSelectedFiguresLineColor(toJavaColor(lineColorPicker.getValue()));
			redrawCanvas();
		});
		fillColorPicker.setOnAction((event) -> {
			canvasState.setSelectedFiguresFillColor(toJavaColor(fillColorPicker.getValue()));
			redrawCanvas();
		});

		canvas.setOnMouseReleased(event -> {
			Point endPoint = new Point(event.getX(), event.getY());
			if (startPoint == null || event.isStillSincePress()) {
				return;
			}

			// Si se movio el mouse y hay figuras seleccionadas, me fijo caigo so
			/*
			if (!canvasState.isSelectedFiguresEmpty()) {
				if (!canvasState.checkPointIsInAnySelectedFigure(endPoint))
					canvasState.resetSelectedFigures();
			} else if (!isEndPointValid(endPoint) && !lineButton.isSelected()) {
				return;
			} else if (selectionButton.isSelected() && canvasState.isSelectedFiguresEmpty()) {
				Figure selectionRectangle = new Rectangle(DEFAULT_LINE_WIDTH, Color.RED, Color.TRANSPARENT, startPoint, endPoint);
				selectFigures(selectionRectangle);
			} else {
				for (FigureToggleButton figureButton : figureToggleButtonArr) {
					if (figureButton.isSelected()) {
						Figure newFigure = figureButton.getFigure(lineWidthSlider.getValue(), lineColorPicker.getValue(), fillColorPicker.getValue(), startPoint, endPoint);
						canvasState.addFigure(newFigure);
						// en vez de dibujar todas las figuras, dibujo la nueva arriba de todas
						drawFigure(newFigure);
						startPoint = null;
						// para que no se dibuje todas las figuras denuevo
						return;
					}
				}
			}
			startPoint = null;
			redrawCanvas();
			*/
			if (!canvasState.isSelectedFiguresEmpty()) {
				if (!canvasState.checkPointIsInAnySelectedFigure(endPoint))
					canvasState.resetSelectedFigures();
				startPoint = null;
				redrawCanvas();
				return;
			}
			if (!isEndPointValid(endPoint) && !lineButton.isSelected())
				return;

			if (selectionButton.isSelected() && canvasState.isSelectedFiguresEmpty()) {
				Figure selectionRectangle = new Rectangle(DEFAULT_LINE_WIDTH, java.awt.Color.RED, java.awt.Color.WHITE, startPoint, endPoint);
				if (!canvasState.selectFigures(selectionRectangle))
					canvasState.resetSelectedFigures();

				updateSelectedFiguresLabel(false);
				startPoint = null;
				redrawCanvas();
				return;
			}
			if(selectionButton.isSelected()) {return;}
			for (FigureToggleButton figureButton : figureToggleButtonArr) {
				if (figureButton.isSelected()) {
					Figure newFigure = figureButton.getFigure(lineWidthSlider.getValue(), toJavaColor(lineColorPicker.getValue()), toJavaColor(fillColorPicker.getValue()), startPoint, endPoint);
					canvasState.addFigure(newFigure);
					// en vez de dibujar todas las figuras, dibujo la nueva arriba de todas
					drawFigure(newFigure);
					startPoint = null;
					// para que no se dibuje todas las figuras denuevo
					return;
				}
			}
		});

		canvas.setOnMouseMoved(event -> {
			if (!canvasState.isSelectedFiguresEmpty())
				return;
			Point eventPoint = new Point(event.getX(), event.getY());
			Figure figureOnPoint = canvasState.getFigureOnPoint(eventPoint);

			if(figureOnPoint != null) {
				statusPane.updateStatus(figureOnPoint.toString());
			} else {
				statusPane.updateStatus(eventPoint.toString());
			}
		});

		canvas.setOnMouseClicked(event -> {
			// Si no esta apretado el boton de seleccion no hay nada que hacer
			// Este evento no deberia interferir con el de soltar el mouse,
			// entonces agregamos la condicion de que el mouse apriete y suelte en el mismo lugar
			if(!(selectionButton.isSelected() && event.isStillSincePress()))
				return;

			Point eventPoint = new Point(event.getX(), event.getY());
			boolean found = canvasState.toggleSelectionFigure(eventPoint);
			if (!found) {
				canvasState.resetSelectedFigures();
			}
			updateSelectedFiguresLabel(found);
			redrawCanvas();
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
				canvasState.moveSelectedFigures(diffX, diffY);
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

	// Cambiariamos el code pero mezcla el back y el front
	// Mejor lo dejamos asi :/
	void drawFigure(Figure figure) {
		if (canvasState.selectedFigures().contains(figure)) {
			gc.setStroke(Color.RED);
		} else {
			gc.setStroke(toFxColor(figure.getLineColor()));
		}
		gc.setLineWidth(figure.getLineThickness());
		gc.setFill(toFxColor(figure.getFillColor()));
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

	boolean isEndPointValid(Point endPoint) {
		return !(endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY());
	}

	void updateSelectedFiguresLabel(boolean found) {
		if(canvasState.isSelectedFiguresEmpty()) {
			statusPane.updateStatus(found ? "Ultima figura deseleccionada" :"Ninguna figura encontrada");
			return;
		}
		StringBuilder label = new StringBuilder("En seleccion: ");
		for (Figure selectedFigure : canvasState.selectedFigures()) {
			label.append(selectedFigure.toString());
		}
		statusPane.updateStatus(label.toString());
	}

	java.awt.Color toJavaColor(Color fxColor) {
		return new java.awt.Color((float) fxColor.getRed(),(float) fxColor.getGreen(),(float) fxColor.getBlue(),(float) fxColor.getOpacity());
	}

	Color toFxColor(java.awt.Color javaColor) {
		return new Color(javaColor.getRed() / 255.0, javaColor.getGreen() / 255.0, javaColor.getBlue() / 255.0, javaColor.getAlpha() / 255.0);
	}

	void changeToSelect(ToggleButton button) {
		button.setSelected(false);
		selectionButton.setSelected(true);
		selectionButton.requestFocus();
	}
}
