package Tableviews;

import DB_Model.DBAppointments;
import DB_Model.DBCustomers;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CustomerReportTableview {
    public ObservableValue<String> customer;
    public ObservableValue<Integer> total;
    public static ObservableList<CustomerReportTableview> customerReport = FXCollections.observableArrayList();

    public CustomerReportTableview(ObservableValue<String> customer, ObservableValue<Integer> total) {
        this.customer = customer;
        this.total = total;
    }

    public ObservableValue<String> getCustomer() {
        return customer;
    }

    public ObservableValue<Integer> getTotal() {
        return total;
    }

    /**
     * - Generates all customerReportTableview items for display in the relevant tableview.
     */
    public static void generateReport() {
        customerReport.clear();
        for (DBCustomers C : DBCustomers.getAllDBCustomers()) {
            int count = 0;
            for (DBAppointments A : DBAppointments.getAllAppointments()) {
                if (C.getCustomerID() == A.getCustomerID()) {
                    ++count;
                }
            }
            CustomerReportTableview customerReportTableview = new CustomerReportTableview(new SimpleStringProperty(C.getName()), new SimpleIntegerProperty(count).asObject());
            customerReport.add(customerReportTableview);
        }
    }
}
