package eu.mihosoft.jgrounds;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class LevelView extends AnchorPane {

    private LevelViewController controller;

    public LevelView(Level l) {
        Map map = new Map(l);

        // map.getView().setStyle("-fx-background-color: linear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%)");

        loadFXML();

        controller.setMap(map);

        AnchorPane rootPane = controller.getRootPane();

        AnchorPane.setLeftAnchor(rootPane,0.0);
        AnchorPane.setTopAnchor(rootPane,0.0);
        AnchorPane.setRightAnchor(rootPane,0.0);
        AnchorPane.setBottomAnchor(rootPane,0.0);

        getChildren().add(rootPane);
    }

    public void initLevel() {
        controller.initLevel();
    }

    private void loadFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("LevelUI.fxml"));

        try {
            fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(JGrounds.class.getName()).
                    log(java.util.logging.Level.SEVERE, "Cannot load FXML file ' LevelUI.fxml'.", ex);
        }

        controller = fxmlLoader.getController();
    }

}
