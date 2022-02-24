package controller;

import DAO.DBAppointment;
import DAO.DBContact;
import DAO.DBCustomer;
import DAO.DBUser;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;
import utils.InputValidation;
import utils.JDBC;
import utils.TimeConversion;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.ResourceBundle;

/**
 * Holds methods for the AppointmentView Controller.
 */
public class AppointmentViewController implements Initializable {

    Stage stage;
    Parent scene;
    @FXML
    private ComboBox<Contact> cbContactId;
    @FXML
    private ComboBox<LocalTime> cbEndTime;
    @FXML
    private ComboBox<LocalTime> cbStartTime;
    @FXML
    private DatePicker dpEnd;
    @FXML
    private DatePicker dpStart;
    @FXML
    private TextField tfAId;
    @FXML
    private TextField tfCustomerId;
    @FXML
    private TextArea tfDesc;
    @FXML
    private TextField tfLocation;
    @FXML
    private TextField tfTitle;
    @FXML
    private TextField tfType;
    @FXML
    private TextField tfUId;

    private ObservableList<Contact> contactList = DBContact.getAllContacts();
    private Appointment modApp;
    private ZoneId localZoneId = ZoneId.systemDefault();
    private int customerId;

    /**
     * Sends the user back to the main menu without saving.
     * @param event cancel button is clicked.
     * @throws IOException if scene fails.
     */
    @FXML
    void onButtonCancel(ActionEvent event) throws IOException {
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Insert's or updates an appointment in the appointments database table.<br>
     * Reads user input and checks if any fields are blank. If they are, the user is alerted and returned.
     * If the start time is in the past, the user is alerted and returned.
     * If the start time is after the end time, the user is alerted and returned.
     * The entered date and time is checked for schedule conflicts for the customer. If one is found, the user is alerted and returned.
     * If an appointment is successfully created or updated, the user is returned to the main menu.
     * @param event save button is clicked.
     * @throws IOException on scene failures.
     */
    @FXML
    void onButtonSave(ActionEvent event) throws IOException, SQLException {

        boolean success = false;
        int userId = Integer.parseInt(tfUId.getText());
        Contact contact = cbContactId.getValue();
        String title = tfTitle.getText();
        String type = tfType.getText();
        String desc = tfDesc.getText();
        String location = tfLocation.getText();
        LocalDate dateStart = dpStart.getValue();
        LocalTime timeStart = cbStartTime.getValue();
        LocalDate dateEnd = dpEnd.getValue();
        LocalTime timeEnd = cbEndTime.getValue();
        int custId = Integer.parseInt(tfCustomerId.getText());

        if(!InputValidation.checkStringApp(title, type, desc, location))
            return;

        if(!DBUser.userCheck(userId) || !DBCustomer.customerCheck(custId))
            return;

        if (dateStart.isBefore(LocalDate.now())) {
            Alert invalid = new Alert(Alert.AlertType.ERROR);
            invalid.setTitle("Invalid Date Entry");
            invalid.setContentText("Start date cannot be in the past.");
            invalid.showAndWait();
            return;
        }

        if(dateEnd.isBefore(dateStart) || timeEnd.isBefore(timeStart)){
            Alert invalid2 = new Alert(Alert.AlertType.ERROR);
            invalid2.setTitle("Invalid Date Entry");
            invalid2.setContentText("Start date/time must be before end date/time.");
            invalid2.showAndWait();
            return;
        }

        LocalDateTime dtStart = LocalDateTime.of(dateStart, timeStart);
        LocalDateTime dtEnd = LocalDateTime.of(dateEnd, timeEnd);

        if(DBAppointment.checkOverlap(custId, dtStart, dtEnd))
            return;

        if (modApp == null) {
            DBAppointment.addAppointment(title, type, desc, location, dtStart, dtEnd, custId, contact);
            success = true;
        } else {
            int appId = Integer.parseInt(tfAId.getText());
            DBAppointment.modAppointment(title, type, desc, location, dtStart, dtEnd, custId, userId, contact, appId);
            success = true;
        }
        if(success) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.close();
        }
        else { return; }
    }

    /**
     * Initializes the appointment view to add or modify an appointment within business operating hours.<br>
     * The hour combo box is initialized with selections in the users local time that adhere to the business operating hours of 8am to 10pm EST.
     * The userID field is populated with the current logged on user's ID. Combo box of contacts is pulled from the database.
     * If the modify appointment button was clicked, all the data is prepopulated and the save button calls UPDATE instead of INSERT INTO.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbContactId.setItems(contactList);

        LocalDateTime ults = TimeConversion.userLocalTime(localZoneId, 8);
        LocalDateTime ulte = TimeConversion.userLocalTime(localZoneId, 22);

        while(ults.isBefore(ulte)){
            cbStartTime.getItems().add(ults.toLocalTime());
            ults = ults.plusMinutes(10);
            cbEndTime.getItems().add(ults.toLocalTime());
        }
    }

    /**
     * Sets text field for Customer ID.
     * @param customer selected customer to get data from.
     */
    public void sendCustomer(Customer customer){
        customerId = customer.getId();
        tfCustomerId.setText(String.valueOf(customerId));
        tfUId.setText(String.valueOf(JDBC.getCurrentUser().getId()));
        cbContactId.getSelectionModel().selectFirst();
    }

    /**
     * Initializes the appointment selection data into the fields to be updated.
     * @param app selected appointment to edit.
     */
    public void sendApp(Appointment app){
        modApp = app;

        tfAId.setText(String.valueOf(modApp.getId()));
        tfUId.setText(String.valueOf(modApp.getUserId()));
        cbContactId.getSelectionModel().select( (modApp.getContactId() - 1) );
        tfTitle.setText(modApp.getTitle());
        tfType.setText(modApp.getType());
        tfDesc.setText(modApp.getDescription());
        dpStart.setValue(modApp.getStartDate());
        dpEnd.setValue(modApp.getEndDate());
        cbStartTime.setValue(modApp.getStartTime());
        cbEndTime.setValue(modApp.getEndTime());
        tfLocation.setText(modApp.getLocation());
        tfCustomerId.setText(String.valueOf(modApp.getCustomerId()));
    }
}
