package eu.mihosoft.jgrounds;

import eu.mihosoft.scaledfx.ScalableContentPane;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

public class MapView extends ScalableContentPane {

    private final int mapPadding = 10;

    private final Map map;
    private final Pane mapPane;

    private final int tileWidth = 64;
    private final int tileHeight = 32;

    public MapView(Map map) {

        this.map = map;
        mapPane = new Pane();

        Group mapGroup = new Group(mapPane);
        Rectangle boundsRect = new Rectangle();
        boundsRect.widthProperty().bind(mapPane.widthProperty());
        boundsRect.heightProperty().bind(mapPane.heightProperty());
        mapPane.setClip(boundsRect);

        StackPane container = new StackPane(mapGroup);
        StackPane.setAlignment(mapPane,Pos.CENTER);

        setContent(container);

        this.setPadding(new Insets(mapPadding));
    }

    public void addTile(Node node, int i, int j) {
        
        double x = (j * getTileWidth()  / 2) + (i * getTileWidth()  / 2);
        double y = (i * getTileHeight() / 2) - (j * getTileHeight() / 2);

        // center y
        y += (map.getHeight() * tileHeight) / 2.0;

        node.setLayoutX(x);
        node.setLayoutY(y);

        getMapPane().getChildren().add(node);
    }

    public void removeNode(Node n) {
        if(n== null) return;
        getMapPane().getChildren().remove(n);
    }

    /**
     * @return the tileWidth
     */
    public int getTileWidth() {
        return tileWidth;
    }

    /**
     * @return the tileHeight
     */
    public int getTileHeight() {
        return tileHeight;
    }

    /**
     * @return the mapPane
     */
    public Pane getMapPane() {
        return mapPane;
    }
}
