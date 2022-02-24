package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.User;
import utils.JDBC;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/** Handles database interaction for the User class. */
public class DBUser {

    private final static Logger LOG = Logger.getLogger("login_activity.txt");
    private static PreparedStatement ps;
    private static ResultSet rs;

    /**
     * Creates an ObservableList of all the users in the database table.
     * @return an ObservableList of users.
     */
    public static ObservableList<User> getAllUsers(){
        ObservableList<User> userList = FXCollections.observableArrayList();

        try{
            String select = "SELECT User_ID, User_Name FROM users";

            ps = JDBC.connection.prepareStatement(select);
            rs = ps.executeQuery();

            // go through results one row at a time
            while (rs.next()){
                int id = rs.getInt("User_ID");
                String name = rs.getString("User_Name");
                User u = new User(id, name);
                userList.add(u);
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
        return userList;
    }

    /**
     * Validates a users login information and logs attempts to a text file.<br>
     * Checks for a username match in the users database table. If it finds a unique match, it checks the password entry.
     * If the password matches the users input, the connection is established.
     * Failed and successful attempts are logged to a text file via Logger.
     * @param name user input user name.
     * @param pw user input password.
     * @return true if user successfully logged in.
     */
    public static boolean validateLogin(String name, String pw){

        try {
            FileHandler fh = new FileHandler("login_activity.txt", true);
            SimpleFormatter sf = new SimpleFormatter();
            fh.setFormatter(sf);
            LOG.addHandler(fh);
        }
        catch (IOException e){
            Logger.getLogger(DBUser.class.getName()).log(Level.INFO, null, e);
        }
        try {
            JDBC.openConnection();
            String validateLogin = "SELECT User_ID, User_Name, Password FROM users WHERE User_Name = ?";

            ps = JDBC.connection.prepareStatement(validateLogin);
            ps.setString(1, name);
            rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                LOG.log(Level.INFO, "Failed login attempt.\n" + "Username " + name + " not found.");
            } else {
                while (rs.next()) {
                    String checkP = rs.getString("Password");
                    int id = rs.getInt("User_ID");
                    String uName = rs.getString("User_Name");

                    if (!checkP.equals(pw)) {
                        LOG.log(Level.INFO, "Failed login attempt. Incorrect password.\n" + "User ID: " + id + "\n" + "User name: " + uName);
                        try {
                            ps.close();
                            rs.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        return false;
                    } else {
                        User u = new User(id, uName);
                        JDBC.setCurrentUser(u);
                        LOG.log(Level.INFO, "Successful login attempt.\n" + "User ID: " + id + "\n" + "User name: " + uName);
                        try {
                            ps.close();
                            rs.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        try{
            ps.close();
            rs.close();
            JDBC.closeConnection();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks if user ID input exists in the users database table and alerts the user if not.
     * @param id user id to check.
     * @return if users exists.
     * @throws SQLException if SQL statement error is thrown.
     */
    public static boolean userCheck (int id) throws SQLException {
        String check = "SELECT User_ID FROM users WHERE User_ID = ?";

        ps = JDBC.connection.prepareStatement(check);
        ps.setInt(1, id);
        rs = ps.executeQuery();

        if (!rs.isBeforeFirst()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("User ID not found.");
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
