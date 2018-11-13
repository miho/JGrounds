package eu.mihosoft.jgrounds;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Map {

    private final int width;
    private final int height;

    private String floorMap;
    private String entityMap;

    private int sceneIndex;

    private final ObservableList<Entity> entities
            = FXCollections.observableArrayList();
    private final ObservableList<Node> tiles
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

                    e.xProperty().addListener(l -> {
                        double x = (e.getY() * view.getTileWidth() / 2)
                                + (e.getX() * view.getTileWidth() / 2);
                        double y = (e.getX() * view.getTileHeight() / 2)
                                - (e.getY() * view.getTileHeight() / 2);

                        // center y
                        y += (getHeight() * view.getTileHeight()) / 2.0 + view.getTileHeight();

                        TranslateTransition transition = new TranslateTransition(Duration.millis(400), e.getView());
                        transition.setFromX(e.getView().getTranslateX());
                        transition.setFromY(e.getView().getTranslateY());
                        transition.setToX(x);
                        transition.setToY(y);
                        transition.play();

                        transition.setOnFinished((ae)-> {
                            if(!isParsing && getCurrentScene().getGoalCondition().check(this)) {
                                if(sceneIndex+1 >= level.getScenes().size()) {
                                    e.showDone().setOnFinished((aev)-> {
                                        nextScene();
                                    });
                                } else {
                                    nextScene();
                                }
                            }
                        });
                    });

                    e.yProperty().addListener(l -> {

                        double x = (e.getY() * view.getTileWidth() / 2)
                                + (e.getX() * view.getTileWidth() / 2);
                        double y = (e.getX() * view.getTileHeight() / 2)
                                - (e.getY() * view.getTileHeight() / 2);

                        // center y
                        y += (getHeight() * view.getTileHeight()) / 2.0 + view.getTileHeight();

                        TranslateTransition transition = new TranslateTransition(Duration.millis(400), e.getView());
                        transition.setFromX(e.getView().getTranslateX());
                        transition.setFromY(e.getView().getTranslateY());
                        transition.setToX(x);
                        transition.setToY(y);
                        transition.play();

                        transition.setOnFinished((ae)-> {
                            if(!isParsing && getCurrentScene().getGoalCondition().check(this)) {
                                if(sceneIndex+1 >= level.getScenes().size()) {
                                    e.showDone().setOnFinished((aev)->{
                                        nextScene();
                                    });
                                } else {
                                    nextScene();
                                }
                            }
                        });

                    });
                }

                for (Entity e : c.getRemoved()) {
                    view.getMapPane().getChildren().remove(e.getView());
                }
            }
        });

        parseMaps();
    }

    public void nextScene() {
        sceneIndex++;

        if(sceneIndex >= level.getScenes().size()) {
            level.levelDonePropertyWritable().set(true);
            return;
        }

        parseMaps();

        List<Entity> revertedEntities = new ArrayList<>(entities);
        Collections.reverse(revertedEntities);
        revertedEntities.forEach(entity -> entity.getView().toFront());
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

                int tileIndex = i + (height - 1 - j) * width;

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
                            tileView = new TileView("" + tileType);
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
                        Entity entity = new Entity(""+entityType);
                        entity.setMap(this);
                        entities.add(entity);
                        entity.setLocation(i, j);
                        break;
                }
            } // end for j
        } // end for i

        isParsing = false;
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



