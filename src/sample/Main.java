package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Application main class
 * @author Daniel Czyz
 * @version 1.0.1 29/05/2021
 */
public class Main extends Application {

    /**
     * Loads on application start Login screen window
     * @param primaryStage primary stage
     * @throws Exception java.io.Exception when wrong path to files given or files are missing
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("LoginSystem/Login.fxml"));

        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root, 600, 400));
        Image img = new Image(getClass().getResourceAsStream("/images/logo_blue.png"));
        primaryStage.getIcons().add(img);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
