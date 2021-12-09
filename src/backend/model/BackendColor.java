package backend.model;

// Clase propia que modela un Color para no depender de una libreria de front-end

public class BackendColor {
    private final double red; // valor de rojo del color en escala de [0-1]
    private final double green; // valor de verde del color en escala de [0-1]
    private final double blue; // valor de blue del color en escala de [0-1]
    private final double transparency; // valor de trasparencia del color en escala de [0-1]

    // Macros que vamos a utilizar
    public static final BackendColor RED = new BackendColor(1.0, 0, 0, 1.0);
    public static final BackendColor TRANSPARENT = new BackendColor(0, 0, 0, 0);

    public BackendColor(double red, double green, double blue, double transparency) {
        this.red = red;
        this.blue = blue;
        this.green = green;
        this.transparency = transparency;
    }

    public double getRed() {
        return red;
    }

    public double getBlue() {
        return blue;
    }

    public double getGreen() {
        return green;
    }

    public double getTransparency() {
        return transparency;
    }
}
