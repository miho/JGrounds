package eu.mihosoft.jgrounds;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Duke {

    private final Map map;
    private final Entity dukeEntity;

    private int directionX = 1;
    private int directionY = 0;

    private final List<Entity> collectedGems = new ArrayList<>();

    private static long delay = 500;

    public Duke(Map map) {
        this.map = map;
        this.dukeEntity = map.getDuke();
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

    public void turnRight() {

        Platform.runLater(()-> {
            int tmpX = directionX;
            directionX = directionY;
            directionY = -tmpX;
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
    }

    public void move() {

        if(isBlocked()) {
            dukeEntity.showError();
            throw new RuntimeException("Cannot move forward! Blocked by wall!");
        }

        Platform.runLater(()-> {
            // TODO fix that
            // dukeEntity.getView().toFront();

            dukeEntity.setLocation(dukeEntity.getX()+directionX, dukeEntity.getY()+directionY);
        });

        System.out.println("X: " + directionX + ", Y: " + directionY);

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
            Thread.sleep(Duke.delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void setDelay(long delay) {
        Duke.delay = delay;
    }
}
