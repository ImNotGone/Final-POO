package backend.interfaces;

import backend.model.BackendColor;

public interface Coloreable {

    // Getters y setters para las propiedades estéticas de las figuras
    BackendColor getLineColor();

    BackendColor getFillColor();

    double getLineWidth();

    void setLineWidth(double lineWidth);

    void setLineColor(BackendColor lineColor);

    void setFillColor(BackendColor fillColor);
}
