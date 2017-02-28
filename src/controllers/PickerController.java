package controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import others.GlassRobot;
import javafx.stage.StageStyle;
import others.WindowControl;

import java.util.Optional;

import static utils.MathUtils.getRoundValue;
import static utils.MathUtils.toHalfRouned;

/**
 * Created by Galib on 2/26/2017.
 */
public class PickerController implements ChangeListener {


    @FXML
    Button rgbValue;
    @FXML
    Button haxeValue;
    @FXML
    Rectangle colorView;
    @FXML
    Canvas pixelView;
    @FXML
    Slider rSlider;
    @FXML
    Slider gSlider;
    @FXML
    Slider bSlider;
    @FXML
    Slider aSlider;
    @FXML
    Label rLabel;
    @FXML
    Label gLabel;
    @FXML
    Label bLabel;
    @FXML
    Label aLabel;
    @FXML
    Pane layout;

    private GlassRobot robot;
    private KeyCodeCombination keyCombinationC, keyCombinationX;
    private Boolean updateSwitch = false;
    private GraphicsContext gc;
    private ClipboardContent content;
    private Color customColor;
    private double xOffset;
    private double yOffset;
    private Tooltip tt;


    // SETTING UP ALL THE BINDINGS, HERE WE ARE BINDING SLIDERS.
    private void setBindings() {
        rSlider.valueProperty().addListener(this);
        gSlider.valueProperty().addListener(this);
        bSlider.valueProperty().addListener(this);
        aSlider.valueProperty().addListener(this);
    }


    private void setOthers() {

        tt = new Tooltip("Press Space to Choose Color. " +
                "Press Space Again to Freeze Color. " +
                "Press Shift + X to copy Haxe Color. " +
                "Press Shift + C to copy RGBA Color. " +
                "Or Click on the Color Button to Copy Color.");
        tt.setWrapText(true);
        tt.setMaxWidth(200);

        content = new ClipboardContent();

    }


    // MR. ROBOT IS GETTING READY FOR ACTION :D
    private void setRobot() {
        robot = new GlassRobot();
        gc = pixelView.getGraphicsContext2D();

    }


    // SETTING UP ALL THE KEYBOARD SHORTCUTS.
    private void setKeyboardActions() {
        keyCombinationC = new KeyCodeCombination(KeyCode.C, KeyCombination.SHIFT_ANY);
        keyCombinationX = new KeyCodeCombination(KeyCode.X, KeyCombination.SHIFT_ANY);

        layout.addEventFilter(KeyEvent.KEY_PRESSED, event -> {

            if (keyCombinationX.match(event)) {

                copyToClipBoard(customColor.toString());

            } else if (keyCombinationC.match(event)) {

                copyToClipBoard(getRoundValue(customColor.getRed()) + "," +
                        getRoundValue(customColor.getGreen()) + "," +
                        getRoundValue(customColor.getBlue()) + "," +
                        toHalfRouned(customColor.getOpacity()));

            } else if (event.getCode() == KeyCode.SPACE) {

                updateToggle();

            }
        });
    }

    // COPY TO CLIPBOARD. USER CAN PASTE VALUE ANY WHERE NOW.
    private void copyToClipBoard(String value) {

        content.putString(value);
        Clipboard.getSystemClipboard().setContent(content);
        layout.requestFocus();

    }


    // SCANNING SCREEN , GETTING COLOR FROM SCREEN.
    // PUTTING THE COLORS IN THE SLIDERS.
    private void update() {
        Color realColor = robot.getCapturePixelColor(robot.getMouseLocation());
        gc.drawImage(robot.getCaptureRegion(
                new Rectangle2D(robot.getMouseLocation().getX() - 7, robot.getMouseLocation().getY() - 7, 14, 14)),
                0, 0,
                pixelView.getWidth(), pixelView.getHeight());

        rSlider.setValue(getRoundValue(realColor.getRed()));
        gSlider.setValue(getRoundValue(realColor.getGreen()));
        bSlider.setValue(getRoundValue(realColor.getBlue()));
        aSlider.setValue(realColor.getOpacity());


    }


    // WE ARE GETTING VALUES FROM SLIDERS AND SHOWING IT TO USER.
    private void updateColor() {

        customColor = Color.rgb((int) rSlider.getValue(), (int) gSlider.getValue(),
                (int) bSlider.getValue(), aSlider.getValue());

        colorView.setFill(customColor);

        rLabel.setText(getRoundValue(customColor.getRed()) + "");
        gLabel.setText(getRoundValue(customColor.getGreen()) + "");
        bLabel.setText(getRoundValue(customColor.getBlue()) + "");
        aLabel.setText(toHalfRouned(customColor.getOpacity()) + "");

        rgbValue.setText(getRoundValue(customColor.getRed()) + "," +
                getRoundValue(customColor.getGreen()) + "," +
                getRoundValue(customColor.getBlue()) + "," +
                toHalfRouned(customColor.getOpacity()));
        haxeValue.setText(customColor.toString().substring(2));
    }


    // TOGGLING SCREEN SCANNING FOR COLORS.
    private void updateToggle() {

        updateSwitch = !updateSwitch;

        if (updateSwitch) {
            Thread t = new Thread(() -> {
                while (updateSwitch) {
                    try {
                        Platform.runLater(this::update);
                        Thread.sleep(50);
                    } catch (Exception e) {
                    }
                }
            });
            t.start();
        }
    }


    // WILL BE FIRE WHEN LAYOUT WILL BE LOADED.
    // THIS IS WHERE ALL ACTIONS STARTS FROM.
    @FXML
    private void initialize() {
        setRobot();
        setKeyboardActions();
        setBindings();
        setOthers();
    }

    // WHEN USER WILL CLICK ON THE COLOR BUTTONS THIS WILL FIRE.
    // WE ARE GETTING THE TEXT FROM THE BUTTON AND COPING IT TO THE CLIPBOARD.
    @FXML
    private void clicked(ActionEvent event) {
        copyToClipBoard(((Button) event.getTarget()).getText());
    }


    @FXML
    public void close(MouseEvent evt) {

        // SHOWING A DIALOG TO CONFIRM USER ACTION WHEN USER TRYING TO CLOSE THE APPLICATION.
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Quark Color Picker is Closing");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to Exit ?");
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initOwner(layout.getScene().getWindow());
        alert.setOnCloseRequest(event -> alert.hide());


        ButtonType buttonSystemTray = new ButtonType("System Tray");
        ButtonType buttonClose = new ButtonType("Close");
        ButtonType buttonCancel = new ButtonType("Cancel");

        //ADDING BUTTONS.
        alert.getButtonTypes().setAll(buttonSystemTray, buttonClose,buttonCancel);




        // USER CAN CHOOSE WHAT THEY WANT TO DO.
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonSystemTray){
            WindowControl.getInstance().hideWindow();
        } else if (result.get() == buttonClose) {
            System.exit(0);
        } else if (result.get() == buttonCancel) {
            alert.close();
        }
    }

    // WHEN EVER SLIDERS ARE MOVING THIS WILL FIRE.
    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        updateColor();
    }


    //PRESSING AND DRAGGING WINDOW , SINCE WE ARE USING CUSTOM WINDOW WE HAVE TO DO IT MANUALLY.
    @FXML
    public void onPressed(MouseEvent event) {
        xOffset = layout.getScene().getWindow().getX() - event.getScreenX();
        yOffset = layout.getScene().getWindow().getY() - event.getScreenY();
    }

    @FXML
    public void onDrag(MouseEvent event) {
        layout.getScene().getWindow().setX(event.getScreenX() + xOffset);
        layout.getScene().getWindow().setY(event.getScreenY() + yOffset);
    }

    // INSTRUCTION HIDE AND SHOW ON WINDOW CLICK.
    @FXML
    public void paneOnClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {

            tt.show(layout, event.getScreenX(), event.getScreenY());

        } else if (event.getButton() == MouseButton.PRIMARY) {
            tt.hide();
        }
    }


}
