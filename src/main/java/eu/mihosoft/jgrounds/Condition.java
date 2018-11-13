package eu.mihosoft.jgrounds;

import java.util.Optional;

public abstract class Condition {
    private String conditionText;

    private Condition(String conditionText) {
        this.conditionText = conditionText;
    }

    public String getConditionText() {
        return conditionText;
    }

    public abstract boolean check(Map m);


    static class EntityMapCondition extends Condition {

        private String goalMap;

        public EntityMapCondition(String goalMap, String conditionText) {
            super(conditionText);
            this.goalMap = goalMap;
        }

        @Override
        public boolean check(Map m) {

            if(goalMap.length()!=m.getEntityMap().length()) {
                System.err.println("WARNING: compared maps are not equal in size!" +
                        " '#goal:"+goalMap.length()+" != #entity:"+m.getEntityMap().length() + "");
                return false;
            }

            for(int i = 0; i < m.getEntityMap().length();i++) {

                //char entityChar = m.getEntityMap().charAt(i);
                char goal = goalMap.charAt(i);

                int entityX = i % m.getWidth();
                int entityY = m.getHeight() -1 - (i / m.getWidth());

                boolean isDuke = false;

                if(m.hasDuke()) {
                    Entity duke = m.getDuke();
                    isDuke = (duke.getX() == entityX && duke.getY() == entityY);
                }

                if(goal == ' ' || isDuke) {
                    continue;
                }

                Optional<Entity> entity = m.getEntities().stream().
                        filter(e -> e.getX() == entityX && e.getY() == entityY).findFirst();

                if(goal=='0'&& entity.isPresent()) {
                    return false;
                }

                if(goal!='0' && !entity.isPresent()) {
                    return false;
                }

                if(entity.isPresent() && goal!=entity.get().getType().charAt(0)) {
                    return false;
                }
            }

            return true;
        }
    }

}
