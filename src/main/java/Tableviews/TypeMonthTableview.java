package Tableviews;

import DB_Model.DBAppointments;
import Helper.TimeHelper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Objects;

public class TypeMonthTableview {
    ObservableValue<String> type;
    ObservableValue<String> month;
    ObservableValue<Integer> total;

    public static ObservableList<TypeMonthTableview> typeMonthReport = FXCollections.observableArrayList();

    public TypeMonthTableview(ObservableValue<String> type, ObservableValue<String> month, ObservableValue<Integer> total) {
        this.type = type;
        this.month = month;
        this.total = total;
    }

    public ObservableValue<String> getType() {
        return type;
    }

    public ObservableValue<String> getMonth() {
        return month;
    }

    public ObservableValue<Integer> getTotal() {
        return total;
    }

    /**
     * - Generates all TypeMonthTableview items for display in the relevant tableview.
     */
    public static void generateReport() {
        typeMonthReport.clear();
        ObservableList<String> allTypes = FXCollections.observableArrayList();

        for (DBAppointments A : DBAppointments.getAllAppointments()) {
            boolean typeFound = false;
            for (String type : allTypes) {
                if (Objects.equals(type, A.getType())) {
                    typeFound = true;
                    break;
                }
            }
            if (!typeFound) {
                allTypes.add(A.getType());
            }
        }

        for (String month : TimeHelper.getMonths()) {
            for (String type : allTypes) {
                int count = 0;
                for (DBAppointments A : DBAppointments.getAllAppointments()) {
                    if ((Objects.equals(A.getType(), type)) && (Objects.equals(TimeHelper.breakDownTime(A.getStart(), "month"), month))) {
                        ++count;
                    }
                }
                if (count > 0) {
                    TypeMonthTableview typeMonthTableview = new TypeMonthTableview(new SimpleStringProperty(type), new SimpleStringProperty(month), new SimpleIntegerProperty(count).asObject());
                    typeMonthReport.add(typeMonthTableview);
                }
            }
        }
    }
}
