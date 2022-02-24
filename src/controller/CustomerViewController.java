package controller;

import DAO.DBCountry;
import DAO.DBCustomer;
import DAO.DBDivision;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.Division;
import utils.InputValidation;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** Contains the methods for the Customer View Controller (Contains lambdas).  */
public class CustomerViewController implements Initializable {

    @FXML
    private ComboBox<Country> cbCountry;
    @FXML
    private ComboBox<Division> cbDivision;
    @FXML
    private TextField tfAddress;
    @FXML
    private TextField tfCId;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfPhone;
    @FXML
    private TextField tfPost;

    private Stage stage;
    private Parent scene;
    private ObservableList<Country> c = DBCountry.getAllCountries();
    private ObservableList<Division> d = DBDivision.getAllDivisions();
    private Customer modCustomer;

    /**
     * Initializes the combo box values for Country.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbCountry.setItems(c);
    }

    /**
     * Initializes fields with Customer data from user selection in Customer table.<br>
     * Method called from loader on MainView, accepts Customer from Customer table selection.
     * Sets a customer instance for use in the save button action.
     * Sets a sentCountry string to the Country Name from the customer data.
     * Sets each textfield with Customer getters.<br>
     * The country combo box is set with an enhanced for loop. The sent Country is
     * checked against each combo box entry's getName. If a match is found, setValue.<br>
     * The division combo box is set with an enhanced for loop. The sent Division is
     * checked against each combo box entry's getName. If a match is found, setValue.
     * @param customer Customer selection from customer table.
     */
    public void sendCustomer(Customer customer){
        modCustomer = customer;

        tfCId.setText(String.valueOf(customer.getId()));
        tfName.setText(customer.getName());
        tfAddress.setText(customer.getAddress());
        tfPost.setText(customer.getPostalCode());
        tfPhone.setText(customer.getPhone());

        for (Country country : cbCountry.getItems()){
            if (country.getName().equals(customer.getCountry())){
                cbCountry.setValue(country);
                break;
            }
        }
        FilteredList<Division> filter = new FilteredList<>(d, i -> i.getCountryId() == cbCountry.getSelectionModel().getSelectedItem().getId());
        cbDivision.setItems(filter);

        for (Division division : cbDivision.getItems()){
            if (division.getDivision().equals(customer.getDivision())){
                cbDivision.setValue((division));
            }
        }
    }

    /**
     * Either INSERTs or UPDATEs a customer into the customers table in the database.<br>
     * Checks if any field is blank with InputValidation and alerts user if there is a blank field.
     * If the user is adding a new customer, modCustomer will be null and the addCustomer method will be called to INSERT the new customer.
     * If the user is modifying a customer, modCustomer will be populated and modCustomer is called to UPDATE the customer.
     * If either INSERT or UPDATE is successful, boolean success is set to true and the user is taken back to the main menu.
     * @param actionEvent clicking the Save button.
     * @throws IOException if there is a problem with switching scenes.
     */
    public void onButtonSave(ActionEvent actionEvent) throws IOException {

        boolean success = false;
        String name = tfName.getText();
        String address = tfAddress.getText();
        Division division = cbDivision.getValue();
        String post = tfPost.getText();
        String phone = tfPhone.getText();

        if(!InputValidation.checkStringCustomer(name, address, phone, post)){
            return;
        }

        if (modCustomer == null) {
            DBCustomer.addCustomer(name, address, division, post, phone);
            success = true;
        } else {
            int cid = Integer.parseInt(tfCId.getText());
            DBCustomer.modCustomer(name, address, division, post, phone, cid);
            success = true;
        }

        if(success) {
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }
        else{
            return;
        }
    }

    /**
     * Sends the user back to the main menu without creating a customer.
     * @param actionEvent Cancel button clicked.
     * @throws IOException if scene fails.
     */
    public void onButtonCancel(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Filters the division list to only have locations that match the selected country (LAMBDA TO REDUCE LoC). <br>
     * Creates a filteredlist with a lambda expression matching the countryId in the Division list to the Id in the Country list.
     * @param actionEvent combobox clicked and country selected.
     */
    public void onCBCountry(ActionEvent actionEvent) {
        FilteredList<Division> filter = new FilteredList<>(d, i -> i.getCountryId() == cbCountry.getSelectionModel().getSelectedItem().getId());
        cbDivision.setItems(filter);
        cbDivision.getSelectionModel().selectFirst();
    }
}
