package eu.mihosoft.jgrounds;

import javafx.animation.FadeTransition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;

public class Entity {

    private final String type;
    private Map map;
    private TileView view;

    private IntegerProperty xProp = new SimpleIntegerProperty();
    private IntegerProperty yProp = new SimpleIntegerProperty();

    public Entity(String type) {
        this.type = type;
        this.view = new TileView(type, true);
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Node getView() {
        return view;
    }

    public void setLocation(int x, int y) {
        xProperty().set(x);
        yProperty().set(y);
    }

    public void showError() {
        view.showError();
    }

    public FadeTransition showDone() {
        return view.showDone();
    }

    /**
     * @return the x
     */
    public int getX() {
        return xProperty().get();
    }

    /**
     * @return the y
     */
    public int getY() {
        return yProperty().get();
    }

    public IntegerProperty xProperty() {
        return xProp;
    }

    public IntegerProperty yProperty() {
        return yProp;
    }

    public String getType() {
        return type;
    }
}