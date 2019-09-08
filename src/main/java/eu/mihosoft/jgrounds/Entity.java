package eu.mihosoft.jgrounds;

import javafx.animation.FadeTransition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Entity {

    private final String type;
    private Map map;
    private TileView view;
    private boolean entity;

    private IntegerProperty xProp = new SimpleIntegerProperty();
    private IntegerProperty yProp = new SimpleIntegerProperty();

    private int z;

    public Entity(Map map, String type, int x, int y) {
        this(map, type, true, x, y);
    }

    private Entity(Map map, String type, boolean isEntity, int x, int y) {
        this.map = map;
        this.type = type;
        this.entity = isEntity;
        this.setLocation(x, y);
        this.view = new TileView(type, isEntity);

        if(Character.isDigit(type.charAt(0)) || "t".equals(type.toLowerCase()) || (!isEntity && "G".equals(type))) {
            z = 0;
        } else if((isEntity && "G".equals(type))) {
            z = 2;
            setShadow(true);
        } else {
            z = 1;
        }
    }

    public static Entity newTile(Map map, String type, int x, int y) {
        return new Entity(map, type, false, x, y);
    }

    public TileView getView() {
        return view;
    }

    public void setLocation(int x, int y) {
        xProperty().set(x);
        yProperty().set(y);

        // set shadow

    }

    void setShadow(boolean shadow) {
        if(!isEntity()) {
            view.showShadow(shadow); 
        } else {
            Entity tile = map.getTileByLocation(getX(),getY());
            if(tile!=null) {
                tile.setShadow(shadow);
            }
        }
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

    public boolean isEntity() {
        return this.entity;
    }

    void setEntity(boolean entity) {
        this.entity = entity;
    }
}