package Tableviews;

import DB_Model.DBAppointments;
import DB_Model.DBContacts;
import Helper.TimeHelper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Appointments stores a modified version of DBAppointments, specifically meant to be displayed in TableViews.
 *  It's attributes are stored as ObservableValue Objects.
 */
public class AppointmentsTableview {
    ObservableValue<Integer> appointmentID;
    ObservableValue<String> title;
    ObservableValue<String> description;
    ObservableValue<String> location;
    ObservableValue<Integer> contactID;
    ObservableValue<String> type;
    ObservableValue<String> start;
    ObservableValue<String> end;
    ObservableValue<Integer> customerID;
    ObservableValue<Integer> userID;

    public AppointmentsTableview(ObservableValue<Integer> appointmentID, ObservableValue<String> title, ObservableValue<String> description, ObservableValue<String> location, ObservableValue<Integer> contactID, ObservableValue<String> type, ObservableValue<String> start, ObservableValue<String> end, ObservableValue<Integer> customerID, ObservableValue<Integer> userID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contactID = contactID;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
    }

    public AppointmentsTableview() {};

    public static ObservableList<AppointmentsTableview> allAppointments = FXCollections.observableArrayList();

    //*****Getters*****

    public ObservableValue<Integer> getAppointmentID() {
        return appointmentID;
    }

    public ObservableValue<String> getTitle() {
        return title;
    }

    public ObservableValue<String> getDescription() {
        return description;
    }

    public ObservableValue<String> getLocation() {
        return location;
    }

    public ObservableValue<Integer> getContactID() {
        return contactID;
    }

    public ObservableValue<String> getType() {
        return type;
    }

    public ObservableValue<String> getStart() {
        return start;
    }

    public ObservableValue<String> getEnd() {
        return end;
    }

    public ObservableValue<Integer> getCustomerID() {
        return customerID;
    }

    public ObservableValue<Integer> getUserID() {
        return userID;
    }

    //*****Setters*****

    public void setAppointmentID(ObservableValue<Integer> appointmentID) {
        this.appointmentID = appointmentID;
    }

    public void setTitle(ObservableValue<String> title) {
        this.title = title;
    }

    public void setDescription(ObservableValue<String> description) {
        this.description = description;
    }

    public void setLocation(ObservableValue<String> location) {
        this.location = location;
    }

    public void setContactID(ObservableValue<Integer> contactID) {
        this.contactID = contactID;
    }

    public void setType(ObservableValue<String> type) {
        this.type = type;
    }

    public void setStart(ObservableValue<String> start) {
        this.start = start;
    }

    public void setEnd(ObservableValue<String> end) {
        this.end = end;
    }

    public void setCustomerID(ObservableValue<Integer> customerID) {
        this.customerID = customerID;
    }

    public void setUserID(ObservableValue<Integer> userID) {
        this.userID = userID;
    }

    //*****Helper Functions*****

    /**
     *
     * @return A consecutive appointment ID value.
     */
    public static int generateAppointmentID() {
        int max = 1;
        for (DBAppointments A : DBAppointments.allDBAppointments) {
            if(A.getAppointmentID() > max) {
                max = A.getAppointmentID();
            }
        }
        return max + 1;
    }

    /**
     * Takes every DBAppointment from allDBAppointments and converts it to an Appointment object for display in a TableView
     */
    public static void generateAllAppointments() {
        allAppointments.clear();
        for(int i = 0; i < DBAppointments.allDBAppointments.size(); ++i) {
            DBAppointments A = DBAppointments.allDBAppointments.get(i);
            ObservableValue<Integer> appointmentID = new SimpleIntegerProperty(A.getAppointmentID()).asObject();
            ObservableValue<String> title = new SimpleStringProperty(A.getTitle());
            ObservableValue<String> description = new SimpleStringProperty(A.getDescription());
            ObservableValue<String> location = new SimpleStringProperty(A.getLocation());
            ObservableValue<Integer> contactID = new SimpleIntegerProperty(A.getContactID()).asObject();
            ObservableValue<String> type = new SimpleStringProperty(A.getType());
            ObservableValue<String> start = new SimpleStringProperty(TimeHelper.getLocal(A.getStart()));
            ObservableValue<String> end = new SimpleStringProperty(TimeHelper.getLocal(A.getEnd()));
            ObservableValue<Integer> customerID = new SimpleIntegerProperty(A.getCustomerID()).asObject();
            ObservableValue<Integer> userID = new SimpleIntegerProperty(A.getUserID()).asObject();
            AppointmentsTableview B = new AppointmentsTableview(appointmentID, title, description, location, contactID, type, start, end, customerID, userID);
            allAppointments.add(B);
        }
    }

    /**
     *
     * @param location - the location to be broken down
     * @param result - the part of the location we want (street, city, state/province, country)
     * @return - the selected part of the location string.
     */
    public static String breakDownLocation(String location, String result) {
        //result can be "street", "city", "state/province", "country"
        //check if location string is in correct format street, city, state/province, country
        boolean hasStreet = false;
        boolean hasCity = false;
        boolean hasStateProvince = false;
        boolean hasCountry = false;

        String street = location;
        String city = "Not Found";
        String stateProvince = "Not Found";
        String country = "Not Found";

        int cursor = 0;

        for(int i = 0; i < location.length(); ++i) {
            if(!hasStreet && location.charAt(i) == ',') {
                street = location.substring(cursor, i);
                cursor = i + 2;
                hasStreet = true;
            }
            else if (hasStreet && !hasCity && location.charAt(i) == ',') {
                city = location.substring(cursor, i);
                cursor = i + 2;
                hasCity = true;
            }
            else if (hasCity && !hasStateProvince && location.charAt(i) == ',') {
                stateProvince = location.substring(cursor, i);
                country = location.substring(i + 2);
                hasStateProvince = true;
                hasCountry = true;
            }
        }

        if (result.equals("street")) {return street;}
        else if (result.equals("city")) {return city;}
        else if (result.equals("state/province")) {return stateProvince;}
        else if (result.equals("country")) {return country;}
        else {return location;}

    }

    /**
     *
     * @return - An ObservableList of all appointments with a userID that matches the active user
     */
    public static ObservableList<AppointmentsTableview> getAppointmentByContact(String contactName) {
        ObservableList<AppointmentsTableview> filteredAppointments = FXCollections.observableArrayList();

        int contactID = DBContacts.getIDFromName(contactName);

        for(AppointmentsTableview A : allAppointments) {
            if(A.getContactID().getValue() == contactID) {
                filteredAppointments.add(A);
            }
        }
        return filteredAppointments;
    }


}
