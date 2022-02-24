package controller;

import DAO.DBAppointment;
import DAO.DBCustomer;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.ResourceBundle;

/** Holds methods for the MainView Controller (Contains lambdas). */
public class MainViewController implements Initializable {

    public Button buttonExit;
    public Label labelAppAlert;
    public RadioButton radioAll;
    public RadioButton radioWeek;
    public RadioButton radioMonth;
    @FXML
    private TableColumn<Appointment, Integer> colAUserId;
    @FXML
    private TableColumn<Appointment, Integer> colAConId;
    @FXML
    private TableColumn<Appointment, Integer> colACustId;
    @FXML
    private TableColumn<Appointment, String> colADesc;
    @FXML
    private TableColumn<Appointment, String> colAEnd;
    @FXML
    private TableColumn<Appointment, Integer> colAId;
    @FXML
    private TableColumn<Appointment, String> colALocation;
    @FXML
    private TableColumn<Appointment, String> colAStart;
    @FXML
    private TableColumn<Appointment, String> colATitle;
    @FXML
    private TableColumn<Appointment, String> colAType;
    @FXML
    public TableColumn<Customer, Integer> colCId;
    @FXML
    public TableColumn<Customer, String> colCName;
    @FXML
    public TableColumn<Customer, String> colCAddress;
    @FXML
    public TableColumn<Customer, String> colCPostal;
    @FXML
    public TableColumn<Customer, String> colCDivision;
    @FXML
    public TableColumn<Customer, String> colCCountry;
    @FXML
    public TableColumn<Customer, String> colCPhone;
    @FXML
    private TableView<Appointment> tableAppointment;
    @FXML
    private TableView<Customer> tableCustomer;
    @FXML
    private ToggleGroup tgView;

    Stage stage;
    Parent scene;
    private ObservableList<Appointment> atv = DBAppointment.getAllAppointments();

    /**
     * Opens the menu to add an appointment.
     * @param event Add appointment button clicked.
     */
    @FXML
    void onButtonAddApp(ActionEvent event) throws IOException {

        Customer app = tableCustomer.getSelectionModel().getSelectedItem();
        if (app == null) {
            Alert select = new Alert(Alert.AlertType.ERROR);
            select.setTitle("Error");
            select.setContentText("No customer selected. Please select a customer.");
            select.showAndWait();
            return;
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/AppointmentView.fxml"));
        loader.load();

        AppointmentViewController avController = loader.getController();
        avController.sendCustomer(app);

        stage = new Stage();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(((Button)event.getSource()).getScene().getWindow());
        stage.centerOnScreen();
        stage.showAndWait();

        tableAppointment.setItems(DBAppointment.getAllAppointments());
        colAId.setSortType(TableColumn.SortType.ASCENDING);
        tableAppointment.getSortOrder().add(colAStart);
        tableAppointment.sort();
        tgView.selectToggle(radioAll);
    }

    /**
     * Opens the menu to add a customer.
     * @param event Add customer button clicked.
     * @throws IOException if scene fails.
     */
    @FXML
    void onButtonAddCustomer(ActionEvent event) throws IOException {
        scene = FXMLLoader.load(getClass().getResource("/view/CustomerView.fxml"));
        stage = new Stage();
        stage.setScene(new Scene(scene));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(((Button)event.getSource()).getScene().getWindow());
        stage.centerOnScreen();
        stage.showAndWait();

        tableCustomer.setItems(DBCustomer.getAllCustomers());
        colCId.setSortType(TableColumn.SortType.ASCENDING);
        tableCustomer.getSortOrder().add(colCId);
        tableCustomer.sort();

    }

    /**
     * DELETEs selected appointment from appointment database table.<br>
     * Checks for an appointment selection. If no selection is found, alerts the user they need to select an entry first.
     * If a selection is found, the selected appointment is passed to the delAppointment method to delete the corresponding entry.
     * The user is then returned to the main menu and the table is sorted by customer id.
     * @param event Delete Appointment button clicked.
     */
    @FXML
    void onButtonDeleteApp(ActionEvent event) {

        Appointment a = tableAppointment.getSelectionModel().getSelectedItem();
        if (a == null) {
            Alert select = new Alert(Alert.AlertType.ERROR);
            select.setTitle("Error");
            select.setContentText("No appointment selected. Please select an appointment.");
            select.showAndWait();
            return;
        }
        DBAppointment.delAppointment(a);

        tableAppointment.setItems(DBAppointment.getAllAppointments());
        colAId.setSortType(TableColumn.SortType.ASCENDING);
        tableAppointment.getSortOrder().add(colAStart);
        tableAppointment.sort();
        tgView.selectToggle(radioAll);
    }

    /**
     * DELETEs selected user from customers database table.<br>
     * Checks for a user selection. If no selection is found, alerts the user they need to select an entry first.
     * If a selection is found, the selected customer is passed to the delCustomer method to delete the corresponding entry.
     * The user is then returned to the main menu and the table is sorted by customer id.
     * @param event Delete Customer button clicked.
     */
    @FXML
    void onButtonDeleteCustomer(ActionEvent event) {

        Customer d = tableCustomer.getSelectionModel().getSelectedItem();

        if (d == null) {
            Alert select = new Alert(Alert.AlertType.ERROR);
            select.setTitle("Error");
            select.setContentText("No customer selected. Please select a customer.");
            select.showAndWait();
            return;
        }

        DBCustomer.delCustomer(d);

        tableCustomer.setItems(DBCustomer.getAllCustomers());
        colCId.setSortType(TableColumn.SortType.ASCENDING);
        tableCustomer.getSortOrder().add(colCId);
        tableCustomer.sort();

        tableAppointment.setItems(DBAppointment.getAllAppointments());
        colAId.setSortType(TableColumn.SortType.ASCENDING);
        tableAppointment.getSortOrder().add(colAStart);
        tableAppointment.sort();
        tgView.selectToggle(radioAll);
    }

    /**
     * Opens the update appointment menu.<br>
     * Checks for an appointment selection. If no selection is found, alerts the user they need to select an entry first.
     * If a selection is found, that selection is passed to the AppointmentView sendApp method.
     * @param event Modify Appointment button clicked.
     */
    @FXML
    void onButtonModApp(ActionEvent event) throws IOException {

        Appointment a = tableAppointment.getSelectionModel().getSelectedItem();
        if (a == null) {
            Alert select = new Alert(Alert.AlertType.ERROR);
            select.setTitle("Error");
            select.setContentText("No appointment selected. Please select an appointment.");
            select.showAndWait();
            return;
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/AppointmentView.fxml"));
        loader.load();

        AppointmentViewController avController = loader.getController();
        avController.sendApp(a);

        stage = new Stage();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(((Button)event.getSource()).getScene().getWindow());
        stage.centerOnScreen();
        stage.showAndWait();

        tableAppointment.setItems(DBAppointment.getAllAppointments());
        colAId.setSortType(TableColumn.SortType.ASCENDING);
        tableAppointment.getSortOrder().add(colAStart);
        tableAppointment.sort();

        tgView.selectToggle(radioAll);
    }

    /**
     * Opens the update customer menu.<br>
     * Checks for a user selection. If no selection is found, alerts the user they need to select an entry first.
     * If a selection is found, that selection is passed to the CustomerView sendCustomer method.
     * @param event Modify Customer button clicked.
     */
    @FXML
    void onButtonModCustomer(ActionEvent event) throws IOException {

        Customer m = tableCustomer.getSelectionModel().getSelectedItem();
        if (m == null) {
            Alert select = new Alert(Alert.AlertType.ERROR);
            select.setTitle("Error");
            select.setContentText("No customer selected. Please select a customer.");
            select.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/CustomerView.fxml"));
        loader.load();

        CustomerViewController cvController = loader.getController();
        cvController.sendCustomer(m);

        stage = new Stage();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(((Button)event.getSource()).getScene().getWindow());
        stage.centerOnScreen();
        stage.showAndWait();

        tableCustomer.setItems(DBCustomer.getAllCustomers());
        colCId.setSortType(TableColumn.SortType.ASCENDING);
        tableCustomer.getSortOrder().add(colCId);
        tableCustomer.sort();
    }

    /**
     * Shows all appointments in the appointment tableview.
     * @param event View All radio button clicked.
     */
    @FXML
    void onRadioViewAll(ActionEvent event) {

        tableAppointment.setItems(DBAppointment.getAllAppointments());
        colAStart.setSortType(TableColumn.SortType.ASCENDING);
        tableAppointment.getSortOrder().add(colAStart);
        tableAppointment.sort();
    }

    /**
     * Shows only appointments occurring in the current month (Uses lambda for fewer LoC).<br>
     * Users the Month class to check the month of an appointment and the current month.
     * A filtered list is created with a lambda expression that checks for matches between the current month and an appointment month.
     * @param event View by month radio button clicked.
     */
    @FXML
    void onRadioViewMonth(ActionEvent event) {
        ObservableList<Appointment> atvm = DBAppointment.getAllAppointments();
        Month mon = LocalDate.now().getMonth();
        FilteredList<Appointment> filterMonth = new FilteredList<>(atvm, i -> i.getStartMonth() == mon);
        tableAppointment.setItems(filterMonth);
        colAStart.setSortType(TableColumn.SortType.ASCENDING);
        tableAppointment.getSortOrder().add(colAStart);
        tableAppointment.sort();
    }

    /**
     * Shows only appointments occurring in the current week (Uses lambda for fewer LoC).<br>
     * Uses WeekFields to check week of appointment and current week.
     * A filtered list is created with a lambda expression that checks for matches between the current week and an appointment week.
     * @param event View by week radio button clicked.
     */
    @FXML
    void onRadioViewWeek(ActionEvent event) {
        ObservableList<Appointment> atvw = DBAppointment.getAllAppointments();
        WeekFields week = WeekFields.of(Locale.getDefault());
        int weekNum = LocalDate.now().get(week.weekOfWeekBasedYear());
        FilteredList<Appointment> filterWeek = new FilteredList<>(atvw, i -> i.getWeek() == weekNum);
        tableAppointment.setItems(filterWeek);
        colAStart.setSortType(TableColumn.SortType.ASCENDING);
        tableAppointment.getSortOrder().add(colAStart);
        tableAppointment.sort();
    }

    /**
     * Changes to the reports view.
     * @param actionEvent clicks Reports button.
     * @throws IOException catches scene errors.
     */
    @FXML
    public void onButtonReports(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ReportView.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * Closes database connection and closes the program.
     * @param actionEvent exit button is clicked.
     */
    @FXML
    public void onButtonExit(ActionEvent actionEvent) {
        try {
            stage = (Stage) buttonExit.getScene().getWindow();
            stage.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Checks for appointments within the next 15 minutes and initializes the customer and appointment tableviews.<br>
     * Customer and appointment table view columns are initialized. Current local time is established.
     * The appointment start time column is checked with a for loop against the current time to check if there are any appointments that start within 15 minutes.
     * If an appointment is found, the user is alerted with appoint ID and starte date/time.
     * If no appointment is found, the user is notified that there are no appointments within the next 15 minutes.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //get customers for table
        tableCustomer.setItems(DBCustomer.getAllCustomers());
        //set columns
        colCId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCPostal.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        colCDivision.setCellValueFactory(new PropertyValueFactory<>("division"));
        colCCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        colCPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        // set initial sort
        colCId.setSortType(TableColumn.SortType.ASCENDING);
        tableCustomer.getSortOrder().add(colCId);
        tableCustomer.sort();

        tableAppointment.setItems(atv);

        colAId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colATitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colADesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colALocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colAType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colAStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        colAEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        colACustId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colAConId.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        colAUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));

        colAStart.setSortType(TableColumn.SortType.ASCENDING);
        tableAppointment.getSortOrder().add(colAStart);
        tableAppointment.sort();

        LocalDateTime ct = LocalDateTime.now();
        labelAppAlert.setText("There are no appointments within 15 minutes of login.");
        for (Appointment i : atv){
            if (i.getStart().isAfter(ct) && i.getStart().minusMinutes(16).isBefore(ct)){
                labelAppAlert.setText("APPOINTMENT " + i.getId() + " " + i.getStartDate() + " " + i.getStartTime() + " starts within 15 minutes!");
                break;
            }
        }
    }
}