package eu.mihosoft.jgrounds;

import groovy.lang.GroovyShell;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
// import org.fxmisc.flowless.VirtualizedScrollPane;
// import org.fxmisc.richtext.CodeArea;
// import org.fxmisc.richtext.LineNumberFactory;


import java.net.URL;
import java.util.ResourceBundle;

public class LevelViewController implements Initializable{

    private GroovyShell shell;
    private TextArea codeArea;
    // private CodeArea codeArea;
    private Map map;

    private Thread thread;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        shell = new GroovyShell();

        codeArea = /*new CodeArea();//*/ new TextArea();
        codeArea.setText("duke.move()");
        // codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        // VirtualizedScrollPane<CodeArea> vsPane = new VirtualizedScrollPane<>(codeArea);

        ScrollPane vsPane = new ScrollPane(codeArea);
        vsPane.setFitToWidth(true);
        vsPane.setFitToHeight(true);

        codeViewPane.getChildren().add(vsPane);
        AnchorPane.setLeftAnchor(vsPane,0.0);
        AnchorPane.setTopAnchor(vsPane,0.0);
        AnchorPane.setRightAnchor(vsPane,0.0);
        AnchorPane.setBottomAnchor(vsPane,0.0);
    }

    @FXML private HBox runtimeControlPane;

    @FXML private AnchorPane worldViewPane;

    @FXML private AnchorPane codeViewPane;

    @FXML private VBox commandViewPane;

    @FXML private AnchorPane rootPane;

    public void setMap(Map map) {

        this.map = map;

        worldViewPane.getChildren().clear();

        MapView mapView = map.getView();
        worldViewPane.getChildren().add(mapView);

        AnchorPane.setLeftAnchor(mapView,0.0);
        AnchorPane.setTopAnchor(mapView,0.0);
        AnchorPane.setRightAnchor(mapView,0.0);
        AnchorPane.setBottomAnchor(mapView,0.0);

        shell.setVariable("map", map);
        shell.setVariable("duke", new Duke(map));

        map.getView().setStyle("-fx-background-color: linear-gradient(#434b5e 0%, #110922 50%, #434b5e 100%)");

        worldViewPane.layout();
    }

    public AnchorPane getRootPane() {
        return rootPane;
    }

    @FXML private void onCompileAction(ActionEvent ae) {

        onStopAction(ae);

        thread = new Thread(() -> {

            shell.evaluate(codeArea.getText());

        });

        thread.start();
    }

    @FXML private void onStopAction(ActionEvent ae) {

        if(thread!=null) {
            // TODO 13.11.2018 how to properly stop groovy script evaluation?
            thread.stop();
        }

        if(map!=null) {
            Map newMap = new Map(map.getLevel());
            if(newMap.getDuke().getX()!=map.getDuke().getX()
                    ||newMap.getDuke().getY()!=map.getDuke().getY()) {
                setMap(newMap);
            }
        }
    }

    public void initLevel() {
        onStopAction(null);
    }
}
