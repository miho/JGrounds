package eu.mihosoft.jgrounds;

import groovy.lang.GroovyShell;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
// import org.fxmisc.flowless.VirtualizedScrollPane;
// import org.fxmisc.richtext.CodeArea;
// import org.fxmisc.richtext.LineNumberFactory;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.awt.GridLayout;
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
        codeArea.replaceText(0, 0, "// your code");
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

        GridPane commandPane = new GridPane();

        double proportionCol = 100/3;
        double proportionRow = 100/2;

        for(int i = 0 ; i <3; i++) {
            
            RowConstraints row = new RowConstraints();
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(proportionCol);
            
            if( i<2 ){
                row.setPercentHeight(proportionRow);
            } else {
                row.setPercentHeight(0);
            }
            commandPane.getRowConstraints().add(row);
            commandPane.getColumnConstraints().add(col);
        }

        commandViewPane.getChildren().clear();
        commandViewPane.getChildren().add(commandPane);

        Button forwardBtn = new Button();
        forwardBtn.getStyleClass().add("forward-button");
        forwardBtn.setMinWidth(65);
        forwardBtn.setMinHeight(65);
        Button leftBtn = new Button();
        leftBtn.getStyleClass().add("left-button");
        leftBtn.setMinWidth(65);
        leftBtn.setMinHeight(65);
        Button rightBtn = new Button();
        rightBtn.getStyleClass().add("right-button");
        rightBtn.setMinWidth(65);
        rightBtn.setMinHeight(65);
        Button collectBtn = new Button();
        collectBtn.getStyleClass().add("collect-button");
        collectBtn.setMinWidth(65);
        collectBtn.setMinHeight(65);

        forwardBtn.setOnAction((ae)->{
            int pos = codeArea.getCaretPosition();

            String newLine;
            if(pos>0 && !codeArea.getText(pos-1, pos-1).equals("\n")) {
                newLine="\n";
            } else {
                newLine="";
            }

            codeArea.replaceText(0,pos,codeArea.getText(0, pos)+ newLine+"duke.move()");
        });
        leftBtn.setOnAction((ae)->{
            int pos = codeArea.getCaretPosition();

            String newLine;
            if(pos>0 && !codeArea.getText(pos-1, pos-1).equals("\n")) {
                newLine="\n";
            } else {
                newLine="";
            }

            codeArea.replaceText(0,pos,codeArea.getText(0, pos)+ newLine+"duke.turnLeft()");
        });
        rightBtn.setOnAction((ae)->{
            int pos = codeArea.getCaretPosition();

            String newLine;
            if(pos>0 && !codeArea.getText(pos-1, pos-1).equals("\n")) {
                newLine="\n";
            } else {
                newLine="";
            }

            codeArea.replaceText(0,pos,codeArea.getText(0, pos)+ newLine+"duke.turnRight()");
        });
        collectBtn.setOnAction((ae)->{
            int pos = codeArea.getCaretPosition();

            String newLine;
            if(pos>0 && !codeArea.getText(pos-1, pos-1).equals("\n")) {
                newLine="\n";
            } else {
                newLine="";
            }

            codeArea.replaceText(0,pos,codeArea.getText(0, pos)+ newLine+"duke.collect()");
        });

        commandPane.add(forwardBtn, 1, 0);
        commandPane.add(leftBtn, 0, 1);
        commandPane.add(rightBtn, 2, 1);
        commandPane.add(collectBtn, 1, 1);

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


// class ImageButton extends Parent {

//     private Image btnImage;
//     private Image btnPressedImg;

//     private final ImageView imageview;

//     public ImageButton(String img, String pressedImg) {
//         this.btnImage = new Image(getClass().getResource(img).toExternalForm());
//         this.btnPressedImg = new Image(getClass().getResource(img).toExternalForm());
//         this.imageview = new ImageView(btnImage);
//         this.getChildren().add(this.imageview);

//         this.imageview.setOnMousePressed(new EventHandler<MouseEvent>() {

//             public void handle(MouseEvent evt) {
//                 imageview.setImage(btnPressedImg);
//             }
//         });

//         // TODO other event handlers like mouse up

//     } 

// }
