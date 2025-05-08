package com.project.schedule_app;

import DB_Model.*;
import Helper.TimeHelper;
import Tableviews.*;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Brandon Jones
 */
public class Modify_Appointment_Controller {
    public Text appointment_id_text;
    public ComboBox contact_id_combo;
    public ComboBox customer_id_combo;
    public TextField title_field;
    public TextField type_field;
    public TextArea description_area;
    public TextField street_address_field;
    public TextField city_field;
    public ComboBox country_combo;
    public ComboBox first_level_combo;
    public Button modify_appointment_button;
    public Button cancel_button;
    public DatePicker datePicker;
    public TextField timePickerStart;
    public TextField timePickerEnd;

    public LocalTime startLocalTime;
    public LocalTime endLocalTime;
    public LocalDate startLocalDate;

    String customerName = DBCustomers.getNameFromID(DBAppointments.selectedAppointment.getCustomerID());
    String contactName = DBContacts.getNameFromID(DBAppointments.selectedAppointment.getContactID());
    String stateProvince = AppointmentsTableview.breakDownLocation(DBAppointments.selectedAppointment.getLocation(), "state/province");
    String country = AppointmentsTableview.breakDownLocation(DBAppointments.selectedAppointment.getLocation(), "country");

    Alert_Interface warningAlert = (t, h, c) -> {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(t);
        alert.setHeaderText(h);
        alert.setContentText(c);
        alert.showAndWait();
    };

    /**
     * - Fills in all combo boxes and prompts with selected appointment information, runs when the window opens.
     */
    public void initialize() {
        //Fill all location options with selectedAppointment attributes
        country_combo.setPromptText(AppointmentsTableview.breakDownLocation(DBAppointments.selectedAppointment.getLocation(), "country"));
        first_level_combo.setPromptText(AppointmentsTableview.breakDownLocation(DBAppointments.selectedAppointment.getLocation(), "state/province"));
        city_field.setText(AppointmentsTableview.breakDownLocation(DBAppointments.selectedAppointment.getLocation(), "city"));
        street_address_field.setText(AppointmentsTableview.breakDownLocation(DBAppointments.selectedAppointment.getLocation(), "street"));

        //Fill other options in the combo boxes
        country_combo.setItems(DBCountries.getAllNames());
        first_level_combo.setItems(DBFirstLevelDiv.getDivisionNameFromCountry(AppointmentsTableview.breakDownLocation(DBAppointments.selectedAppointment.getLocation(), "country")));


        //Fill all Time Options with selectedAppointment attributes
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter localTimeFormat = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter localDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime startLocalDateTime = TimeHelper.getLocal(LocalDateTime.parse(DBAppointments.selectedAppointment.getStart(), dtf));
        LocalDateTime endLocalDateTime = TimeHelper.getLocal(LocalDateTime.parse(DBAppointments.selectedAppointment.getEnd(), dtf));
        startLocalDate = startLocalDateTime.toLocalDate();
        startLocalTime = startLocalDateTime.toLocalTime();
        endLocalTime = endLocalDateTime.toLocalTime();
        timePickerStart.setText(startLocalTime.format(localTimeFormat));
        timePickerEnd.setText(endLocalTime.format(localTimeFormat));
        datePicker.setValue(startLocalDate);

        //Fill out Appointment Details with selectedAppointment Attributes
        title_field.setText(DBAppointments.selectedAppointment.getTitle());
        type_field.setText(DBAppointments.selectedAppointment.getType());
        description_area.setText(DBAppointments.selectedAppointment.getDescription());
        appointment_id_text.setText("Appointment ID: " + DBAppointments.getSelectedAppointment().getAppointmentID());
        customer_id_combo.setPromptText(DBCustomers.getNameFromID(DBAppointments.selectedAppointment.getCustomerID()));
        contact_id_combo.setPromptText(DBContacts.getNameFromID(DBAppointments.selectedAppointment.getContactID()));

        //Fill other options in the combo boxes
        contact_id_combo.setItems(DBContacts.getAllNames());
        customer_id_combo.setItems(DBCustomers.getAllNames());


    }

    /**
     * - Performs all validation checks on inputted information, if user input passes validation, deletes old appointment and creates an updated appointment and adds it to the database.
     * - Uses lambda warningAlert to create alerts more efficiently.
     */
    public void modify_appointment_button_pressed(ActionEvent actionEvent) throws IOException {
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
        int appointmentID = DBAppointments.selectedAppointment.getAppointmentID();
        String title = title_field.getText();
        String description = description_area.getText();
        String location = street_address_field.getText() + ", " + city_field.getText() + ", " + stateProvince + ", " + country;
        String type = type_field.getText();
        String start = TimeHelper.getUTC(startLocalDate, startLocalTime);
        String end = TimeHelper.getUTC(startLocalDate, endLocalTime);
        String createDate = DBAppointments.selectedAppointment.getCreateDate();
        String lastUpdate = TimeHelper.getUTC();
        String createdBy = DBAppointments.selectedAppointment.getCreatedBy();
        String updatedBy = DBUsers.getActiveUser().getName();
        int customerID = DBCustomers.getIDFromName(customerName);
        int userID = DBUsers.getActiveUser().getUserID();
        int contactID = DBContacts.getIDFromName(contactName);

        //Check if the appointment is in the future
        if (TimeHelper.inPast(start)) {
            warningAlert.alert("Warning", "Appointment starts in the past", "Please adjust the appointment start time \n so it is at a future date and time.");
            return;
        }

        //Check if there is a schedule conflict
        if (DBAppointments.conflictChecker(start, end, customerID)) {
            return;
        }

        //Create a DBAppointments Object
        DBAppointments newAppointment = new DBAppointments(appointmentID, title, description, location, type, start, end, createDate, createdBy, lastUpdate, updatedBy, customerID, userID, contactID);

        //Add the appointment to the database
        DBAppointments.deleteAppointment(DBAppointments.selectedAppointment.getAppointmentID());
        DBAppointments.addAppointment(newAppointment);

        Stage dashboard = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("EN_Main_View.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        dashboard.setTitle("Appointment Manager");
        dashboard.setScene(scene);
        dashboard.show();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * - Closes the current window and goes back to the main window.
     */
    public void cancel_button_pressed(ActionEvent actionEvent) throws IOException{
        Stage dashboard = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("EN_Main_View.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        dashboard.setTitle("Appointment Manager");
        dashboard.setScene(scene);
        dashboard.show();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * - Takes the newly selected customer name and updates the selected appointment with the new name.
     */
    public void customerChanged() {
        customerName = customer_id_combo.getSelectionModel().getSelectedItem().toString();
    }

    /**
     * - Takes the newly selected contact name and updates the selected appointment with the new contact.
     */
    public void contactChanged() {
        contactName = contact_id_combo.getSelectionModel().getSelectedItem().toString();
    }

    /**
     * - Takes the newly selected division and updates the selected appointment division.
     */
    public void stateProvinceChanged() {
        stateProvince = first_level_combo.getSelectionModel().getSelectedItem().toString();
    }

    /**
     * - Takes the newly selected country and updates the division combo with that country's divisions.
     */
    public void countryChanged() {
        String selectedCountry = country_combo.getSelectionModel().getSelectedItem().toString();
        first_level_combo.setItems(DBFirstLevelDiv.getDivisionNameFromCountry(selectedCountry));
        country = country_combo.getSelectionModel().getSelectedItem().toString();
    }


    /**
     *
     * @param keyEvent the action of a user typing a key into the timePicker field
     * - Uses lambda warningAlert to create alerts more efficiently.
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
     * - Uses lambda warningAlert to create alerts more efficiently.
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

