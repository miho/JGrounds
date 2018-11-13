package eu.mihosoft.jgrounds;

import eu.mihosoft.scaledfx.ScalableContentPane;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;

public class MapView extends ScalableContentPane {

    private final int mapPadding = 10;

    private final Map map;
    private final TileContainer mapPane;

    private final int tileWidth = 64;
    private final int tileHeight = 32;

    static class TileContainer extends Pane {

        public TileContainer(double width, double height) {
            setMaxSize(width, height);
            setMinSize(width, height);
        }

        @Override
        public ObservableList<Node> getChildren() {
            return super.getChildren();
        }
    }

    static class ScaleWrapper extends StackPane {
        @Override
        public void layoutChildren() {
            super.layoutChildren();
        }
    }

    public MapView(Map map) {

        this.map = map;
        mapPane = new TileContainer(tileWidth*map.getWidth(), tileHeight*map.getHeight()+16);
        //mapPane.setStyle("-fx-border-color: red");

        ScaleWrapper scaleWrapper = new ScaleWrapper();
        scaleWrapper.getChildren().add(mapPane);
        setContent(scaleWrapper);
        scaleWrapper.setManaged(false);
        scaleWrapper.setPrefSize(tileWidth*map.getWidth(), tileHeight*map.getHeight()+16);
        scaleWrapper.setMinSize(tileWidth*map.getWidth(), tileHeight*map.getHeight()+16);
        scaleWrapper.setMaxSize(tileWidth*map.getWidth(), tileHeight*map.getHeight()+16);
        this.setPadding(new Insets(mapPadding));
    }

    public void addTile(Node node, int i, int j) {
        double x = (j * getTileWidth() / 2) + (i * getTileWidth() / 2);
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

//                        p = new Polygon(
//                                getView().getTileWidth() / 2, 0.0,
//                                getView().getTileWidth(), getView().getTileHeight() / 2,
//                                getView().getTileWidth() / 2, getView().getTileHeight(),
//                                0.0, getView().getTileHeight() / 2
//                        );
//                        p.setStroke(Color.BLACK);
//                        p.setStrokeWidth(0.1);
//                        p.setFill(new ImagePattern(
//                                new Image("/eu/mihosoft/jgrounds/W" + ".png")));
