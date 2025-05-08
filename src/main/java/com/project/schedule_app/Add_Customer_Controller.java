package com.project.schedule_app;

import DB_Model.DBCountries;
import DB_Model.DBCustomers;
import DB_Model.DBFirstLevelDiv;
import DB_Model.DBUsers;
import Helper.TimeHelper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

public class Add_Customer_Controller {
    public Text customerID_text;
    public TextField firstName_field;
    public TextField lastName_field;
    public TextField address_field;
    public TextField postal_field;
    public TextField phone_field;
    public Button addCustomer_button;
    public Button cancel_button;
    public ComboBox<String> division_combo;
    public ComboBox<String> country_combo;

    int countryID;

    /**
     *  - Initializes all Elements on the page when the window is first opened.
     */
    public void initialize() {
        //Generate CustomerID
        int customerID = DBCustomers.generateCustomerID();
        //Display the ID
        customerID_text.setText("Customer ID: " + customerID);
        //Display a placeholder List for First Level Division until a country is picked
        division_combo.setItems(FXCollections.observableArrayList("--"));
        //Display all country options
        country_combo.setItems(DBCountries.getAllNames());
    }

    /**
     *
     * - Validates all user inputs and uses them to add a customer to the database
     */
    public void addCustomer_button_pressed(ActionEvent actionEvent) throws IOException{

        //Ensure all fields are filled
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
        if (division_combo.getSelectionModel().getSelectedItem() == null) {
            isEmpty = true;
        }
        if (country_combo.getSelectionModel().getSelectedItem() == null) {
            isEmpty = true;
        }
        if (isEmpty) {
            Alert emptyAlert = new Alert(Alert.AlertType.WARNING);
            emptyAlert.setTitle("Cannot Add Customer");
            emptyAlert.setHeaderText("Missing Data");
            emptyAlert.setContentText("Please fill out every field before submitting.");
            Optional<ButtonType> emptyAlertResult = emptyAlert.showAndWait();
            if (emptyAlertResult.isPresent() && emptyAlertResult.equals(ButtonType.OK)) {
                return;
            }
        }

        //Check if address is correct format
        if (!DBCustomers.addressFormatChecker(address_field.getText(), countryID)) {
            Alert badAddress = new Alert(Alert.AlertType.WARNING);
            badAddress.setTitle("Warning");
            badAddress.setHeaderText("Invalid Address");
            badAddress.setContentText("Please ensure address follows format: \n (123 Main Street, City) - U.K (123 Main Street, District, City)");
            badAddress.showAndWait();
            return;
        }

        //Harvest all user inputs for customer creation
        int customerID = DBCustomers.generateCustomerID();
        String name = (firstName_field.getText() + " " + lastName_field.getText());
        String address = address_field.getText();
        String postalCode = postal_field.getText();
        String phone = phone_field.getText();
        String createDate = TimeHelper.getUTC();
        String createdBy = DBUsers.getActiveUser().getName();
        String lastUpdate = TimeHelper.getUTC();
        String updatedBy = DBUsers.activeUser.getName();
        int divisionID = DBFirstLevelDiv.getIDFromName(division_combo.getSelectionModel().getSelectedItem());

        //Create a new customer
        DBCustomers newCustomer = new DBCustomers(customerID, name, address, postalCode, phone, createDate, createdBy, lastUpdate, updatedBy, divisionID);

        //Add to database
        DBCustomers.addCustomer(newCustomer);

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
     * - Opens the all appointments window and closes the current window
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
     *  - Displays the list of first level divisions in the first level combo box for the country that is selected.
     */
    public void country_combo_pressed() {
        //Set the proper first level divisions for the selected country
        division_combo.setItems(DBFirstLevelDiv.getDivisionNameFromCountry(country_combo.getSelectionModel().getSelectedItem()));
        division_combo.setPromptText("--");
        countryID = DBCountries.getIDFromName(country_combo.getSelectionModel().getSelectedItem());
    }
}
