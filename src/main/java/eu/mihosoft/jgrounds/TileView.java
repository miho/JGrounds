package eu.mihosoft.jgrounds;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class TileView extends Region {

    private String type;

    private int width = 64;
    private int height = 32;

    private ImageView view;
    private ImageView shadowView;
    private ImageView errorView;
    private ImageView levelDoneView;

    private boolean entity;

    public TileView(String type) {
        this(type, false);
    }

    public TileView(String type, boolean entity) {
        this.type = type;
        this.entity = entity;

        setPrefSize(width, height);
        setMaxSize(width, height);
        setMinSize(width, height);

        String imgName;

        switch (type) {
            case "p":
                imgName = "P-off";
                break;
            case "P":
                imgName = "P-on";
                break;
            case "F":
                imgName = "T-off";
                break;
            case "f":
                imgName = "T_-off";
                break;
            case "T":
                imgName = "T-on";
                break;
            case "t":
                imgName = "T_-on";
                break;
            case "-":
                imgName = "W";
                break;
            case "|":
                imgName = "W_";
                break;
            case "v":
                imgName = "V_";
                break;
            default:
                imgName = type;
        }

        view = new ImageView(
                new Image("/eu/mihosoft/jgrounds/" + (entity?"entities/":"tiles/")+imgName + ".png")
        );

        view.setPreserveRatio(true);
        view.setFitWidth(width);

        this.getChildren().add(view);
        view.setTranslateY(-view.getLayoutBounds().getHeight());

        // ---

        if(!isEntity()) {
            shadowView = new ImageView(
                new Image("/eu/mihosoft/jgrounds/" + (entity?"entities/":"tiles/")/*+imgName*/ + "0-shadow.png")
            );

            shadowView.setPreserveRatio(true);
            shadowView.setFitWidth(width);

            shadowView.setVisible(false);

            this.getChildren().add(shadowView);
            shadowView.setTranslateY(-shadowView.getLayoutBounds().getHeight());
        }

        if(entity && "D".equals(type)) {
            errorView = new ImageView(
                    new Image("/eu/mihosoft/jgrounds/" + (entity ? "entities/" : "tiles/") + imgName + "-ERROR.png")
            );

            errorView.setPreserveRatio(true);
            errorView.setFitWidth(width);

            errorView.setOpacity(0.0);

            this.getChildren().add(errorView);
            errorView.setTranslateY(-errorView.getLayoutBounds().getHeight());

            levelDoneView = new ImageView(
                    new Image("/eu/mihosoft/jgrounds/" + (entity ? "entities/" : "tiles/") + imgName + "-DONE.png")
            );

            levelDoneView.setPreserveRatio(true);
            levelDoneView.setFitWidth(width);

            levelDoneView.setOpacity(0.0);

            this.getChildren().add(levelDoneView);
            levelDoneView.setTranslateY(-levelDoneView.getLayoutBounds().getHeight());
        }

        setTranslateY(32);

        // hover effect for gems
        if(entity && "G".equals(type)) {
            TranslateTransition translateTransition =
                    new TranslateTransition(Duration.millis(2000), view);
            translateTransition.setFromY(view.getTranslateY()-3);
            translateTransition.setToY(view.getTranslateY()-0.5);
            translateTransition.setInterpolator(Interpolator.EASE_BOTH);
            translateTransition.setCycleCount(Transition.INDEFINITE);
            translateTransition.setAutoReverse(true);
            translateTransition.play();
        }
    }

    void setImage(String imagePath) {
        view.setImage(new Image("/eu/mihosoft/jgrounds/"+imagePath));
    }

    public void showShadow(boolean shadow) {
        if(!isEntity()) {
            shadowView.setVisible(shadow);
        }
    }

    public boolean isEntity() {
        return entity;
    }

    public void showError() {

        if(errorView==null) return;

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(100), errorView);
        fadeTransition.setFromValue(errorView.getOpacity());
        fadeTransition.setToValue(1.0);
        fadeTransition.setInterpolator(Interpolator.EASE_BOTH);
        fadeTransition.setCycleCount(5);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();

        fadeTransition.setOnFinished((ae)->{

        });

    }

    public FadeTransition showDone() {

        if(levelDoneView==null) return new FadeTransition();

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(250), levelDoneView);
        fadeTransition.setFromValue(levelDoneView.getOpacity());
        fadeTransition.setToValue(1.0);
        fadeTransition.setInterpolator(Interpolator.EASE_BOTH);
        fadeTransition.setCycleCount(3);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();

        return fadeTransition;

    }

	public void hideError() {
        errorView.setOpacity(0.0);
	}
}