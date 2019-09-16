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


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JGrounds extends Application {

    private Stage primaryStage;

    private Scene scene;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void setLevel(Level l) {
        LevelView levelView = new LevelView(l);

        double w = scene==null?800:scene.getWidth();
        double h = scene==null?600:scene.getHeight();

        this.scene = new Scene(levelView, w, h);
        scene.getStylesheets().add(getClass().getResource(
            "/eu/mihosoft/jgrounds/ui/style.css").toExternalForm()
        );
        primaryStage.setScene(scene);

        levelView.initLevel();
    }

    public void setLevels(Level... levels) {
        Level prevLevel = null;
        for(Level l : levels) {
            if(prevLevel != null) {
                prevLevel.setLevelTransition(()->setLevel(l));
            } else {
                setLevel(l);
            }

            prevLevel = l;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        try {

            this.primaryStage = primaryStage;

            primaryStage.setTitle("JGrounds App running on JDK " + System.getProperty("java.version"));
            primaryStage.show();

            Level[] levels = {
                //Level.frontVSBackDebugLevel(),
                Level.portalLevel(),
                Level.newDefaultLevel(),
                Level.levelTwo(),
                Level.levelThree(),
                Level.levelFour(),
                Level.levelFive()
            };

            setLevels(levels);

        } catch (Throwable ex) {
            ex.printStackTrace(System.err);
        }
    }

    @Override
    public void stop() throws Exception {
        System.exit(0);
    }

}




