package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Appointment;
import model.Contact;
import utils.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/** Holds database interaction methods for Appointments (Contains lambda). */
public class DBAppointment {

    private static PreparedStatement ps;
    private static ResultSet rs;

    /**
     * Creates an observablelist of appointments from the appointment database table for the appointment tableview.
     * SELECTs needed data from the table and uses a while loop to extract data from a ResultSet.
     * The observablelist is created one appointment at a time with the appointment constructor.
     * @return all appointments.
     */
    public static ObservableList<Appointment> getAllAppointments(){
        ObservableList<Appointment> appList = FXCollections.observableArrayList();

        try {
            String select = "SELECT Appointment_ID, Title, Description, Location, Contact_ID, Type, Start, End, Customer_ID, User_ID " +
                    "FROM appointments";

            ps = JDBC.connection.prepareStatement(select);
            rs = ps.executeQuery();

            while (rs.next()){
                int aid = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String desc = rs.getString("Description");
                String location = rs.getString("Location");
                int contactId = rs.getInt("Contact_ID");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                LocalDateTime dts = start.toLocalDateTime();
                LocalDateTime dte = end.toLocalDateTime();

                Appointment a = new Appointment(aid, title, desc, location, type, dts, dte, customerId, userId, contactId);
                appList.add(a);
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
        return appList;
    }

    /**
     * INSERTs a new appointment entry into the appointments database table.<br>
     * INSERTs user input data and time is converted to a UTC timestamp due to mySQL driver V8.0.X.
     * Creates a localized datetime which is converted to a timestamp for the creation and update time.
     * @param title title to set.
     * @param type type to set.
     * @param desc description to set.
     * @param location location to set.
     * @param dtStart start time to set.
     * @param dtEnd end time to set.
     * @param customerId customer ID to set.
     * @param contact contact to set.
     */
    public static void addAppointment (String title, String type, String desc, String location,
                                       LocalDateTime dtStart, LocalDateTime dtEnd, int customerId, Contact contact){
        try {
            String insert = "INSERT INTO appointments VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            LocalDateTime now = LocalDateTime.now();
            Timestamp ts = Timestamp.valueOf(now);
            Timestamp start = Timestamp.valueOf(dtStart);
            Timestamp end = Timestamp.valueOf(dtEnd);

            ps = JDBC.connection.prepareStatement(insert);
            ps.setString(1, title);
            ps.setString(2, desc);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setTimestamp(5, start);
            ps.setTimestamp(6, end);
            ps.setTimestamp(7, ts);
            ps.setString(8, JDBC.getCurrentUser().getName());
            ps.setTimestamp(9, ts);
            ps.setString(10, JDBC.getCurrentUser().getName());
            ps.setInt(11, customerId);
            ps.setInt(12, JDBC.getCurrentUser().getId());
            ps.setInt(13, contact.getId());
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
     * Modifies user selected appointment in the appointments database table.<br>
     * User input is passed into method and UPDATE is used to modify the data where the Appointment_ID matches the users selection.
     * A LocalDateTime captures the current time when the button is pressed. It is converted to Timestamp to be entered into the table.
     * PreparedStatement is used to pass each value into the corresponding column.
     * @param title title update via user input.
     * @param type type update via user input.
     * @param desc description update via user input.
     * @param location location update via user input.
     * @param dtStart start date and time via user input.
     * @param dtEnd end date and time via user input.
     * @param customerId customer ID via user input.
     * @param contact contact via user input.
     * @param appId appointment ID via user selection.
     */
    public static void modAppointment (String title, String type, String desc, String location, LocalDateTime dtStart,
                                       LocalDateTime dtEnd, int customerId, int uid, Contact contact, int appId){
        try {
            String update = "UPDATE appointments " +
                    "SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, " +
                    "Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? " +
                    "WHERE Appointment_ID = ?";
            LocalDateTime now = LocalDateTime.now();
            Timestamp ts = Timestamp.valueOf(now);
            Timestamp start = Timestamp.valueOf(dtStart);
            Timestamp end = Timestamp.valueOf(dtEnd);

            ps = JDBC.connection.prepareStatement(update);
            ps.setString(1, title);
            ps.setString(2, desc);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setTimestamp(5, start);
            ps.setTimestamp(6, end);
            ps.setTimestamp(7, ts);
            ps.setString(8, JDBC.getCurrentUser().getName());
            ps.setInt(9, customerId);
            ps.setInt(10, uid);
            ps.setInt(11, contact.getId());
            ps.setInt(12, appId);
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
     * DELETEs an appointment from the appointments database table (Uses lambda for fewer LoC).<br>
     * A confirmation box asks the user if they are sure they want to delete the appointment.
     * If the user selects OK, the appointment is deleted and the user is notified.
     * Confirmation box saves on lines of code and class imports with a lambda expression.
     * If an appointment is deleted, the user is returned to the main view.
     * @param a the appointment to delete.
     */
    public static void delAppointment (Appointment a) {

        int aid = a.getId();
        String atype = a.getType();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Appointment Confirmation");
        alert.setContentText("This will remove the selected appointment (" + aid + " " + atype + ") from the database.\n" +
                "Are you sure you want to delete this appointment?");
        alert.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try {
                    String delete = "DELETE FROM appointments WHERE Appointment_ID = ?";

                    ps = JDBC.connection.prepareStatement(delete);
                    ps.setInt(1, aid);
                    ps.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
                alert2.setTitle("Deletion Successful");
                alert2.setContentText("Successfully deleted (" + aid + " " + atype + ")");
                alert2.showAndWait();
            }
        });

        try {
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks the appointment table to time conflicts.<br>
     * Finds appointments by Customer_ID and checks for time conflicts.
     * If a conflict is found, the user is notified and the appointment is not created.
     * @param customerId customer ID to match for row entry.
     * @param start start time/date to check.
     * @param end end time/date to check.
     * @return if a conflict was found or not.
     */
    public static boolean checkOverlap(int customerId, LocalDateTime start, LocalDateTime end){

        try{
            String check = "SELECT Start, End FROM appointments WHERE Customer_ID = ?";

            ps = JDBC.connection.prepareStatement(check);
            ps.setInt(1, customerId);
            rs = ps.executeQuery();

            if(!rs.isBeforeFirst()){
            }
            else{
                while(rs.next()){
                    Timestamp tss = rs.getTimestamp("Start");
                    Timestamp tse = rs.getTimestamp("End");
                    LocalDateTime a = tss.toLocalDateTime();
                    LocalDateTime b = tse.toLocalDateTime();

                    if ( (start.isEqual(a) || start.isAfter(a)) && (start.isBefore(b)) ) {
                        errorWindow("Starts while another meeting in progress.");
                        try{
                            ps.close();
                            rs.close();
                        }
                        catch (SQLException e){
                            e.printStackTrace();
                        }
                        return true;
                    }
                    else if ( (end.isAfter(a) && (end.isBefore(b) || end.isEqual(b))) ) {
                        errorWindow("Another meeting starts before this ends.");
                        try{
                            ps.close();
                            rs.close();
                        }
                        catch (SQLException e){
                            e.printStackTrace();
                        }
                        return true;
                    }
                    else if ( (start.isBefore(a) || start.isEqual(a)) && (end.isAfter(b) || end.isEqual(b)) ) {
                        errorWindow("Another meeting stars and ends within this window.");
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
        return false;
    }

    /**
     * Returns a list of unique type entries for filtering.
     * @return list of distinct type column entries.
     * @throws SQLException catches SQL exceptions.
     */
    public static ObservableList<String> uniqueType() throws SQLException {
        String u = "SELECT DISTINCT Type FROM appointments";
        ObservableList<String> o = FXCollections.observableArrayList();

        ps = JDBC.connection.prepareStatement(u);
        rs = ps.executeQuery();
        while(rs.next()) {
            String d = rs.getString(1);
            o.add(d);
        }
        try{
            ps.close();
            rs.close();}
        catch (SQLException e){
            e.printStackTrace();
        }

        return o;
    }

    /**
     * Template for error window popups.
     * @param s string to display in the window.
     */
    private static void errorWindow (String s){
        Alert invalid = new Alert(Alert.AlertType.ERROR);
        invalid.setTitle("Invalid Entry.");
        invalid.setContentText(s);
        invalid.showAndWait();
    }
}
