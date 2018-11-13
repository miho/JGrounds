package eu.mihosoft.jgrounds;

public class Scene {

    private final String tilesMap;
    private final String entityMap;
    private final Condition goalCondition;

    public Scene(String tilesMap, String entityMap, Condition goalCondition) {
        this.tilesMap = tilesMap;
        this.entityMap = entityMap;
        this.goalCondition = goalCondition;
    }

    public Condition getGoalCondition() {
        return goalCondition;
    }

    public String getTilesMap() {
        return tilesMap;
    }

    public String getEntityMap() {
        return entityMap;
    }
}
