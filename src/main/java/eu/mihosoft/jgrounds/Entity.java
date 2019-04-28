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

    private int z;

    public Entity(String type) {
        this(type, true);
    }

    private Entity(String type, boolean isEntity) {
        this.type = type;
        this.view = new TileView(type, isEntity);

        if(Character.isDigit(type.charAt(0)) || "t".equals(type.toLowerCase()) || (!isEntity && "G".equals(type))) {
            z = 0;
        } else if((isEntity && "G".equals(type))) {
            z = 2;
        } else{
            z = 1;
        }
    }

    public static Entity newTile(String type) {
        return new Entity(type, false);
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public TileView getView() {
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
     * @return the x coordinate of this entity
     */
    public int getX() {
        return xProperty().get();
    }

    /**
     * @return the y coordinate of this entity
     */
    public int getY() {
        return yProperty().get();
    }

    /**
     *
     * @return the z coordinate of this entity
     */
    public int getZ() {
        return this.z;
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