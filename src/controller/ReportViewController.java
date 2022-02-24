package controller;

import DAO.DBAppointment;
import DAO.DBContact;
import DAO.DBCountry;
import DAO.DBDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Country;
import model.Division;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

/**
 * Holds methods for the ReportView Controller (Contains lambdas).<br>
 * Additional report for project requirement 3-f is the total number of areas our business services per country
 * and when that division was added to our service area.
 */
public class ReportViewController implements Initializable {

    @FXML
    private Label labelNumContact;
    @FXML
    private Label labelNumCountry;
    @FXML
    private Label labelNumType;
    @FXML
    private Label labelNumMonth;
    @FXML
    private ComboBox<Contact> cbContact;
    @FXML
    private ComboBox<Month> cbMonth;
    @FXML
    private ComboBox<String> cbType;
    @FXML
    private ComboBox<Country> cbCountry;
    @FXML
    private TableColumn<Appointment, Integer> colACConId;
    @FXML
    private TableColumn<Appointment, Integer> colACCustId;
    @FXML
    private TableColumn<Appointment, String> colACDesc;
    @FXML
    private TableColumn<Appointment, LocalDateTime> colACEnd;
    @FXML
    private TableColumn<Appointment, Integer> colACId;
    @FXML
    private TableColumn<Appointment, String> colACLocation;
    @FXML
    private TableColumn<Appointment, LocalDateTime> colACStart;
    @FXML
    private TableColumn<Appointment, String> colACTitle;
    @FXML
    private TableColumn<Appointment, String> colACType;
    @FXML
    private TableColumn<Appointment, Integer> colAMConId;
    @FXML
    private TableColumn<Appointment, Integer> colAMCustId;
    @FXML
    private TableColumn<Appointment, String> colAMDesc;
    @FXML
    private TableColumn<Appointment, LocalDateTime> colAMEnd;
    @FXML
    private TableColumn<Appointment, Integer> colAMId;
    @FXML
    private TableColumn<Appointment, String> colAMLocation;
    @FXML
    private TableColumn<Appointment, LocalDateTime> colAMStart;
    @FXML
    private TableColumn<Appointment, String> colAMTitle;
    @FXML
    private TableColumn<Appointment, String> colAMType;
    @FXML
    private TableColumn<Appointment, Integer> colATConId;
    @FXML
    private TableColumn<Appointment, Integer> colATCustId;
    @FXML
    private TableColumn<Appointment, String> colATDesc;
    @FXML
    private TableColumn<Appointment, LocalDateTime> colATEnd;
    @FXML
    private TableColumn<Appointment, Integer> colATId;
    @FXML
    private TableColumn<Appointment, String> colATLocation;
    @FXML
    private TableColumn<Appointment, LocalDateTime> colATStart;
    @FXML
    private TableColumn<Appointment, String> colATTitle;
    @FXML
    private TableColumn<Appointment, String> colATType;
    @FXML
    private TableColumn<Division, Integer> colDId;
    @FXML
    private TableColumn<Division, String> colDName;
    @FXML
    private TableColumn<Division, LocalDateTime> colDAdded;
    @FXML
    private TableColumn<Division, LocalDateTime> colDUpdated;
    @FXML
    private TableColumn<Division, Integer> colDCountryId;
    @FXML
    private TableView<Appointment> tableAppointmentType;
    @FXML
    private TableView<Appointment> tableAppointmentMonth;
    @FXML
    private TableView<Appointment> tableAppointmentContact;
    @FXML
    private TableView<Division> tableCountryDivisions;

    private ObservableList<Contact> c = DBContact.getAllContacts();
    private ObservableList<Appointment> d = DBAppointment.getAllAppointments();
    private ObservableList<Country> ct = DBCountry.getAllCountries();
    private ObservableList<Division> dv = DBDivision.getAllDivisions();
    private ObservableList<Month> monthList = FXCollections.observableArrayList();





    /**
     * Returns the user to the main view.
     * @param event back button is clicked.
     * @throws IOException for scene failures.
     */
    @FXML
    void onButtonBack(ActionEvent event) throws IOException {
        Stage stage;
        Parent scene;
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();

    }

    /**
     * Creates a filtered appointment list report to display in the table view based on contact selected in combo box and displays number of entries (USES LAMBDA FOR FEWER LoC).
     * @param event contact selection in the combo box.
     */
    @FXML
    void onCBContact(ActionEvent event) {
        try{
            FilteredList<Appointment> filter = new FilteredList<>(d, i -> i.getContactId() == cbContact.getSelectionModel().getSelectedItem().getId());
            tableAppointmentContact.setItems(filter);
            labelNumContact.setText("Total appointments by selected contact: " + String.valueOf(tableAppointmentContact.getSelectionModel().getTableView().getItems().size()));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Creates a filtered appointment list report to display in the table view based on month selected in combo box and displays number of entries (USES LAMBDA FOR FEWER LoC).
     * @param event month selection in the combo box.
     */
    @FXML
    void onCBMonth(ActionEvent event) {
        try {
            FilteredList<Appointment> filterMonth = new FilteredList<>(d, i -> i.getStartMonth() == cbMonth.getValue());
            tableAppointmentMonth.setItems(filterMonth);
            labelNumMonth.setText("Total appointments by selected month: " + String.valueOf(tableAppointmentMonth.getSelectionModel().getTableView().getItems().size()));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Creates a filtered appointment list report to display in the table view based on type selected in combo box and displays number of entries (USES LAMBDA FOR FEWER LoC).
     * @param event type selection in the combo box.
     */
    @FXML
    void onCBType(ActionEvent event) {
        try {
            FilteredList<Appointment> ft = new FilteredList<>(d, i -> i.getType().equals(cbType.getValue()));
            tableAppointmentType.setItems(ft);
            labelNumType.setText("Total appointments by selected type: " + String.valueOf(tableAppointmentType.getSelectionModel().getTableView().getItems().size()));
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Creates a filtered division list to display in the table view based on country selected in combo box and displays number of entries (USES LAMBDA FOR FEWER LoC).
     * @param event country selection in the combo box.
     */
    public void onCBCountry(ActionEvent event) {
        try {
            FilteredList<Division> fd = new FilteredList<>(dv, i -> i.getCountryId() == cbCountry.getSelectionModel().getSelectedItem().getId());
            tableCountryDivisions.setItems(fd);
            labelNumCountry.setText("Total divisions in selected country: " + String.valueOf(tableCountryDivisions.getSelectionModel().getTableView().getItems().size()));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Initializes table views, combo boxes, and SELECT data labels.<br>
     * Combobox of Month type is filled with a for loop iterating through each month as an int but returning the Month value associated with that number.
     * Type combobox uses a select distinct SQL method.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //set up all table views with lists
        tableCountryDivisions.setItems(dv);
        tableAppointmentType.setItems(d);
        tableAppointmentMonth.setItems(d);
        tableAppointmentContact.setItems(d);

        // contact tableview
        colACId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colACTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colACDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colACLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colACType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colACStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        colACEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        colACCustId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colACConId.setCellValueFactory(new PropertyValueFactory<>("contactId"));

        // month table view
        colAMId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAMTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAMDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colAMLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colAMType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colAMStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        colAMEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        colAMCustId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colAMConId.setCellValueFactory(new PropertyValueFactory<>("contactId"));

        //type table view
        colATId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colATTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colATDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colATLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colATType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colATStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        colATEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        colATCustId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colATConId.setCellValueFactory(new PropertyValueFactory<>("contactId"));

        //country division table view
        colDId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDName.setCellValueFactory(new PropertyValueFactory<>("division"));
        colDAdded.setCellValueFactory(new PropertyValueFactory<>("created"));
        colDUpdated.setCellValueFactory(new PropertyValueFactory<>("updated"));
        colDCountryId.setCellValueFactory(new PropertyValueFactory<>("countryId"));

        //populate comboboxes
        cbContact.setItems(c);
        cbCountry.setItems(ct);

        int i = 1;
        Month m;
        while (i < 13){
            m = Month.of(i);
            monthList.add(m);
            i++;
        }
        cbMonth.setItems(monthList);

        try {
            cbType.setItems(DBAppointment.uniqueType());
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}

