package eu.mihosoft.jgrounds;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Duke {

    private final Map map;
    private final Entity dukeEntity;

    private static int defaultDirectionX = 1;
    private static int defaultDirectionY = 0;

    private int directionX = defaultDirectionX;
    private int directionY = defaultDirectionY;

    private final List<Entity> collectedGems = new ArrayList<>();

    private static long delay = 500;

    public Duke(Map map) {
        this.map = map;
        this.dukeEntity = map.getDukeEntity();
        reset();
    }

    public boolean isInDefaultOrientation() {
        return directionX == defaultDirectionX && directionY == defaultDirectionY;
    }

    public boolean isBlocked() {
        int newX = dukeEntity.getX()+directionX;
        int newY = dukeEntity.getY()+directionY;

        if(newX < 0 || newY < 0 || newX > map.getWidth()-1 || newY > map.getHeight()-1 ) {
            return true;
        }

        switch (map.getTileTypeAt(newX,newY)) {
            case '|': return true;
            case '-': return true;
            case '+': return true;
            case 'F': return true;
            case 'f': return true;
            case 'V': return true;
            case 'v': return true;
            default:
                return false;
        }
    }

    private void updateDirectionImg() {
        if(directionX == 1 && directionY == 0) {
            dukeEntity.getView().setImage("entities/D-direction-0.png");
        } else if(directionX == 0 && directionY == 1) {
            dukeEntity.getView().setImage("entities/D-direction-3.png");
        } else if(directionX == -1 && directionY == 0) {
            dukeEntity.getView().setImage("entities/D-direction-2.png");
        } else if(directionX == 0 && directionY == -1) {
            dukeEntity.getView().setImage("entities/D-direction-1.png");
        }
    }

    public void turnRight() {

        Platform.runLater(()-> {
            int tmpX = directionX;
            directionX = directionY;
            directionY = -tmpX;
            updateDirectionImg();
        });

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void turnLeft() {

        Platform.runLater(()-> {
            int tmpX = directionX;
            directionX = -directionY;
            directionY = tmpX;
            updateDirectionImg();
        });

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        directionX = 1;
        directionY = 0;
        updateDirectionImg();
    }

    private boolean nextTileIsOfType(char tileType) {
        int nextX = dukeEntity.getX()+directionX;
        int nextY = dukeEntity.getY()+directionY;

        return tileType==map.getTileTypeAt(nextX, nextY);
    }

    /**
     * @return the directionX
     */
    public int getDirectionX() {
        return directionX;
    }

    /**
     * @return the directionY
     */
    public int getDirectionY() {
        return directionY;
    }

    public void move() {

        if(isBlocked()) {
            dukeEntity.showError();
            throw new RuntimeException("Cannot move forward! Blocked by wall!");
        }

        long waitDuration = 500;

        if(nextTileIsOfType('P')) {
            waitDuration = 2000;
        }

        Platform.runLater(()-> {
            // TODO fix that
            // dukeEntity.getView().toFront();

            dukeEntity.setLocation(dukeEntity.getX()+directionX, dukeEntity.getY()+directionY);

            char tileType = map.getTileTypeAt(dukeEntity.getX(), dukeEntity.getY());

            // beam via portal
            if('P' == tileType) {

                Entity startPortal = map.getTileByLocation(dukeEntity.getX(), dukeEntity.getY());
                
                // look for other portal
                Entity targetPortal = map.getTilesByType('P').stream().
                    filter(t->t!=startPortal).findFirst().orElse(null);

                if(targetPortal==null) {
                    throw new RuntimeException("Level design error: no target portal found!"); 
                }  
                
                Timeline portalTimeline = new Timeline(
                    new KeyFrame(Duration.millis(0),    new KeyValue(dukeEntity.getView().opacityProperty(), 1.0)),
                    new KeyFrame(Duration.millis(800),  new KeyValue(dukeEntity.getView().opacityProperty(), 0.0)),
                    new KeyFrame(Duration.millis(800),  (ae) -> {    dukeEntity.setLocation(
                                                                        targetPortal.getX(),
                                                                        targetPortal.getY(),
                                                                        true
                                                                     );
                                                                     map.updateZOrder(dukeEntity);
                                                        }),
                    new KeyFrame(Duration.millis(800),  new KeyValue(dukeEntity.getView().opacityProperty(), 0.0)),
                    new KeyFrame(Duration.millis(1600), new KeyValue(dukeEntity.getView().opacityProperty(), 1.0))
                );

                portalTimeline.play();
            }// end if enabled portal
        }); // end runLater

        try {
            Thread.sleep(waitDuration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void noOp() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void collect() {

        Platform.runLater(()-> {
            List<Entity> collectedEntities = new ArrayList<>();

            map.getEntities().stream().filter(entity -> !"D".equals(entity.getType())).
                    filter(entity -> entity.getX() == dukeEntity.getX()).
                    filter(entity -> entity.getY() == dukeEntity.getY()).forEach(entity -> {
                collectedGems.add(entity);
                collectedEntities.add(entity);
            });

            collectedEntities.stream().forEach(entity -> {

                entity.setShadow(false);

                entity.setIgnoreForCompare(true);

                map.checkCondition(dukeEntity);

                TranslateTransition translateTransition =
                        new TranslateTransition(Duration.millis(500), entity.getView());
                translateTransition.setFromY(entity.getView().getTranslateY());
                translateTransition.setToY(-100);
                translateTransition.setCycleCount(1);
                translateTransition.setInterpolator(Interpolator.EASE_IN);
                translateTransition.play();

                translateTransition.setOnFinished((ae)-> {
                    map.getEntities().remove(entity);
                });

                FadeTransition fadeTransition =
                        new FadeTransition(Duration.millis(450), entity.getView());
                fadeTransition.setFromValue(1.0);
                fadeTransition.setToValue(0.0);
                fadeTransition.setCycleCount(1);
                fadeTransition.setAutoReverse(true);
                fadeTransition.play();

            });
        });

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void setDelay(long delay) {
        Duke.delay = delay;
    }
}
