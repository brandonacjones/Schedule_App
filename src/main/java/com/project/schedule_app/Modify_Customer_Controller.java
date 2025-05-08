package com.project.schedule_app;

import DB_Model.DBCountries;
import DB_Model.DBCustomers;
import DB_Model.DBFirstLevelDiv;
import DB_Model.DBUsers;
import Helper.TimeHelper;
import Tableviews.CustomersTableview;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * @author Brandon Jones
 */
public class Modify_Customer_Controller {

    public Text customerID_text;
    public TextField firstName_field;
    public TextField lastName_field;
    public TextField address_field;
    public TextField postal_field;
    public TextField phone_field;
    public ComboBox<String> division_combo;
    public ComboBox<String> country_combo;
    public Button editCustomer_button;
    public Button cancel_button;

    int divisionID = DBCustomers.selectedCustomer.getDivisionID();
    int countryID = DBCountries.getCountryIDFromDivision(divisionID);

    /**
     * - Fills all combo boxes and fields with selected customer information.
     */
    public void initialize() {
        //Fill all fields with selected Customer Data
        customerID_text.setText("Customer ID: " + DBCustomers.selectedCustomer.getCustomerID());
        firstName_field.setText(DBCustomers.selectedCustomer.getFirstOrLast(DBCustomers.selectedCustomer.getName(), 1));
        lastName_field.setText(DBCustomers.selectedCustomer.getFirstOrLast(DBCustomers.selectedCustomer.getName(), 2));
        address_field.setText(DBCustomers.selectedCustomer.getAddress());
        postal_field.setText(DBCustomers.selectedCustomer.getPostalCode());
        phone_field.setText(DBCustomers.selectedCustomer.getPhone());
        division_combo.setItems(DBFirstLevelDiv.getDivisionNameFromCountry(countryID));
        division_combo.setPromptText(DBFirstLevelDiv.getNameFromID(divisionID));
        country_combo.setItems(DBCountries.getAllNames());
        country_combo.setPromptText(DBCountries.getNameFromDivision(divisionID));
    }

    /**
     * - Performs all validation checks on user inputs, if user input passes, deletes old customer and adds updated customer to the database.
     */
    public void editCustomer_button_pressed(ActionEvent actionEvent) throws IOException{

        //Check that all fields and combo boxes have information, if they are empty, display an alert and exit the method
        boolean isEmpty = false;

        if (firstName_field.getText().isEmpty()) {
            isEmpty = true;
        }
        if (lastName_field.getText().isEmpty()) {
            isEmpty = true;
        }
        if (address_field.getText().isEmpty()) {
            isEmpty = true;
        }
        if (postal_field.getText().isEmpty()) {
            isEmpty = true;
        }
        if (phone_field.getText().isEmpty()) {
            isEmpty = true;
        }
        if (country_combo.getSelectionModel().getSelectedItem() == null) {
            isEmpty = true;
        }
        if (division_combo.getSelectionModel().getSelectedItem() == null) {
            isEmpty = true;
        }

        if (isEmpty) {
            Alert emptyAlert = new Alert(Alert.AlertType.WARNING);
            emptyAlert.setTitle("Warning");
            emptyAlert.setHeaderText("Missing Data");
            emptyAlert.setContentText("Please fill out every field before submitting.");
            emptyAlert.showAndWait();
            return;
        }

        //Check that the address is formatted correctly
        if (!DBCustomers.addressFormatChecker(address_field.getText(), countryID)) {
            Alert wrongAddress = new Alert(Alert.AlertType.WARNING);
            wrongAddress.setTitle("Warning");
            wrongAddress.setHeaderText("Invalid Address");
            wrongAddress.setContentText("Please ensure address follows format: \n (123 Main Street, City) - U.K (123 Main Street, District, City)");
            wrongAddress.showAndWait();
            return;
        }

        //Harvest all information for the selected customer
        DBCustomers.selectedCustomer.setCustomerID(DBCustomers.selectedCustomer.getCustomerID());
        DBCustomers.selectedCustomer.setName((firstName_field.getText()) + " " + (lastName_field.getText()));
        DBCustomers.selectedCustomer.setAddress(address_field.getText());
        DBCustomers.selectedCustomer.setPostalCode(postal_field.getText());
        DBCustomers.selectedCustomer.setPhone(phone_field.getText());
        DBCustomers.selectedCustomer.setDivisionID(divisionID);
        DBCustomers.selectedCustomer.setLastUpdate(TimeHelper.getUTC());
        DBCustomers.selectedCustomer.setUpdatedBy(DBUsers.getActiveUser().getName());

        //update the customer in the database
        DBCustomers.modifyCustomer(DBCustomers.selectedCustomer.getCustomerID());

        //refresh the customers list from the database
        DBCustomers.getAllDBCustomers();

        //refresh the customer list used for the tableview
        CustomersTableview.generateAllCustomers();

        //Open the all appointments window
        Stage dashboard = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("EN_Main_View.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        dashboard.setTitle("Appointment Manager");
        dashboard.setScene(scene);
        dashboard.show();

        //Close the current window
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * - Closes the current window and opens the main window.
     */
    public void cancel_button_pressed(ActionEvent actionEvent) throws IOException {
        //Open the all appointments window
        Stage dashboard = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("EN_Main_View.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        dashboard.setTitle("Appointment Manager");
        dashboard.setScene(scene);
        dashboard.show();

        //Close the current window
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * - Takes the selected division name and converts it to a corresponding division ID to update the customer.
     */
    public void division_combo_pressed() {
        divisionID = Integer.parseInt(division_combo.getSelectionModel().getSelectedItem().toString());
    }

    /**
     * - Fills in the division combo with all divisions belonging to the selected country.
     */
    public void country_combo_pressed() {
        String selectedCountry = country_combo.getSelectionModel().getSelectedItem().toString();
        countryID = DBCountries.getIDFromName(selectedCountry);
        division_combo.setPromptText("--");
        division_combo.setItems(DBFirstLevelDiv.getDivisionNameFromCountry(countryID));
    }
}
