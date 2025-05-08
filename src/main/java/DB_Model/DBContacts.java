package DB_Model;

import Database.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Brandon Jones
 * - The DBContacts class mirrors the contacts table in the database
 */
public class DBContacts {

    int contactID;
    String name;
    String email;

    public DBContacts(int contactID, String name, String email) {
        this.contactID = contactID;
        this.name = name;
        this.email = email;
    }

    //*****Getters*****


    public int getContactID() {
        return contactID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }


    //*****Setters*****


    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //*****Helper Methods*****

    /**
     *
     * @return an ObservableList of all DBContacts objects, created by pulling from the contacts table in the database
     */
    public static ObservableList<DBContacts> getAllContacts() {
        ObservableList<DBContacts> allContacts = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM Contacts";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int contactID = rs.getInt(1);
                String name = rs.getString(2);
                String email = rs.getString(3);

                DBContacts C = new DBContacts(contactID, name, email);
                allContacts.add(C);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return allContacts;
    }

    /**
     *
     * @return an ObservableList of all contact names
     */
    public static ObservableList<String> getAllNames() {
        ObservableList<String> allNames = FXCollections.observableArrayList();
        for (DBContacts C : getAllContacts()) {
            allNames.add(C.getName());
        }
        return allNames;
    }

    /**
     *
     * @param contactID The contact ID of the contact to be searched
     * @return the contact name matching the contactID
     */
    public static String getNameFromID(int contactID) {
        for(DBContacts C : getAllContacts()) {
            if (C.getContactID() == contactID) {
                return C.getName();
            }
        }
        return "Not Found";
    }

    /**
     *
     * @param name the contact name to be searched
     * @return the contactID matching the contact name
     */
    public static int getIDFromName(String name) {
        for (DBContacts C : getAllContacts()) {
            if (C.getName().equals(name)) {
                return C.getContactID();
            }
        }
        return 1;
    }
}
