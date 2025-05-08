package DB_Model;

import Database.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author - Brandon Jones
 *
 * - The DBCustomers class is meant to reflect the database storage of all customers, all attributes stored as String and int
 */
public class DBCustomers {

    int customerID;
    String name;
    String address;
    String postalCode;
    String phone;
    String createDate;
    String createdBy;
    String lastUpdate;
    String updatedBy;
    int divisionID;

    public static ObservableList<DBCustomers> allDBCustomers = FXCollections.observableArrayList();

    public static DBCustomers selectedCustomer = new DBCustomers();

    public DBCustomers(int customerID, String name, String address, String postalCode, String phone, String createDate, String createdBy, String lastUpdate, String updatedBy, int divisionID) {
        this.customerID = customerID;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.updatedBy = updatedBy;
        this.divisionID = divisionID;
    }

    public DBCustomers() {};

    //*****Getters*****

    public int getCustomerID() {
        return customerID;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhone() {
        return phone;
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

    public int getDivisionID() {
        return divisionID;
    }

    //*****Setters*****

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    //*****Helper Methods*****

    /**
     *  - Gets all customers from the database and adds them to an ObservableList called allCustomers
     */
    public static ObservableList<DBCustomers> getAllDBCustomers() {
        allDBCustomers.clear();

        try {
            String sql = "SELECT * FROM Customers";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int customerID = rs.getInt(1);
                String name = rs.getString(2);
                String address = rs.getString(3);
                String postalCode = rs.getString(4);
                String phone = rs.getString(5);
                String createDate = rs.getString(6);
                String createdBy = rs.getString(7);
                String lastUpdate = rs.getString(8);
                String updatedBy = rs.getString(9);
                int divisionID = rs.getInt(10);

                DBCustomers C = new DBCustomers(customerID, name, address, postalCode, phone, createDate, createdBy, lastUpdate, updatedBy, divisionID);
                allDBCustomers.add(C);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return allDBCustomers;
    }

    /**
     *
     * @param id - The customerID to be converted to a name
     * @return - The name of the customer with the passed customerID
     */
    public static String getNameFromID(int id) {
        String name = null;
        for(DBCustomers C : DBCustomers.allDBCustomers) {
            if (C.getCustomerID() == id) {
                name = C.getName();
            }
        }
        return name;
    }

    /**
     *
     * @param name - The Name to be converted to an ID
     * @return - The customerID matching the passed Name
     */
    public static int getIDFromName(String name) {
        int id = 1;
        for(DBCustomers C : allDBCustomers) {
            if (C.getName().equals(name)) {
                return C.getCustomerID();
            }
        }
        return id;
    }

    /**
     *
     * @return - An ObservableList of all customer names
     */
    public static ObservableList<String> getAllNames() {
        ObservableList<String> allNames = FXCollections.observableArrayList();
        for (DBCustomers C : allDBCustomers) {
            allNames.add(C.getName());
        }
        return allNames;
    }

    /**
     *
     * @return - A unique CustomerID
     *
     */
    public static int generateCustomerID() {
        int max = 1;
        for (DBCustomers C : DBCustomers.allDBCustomers) {
            if (C.getCustomerID() > max) {
                max = C.getCustomerID();
            }
        }
        return (max + 1);
    }

    /**
     *
     * @param C A DBCustomers object containing all attributes to become a customer in the database.
     *
     * - Adds a customer to the database
     */
    public static void addCustomer(DBCustomers C) {
        try {
            String sql = "INSERT INTO Customers VALUES ('" + C.getCustomerID() + "', '" + C.getName() + "', '" + C.getAddress() + "', '"
                    + C.getPostalCode() + "', '" + C.getPhone() + "', '" + C.getCreateDate() + "', '" + C.getCreatedBy() + "', '"
                    + C.getLastUpdate() + "', '" + C.getUpdatedBy() + "', '" + C.getDivisionID() + "');";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.execute();
            System.out.println("Customer added Successfully!");
            System.out.println(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param ID The ID of the customer being removed
     *
     * - First deletes all appointments involving the customer, then deletes the customer.
     */
    public static void removeCustomer(int ID) {
        try {
            //Delete customer appointments from database
            String deleteAppointment = "DELETE FROM `client_schedule`.`Appointments` WHERE (`Customer_ID` = '" + ID + "');";
            PreparedStatement psAppointment = JDBC.getConnection().prepareStatement(deleteAppointment);
            psAppointment.execute();
            System.out.println("Appointments belonging to Customer " + ID + " removed successfully.");

            //Delete customer from database
            String deleteCustomer = "DELETE FROM `client_schedule`.`Customers` WHERE (`Customer_ID` = '" + ID + "');";
            PreparedStatement psCustomer = JDBC.getConnection().prepareStatement(deleteCustomer);
            psCustomer.execute();
            System.out.println("Customer " + ID + " removed successfully.");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param ID The ID of the customer being modified
     *
     * - Deletes the current customer with this ID and creates a new customer with a matching ID and updated attributes.
     */
    public static void modifyCustomer(int ID) {
        try {
            //Delete customer from database
            String deleteCustomer = "DELETE FROM `client_schedule`.`Customers` WHERE (`Customer_ID` = '" + ID + "');";
            PreparedStatement psCustomer = JDBC.getConnection().prepareStatement(deleteCustomer);
            psCustomer.execute();
            System.out.println("Customer " + ID + " removed successfully.");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param name The name to be parsed
     * @param firstOrLast An integer that chooses which name to return, 1 for first name, 2 for last name
     * @return the first or last name from the full name
     */
    public static String getFirstOrLast(String name, int firstOrLast) {
        String first = "firstName";
        String last = "lastName";
        for(int i = 0; i < name.length(); ++i) {
            if (name.charAt(i) == ' ') {
                first = name.substring(0, i);
                last = name.substring(i + 1);
            }
        }
        if (firstOrLast == 1) {
            return first;
        }
        else if (firstOrLast == 2) {
            return last;
        }
        else {
            System.out.println("Invalid input for firstOrLast, must be 1 or 2");
            return name;
        }
    }

    /**
     *
     * @param address - The address to be checked in the form of a string.
     * @param countryID - Determines what format to check the address against. (U.S. = 1, Canada = 2, U.K. == 3)
     * @return - A true value if the address is formatted correctly, a false value otherwise.
     */
    public static boolean addressFormatChecker(String address, int countryID) {
        //Make sure the address matches the country it belongs too
        //U.S. 123 ABC Street, City -- CountryID 1
        //Canadian 123 ABC Street, city -- CountryID 2
        //U.K. 123 ABC Street, neighborhood, city -- CountryID 3

        String s1 = "Null"; //Street Number
        String s2 = "Null"; //Street Name
        String s3 = "Null"; //Street Type
        String s4 = "Null"; //Neighborhood
        String s5 = "Null"; //City

        boolean b1 = false;
        boolean b2 = false;
        boolean b3 = false;
        boolean b4 = false;
        boolean isValid = true;

        int cursor = 0;

        if (countryID == 1 || countryID == 2) {
            for (int i = 0; i < address.length(); ++i) {
                //Street Number substring
                if (address.charAt(i) == ' ' && !b1) {
                    s1 = address.substring(cursor, i);
                    b1 = true;
                    cursor = i + 1;
                }
                //Street Name substring
                else if (address.charAt(i) == ' ' && !b2) {
                    s2 = address.substring(cursor, i);
                    b2 = true;
                    cursor = i + 1;
                }
                //Street Type and City Name substrings
                else if (address.charAt(i) == ',' && !b3) {
                    s3 = address.substring(cursor, i);
                    b3 = true;
                    s5 = address.substring(i + 2, address.length());
                }
            }
            //Check Street Number is Digits
            for (int i = 0; i < s1.length(); ++i) {
                if (!Character.isDigit(s1.charAt(i))) {
                    isValid = false;
                }
            }
            //Check Street Name is Alpha
            for (int i = 0; i < s2.length(); ++i) {
                if (!Character.isAlphabetic(s2.charAt(i))) {
                    isValid = false;
                }
            }
            //Check Street Type is Alpha
            for (int i = 0; i < s3.length(); ++i) {
                if (!Character.isAlphabetic(s3.charAt(i))) {
                    isValid = false;
                }
            }
            //Check City Name is Alpha
            for (int i = 0; i < s5.length(); ++i) {
                if (!Character.isAlphabetic(s5.charAt(i))) {
                    isValid = false;
                }
            }
        }
        else {
            for (int i = 0; i < address.length(); ++i) {
                //Street Number Substring
                if (address.charAt(i) == ' ' && !b1) {
                    s1 = address.substring(cursor, i);
                    b1 = true;
                    cursor = i + 1;
                }
                //Street Name Substring
                else if (address.charAt(i) == ' ' && !b2) {
                    s2 = address.substring(cursor, i);
                    b2 = true;
                    cursor = i + 1;
                }
                //Street Type Substring
                else if (address.charAt(i) == ',' && !b3) {
                    s3 = address.substring(cursor, i);
                    b3 = true;
                    cursor = i + 2;
                }
                //Neighborhood and City Substrings
                else if (address.charAt(i) == ',' && !b4) {
                    s4 = address.substring(cursor, i);
                    b4 = true;
                    s5 = address.substring(i + 2, address.length());
                }
            }
            //Check Street Number is Digits
            for (int i = 0; i < s1.length(); ++i) {
                if (!Character.isDigit(s1.charAt(i))) {
                    isValid = false;
                }
            }
            //Check Street Name is Alpha
            for (int i = 0; i < s2.length(); ++i) {
                if (!Character.isAlphabetic(s2.charAt(i))) {
                    isValid = false;
                }
            }
            //Check Street Type is Alpha
            for (int i = 0; i < s3.length(); ++i) {
                if (!Character.isAlphabetic(s3.charAt(i))) {
                    isValid = false;
                }
            }
            //Check Neighborhood is Alpha
            for (int i = 0; i < s4.length(); ++i) {
                if (!Character.isAlphabetic(s4.charAt(i))) {
                    isValid = false;
                }
            }
            //Check City Name is Alpha
            for (int i = 0; i < s5.length(); ++i) {
                if (!Character.isAlphabetic(s5.charAt(i))) {
                    isValid = false;
                }
            }
        }

        return isValid;
    }
}
