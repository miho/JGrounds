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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Level {

    private final List<Scene> scenes = new ArrayList<>();

    private final int mapWidth;
    private final int mapHeight;

    private final BooleanProperty levelDoneProperty = new SimpleBooleanProperty();
    private Runnable levelDoneRunnable;

    public Level(int mapWidth, int mapHeight, Scene... scenes) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.scenes.addAll(Arrays.asList(scenes));

        levelDoneProperty().addListener((o)->{
            if(levelDoneProperty().get() && levelDoneProperty!=null) {
                levelDoneRunnable.run();
            }
        });
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }
    public static Level newDefaultLevel() {

        String tilesMap = "" +
                "+V-V-V+" +
                "v00000v" +
                "|00000|" +
                "v000G0v" +
                "|00000|" +
                "v00000v" +
                "+V-V-V+";

        String entityMap = "" +
                "0000000" +
                "0000000" +
                "0000000" +
                "00D0000" +
                "0000000" +
                "0000000" +
                "0000000";

        String goalState = "" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000D00" +
                "0000000" +
                "0000000" +
                "0000000";

        return new Level(7, 7, new Scene(tilesMap, entityMap,
                new Condition.EntityMapCondition(goalState, "Duke has to reach his goal.")));
    }

    public Level setLevelTransition(Runnable r) {
        this.levelDoneRunnable = r;

        return this;
    }

    public static Level levelTwo() {

        String tilesMap = "" +
                "+V+V-V+" +
                "v0v000v" +
                "|0|000|" +
                "v00000v" +
                "|000|0|" +
                "v000vGv" +
                "+V-V+V+";

        String entityMap = "" +
                "0000000" +
                "0D00000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000";

        String goalState = "" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "00000D0" +
                "0000000";


        return new Level(7, 7, new Scene(tilesMap, entityMap,
                new Condition.EntityMapCondition(goalState, "Duke has to reach his goal.")));
    }

    public ReadOnlyBooleanProperty levelDoneProperty() {
        return levelDoneProperty;
    }

    BooleanProperty levelDonePropertyWritable() {
        return levelDoneProperty;
    }

    public static Level levelThree() {

        String tilesMap = "" +
                "+V-V-V+" +
                "v0000Gv" +
                "|00000|" +
                "v00000v" +
                "|00000|" +
                "v00000v" +
                "+V-V-V+";

        String entityMap = "" +
                "0000000" +
                "0G00000" +
                "0000G00" +
                "0D00000" +
                "000G000" +
                "0000000" +
                "0000000";

        String goalState = "" +
                "       " +
                "     D " +
                "       " +
                "       " +
                "       " +
                "       " +
                "       ";


        return new Level(7, 7, new Scene(tilesMap, entityMap,
                new Condition.EntityMapCondition(goalState, "Duke has to reach his goal and can optionally collect some gems.")));
    }

    public static Level levelFour() {

        String tilesMap1 = "" +
                "+V+V-V+" +
                "v0v000v" +
                "|0|000|" +
                "vGf000v" +
                "|0|000|" +
                "v0v000v" +
                "+V+V-V+";

        String entityMap1 = "" +
                "0000000" +
                "000G0D0" +
                "0000000" +
                "0000G00" +
                "0000000" +
                "00000G0" +
                "0000000";

        String goalState1 = "" +
                "       " +
                "   0   " +
                "       " +
                "    0  " +
                "       " +
                "     0 " +
                "       ";

        String tilesMap2 = "" +
                "+V+V-V+" +
                "v0v000v" +
                "|0|000|" +
                "vGt000v" +
                "|0|000|" +
                "v0v000v" +
                "+V+V-V+";

        String entityMap2 = "" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000";

        String goalState2 = "" +
                "       " +
                "       " +
                "       " +
                " D     " +
                "       " +
                "       " +
                "       ";

        return new Level(7, 7,
                new Scene(tilesMap1, entityMap1,
                    new Condition.EntityMapCondition(goalState1, "Duke has to reach his goal and needs to collect all gems to open the door.")
                ),
                new Scene(tilesMap2, entityMap2,
                    new Condition.EntityMapCondition(goalState2, "Duke has to reach his goal and needs to collect all gems to open the door.")
                )
        );
    }

    public List<Scene> getScenes() {
        return scenes;
    }

    public static Level levelFive() {
        String floorMap =
                "+V-V-V-V-V-V-V-V+" +
                "v000000000000000v" +
                "+V-V-V-V-V-V-V+0|" +
                "|0000000000000vGv" +
                "v0000000000000+V+" +
                "|000000000000000v" +
                "v000000000000000+" +
                "|000000000000000v" +
                "+V-V-V-V-V-V-V-V+";


        String entityMap =
                "                 " +
                "0D00000000000000 " +
                "               0 " +
                "               0 " +
                "                 " +
                "                 " +
                "                 " +
                "                 " +
                "                 ";

        String goalState =
                "                 " +
                "0000000000000000 " +
                "               0 " +
                "               D " +
                "                 " +
                "                 " +
                "                 " +
                "                 " +
                "                 ";

        return new Level(17, 9, new Scene(floorMap, entityMap,
                new Condition.EntityMapCondition(goalState, "Duke has to reach his goal.")));
    }

    public static Level portalLevel() {
        String floorMap1 =
                "+V-V-V+" +
                "v000p0v" +
                "|00000|" +
                "v00000v" +
                "+V-V-V+" +
                "       " +
                "+V-V-V+" +
                "v00p00v" +
                "|00000|" +
                "v00G00v" +
                "+V-V-V+";

        String entityMap1 =
                "0000000" +
                "0D0G000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "       " +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000";

        String goalState1 =
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "       " +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000";

        String floorMap2 =
                "+V-V-V+" +
                "v000P0v" +
                "|00000|" +
                "v00000v" +
                "+V-V-V+" +
                "       " +
                "+V-V-V+" +
                "v00P00v" +
                "|00000|" +
                "v00G00v" +
                "+V-V-V+";

        String entityMap2 =
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "       " +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000";

        String goalState2 =
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "       " +
                "0000000" +
                "0000000" +
                "000D000" +
                "0000000";

        return new Level(7,11,
                new Scene(floorMap1, entityMap1,
                new Condition.EntityMapCondition(goalState1, "Duke has to reach his goal.")), 
                new Scene(floorMap2, entityMap2,
                new Condition.EntityMapCondition(goalState2, "Duke has to reach his goal.")) 
        );
    }
}
