package DB_Model;

import Database.JDBC;
import Helper.TimeHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Brandon Jones
 * DBAppointments holds appointment objects that mirror those in the database, these are to be stored as
 * int and String values.
 */
public class DBAppointments {

    //All attributes for Appointments in the database
    int appointmentID;
    String title;
    String description;
    String location;
    String type;
    String start;
    String end;
    String createDate;
    String createdBy;
    String lastUpdate;
    String updatedBy;
    int customerID;
    int userID;
    int contactID;

    //The selected appointment, used for displaying selected appointment attributes in modify appointment form.
    public static DBAppointments selectedAppointment = new DBAppointments();

    public static DBAppointments upcomingAppointment = new DBAppointments();

    public static ObservableList<DBAppointments> allDBAppointments = FXCollections.observableArrayList();

    //*****Constructors*****

    public DBAppointments() {}

    public DBAppointments(int appointmentID, String title, String description, String location, String type, String start, String end, String createDate, String createdBy, String lastUpdate, String updatedBy, int customerID, int userID, int contactID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.updatedBy = updatedBy;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    //*****Getters*****

    public int getAppointmentID() {
        return appointmentID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
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

    public int getCustomerID() {
        return customerID;
    }

    public int getUserID() {
        return userID;
    }

    public int getContactID() {
        return contactID;
    }

    public static DBAppointments getSelectedAppointment() {
        return selectedAppointment;
    }


    //*****Setters*****

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
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

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public static void setSelectedAppointment(DBAppointments appointment) {selectedAppointment = appointment;}


    //*****Helper Methods*****

    /**
     * @return - An observableList of DBAppointments mirroring all appointments in the database.
     */
    public static ObservableList<DBAppointments> getAllAppointments() {
        //Clear allDBAppointments before method execution, otherwise there will be duplicates in allDBAppointments
        allDBAppointments.clear();
        try {
            String sql = "SELECT * FROM Appointments";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int appointmentID = rs.getInt(1);
                String title = rs.getString(2);
                String description = rs.getString(3);
                String location = rs.getString(4);
                String type = rs.getString(5);
                String start = rs.getString(6);
                String end = rs.getString(7);
                String createDate = rs.getString(8);
                String createdBy = rs.getString(9);
                String lastUpdate = rs.getString(10);
                String updatedBy = rs.getString(11);
                int customerID = rs.getInt(12);
                int userID = rs.getInt(13);
                int contactID = rs.getInt(14);


                DBAppointments A = new DBAppointments(appointmentID, title, description, location, type, start, end, createDate, createdBy, lastUpdate, updatedBy, customerID, userID, contactID);
                allDBAppointments.add(A);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return allDBAppointments;
    }

    /**
     *
     * @param A - A DBAppointment object mirroring the database appointment table, to be added to the database.
     */
    public static void addAppointment(DBAppointments A) {
        int appointmentID = A.getAppointmentID();
        String title = A.getTitle();
        String description = A.getDescription();
        String location = A.getLocation();
        String type = A.getType();
        String start = A.getStart();
        String end = A.getEnd();
        String createDate = A.getCreateDate();
        String createdBy = String.valueOf(A.getCreatedBy());
        String lastUpdate = A.getLastUpdate();
        String updatedBy = String.valueOf(A.getUpdatedBy());
        int customerID = A.getCustomerID();
        int userID = A.getUserID();
        int contactID = A.getContactID();

        try {
            String sql = "INSERT INTO Appointments VALUES ('"
                    + appointmentID + "', '" + title + "', '"
                    + description + "', '" + location + "', '"
                    + type + "', '" + start + "', '" + end + "', '"
                    + createDate + "', '" + createdBy + "', '"
                    + lastUpdate + "', '" + updatedBy + "', '"
                    + customerID + "', '" + userID + "', '"
                    + contactID + "');";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.execute();

            System.out.println("Added Appointment to Database!");
            System.out.println(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param appointmentID to find the selected appointment in the database.
     */
    public static void deleteAppointment(int appointmentID) {
        try {
            String sql = "DELETE FROM `client_schedule`.`Appointments` WHERE (`Appointment_ID` = '" + appointmentID + "');";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.execute();
            System.out.println("Appointment " + appointmentID + " deleted successfully.");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param appointmentID - The ID of the currently selected appointment in the TableView.
     * @return - The DBAppointment Object the Appointment ID refers to.
     */
    public static DBAppointments getAppointment(int appointmentID) {
        boolean DBAppointmentFound = false;
        for(DBAppointments A : allDBAppointments) {
            if (A.getAppointmentID() == appointmentID) {
                DBAppointmentFound = true;
                return A;
            }
        }
        if (!DBAppointmentFound) {
            System.out.println("Selected Appointment Not Found!");
        }
        return null;
    }

    /**
     *
     * @param start Appointment start
     * @param end Appointment end
     * @param customerID Customer that the appointment belongs to
     * @return True if there is a schedule conflict
     */
    public static boolean conflictChecker(String start, String end, int customerID) {

        boolean isConflict = false;

        //Convert times to LocalDateTimes
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(start, dtf);
        LocalDateTime endTime = LocalDateTime.parse(end, dtf);

        //Create List of customer appointments
        ObservableList<DBAppointments> customerAppointments = FXCollections.observableArrayList();
        for (DBAppointments A : getAllAppointments()) {
            if (A.getCustomerID() == customerID) {
                customerAppointments.add(A);
            }
        }

        for (DBAppointments A : customerAppointments) {
            //Convert Each appointment start and end to LocalDateTime to do a comparison
            LocalDateTime startTimeA = LocalDateTime.parse(A.getStart(), dtf);
            LocalDateTime endTimeA = LocalDateTime.parse(A.getEnd(), dtf);

            if (((startTime.isAfter(startTimeA)) && (startTime.isBefore(endTimeA))) || ((endTime.isAfter(startTimeA)) && (endTime.isBefore(endTimeA)))) {
                isConflict = true;

                //Convert "A" times to local for display purposes
                String localStart = TimeHelper.getLocal(A.getStart());
                String localEnd = TimeHelper.getLocal(A.getEnd());

                System.out.println("Local Start Time: " + localStart + " UTC Start Time: " + A.getStart());
                System.out.println("Local End Time: " + localEnd + " UTC End Time: " + A.getEnd());

                Alert scheduleConflict = new Alert(Alert.AlertType.WARNING);
                scheduleConflict.setTitle("Warning");
                scheduleConflict.setHeaderText("Schedule Conflict");
                scheduleConflict.setContentText("Customer already has an appointment during this time. \nStart: " + localStart + " End: " + localEnd);
                scheduleConflict.showAndWait();

                return isConflict;
            }
        }

        return isConflict;

    }

    /**
     *
     * @return - An observableList of all types of appointments, no repeated types in the list.
     */
    public static ObservableList<String> getAllTypes() {
        ObservableList<String> allTypes = FXCollections.observableArrayList();

        for (DBAppointments A : getAllAppointments()) {
            String type = A.getType();
            boolean typeAdded = false;
            for (String S : allTypes) {
                if (type.equals(S)) {
                    typeAdded = true;
                }
            }
            if (!typeAdded) {
                allTypes.add(type);
            }
        }
        return allTypes;
    }

}
