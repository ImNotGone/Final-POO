package backend;

import backend.model.Figure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CanvasState extends ArrayList<Figure>{

    public CanvasState(){
        super();
    }

    public boolean addFigure(Figure figure) {
        return this.add(figure);
    }


    public List<Figure> figures() {
        return new ArrayList<>(this);
    }


}
