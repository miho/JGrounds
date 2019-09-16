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