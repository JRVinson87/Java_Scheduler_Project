package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import utils.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Holds database interaction SQL methods for the Contact class. */
public class DBContact {

    private static PreparedStatement ps;
    private static ResultSet rs;

    /**
     * Creates a list of all contacts in the contacts databasetable.
     * @return an ObservableList of Contact
     */
    public static ObservableList<Contact> getAllContacts(){
        ObservableList<Contact> contactList = FXCollections.observableArrayList();

        try{
            String select = "SELECT Contact_ID, Contact_Name, Email FROM contacts";

            ps = JDBC.connection.prepareStatement(select);
            rs = ps.executeQuery();

            // go through results one row at a time
            while (rs.next()){
                int id = rs.getInt("Contact_ID");
                String name = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                Contact c = new Contact(id, name, email);
                contactList.add(c);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            ps.close();
            rs.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return contactList;
    }
}
