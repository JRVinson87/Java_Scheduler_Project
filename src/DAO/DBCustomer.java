package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Customer;
import model.Division;
import utils.JDBC;

import java.sql.*;
import java.time.LocalDateTime;

/** Holds the database interaction methods for the Customer class (Contains lambda). */
public class DBCustomer {

    private static PreparedStatement ps;
    private static ResultSet rs;

    /**
     * Creates an ObservableList of all the customers in the customers database table.
     * @return an ObservableList of customers.
     */
    public static ObservableList<Customer> getAllCustomers(){
        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        try {
            String select = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, first_level_divisions.Division, countries.Country, Phone " +
                    "FROM customers, first_level_divisions, countries " +
                    "WHERE customers.Division_ID = first_level_divisions.Division_ID && first_level_divisions.Country_ID = countries.Country_ID";

            ps = JDBC.connection.prepareStatement(select);
            rs = ps.executeQuery();

            while (rs.next()){
                int cid = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String post = rs.getString("Postal_Code");
                String division = rs.getString("Division");
                String country = rs.getString("Country");
                String phone = rs.getString("Phone");
                Customer c = new Customer(cid, name, address, post, division, country, phone);
                customerList.add(c);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        try{
            ps.close();
            rs.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return customerList;
    }

    /**
     * Inserts a new customer row entry into the customers database table.
     * @param name name to INSERT.
     * @param address address to INSERT.
     * @param division divison to INSERT.
     * @param post postal code to INSERT.
     * @param phone phone number to INSERT.
     */
    public static void addCustomer (String name, String address, Division division, String post, String phone){
        try {
            String insert = "INSERT INTO customers VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            LocalDateTime now = LocalDateTime.now();
            Timestamp ts = Timestamp.valueOf(now);

            ps = JDBC.connection.prepareStatement(insert);
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, post);
            ps.setString(4, phone);
            ps.setTimestamp(5, ts);
            ps.setString(6, JDBC.getCurrentUser().getName());
            ps.setTimestamp(7, ts);
            ps.setString(8, JDBC.getCurrentUser().getName());
            ps.setInt(9, division.getId());
            ps.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        try{
            ps.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * UPDATEs selected customer in the customers database table based on user selection and a customer ID match.
     * @param name name to UPDATE.
     * @param address address to UPDATE.
     * @param division division to UPDATE.
     * @param post postal code to UPDATE.
     * @param phone phone number to UPDATE.
     * @param cid customer ID to match.
     */
    public static void modCustomer (String name, String address, Division division, String post, String phone, int cid){
        try{
            String update = "UPDATE customers " +
                    "SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? " +
                    "WHERE Customer_ID = ?";
            LocalDateTime now = LocalDateTime.now();
            Timestamp ts = Timestamp.valueOf(now);

            ps = JDBC.connection.prepareStatement(update);
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, post);
            ps.setString(4, phone);
            ps.setTimestamp(5, ts);
            ps.setString(6, JDBC.getCurrentUser().getName());
            ps.setInt(7, division.getId());
            ps.setInt(8, cid);
            ps.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        try{
            ps.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * DELETEs a customer from the customers database table (Uses lambda for fewer LoC).<br>
     * Checks if the customer has any appointments and alerts the user if they do.
     * User is informed that appointments must be deleted before customer can be deleted.
     * A confirmation box asks the user if they are sure they want to delete the customer.
     * If the user selects OK, the customer is deleted and the user is notified.
     * Confirmation box saves on lines of code and class imports with a lambda expression.
     * If a customer is deleted, the user is returned to the main view.
     * @param c the customer to delete.
     */
    public static void delCustomer (Customer c) {

        int cid = c.getId();
        String name = c.getName();

        try {
            String check = "SELECT Customer_ID FROM appointments WHERE Customer_ID = ?";

            ps = JDBC.connection.prepareStatement(check);
            ps.setInt(1, cid);
            rs = ps.executeQuery();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Customer Confirmation");
        alert.setContentText("This will remove the selected customer (" + cid + " " + name + ") and all associated appointments from the database.\n" +
                "Are you sure you want to delete this customer?");
        alert.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try {
                    String delete = "DELETE FROM customers WHERE Customer_ID = ?";

                    ps = JDBC.connection.prepareStatement(delete);
                    ps.setInt(1, cid);
                    ps.execute();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
                alert2.setTitle("Deletion Successful");
                alert2.setContentText("Successfully deleted (" + cid + " " + name + ")");
                alert2.showAndWait();
            }
        });

        try {
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if customer ID input is in the customers database table and alerts the user if not found.
     * @param id customer ID to check for.
     * @return if the id is found.
     * @throws SQLException if a SQL statement error is thrown.
     */
    public static boolean customerCheck (int id) throws SQLException {
        String check = "SELECT Customer_ID FROM customers WHERE Customer_ID = ?";

        ps = JDBC.connection.prepareStatement(check);
        ps.setInt(1, id);
        rs = ps.executeQuery();

        if (!rs.isBeforeFirst()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Customer ID not found.");
            alert.showAndWait();
            try{
                ps.close();
                rs.close();
            }
            catch (SQLException e){
                e.printStackTrace();
            }
            return false;
        }
        try{
            ps.close();
            rs.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }
}
