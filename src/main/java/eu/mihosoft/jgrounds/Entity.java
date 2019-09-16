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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * An entity is a dynamic game object. Static game objects are also represented
 * by this class. In this case the entity property is set to true.
 */
public class Entity {

    private final String type;
    private Map map;
    private TileView view;
    private boolean entity;

    private IntegerProperty xProp = new SimpleIntegerProperty();
    private IntegerProperty yProp = new SimpleIntegerProperty();

    private int z;

    private boolean fadeTransitionIgnored;
    private boolean ignoreCompare;

    public Entity(Map map, String type, int x, int y) {
        this(map, type, true, x, y);
    }

    private Entity(Map map, String type, boolean isEntity, int x, int y) {
        this.map = map;
        this.type = type;
        this.entity = isEntity;
        this.setLocation(x, y);
        this.view = new TileView(type, isEntity);

        if(Character.isDigit(type.charAt(0)) 
            || "t".equals(type.toLowerCase()) 
            || (!isEntity && "G".equals(type)) 
            || (!isEntity && "p".equals((type.toLowerCase())))) {
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

        setFadeTransitionIgnored(false);

        xProperty().set(x);
        yProperty().set(y);

        // set shadow

    }

    public void setLocation(int x, int y, boolean disableTransition) {

        setFadeTransitionIgnored(disableTransition);

        xProperty().set(x);
        yProperty().set(y);

        setFadeTransitionIgnored(false);
    }

    private void setFadeTransitionIgnored(boolean state) {
        this.fadeTransitionIgnored = state;
    }

    /**
     * @return the fadeTransitionIgnored
     */
    boolean isFadeTransitionIgnored() {
        return fadeTransitionIgnored;
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

	public void hideError() {
        view.hideError();
	}

	public void setIgnoreForCompare(boolean b) {
        this.ignoreCompare = b;
    }
    
    /**
     * @return the ignoreCompare
     */
    public boolean isIgnoredForCompare() {
        return ignoreCompare;
    }
}