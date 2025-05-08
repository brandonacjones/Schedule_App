package DB_Model;

import Database.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Brandon Jones
 *  - The DBUsers class mirrors the Users table in the database
 */
public class DBUsers {

    public static DBUsers activeUser = new DBUsers();

    int userID;
    String name;
    String password;
    String createDate;
    String createdBy;
    String lastUpdate;
    String updatedBy;

    public DBUsers(int userID, String name, String password, String createDate, String createdBy, String lastUpdate, String updatedBy) {
        this.userID = userID;
        this.name = name;
        this.password = password;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.updatedBy = updatedBy;
    }

    public DBUsers() {};

    //*****Getters*****


    public int getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
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

    public static DBUsers getActiveUser() {
        return activeUser;
    }


    //*****Setters*****


    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public static void setActiveUser(DBUsers user) {
        activeUser = user;
    }


    //*****Helper Methods*****

    /**
     *
     * @return - A list of Users objects matching each user in the database
     */
    public static ObservableList<DBUsers> getAllUsers() {
        //Create Observable list of all users
        ObservableList<DBUsers> allUsers = FXCollections.observableArrayList();

        try {
            //Create SQL query and save it to a String sql
            String sql = "SELECT * FROM Users";

            //Create a PreparedStatement ps from sql
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            //Execute the query and save it to a results set
            ResultSet rs = ps.executeQuery();

            //while there are still items in ResultSet rs, save relevant attributes to Users object
            //and add created Users object to ObservableList allUsers
            while(rs.next()) {
                int userID = rs.getInt(1);
                String name = rs.getString(2);
                String password = rs.getString(3);
                String createDate = rs.getString(4);
                String createdBy = rs.getString(5);
                String lastUpdate = rs.getString(6);
                String updatedBy = rs.getString(7);

                DBUsers user = new DBUsers(userID, name, password, createDate, createdBy, lastUpdate, updatedBy);
                allUsers.add(user);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return allUsers;
    }
}
