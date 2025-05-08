package Tableviews;

import DB_Model.DBCountries;
import DB_Model.DBCustomers;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CustomersTableview {
    ObservableValue<Integer> customerID;
    ObservableValue<String> name;
    ObservableValue<String> address;
    ObservableValue<String> postalCode;
    ObservableValue<String> phone;
    ObservableValue<Integer> divisionID;
    ObservableValue<String> country;

    public static ObservableList<CustomersTableview> allCustomers = FXCollections.observableArrayList();

    public CustomersTableview(ObservableValue<Integer> customerID, ObservableValue<String> name, ObservableValue<String> address, ObservableValue<String> postalCode, ObservableValue<String> phone, ObservableValue<Integer> divisionID, ObservableValue<String> country) {
        this.customerID = customerID;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionID = divisionID;
        this.country = country;
    }

    //*****Getters*****

    public ObservableValue<Integer> getCustomerID() {
        return customerID;
    }

    public ObservableValue<String> getName() {
        return name;
    }

    public ObservableValue<String> getAddress() {
        return address;
    }

    public ObservableValue<String> getPostalCode() {
        return postalCode;
    }

    public ObservableValue<String> getPhone() {
        return phone;
    }

    public ObservableValue<Integer> getDivisionID() {
        return divisionID;
    }

    public ObservableValue<String> getCountry() {
        return country;
    }

    //*****Setters*****

    public void setCustomerID(ObservableValue<Integer> customerID) {
        this.customerID = customerID;
    }

    public void setName(ObservableValue<String> name) {
        this.name = name;
    }

    public void setAddress(ObservableValue<String> address) {
        this.address = address;
    }

    public void setPostalCode(ObservableValue<String> postalCode) {
        this.postalCode = postalCode;
    }

    public void setPhone(ObservableValue<String> phone) {
        this.phone = phone;
    }

    public void setDivisionID(ObservableValue<Integer> divisionID) {
        this.divisionID = divisionID;
    }

    public void setCountry(ObservableValue<String> country) {this.country = country;}

    //*****Helper Methods*****

    /**
     *  - Takes all DBCustomers Objects and converts them to Customers Objects.
     */
    public static void generateAllCustomers() {
        allCustomers.clear();

        for(DBCustomers C : DBCustomers.allDBCustomers) {
            ObservableValue<Integer> customerID = new SimpleIntegerProperty(C.getCustomerID()).asObject();
            ObservableValue<String> name = new SimpleStringProperty(C.getName());
            ObservableValue<String> address = new SimpleStringProperty(C.getAddress());
            ObservableValue<String> postalCode = new SimpleStringProperty(C.getPostalCode());
            ObservableValue<String> phone = new SimpleStringProperty(C.getPhone());
            ObservableValue<Integer> divisionID = new SimpleIntegerProperty(C.getDivisionID()).asObject();
            ObservableValue<String> country = new SimpleStringProperty(DBCountries.getNameFromDivision(C.getDivisionID()));

            allCustomers.add(new CustomersTableview(customerID, name, address, postalCode, phone, divisionID, country));
        }
    }

}
