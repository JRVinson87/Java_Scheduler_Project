package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Division;
import utils.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/** Handles database interaction for the Division class. */
public class DBDivision {

    private static PreparedStatement ps;
    private static ResultSet rs;

    /**
     * Creates and ObservableList of all the division entries in the division database table.
     * @return an ObservableList of divison entries.
     */
    public static ObservableList<Division> getAllDivisions(){
        ObservableList<Division> divisionList = FXCollections.observableArrayList();

        try{
            String select = "SELECT Division_ID, Division, Create_Date, Last_Update, Country_ID FROM first_level_divisions";

            ps = JDBC.connection.prepareStatement(select);
            rs = ps.executeQuery();

            // go through results one row at a time
            while (rs.next()){
                int id = rs.getInt("Division_ID");
                String division = rs.getString("Division");
                Timestamp createTS = rs.getTimestamp("Create_Date");
                Timestamp updateTS = rs.getTimestamp("Last_Update");
                int cid = rs.getInt("Country_ID");
                LocalDateTime ldtC = createTS.toLocalDateTime();
                LocalDateTime ldtU = updateTS.toLocalDateTime();

                Division d = new Division(id, division, ldtC, ldtU, cid);
                divisionList.add(d);
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
        return divisionList;
    }
}
