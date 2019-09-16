/*
 * Copyright 2017-2019 Michael Hoffer <info@michaelhoffer.de>. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY Michael Hoffer <info@michaelhoffer.de> "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL Michael Hoffer <info@michaelhoffer.de> OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of Michael Hoffer <info@michaelhoffer.de>.
 */
package eu.mihosoft.jgrounds;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.util.Duration;

import java.util.*;
import java.util.stream.Collectors;

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

    private void updateLocation(Entity e) {
        
        double x = (e.getY() * view.getTileWidth() / 2) + (e.getX() * view.getTileWidth() / 2);
        double yTmp = (e.getX() * view.getTileHeight() / 2) - (e.getY() * view.getTileHeight() / 2);

        // center y
        double y = yTmp + (getHeight() * view.getTileHeight()) / 2.0 + view.getTileHeight();

        boolean xBigger =   x > e.getView().getTranslateX();
        boolean yBigger =   y > e.getView().getTranslateY();

        TranslateTransition transition = new TranslateTransition(Duration.millis(400), e.getView());
        transition.setFromX(e.getView().getTranslateX());
        transition.setFromY(e.getView().getTranslateY());
        transition.setToX(x);
        transition.setToY(y);

        if(Double.compare(x, 0) == 0 || Double.compare(e.getView().getTranslateX(), 0) == 0 ) {

            e.getView().setTranslateX(x);
            e.getView().setTranslateY(y);

            updateZOrder(e);

            FadeTransition transition2 = new FadeTransition(Duration.millis(1500), e.getView());
            transition2.setFromValue(0.0);
            transition2.setToValue(1.0);
            transition2.play();

        } else {
            if(e.isFadeTransitionIgnored()) {

                e.getView().setTranslateX(x);
                e.getView().setTranslateY(y);

                updateMap(e,x,y);

            } else {
                transition.play();
            }
        }

        if(xBigger || yBigger) {
            updateZOrder(e);
        }

        transition.setOnFinished((ae) -> {
            updateMap(e,x,y);
        });
    }


    private void updateMap(Entity e, double x, double y) {

        boolean xSmaller =  x < e.getView().getTranslateX();
        boolean ySmaller =  y < e.getView().getTranslateY();

        if (xSmaller || ySmaller) {
            updateZOrder(e);
        }

        checkCondition(e);
    }

    void checkCondition(Entity e) {
        if (!isParsing && getCurrentScene().getGoalCondition().check(this)) {
            if (sceneIndex + 1 >= level.getScenes().size()) {
                e.showDone().setOnFinished((aev) -> {
                    nextScene();
                });
            } else {
                nextScene();
            }
        }
    }

    void updateZOrder(Entity entity) {

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
     * Returns tiles specified by type.
     * @param type tile type
     * @return the tiles specified by type
     */
    public List<Entity> getTilesByType(char type) {
        return tiles.stream().filter(
            tile->Objects.equals(tile.getType().charAt(0),type)).
                  collect(Collectors.toList());
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

    public boolean hasDukeEntity() {
        return getDukeEntity()!=null;
    }

    public Entity getDukeEntity() {
        return getEntities().stream().filter(entity -> "D".equals(entity.getType())).findFirst().orElse(null);
    }

    public Level getLevel() {
        return level;
    }
}




/*

# portal level 3

// take 1

duke.turnLeft()
duke.move()
duke.move()
duke.turnRight()
duke.turnRight()
duke.move()
duke.move()
duke.turnLeft()


// take 2

duke.turnLeft()
duke.move()
duke.turnRight()
duke.turnRight()
duke.move()
duke.move()
duke.move()
duke.turnLeft()
duke.move()
duke.turnLeft()
duke.move()
duke.turnLeft()
duke.move()
duke.turnRight()
duke.move()
duke.turnRight()
duke.turnRight()

*/


