package DB_Model;

import Database.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBFirstLevelDiv {

    int divisionID;
    String division;
    String createDate;
    String createdBy;
    String lastUpdate;
    String updatedBy;
    int countryID;

    //Observable list of all Division Objects, mirroring the database
    public static ObservableList<DBFirstLevelDiv> allFirstLevelDiv = FXCollections.observableArrayList();

    public DBFirstLevelDiv(int divisionID, String division, String createDate, String createdBy, String lastUpdate, String updatedBy, int countryID) {
        this.divisionID = divisionID;
        this.division = division;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.updatedBy = updatedBy;
        this.countryID = countryID;
    }

    //*****Getters*****

    public int getDivisionID() {
        return divisionID;
    }

    public String getDivision() {
        return division;
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

    public int getCountryID() {
        return countryID;
    }

    //*****Setters*****

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public void setDivision(String division) {
        this.division = division;
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

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    //*****Helper Methods*****

    /**
     *
     * @return - An observableList of DBFirstLevelDiv objects mirroring all first level divisions in the database.
     */
    public static ObservableList<DBFirstLevelDiv> getAllDivisions() {
        allFirstLevelDiv.clear();
        try {
            String sql = "SELECT * FROM first_level_divisions";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int divisionID = rs.getInt(1);
                String division = rs.getString(2);
                String createDate = rs.getString(3);
                String createdBy = rs.getString(4);
                String lastUpdate = rs.getString(5);
                String updatedBy = rs.getString(6);
                int countryID = rs.getInt(7);

                DBFirstLevelDiv F = new DBFirstLevelDiv(divisionID, division, createDate, createdBy, lastUpdate, updatedBy, countryID);
                allFirstLevelDiv.add(F);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return allFirstLevelDiv;
    }

    /**
     *
     * @return - An ObservableList of all division IDs
     */
    public static ObservableList<Integer> getAllDivisionIDs() {
        ObservableList<Integer> allDivisionIDs = FXCollections.observableArrayList();
        for(DBFirstLevelDiv F : allFirstLevelDiv) {
            allDivisionIDs.add(F.getDivisionID());
        }
        return allDivisionIDs;
    }

    /**
     *
     * @param ID to be converted to a name.
     * @return The division name matching the ID.
     */
    public static String getNameFromID(int ID) {
        for (DBFirstLevelDiv D : getAllDivisions()) {
            if (D.getDivisionID() == ID) {
                return D.getDivision();
            }
        }
        return "Not Found";
    }

    /**
     *
     * @param name to be converted to an ID.
     * @return The division ID matching the name.
     */
    public static int getIDFromName(String name) {
        for(DBFirstLevelDiv D : getAllDivisions()) {
            if (D.getDivision().equals(name)) {
                return D.getDivisionID();
            }
        }
        return 1;
    }

    /**
     *
     * @param countryID the ID of the selected country
     * @return all first level division names for the selected country
     */
    public static ObservableList<String> getDivisionNameFromCountry(int countryID) {
        ObservableList<String> allNames = FXCollections.observableArrayList();
        for (DBFirstLevelDiv D : DBFirstLevelDiv.getAllDivisions()) {
            if (D.getCountryID() == countryID) {
                allNames.add(D.getDivision());
            }
        }
        return allNames;
    }

    /**
     *
     * @param countryName The name of the country to find divisions for
     * @return an observable list of all division names matching the country name
     */
    public static ObservableList<String> getDivisionNameFromCountry(String countryName) {
        ObservableList<String> allNames = FXCollections.observableArrayList();
        int countryID = 1;

        for (DBCountries C : DBCountries.getAllCountries()) {
            if (C.getName().equals(countryName)) {
                countryID = C.getCountryID();
            }
        }

        for (DBFirstLevelDiv D : DBFirstLevelDiv.getAllDivisions()) {
            if (D.getCountryID() == countryID) {
                allNames.add(D.getDivision());
            }
        }
        return allNames;
    }

}
