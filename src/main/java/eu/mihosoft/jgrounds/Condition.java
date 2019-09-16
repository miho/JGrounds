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

import java.util.Objects;
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

                if(m.hasDukeEntity()) {
                    Entity duke = m.getDukeEntity();
                    isDuke = (duke.getX() == entityX && duke.getY() == entityY);
                }

                Optional<Entity> entity = m.getEntities().stream().
                        filter(e -> e.getX() == entityX && e.getY() == entityY).findFirst();

                if(goal == ' ') {
                    continue;
                }

                if (isDuke && noEntityAtDukeLocation(m)) {
                    continue;
                }        

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

        private boolean entityAtIs(Map m, int entityX, int entityY, char type) {

            Optional<Entity> entityAt = m.getEntities().stream().
            filter(e->e!=m.getDukeEntity()).
              filter(e->e.getX()==entityX &&e.getY()==entityY).findAny();
            
            if(!entityAt.isPresent()) {
                return false;
            }
    
            return Objects.equals(""+type,entityAt.get().getType());
        }
    
        private boolean noEntityAtDukeLocation(Map m) {
    
            if(!m.hasDukeEntity()) {
                return false;
            }
    
            Entity duke = m.getDukeEntity();
    
            return m.getEntities().stream().filter(e->!e.isIgnoredForCompare()).filter(e->e!=duke).count() == 0;
        }
    }

}
