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

        primaryStage.setScene(scene);

        levelView.initLevel();
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            this.primaryStage = primaryStage;

            primaryStage.setTitle("JGrounds App running on " + System.getProperty("java.version"));
            primaryStage.show();

            setLevel(
                    Level.newDefaultLevel().
                            setLevelTransition(()->setLevel(Level.levelTwo().
                                    setLevelTransition(()->setLevel(Level.levelThree().
                                            setLevelTransition(()->setLevel(Level.levelFour().
                                                    setLevelTransition(()->setLevel(Level.levelFive()))))))))
            );

        } catch (Throwable ex) {
            ex.printStackTrace(System.err);
        }
    }

    @Override
    public void stop() throws Exception {
        System.exit(0);
    }

}




