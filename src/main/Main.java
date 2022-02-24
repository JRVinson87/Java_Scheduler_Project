/**
 * Software II C195 Scheduler Project.
 * @author Joshua Vinson
 */
package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.JDBC;

import java.util.Locale;
import java.util.ResourceBundle;

/** Main method that initializes the stage and handles users locale and language. */
public class Main extends Application {

    Stage stage;

    /**
     * Main class that launches args and closes database connection when program is exited.
     * @param args launch arguments for Application.
     */
    public static void main(String[] args) {
        launch(args);
        JDBC.closeConnection();
    }

    /**
     * Initializes default locale to set language, loads initial stage.<br>
     * Uses the default locale to grab the correct translation bundle.
     * Loads the initial LoginView.
     * @param primaryStage the initial stage
     * @throws Exception for scene and action handle requests
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        Locale currentLocale = Locale.getDefault();
        ResourceBundle rb = ResourceBundle.getBundle("utils/Translate", currentLocale);

        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"), rb);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
