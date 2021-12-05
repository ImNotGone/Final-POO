package frontend;

import backend.CanvasState;
import backend.model.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;


public class PaintPane extends BorderPane {

	/* ========== Declaración de macros ========== */

	private static final int CANVAS_WIDTH = 800;
	private static final int CANVAS_HEIGHT = 600;
	private static final int MOVEMENT_SPEED = 100; //Mientras más chico el valor, más rápido se mueven
	private static final double SELECTION_RECTANGLE_LINE_WIDTH = 1.0;
	private static final BackendColor SELECTION_RECTANGLE_LINE_COLOR = BackendColor.RED;
	private static final BackendColor SELECTION_RECTANGLE_FILL_COLOR = BackendColor.TRANSPARENT;
	private static final Color FIGURE_SELECTION_LINE_COLOR = Color.RED;
	private static final String CREATING_FIGURE_STRING = "Creando figura: ";
	private static final String CREATED_FIGURE_STRING = "Figura creada: ";
	private static final String CANCELED_FIGURE_CREATION_STRING = "Acción cancelada";
	private static final String SELECTION_RECTANGLE_STRING = "Seleccionando figuras";
	private static final String SELECTION_STRING = "Están seleccionadas: ";
	private static final String DESELECTION_STRING = "Figuras deseleccionadas";
	private static final String NONE_FOUND_STRING = "Ninguna figura encontrada";
	private static final String CANT_MOVE_FORWARD_STRING = "Debe seleccionar las figuras que desea mover al frente";
	private static final String CANT_MOVE_BACK_STRING = "Debe seleccionar las figuras que desea mover al fondo";
	private static final String CANT_DELETE_STRING = "Debe seleccionar las figuras que desea borrar";
	private static final String ON_DELETION_STRING = "Se borraron las figuras seleccionadas";
	private static final String MUST_SELECT_BUTTON_STRING = "Debe seleccionar algún botón";

	/* ========== Variables de instancia ========== */

	// BackEnd
	private final CanvasState canvasState;

	// StatusPane
	private final StatusPane statusPane;

	// Canvas y GraphicsContext
	private final Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
	private final GraphicsContext gc = canvas.getGraphicsContext2D();

	// Barra de botones
	private final ToolBar toolBar = new ToolBar();

	// Dibujar una figura
	private Point startPoint;
	private Figure currentFigure;


	public PaintPane(CanvasState canvasState, StatusPane statusPane) {

		this.canvasState = canvasState;
		this.statusPane = statusPane;

		setLeft(toolBar);
		setRight(canvas);

		/* ========== Acciones para los botones de la toolBar ========== */
		// Decidimos que al apretar los botones de figuras se deseleccionen todas las figuras
		toolBar.setFigureButtonAction(event -> {canvasState.resetSelectedFigures(); redrawCanvas();});

		// Al tocar el botón de borrar, mover al fondo o mover al frente
		// si no hay figuras seleccionadas no se realiza la acción y se muestra un mensaje en la barra de estado.
		// Luego de tocar el botón, se selecciona automáticamente el botón de selección para comodidad del usuario

		toolBar.setForwardAction(event -> {
			if(isSelectedActionPossible(CANT_MOVE_FORWARD_STRING)) {
				canvasState.moveSelectedForward();
				redrawCanvas();
			}
			toolBar.changeToSelect();
		});
		toolBar.setBackAction(event -> {
			if(isSelectedActionPossible(CANT_MOVE_BACK_STRING)) {
				canvasState.moveSelectedBack();
				redrawCanvas();
			}
			toolBar.changeToSelect();
		});
		toolBar.setDeleteAction(event -> {
			if(isSelectedActionPossible(CANT_DELETE_STRING)) {
				canvasState.deleteSelected();
				redrawCanvas();
				statusPane.updateStatus(ON_DELETION_STRING);
			}
			toolBar.changeToSelect();
		});
		toolBar.setLineWidthSliderAction(event -> {canvasState.setSelectedFigureLineWidth(toolBar.getLineWidth()); redrawCanvas();});
		toolBar.setLineColorPickerAction(event -> {canvasState.setSelectedFiguresLineColor(toBackendColor(toolBar.getLineColor())); redrawCanvas();});
		toolBar.setFillColorPickerAction(event -> {canvasState.setSelectedFiguresFillColor(toBackendColor(toolBar.getFillColor())); redrawCanvas();});

		/* ========== Mouse events en el Canvas ========== */

		canvas.setOnMousePressed(event -> {

			// Si se esta creando una figura y se aprieta el mouse antes de soltarlo
			// se cancela la creacion de la figura y se actualiza la barra de estado
			if (currentFigure != null) {
				resetCurrentFigure();
				redrawCanvas();
				statusPane.updateStatus(CANCELED_FIGURE_CREATION_STRING);
			}

			// Si hay algún botón presionado actualizamos el punto de inicio (startPoint) con el punto actual.
			// Si no hay ninguno presionado se muestra un mensaje en la barra de estado
			for (ToggleButton button : toolBar.getButtons()) {
				if (button.isSelected()) {
					startPoint = new Point(event.getX(), event.getY());
					return;
				}
			}
			statusPane.updateStatus(MUST_SELECT_BUTTON_STRING);
		});

		canvas.setOnMouseReleased(event -> {
			// Si no existe un punto de inicio no hacemos nada para evitar un NullPointerException
			// Si el mouse se soltó en el mismo lugar que cuando se apretó no hacemos nada para no interferir con el "click"
			if (startPoint == null || event.isStillSincePress()) {
				resetStartPoint();
				return;
			}

			Point endPoint = new Point(event.getX(), event.getY());

			// Una vez soltado el mouse reseteamos el punto de inicio
			resetStartPoint();

			// Esto se hace para deseleccionar las figuras si al soltar el mouse luego de moverlas el mouse no está dentro de alguna
			if (!canvasState.isSelectedFiguresEmpty()) {
				if (!canvasState.belongsToASelectedFigure(endPoint)) {
					canvasState.resetSelectedFigures();
					statusPane.updateStatus(DESELECTION_STRING);
				}
				redrawCanvas();
				return;
			}

			// Antes de interactuar con la figura en creación 'currentFigure' chequeamos que esta no sea null
			if (currentFigure == null)
				return;

			// Una vez suelto el mouse, si está seleccionado el botón de selección y no hay figuras seleccionadas
			// 'currentFigure' es la figura de selección, entonces seleccionamos las figuras que se encuentren dentro de esta
			if (toolBar.isSelectionButtonSelected() && canvasState.isSelectedFiguresEmpty()) {
				if (!canvasState.selectFigures(currentFigure))
					canvasState.resetSelectedFigures();

				updateSelectedFiguresLabel(false);
				redrawCanvas();
				resetCurrentFigure();
				return;
			}

			// Una vez suelto el mouse, la figura que creo el usuario se agrega al canvasState
			// Si el botón de selección está activo, 'currentFigure' es el rectángulo de selección, entonces no se agrega
			if(!toolBar.isSelectionButtonSelected()) {
				canvasState.addFigure(currentFigure);
				statusPane.updateStatus(CREATED_FIGURE_STRING + currentFigure.toString());
				resetCurrentFigure();
			}
		});


		canvas.setOnMouseClicked(event -> {
			// Si no está apretado el botón de selección no hay nada que hacer
			// Este evento no debería interferir con el de soltar el mouse,
			// entonces agregamos la condición de que el mouse apriete y suelte en el mismo lugar
			if(!(toolBar.isSelectionButtonSelected() && event.isStillSincePress()))
				return;

			Point eventPoint = new Point(event.getX(), event.getY());

			// Si se clickea en una figura, esta cambia su estado de selección
			// Si no se clickea en ninguna figura se deseleccionan todas las figuras
			boolean found = canvasState.toggleSelectionFigure(eventPoint);
			if (!found) {
				canvasState.resetSelectedFigures();
			}
			updateSelectedFiguresLabel(found);
			redrawCanvas();
		});

		canvas.setOnMouseMoved(event -> {
			// Si hay figuras seleccionadas el mensaje de estado (Status Label)
			// no debería actualizarse al mover el mouse (debe seguir mostrando las figuras seleccionadas)
			if (!canvasState.isSelectedFiguresEmpty())
				return;

			// Si no hay figuras seleccionadas, busca una figura en el punto actual
			// si la encuentra imprime que figura es y sus propiedades (Lo cambiamos para que imprima solo la superior)
			// si no encuentra una figura imprime las coordenadas actuales del mouse
			Point eventPoint = new Point(event.getX(), event.getY());
			Figure figureOnPoint = canvasState.getFigureOnPoint(eventPoint);

			statusPane.updateStatus( figureOnPoint == null ? eventPoint.toString() : figureOnPoint.toString());
		});

		canvas.setOnMouseDragged(event -> {
			// Si no existe un punto de inicio no hacemos nada para evitar un NullPointerException
			if (startPoint == null)
				return;

			Point eventPoint = new Point(event.getX(), event.getY());

			// Si está seleccionado el botón de selección y hay figuras seleccionadas, las muevo
			if(toolBar.isSelectionButtonSelected() && !canvasState.isSelectedFiguresEmpty()) {
				double diffX = (eventPoint.getX() - startPoint.getX()) / MOVEMENT_SPEED;
				double diffY = (eventPoint.getY() - startPoint.getY()) / MOVEMENT_SPEED;
				canvasState.moveSelectedFigures(diffX, diffY);
				redrawCanvas();
				return;
			}

			// Se cambió la funcionalidad de la creación de figuras a la acción de 'drag' asi el usuario
			// puede ver en tiempo real la figura que está creando, al soltar el mouse se termina de crear la figura y se agrega al 'canvasState'

			// Mientras se 'draguea' el mouse, actualizamos el mensaje de estado (Status Label) con el punto actual
			// si el mouse se va afuera del canvas, no lo actualizamos
			if(isOnCanvas(eventPoint))
				statusPane.updateStatus(eventPoint.toString());

			// Solo se permite crear figuras de esquina superior hacía esquina inferior, las líneas se pueden crear para cualquier dirección
			if (!isEndPointValid(eventPoint) && !toolBar.isLineButtonSelected()) {
				redrawCanvas();
				resetCurrentFigure();
				return;
			}

			// Si está activo el botón de selección creamos el rectángulo de selección
			// si no iteramos por los botones para ver que figura creamos
			if (toolBar.isSelectionButtonSelected()) {
				currentFigure = new Rectangle(SELECTION_RECTANGLE_LINE_WIDTH, SELECTION_RECTANGLE_LINE_COLOR, SELECTION_RECTANGLE_FILL_COLOR, startPoint, eventPoint);
			} else {
				for (FigureToggleButton figureButton : toolBar.getFigureButtons()) {
					if (figureButton.isSelected()) {
						currentFigure = figureButton.buildFigure(toolBar.getLineWidth(), toBackendColor(toolBar.getLineColor()), toBackendColor(toolBar.getFillColor()), startPoint, eventPoint);
					}
				}
			}

			// Dibujamos la figura actual y actualizamos el mensaje de estado (Status Label)
			if(currentFigure != null) {
				redrawCanvas();
				drawFigure(currentFigure);
				statusPane.updateStatus(toolBar.isSelectionButtonSelected() ? SELECTION_RECTANGLE_STRING : CREATING_FIGURE_STRING + currentFigure.toString());
			}
		});
	}

	/* ========== Métodos auxiliares ========== */

	// Reinicia el canvas y dibuja todas las figuras de nuevo
	private void redrawCanvas() {
		gc.clearRect(0, 0, getWidth(), getHeight());
		for(Figure figure : canvasState.figures()) {
			drawFigure(figure);
		}
	}

	// Se encarga de dibujar la figura que recibe, si la misma está seleccionada le pone un borde rojo para distinguirla
	// No encontramos una manera de limpiar la parte del instanceof sin ensuciar el back
	// Lamentablemente la tuvimos que dejar asi
	private void drawFigure(Figure figure) {
		gc.setStroke(canvasState.selectedFigures().contains(figure) ? FIGURE_SELECTION_LINE_COLOR : toFxColor(figure.getLineColor()));
		gc.setLineWidth(figure.getLineWidth());
		gc.setFill(toFxColor(figure.getFillColor()));
		if (figure instanceof Rectangle ) {
			Rectangle rectangle = (Rectangle) figure;
			gc.fillRect(rectangle.getTopLeft().getX(), rectangle.getTopLeft().getY(),
					Math.abs(rectangle.getTopLeft().getX() - rectangle.getBottomRight().getX()), Math.abs(rectangle.getTopLeft().getY() - rectangle.getBottomRight().getY()));
			gc.strokeRect(rectangle.getTopLeft().getX(), rectangle.getTopLeft().getY(),
					Math.abs(rectangle.getTopLeft().getX() - rectangle.getBottomRight().getX()), Math.abs(rectangle.getTopLeft().getY() - rectangle.getBottomRight().getY()));
		} else if (figure instanceof Ellipse ) {
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

	// Actualiza el mensaje de estado (Status Label)
	// Si hay figuras seleccionadas se muestra la descripción de las mismas
	// Si no hay figuras seleccionadas y 'found' es verdadero es porque se hizo click en la última figura seleccionada
	// Si no hay figuras seleccionadas y 'found' es false es porque se hizo click afuera de las figuras y no se encontró ninguna
	private void updateSelectedFiguresLabel(boolean found) {
		if(canvasState.isSelectedFiguresEmpty()) {
			statusPane.updateStatus(found ? DESELECTION_STRING : NONE_FOUND_STRING);
			return;
		}
		StringBuilder label = new StringBuilder(SELECTION_STRING);
		for (Figure selectedFigure : canvasState.selectedFigures()) {
			label.append(selectedFigure.toString());
		}
		statusPane.updateStatus(label.toString());
	}

	// Si no hay figuras seleccionadas, al tocarse el botón de borrar, mandar al frente o mandar al fondo
	// se muestra 'errorMessage' en el mensaje de estado, devuelve si hay figuras seleccionadas
	private boolean isSelectedActionPossible(String errorMessage) {
		if(canvasState.isSelectedFiguresEmpty()) {
			statusPane.updateStatus(errorMessage);
			return false;
		}
		return true;
	}

	private void resetStartPoint() {
		startPoint = null;
	}

	private void resetCurrentFigure() {
		currentFigure = null;
	}

	// Verifica que el endpoint este abajo a la izq del start point
	private boolean isEndPointValid(Point endPoint) {
		return !(endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY());
	}

	// Verifica que un punto este dentro del canvas
	private boolean isOnCanvas(Point eventPoint) {
		return eventPoint.getX() >= 0 && eventPoint.getY() >= 0 && eventPoint.getX() <= CANVAS_WIDTH && eventPoint.getY() <= CANVAS_HEIGHT;
	}

	// Genera un color de javaFX a partir de un color de back-end
	private Color toFxColor(BackendColor backendColor) {
		return new Color(backendColor.getRed(), backendColor.getGreen(), backendColor.getBlue(), backendColor.getTransparency());
	}

	// Genera un color del back-end a partir de un color de javaFX
	private BackendColor toBackendColor(Color fxColor) {
		return new BackendColor(fxColor.getRed(), fxColor.getGreen(), fxColor.getBlue(), fxColor.getOpacity());
	}
}
