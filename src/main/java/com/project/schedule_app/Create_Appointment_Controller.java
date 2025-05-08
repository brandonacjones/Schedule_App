package com.project.schedule_app;

import DB_Model.*;
import Helper.TimeHelper;
import Tableviews.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author - Brandon Jones
 *
 *  - The controller class for the Create Appointment Window
 */
public class Create_Appointment_Controller {
    public TextField city_field;
    public TextField title_field;
    public TextField street_address_field;
    public TextArea description_area;
    public ComboBox contact_id_combo;
    public ComboBox customer_id_combo;
    public ComboBox first_level_combo;
    public ComboBox country_combo;
    public Button create_appointment_button;
    public Text appointment_id_text;
    public Button cancel_button;
    public TextField type_field;

    
    public DatePicker datePicker;

    public LocalTime startLocalTime;
    public LocalTime endLocalTime;
    public LocalDate startLocalDate;
    public TextField timePickerStart;
    public TextField timePickerEnd;


    Alert_Interface warningAlert = (t, h, c) -> {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(t);
        alert.setHeaderText(h);
        alert.setContentText(c);
        alert.showAndWait();
    };


    /**
     *  - Initializes all elements on the page when the page is opened.
     */
    public void initialize() {

        country_combo.setItems(DBCountries.getAllNames());
        first_level_combo.setItems(FXCollections.observableArrayList("--"));
        customer_id_combo.setItems(DBCustomers.getAllNames());
        contact_id_combo.setItems(DBContacts.getAllNames());
        timePickerStart.setPromptText("HH:MM");
        timePickerEnd.setPromptText("HH:MM");

        appointment_id_text.setText("Appointment ID: " + String.valueOf(AppointmentsTableview.generateAppointmentID()));
    }

    /**
     *  - Fills in the First Level Division combo box with the relevant data based on the selected country.
     */
    public void country_combo_pressed() {
        String selectedCountry = country_combo.getSelectionModel().getSelectedItem().toString();
        first_level_combo.setItems(DBFirstLevelDiv.getDivisionNameFromCountry(selectedCountry));
    }

    /**
     * - Performs all validation checks on user inputs, then adds a new appointment to the database.
     * - Uses lambda expression warningAlert to create alerts more efficiently.
     */
    public void create_appointment_button_pressed(ActionEvent actionEvent) throws IOException {
        //Check no values are empty
        boolean isEmpty = false;
        if (contact_id_combo.getSelectionModel().getSelectedItem() == null) {
            isEmpty = true;
        }
        if (customer_id_combo.getSelectionModel().getSelectedItem() == null) {
            isEmpty = true;
        }
        if (title_field.getText().isEmpty()) {
            isEmpty = true;
        }
        if (type_field.getText().isEmpty()) {
            isEmpty = true;
        }
        if (description_area.getText().isEmpty()) {
            isEmpty = true;
        }
        if (street_address_field.getText().isEmpty()) {
            isEmpty = true;
        }
        if (city_field.getText().isEmpty()) {
            isEmpty = true;
        }
        if (country_combo.getSelectionModel().getSelectedItem() == null) {
            isEmpty = true;
        }
        if (first_level_combo.getSelectionModel().getSelectedItem() == null) {
            isEmpty = true;
        }
        if (datePicker.getValue() == null) {
            isEmpty = true;
        }
        if (timePickerStart.getText().isEmpty()) {
            isEmpty = true;
        }
        if (timePickerEnd.getText().isEmpty()) {
            isEmpty = true;
        }


        //Display Alert if isEmpty == true
        if (isEmpty) {
            warningAlert.alert("Warning", "Cannot Create Appointment", "Please fill out every field before submitting.");
            return;
        }

        //Pull data from input fields and combo boxes
        int appointmentID = AppointmentsTableview.generateAppointmentID();
        String title = title_field.getText();
        String description = description_area.getText();
        String location = street_address_field.getText() + ", " + city_field.getText() + ", " + first_level_combo.getSelectionModel().getSelectedItem() + ", " + country_combo.getSelectionModel().getSelectedItem();
        String type = type_field.getText();
        String start = TimeHelper.getUTC(startLocalDate, startLocalTime);
        String end = TimeHelper.getUTC(startLocalDate, endLocalTime);
        String createDate = TimeHelper.getUTC();
        String createdBy = DBUsers.getActiveUser().getName();
        String lastUpdate = TimeHelper.getUTC();
        String updatedBy = DBUsers.getActiveUser().getName();
        int customerID = DBCustomers.getIDFromName(customer_id_combo.getSelectionModel().getSelectedItem().toString());
        int userID = DBUsers.getActiveUser().getUserID();
        int contactID = DBContacts.getIDFromName(contact_id_combo.getSelectionModel().getSelectedItem().toString());

        //Check if start and end are within business hours 0800 - 1800 ET
        if((!TimeHelper.withinBusinessHours(start)) || (!TimeHelper.withinBusinessHours(end))) {
            Alert outsideHours = new Alert(Alert.AlertType.WARNING);
            outsideHours.setTitle("Warning");
            outsideHours.setHeaderText("Outside Business Hours");
            outsideHours.setContentText("This appointment falls outside our business hours (0800 - 1800 ET)");
            outsideHours.showAndWait();
            return;
        }

        //Check if the appointment is in the future
        if (TimeHelper.inPast(start)) {
            warningAlert.alert("Warning", "Appointment starts in the past", "Please adjust the appointment start time \n so it is at a future date and time.");
            return;
        }

        //Check if customer already has an appointment during these times
        if (DBAppointments.conflictChecker(start, end, customerID)) {
            return;
        }

        //Create a DBAppointments Object
        DBAppointments newAppointment = new DBAppointments(appointmentID, title, description, location, type, start, end, createDate, createdBy, lastUpdate, updatedBy, customerID, userID, contactID);

        //Add the appointment to the database
        DBAppointments.addAppointment(newAppointment);

        //Reopen the All Appointments page.
        Stage dashboard = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("EN_Main_View.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        dashboard.setTitle("Appointment Manager");
        dashboard.setScene(scene);
        dashboard.show();

        //Close the current window.
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();

    }


    /**
     * - Reopens the All Appointments window, and closes the current window.
     */
    public void cancel_button_pressed(ActionEvent actionEvent) throws IOException{
        //Open the All Appointments Window
        Stage dashboard = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("EN_Main_View.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        dashboard.setTitle("Appointment Manager");
        dashboard.setScene(scene);
        dashboard.show();

        //Closes the current window
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     *
     * @param keyEvent the action of a user typing a key into the timePicker field
     *
     */
    public void timePickerStartPressed(KeyEvent keyEvent) {
        if ((timePickerStart.getLength() == 2)) {
            timePickerStart.setText(timePickerStart.getText() + ":");
            timePickerStart.positionCaret(3);
        }
        if (timePickerStart.getLength() > 4) {
            timePickerStart.setText(timePickerStart.getText(0, 5));
            timePickerStart.positionCaret(5);
        }
        if (timePickerStart.getLength() >= 5) {
            try {
                startLocalTime = LocalTime.of(Integer.parseInt(timePickerStart.getText(0, 2)), Integer.parseInt(timePickerStart.getText(3,5)));
            } catch (Exception e) {
                warningAlert.alert("Warning", "Invalid Time", "Please enter a valid start time.");
            }
        }
    }

    /**
     *
     * @param keyEvent the action of a user typing a key into the timePicker field
     *
     */
    public void timePickerEndPressed(KeyEvent keyEvent) {
        if ((timePickerEnd.getLength() == 2)) {
            timePickerEnd.setText(timePickerEnd.getText() + ":");
            timePickerEnd.positionCaret(3);
        }
        if (timePickerEnd.getLength() > 4) {
            timePickerEnd.setText(timePickerEnd.getText(0, 5));
            timePickerEnd.positionCaret(5);
        }
        if (timePickerEnd.getLength() >= 5) {
            try {
                endLocalTime = LocalTime.of(Integer.parseInt(timePickerEnd.getText(0, 2)), Integer.parseInt(timePickerEnd.getText(3,5)));
            } catch (Exception e) {
                warningAlert.alert("Warning", "Invalid Time", "Please enter a valid end time.");
            }

        }

    }

    /**
     *
     * @param actionEvent the date being picked
     */
    public void datePickerPressed(ActionEvent actionEvent) {
        startLocalDate = datePicker.getValue();
    }

}
