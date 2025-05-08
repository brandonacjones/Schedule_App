package Tableviews;

import DB_Model.DBCustomers;
import DB_Model.DBFirstLevelDiv;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Objects;

public class DivisionTableview {
    ObservableValue<String> division;
    ObservableValue<Integer> total;

    public static ObservableList<DivisionTableview> divisionReport = FXCollections.observableArrayList();

    public DivisionTableview(ObservableValue<String> division, ObservableValue<Integer> total) {
        this.division = division;
        this.total = total;
    }

    public ObservableValue<String> getDivision() {
        return division;
    }

    public ObservableValue<Integer> getTotal() {
        return total;
    }

    /**
     * - Generates all DivisionTableview items for display in the relevant tableview.
     */
    public static void generateReport() {
        divisionReport.clear();
        ObservableList<String> allDivisions = FXCollections.observableArrayList();
        //Get all Divisions without repeats
        for (DBCustomers C : DBCustomers.getAllDBCustomers()) {
            boolean divisionFound = false;
            for (String D : allDivisions) {
                if (Objects.equals(DBFirstLevelDiv.getNameFromID(C.getDivisionID()), D)) {
                    divisionFound = true;
                    break;
                }
            }
            if (!divisionFound) {
                allDivisions.add(DBFirstLevelDiv.getNameFromID(C.getDivisionID()));
            }
        }

        //get totals
        for (String D : allDivisions) {
            int count = 0;
            for (DBCustomers C : DBCustomers.getAllDBCustomers()) {
                if (Objects.equals(DBFirstLevelDiv.getNameFromID(C.getDivisionID()), D)) {
                    ++count;
                }
            }
            if (count > 0) {
                DivisionTableview divisionTableview = new DivisionTableview(new SimpleStringProperty(D), new SimpleIntegerProperty(count).asObject());
                divisionReport.add(divisionTableview);
            }
        }
    }
}
