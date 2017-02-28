import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import others.WindowControl;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("layouts/picker_layout.fxml"));
        primaryStage.setTitle("Quark CPicker");
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        root.getStylesheets().add("css/style.css");
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("icons/quark_icon.png")));

        primaryStage.show();

        new WindowControl(primaryStage);

    }


    public static void main(String[] args) {
        launch(args);
    }




}