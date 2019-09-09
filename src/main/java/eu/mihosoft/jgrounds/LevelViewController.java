package eu.mihosoft.jgrounds;

import groovy.lang.GroovyShell;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
// import org.fxmisc.flowless.VirtualizedScrollPane;
// import org.fxmisc.richtext.CodeArea;
// import org.fxmisc.richtext.LineNumberFactory;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

public class LevelViewController implements Initializable {

    private GroovyShell shell;
    // private TextArea codeArea;
    private CodeArea codeArea;
    private Map map;

    private Thread thread;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        shell = new GroovyShell();

        codeArea = new CodeArea();// /*new CodeArea();//*/ new TextArea();
        codeArea.setStyle("-fx-font-family: courier-new; -fx-font-size: 16pt;");
        // codeArea.setFont(Font.font("Courier New", FontWeight.BOLD, 16));
        // codeArea.setText("duke.move()");
        codeArea.replaceText(0, 0, "duke.move()");
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        VirtualizedScrollPane<CodeArea> vsPane = new VirtualizedScrollPane<>(codeArea);

        // ScrollPane vsPane = new ScrollPane(codeArea);
        // vsPane.setFitToWidth(true);
        // vsPane.setFitToHeight(true);

        codeViewPane.getChildren().add(vsPane);
        AnchorPane.setLeftAnchor(vsPane, 0.0);
        AnchorPane.setTopAnchor(vsPane, 0.0);
        AnchorPane.setRightAnchor(vsPane, 0.0);
        AnchorPane.setBottomAnchor(vsPane, 0.0);
    }

    @FXML
    private HBox runtimeControlPane;

    @FXML
    private AnchorPane worldViewPane;

    @FXML
    private AnchorPane codeViewPane;

    @FXML
    private VBox commandViewPane;

    @FXML
    private Pane dukeControl;

    @FXML
    private AnchorPane rootPane;

    public void setMap(Map map) {

        this.map = map;

        worldViewPane.getChildren().clear();

        MapView mapView = map.getView();
        worldViewPane.getChildren().add(mapView);

        AnchorPane.setLeftAnchor(mapView, 0.0);
        AnchorPane.setTopAnchor(mapView, 0.0);
        AnchorPane.setRightAnchor(mapView, 0.0);
        AnchorPane.setBottomAnchor(mapView, 0.0);

        shell.setVariable("map", map);
        shell.setVariable("duke", new Duke(map));

        map.getView().setStyle("-fx-background-color: linear-gradient(#434b5e 0%, #110922 50%, #434b5e 100%)");

        worldViewPane.layout();

    }

    public AnchorPane getRootPane() {
        return rootPane;
    }

    @FXML
    private void onCompileAction(ActionEvent ae) {

        onStopAction(ae);

       // FXUtils.runAndWait(() -> onStopAction(ae));
        
        thread = new Thread(() -> {

            try {

                

                shell.evaluate(codeArea.getText());
            } catch(Throwable ex) {
                Logger.getLogger(LevelViewController.class.getName()).log(
                    java.util.logging.Level.ALL, ex.getMessage(), ex);
            }

        });

        thread.start();
    }

    @FXML private void onStopAction(ActionEvent ae) {

        if(thread!=null) {
            // TODO 13.11.2018 how to properly stop groovy script evaluation?
            thread.stop();
        }

        map.getDukeEntity().hideError();    

        if(map!=null) {
            Duke duke = (Duke) shell.getVariable("duke");
            Map newMap = new Map(map.getLevel());
            if(newMap.getDukeEntity().getX()!=map.getDukeEntity().getX()
                    ||newMap.getDukeEntity().getY()!=map.getDukeEntity().getY()
                    ||!duke.isInDefaultOrientation()
                    ) {
                setMap(newMap);
            }
        }
    }

    public void initLevel() {
        onStopAction(null);
    }
}
