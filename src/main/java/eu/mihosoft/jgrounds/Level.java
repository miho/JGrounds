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
                "000000000000000" +
                "              G" +
                "               " +
                "               " +
                "               " +
                "               " +
                "               " +
                "               " +
                "               " +
                "               " +
                "               " +
                "               " +
                "               " +
                "               " +
                "               ";

        String entityMap =
                "D00000000000000" +
                "              0" +
                "               " +
                "               " +
                "               " +
                "               " +
                "               " +
                "               " +
                "               " +
                "               " +
                "               " +
                "               " +
                "               " +
                "               " +
                "               ";

        String goalState =
                "000000000000000" +
                "              D" +
                "               " +
                "               " +
                "               " +
                "               " +
                "               " +
                "               " +
                "               " +
                "               " +
                "               " +
                "               " +
                "               " +
                "               " +
                "               ";

        return new Level(15, 15, new Scene(floorMap, entityMap,
                new Condition.EntityMapCondition(goalState, "Duke has to reach his goal.")));
    }
}
