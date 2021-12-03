package backend.model;

@FunctionalInterface
public interface Movable {
    void move(double diffX, double diffY);
}
