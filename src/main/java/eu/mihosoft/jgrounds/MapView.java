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
