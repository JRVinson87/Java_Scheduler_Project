package controller;

import DAO.DBUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.ResourceBundle;

/** Methods for the LoginViewController. */
public class LoginViewController implements Initializable{

    public Label loginZone;
    public Label labelName;
    public Label labelPass;
    public Label labelLocation;
    public TextField textfieldName;
    public PasswordField textfieldPW;
    public Button buttonLogin;
    public Button buttonExit;
    public Label labelInvalidName;
    public Label labelBlank;

    Stage stage;
    Parent scene;

    /**
     * Login button to validate user in DB.<br>
     * When Login button is clicked, textfields are checked to see if they are blank.
     * If textfields are blank, the user is alerted. If fields are populated, validateLogin method is called.
     * If a valid user is found, the DB connection is established and the main menu loads.
     * @param actionEvent Clicking Login button.
     */
    public void onActionLogin(ActionEvent actionEvent) throws IOException {
        labelInvalidName.setVisible(false);
        labelBlank.setVisible(false);

        if (!textfieldName.getText().isBlank() && !textfieldPW.getText().isBlank()){

            if(DBUser.validateLogin(textfieldName.getText(), textfieldPW.getText())){
                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
                stage.setScene(new Scene(scene));
                stage.centerOnScreen();
                stage.show();
            }
            else {
                labelInvalidName.setVisible(true);
            }
        }
        else{
            labelBlank.setVisible(true);
        }
    }

    /**
     * Closes database connection and closes the program.
     * @param actionEvent exit button is clicked.
     */
    public void onActionExit(ActionEvent actionEvent) {
        try {
        Stage stage = (Stage) buttonExit.getScene().getWindow();
        stage.close();
        }
        catch (Exception e){
        e.printStackTrace();
        }
    }

    /**
     * Sets the in scene error message visibility to false and gets the system default timezone.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labelInvalidName.setVisible(false);
        labelBlank.setVisible(false);

        ZoneId currentZone = ZoneId.systemDefault();
        loginZone.setText(currentZone.getId());
    }
}