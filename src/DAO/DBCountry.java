package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import utils.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Holds the database interaction methods for the Country class. */
public class DBCountry {

    private static PreparedStatement ps;
    private static ResultSet rs;

    /**
     * Creates an ObservableList of all country entries in the countries database table.
     * @return an ObservableList of countries
     */
    public static ObservableList<Country> getAllCountries(){
        ObservableList<Country> countryList = FXCollections.observableArrayList();

        try{
            String select = "SELECT Country_ID, Country FROM countries";

            ps = JDBC.connection.prepareStatement(select);
            rs = ps.executeQuery();

            // go through results one row at a time
            while (rs.next()){
                int id = rs.getInt("Country_ID");
                String country = rs.getString("Country");
                Country c = new Country(id, country);
                countryList.add(c);
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
        return countryList;
    }

    /**
     * Counts the number of countries in the countries data table.
     * @return the number of countries in the table.
     * @throws SQLException to catch SQL entry exceptions.
     */
    public static String numCountries () throws SQLException {
        String count = "SELECT COUNT(*) AS total FROM countries";

        ps = JDBC.connection.prepareStatement(count);
        rs = ps.executeQuery();
        rs.next();
        String n = rs.getString(1);
        return n;
    }
}