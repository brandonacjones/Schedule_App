package DB_Model;

import Database.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Brandon Jones
 *
 *  - The objects in this class are meant to mirror the countries database table
 */
public class DBCountries {

    int countryID;
    String name;
    String createDate;
    String createdBy;
    String lastUpdate;
    String updatedBy;

    public DBCountries(int countryID, String name, String createDate, String createdBy, String lastUpdate, String updatedBy) {
        this.countryID = countryID;
        this.name = name;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.updatedBy = updatedBy;
    }

    //*****Getters*****

    public int getCountryID() {
        return countryID;
    }

    public String getName() {
        return name;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    //*****Setters*****


    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    //*****Helper Methods*****

    /**
     *
     * @return An ObservableList of Countries Objects from the Database
     */
    public static ObservableList<DBCountries> getAllCountries() {
        //Create a List to hold all Countries Objects
        ObservableList<DBCountries> allDBCountries = FXCollections.observableArrayList();

        try {
            //Write SQL Query to be Executed
            String sql = "SELECT * FROM Countries;";

            //Create a Prepared Statement to be executed using sql String
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            //Execute the Prepared Statement and save it to a Result Set
            ResultSet rs = ps.executeQuery();

            //While there are still items in the Result Set, Save each attribute to a Countries Object
            //and add it to the allCountries Observable List
            while(rs.next()) {
                int countryID = rs.getInt(1);
                String name = rs.getString(2);
                String createDate = rs.getString(3);
                String createdBy = rs.getString(4);
                String lastUpdate = rs.getString(5);
                String updatedBy = rs.getString(6);

                DBCountries C = new DBCountries(countryID, name, createDate, createdBy, lastUpdate, updatedBy);
                allDBCountries.add(C);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        //Return the result list
        return allDBCountries;
    }

    /**
     *
     * @param divisionID we are trying to match to a country.
     * @return - The corresponding country name.
     */
    public static int getCountryIDFromDivision(int divisionID) {
        int countryID = -1;

        //Find the matching country ID
        for(DBFirstLevelDiv D : DBFirstLevelDiv.getAllDivisions()) {
            if (D.getDivisionID() == divisionID) {
                countryID = D.getCountryID();
            }
        }

        return countryID;
    }

    /**
     *
     * @return ObservableList of all country names.
     */
    public static ObservableList<String> getAllNames() {
        ObservableList<String> allNames = FXCollections.observableArrayList();
        for (DBCountries C : getAllCountries()) {
            allNames.add(C.getName());
        }
        return allNames;
    }

    /**
     *
     * @param divisionID The ID of the division that we need a country name for.
     * @return The corresponding country name
     */
    public static String getNameFromDivision(int divisionID) {
        int countryID = 1;
        for (DBFirstLevelDiv D : DBFirstLevelDiv.getAllDivisions()) {
            if (D.getDivisionID() == divisionID) {
                countryID = D.getCountryID();
            }
        }

        for (DBCountries C : getAllCountries()) {
            if (C.getCountryID() == countryID) {
                return C.getName();
            }
        }

        return "Not Found";
    }

    /**
     *
     * @param name The name of the country we need an ID for
     * @return The relevant CountryID
     */
    public static int getIDFromName(String name)  {
        for (DBCountries C : getAllCountries()) {
            if (C.getName().equals(name)) {
                return C.getCountryID();
            }
        }
        return 1;
    }

}
