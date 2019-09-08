package eu.mihosoft.jgrounds;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.util.Duration;

import java.util.*;

public class Map {

    private final int width;
    private final int height;

    private String floorMap;
    private String entityMap;

    private int sceneIndex;

    private final ObservableList<Entity> entities
            = FXCollections.observableArrayList();
    private final ObservableList<Entity> tiles
            = FXCollections.observableArrayList();

    private final ObservableList<Entity> gems
            = FXCollections.observableArrayList();

    private final Level level;

    private final MapView view;

    private final Node[] tileNodes;

    private boolean isParsing;

    public Map(Level level) {
        this.width = level.getMapWidth();
        this.height = level.getMapHeight();

        this.level = level;

        this.tileNodes = new Node[width*height];

        view = new MapView(this);

        entities.addListener((ListChangeListener.Change<? extends Entity> c) -> {
            while (c.next()) {

                for (Entity e : c.getAddedSubList()) {
                    view.getMapPane().getChildren().add(e.getView());

                    e.getView().toFront();
                    
                    updateLocation(e);

                    e.xProperty().addListener(l->updateLocation(e));
                    e.yProperty().addListener(l->updateLocation(e));

                } // end for

                for (Entity e : c.getRemoved()) {
                    e.setShadow(false);
                    view.getMapPane().getChildren().remove(e.getView());
                }
            }
        });

        parseMaps();

    }

    // private void updateYLocation(Entity e) {
    //     double x = (e.getY() * view.getTileWidth() / 2) + (e.getX() * view.getTileWidth() / 2);
    //     double y = (e.getX() * view.getTileHeight() / 2) - (e.getY() * view.getTileHeight() / 2);

    //     // center y
    //     y += (getHeight() * view.getTileHeight()) / 2.0 + view.getTileHeight();

    //     TranslateTransition transition = new TranslateTransition(Duration.millis(400), e.getView());

    //     transition.setFromX(e.getView().getTranslateX());
    //     transition.setFromY(e.getView().getTranslateY());
    //     transition.setToX(x);
    //     transition.setToY(y);
        
    //     if(Double.compare(y, 0) == 0 || Double.compare(e.getView().getTranslateY(), 0) == 0 ) {
    //         e.getView().setTranslateX(x);
    //         e.getView().setTranslateY(y);

    //         FadeTransition transition2 = new FadeTransition(Duration.millis(800), e.getView());
    //         transition2.setFromValue(0.0);
    //         transition2.setToValue(1.0);
    //         //transition2.setCycleCount(3);
    //         //transition2.setAutoReverse(true);
    //         transition2.play();

    //     } else {
    //         transition.play();
    //     }

    //     boolean yBigger = y > e.getView().getTranslateY();
    //     boolean ySmaller = y < e.getView().getTranslateY();

    //     if (yBigger) {
    //         updateZOrder(e);
    //     }

    //     transition.setOnFinished((ae) -> {

    //         if (ySmaller) {
    //             updateZOrder(e);
    //         }

    //         if (!isParsing && getCurrentScene().getGoalCondition().check(this)) {
    //             if (sceneIndex + 1 >= level.getScenes().size()) {
    //                 e.showDone().setOnFinished((aev) -> {
    //                     nextScene();
    //                 });
    //             } else {
    //                 nextScene();
    //             }
    //         }
    //     });

    // }

    private void updateLocation(Entity e) {
        
        double x = (e.getY() * view.getTileWidth() / 2) + (e.getX() * view.getTileWidth() / 2);
        double y = (e.getX() * view.getTileHeight() / 2) - (e.getY() * view.getTileHeight() / 2);

        // center y
        y += (getHeight() * view.getTileHeight()) / 2.0 + view.getTileHeight();



        TranslateTransition transition = new TranslateTransition(Duration.millis(400), e.getView());
        transition.setFromX(e.getView().getTranslateX());
        transition.setFromY(e.getView().getTranslateY());
        transition.setToX(x);
        transition.setToY(y);

        if(Double.compare(x, 0) == 0 || Double.compare(e.getView().getTranslateX(), 0) == 0 ) {
            e.getView().setTranslateX(x);
            e.getView().setTranslateY(y);

            FadeTransition transition2 = new FadeTransition(Duration.millis(1500), e.getView());
            transition2.setFromValue(0.0);
            transition2.setToValue(1.0);
            transition2.play();

        } else {
            transition.play();
        }

        boolean xBigger = x > e.getView().getTranslateX();
        boolean xSmaller = x < e.getView().getTranslateX();

        if (xBigger) {
            updateZOrder(e);
        }

        transition.setOnFinished((ae) -> {

            if (xSmaller) {
                updateZOrder(e);
            }

            if (!isParsing && getCurrentScene().getGoalCondition().check(this)) {
                if (sceneIndex + 1 >= level.getScenes().size()) {
                    e.showDone().setOnFinished((aev) -> {
                        nextScene();
                    });
                } else {
                    nextScene();
                }
            }
        });
    }

    private void updateZOrder(Entity entity) {

        Comparator<Entity> comparator = (o1, o2) -> {

            if(o2.getZ() != o1.getZ()) {
                return Double.compare(o2.getZ(), o1.getZ());
            }

            int tileIndex1 = coordsToTileIndex(o1.getX(), o1.getY());
            int tileIndex2 = coordsToTileIndex(o2.getX(), o2.getY());

            return Double.compare(tileIndex2, tileIndex1);
        };

        List<Entity> zOrder = new ArrayList<>();
        zOrder.addAll(tiles);
        zOrder.addAll(entities);
        zOrder.addAll(gems);

        Collections.sort(zOrder, comparator);

        for(int idx = 0; idx < zOrder.size(); idx++) {
            Entity e = zOrder.get(idx);
            e.getView().setViewOrder(idx);
        }

    }

    public void nextScene() {
        sceneIndex++;

        if(sceneIndex >= level.getScenes().size()) {
            level.levelDonePropertyWritable().set(true);
            return;
        }

        parseMaps();

//        List<Entity> revertedEntities = new ArrayList<>(entities);
//        Collections.reverse(revertedEntities);
//        revertedEntities.forEach(entity -> entity.getView().toFront());


    }

    public Scene getCurrentScene() {
        return level.getScenes().get(sceneIndex);
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    public char getEntityTypeAt(int x, int y) {
        int tileIndex = x + (height - 1 - y) * width;

        return entityMap.charAt(tileIndex);
    }

    public char getTileTypeAt(int x, int y) {
        int tileIndex = x + (height - 1 - y) * width;

        return floorMap.charAt(tileIndex);
    }

    private int coordsToTileIndex(int x, int y) {
        return x + (height - 1 - y) * width;
    }

    private void parseMaps() {

        isParsing = true;

        String prevFloorMap = floorMap;
        String prevEntityMap = entityMap;

        Scene scene = level.getScenes().get(sceneIndex);

        floorMap = scene.getTilesMap();
        entityMap = scene.getEntityMap();

        for (int i = 0; i < width; i++) {
            for (int j = height - 1; j >= 0; j--) {

                TileView tileView;

                int tileIndex = coordsToTileIndex(i,j);

                char tileType = floorMap.charAt(tileIndex);

                boolean tileTypesDiffer = true;

                if (prevFloorMap!=null) {
                    char prevTileType = prevFloorMap.charAt(tileIndex);
                    tileTypesDiffer = prevTileType!=tileType;
                }

                //if(tileTypesDiffer) {
                    switch (tileType) {
                        case ' ':
                            tileView = null;
                            break;
                        default:
//                            tileView =  new TileView("" + tileType);
                            Entity tile = Entity.newTile(this, "" + tileType, i, j);
                            tileView = tile.getView();
                            tiles.add(tile);
                            tile.setLocation(i, j);
                            break;
                    }

                    if (tileView != null) {
                        if(tileTypesDiffer) {
                            getView().removeNode(tileNodes[tileIndex]);
                        }
                        getView().addTile(tileView, i, j);
                        tileNodes[tileIndex] = tileView;
                    }
               // } // end if tile types are different

                char entityType = entityMap.charAt(tileIndex);

                switch (entityType) {
                    case '0':
                        break;
                    case ' ':
                        break;
                    default:
                        Entity entity = new Entity(this, ""+entityType, i, j);
                        entities.add(entity);
                        entity.setLocation(i, j);
                        break;
                }
            } // end for j
        } // end for i

        isParsing = false;

        updateZOrder(null);
    }

    /**
     * Returns a tile specified by location.
     * @param x x location
     * @param y y location
     * @return the tile specified by location
     */
    public Entity getTileByLocation(int x, int y) {
        return tiles.stream().filter(
            tile->tile.getX() == x && tile.getY() == y).
            findAny().orElse(null);
    }

    /**
     * @return the view
     */
    public MapView getView() {
        return view;
    }

    public ObservableList<Entity> getGems() {
        return gems;
    }

    public ObservableList<Entity> getEntities() {
        return entities;
    }

    public String getEntityMap() {
        return entityMap;
    }

    public boolean hasDuke() {
        return getEntities().stream().filter(entity -> "D".equals(entity.getType())).findFirst().isPresent();
    }

    public Entity getDuke() {
        return getEntities().stream().filter(entity -> "D".equals(entity.getType())).findFirst().get();
    }

    public Level getLevel() {
        return level;
    }
}



