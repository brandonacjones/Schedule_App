package com.project.schedule_app;

import DB_Model.*;
import Helper.TimeHelper;
import Tableviews.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Brandon Jones
 *
 */
public class Main_Controller {

    public RadioButton aAllRadio;
    public RadioButton aWeekRadio;
    public RadioButton aMonthRadio;

    public Button aNextButton;
    public Button aPrevButton;
    public Button aLogOutButton;
    public Button aRefresh;

    public Button rLogOutButton;
    public Button rRefresh;

    public Button cLogOutButton;
    public Button cRefresh;

    public MenuItem aAddAppointment;
    public MenuItem aModifyAppointment;
    public MenuItem aDeleteAppointment;

    public MenuItem cAddCustomer;
    public MenuItem cUpdateCustomer;
    public MenuItem cDeleteCustomer;

    public Text aRangeText;

    public Text rUpcomingTitle;
    public Text rUpcomingID;
    public Text rUpcomingDate;
    public Text rUpcomingTime;
    public Text rTotalByCustomer;
    public Text rByMonthText;
    public Text rByDivisionText;

    public ComboBox rScheduleCombo;


    public TableView<AppointmentsTableview> aTableView = new TableView<AppointmentsTableview>();
    public TableColumn<AppointmentsTableview, Integer> appointment_id_column = new TableColumn<AppointmentsTableview, Integer>();
    public TableColumn<AppointmentsTableview, String> title_column = new TableColumn<AppointmentsTableview, String>();
    public TableColumn<AppointmentsTableview, String> description_column = new TableColumn<AppointmentsTableview, String>();
    public TableColumn<AppointmentsTableview, String> location_column = new TableColumn<AppointmentsTableview, String>();
    public TableColumn<AppointmentsTableview, Integer> contact_column = new TableColumn<AppointmentsTableview, Integer>();
    public TableColumn<AppointmentsTableview, String> type_column = new TableColumn<AppointmentsTableview, String>();
    public TableColumn<AppointmentsTableview, String> start_column = new TableColumn<AppointmentsTableview, String>();
    public TableColumn<AppointmentsTableview, String> end_column = new TableColumn<AppointmentsTableview, String>();
    public TableColumn<AppointmentsTableview, Integer> customer_id_column = new TableColumn<AppointmentsTableview, Integer>();
    public TableColumn<AppointmentsTableview, Integer> user_id_column = new TableColumn<AppointmentsTableview, Integer>();

    public TableView<CustomersTableview> cTableview = new TableView<CustomersTableview>();
    public TableColumn<CustomersTableview, Integer> cCustomerID = new TableColumn<CustomersTableview, Integer>();
    public TableColumn<CustomersTableview, String> cName = new TableColumn<CustomersTableview, String>();
    public TableColumn<CustomersTableview, String> cAddress = new TableColumn<CustomersTableview, String>();
    public TableColumn<CustomersTableview, String> cPostalCode = new TableColumn<CustomersTableview, String>();
    public TableColumn<CustomersTableview, String> cPhone = new TableColumn<CustomersTableview, String>();
    public TableColumn<CustomersTableview, Integer> cDivisionID = new TableColumn<CustomersTableview, Integer>();
    public TableColumn<CustomersTableview, String> cCountry = new TableColumn<CustomersTableview, String>();

    public TableView<AppointmentsTableview> rTableView = new TableView<AppointmentsTableview>();
    public TableColumn<AppointmentsTableview, Integer> rAppointmentID = new TableColumn<AppointmentsTableview, Integer>();
    public TableColumn<AppointmentsTableview, String> rTitle = new TableColumn<AppointmentsTableview, String>();
    public TableColumn<AppointmentsTableview, String> rType = new TableColumn<AppointmentsTableview, String>();
    public TableColumn<AppointmentsTableview, String> rDescription = new TableColumn<AppointmentsTableview, String>();
    public TableColumn<AppointmentsTableview, String> rStart = new TableColumn<AppointmentsTableview, String>();
    public TableColumn<AppointmentsTableview, String> rEnd = new TableColumn<AppointmentsTableview, String>();
    public TableColumn<AppointmentsTableview, Integer> rCustomerID = new TableColumn<AppointmentsTableview, Integer>();

    public TableView<TypeMonthTableview> rTMTableview = new TableView<TypeMonthTableview>();
    public TableColumn<TypeMonthTableview, String> rTMMonth = new TableColumn<TypeMonthTableview, String>();
    public TableColumn<TypeMonthTableview, String> rTMType = new TableColumn<TypeMonthTableview, String>();
    public TableColumn<TypeMonthTableview, Integer> rTMTotal = new TableColumn<TypeMonthTableview, Integer>();

    public TableView<DivisionTableview> rDTableview = new TableView<DivisionTableview>();
    public TableColumn<DivisionTableview, String> rDDivision = new TableColumn<DivisionTableview, String>();
    public TableColumn<DivisionTableview, Integer> rDTotal = new TableColumn<DivisionTableview, Integer>();

    public TableView<CustomerReportTableview> rCTableview = new TableView<CustomerReportTableview>();
    public TableColumn<CustomerReportTableview, String> rCCustomer = new TableColumn<CustomerReportTableview, String>();
    public TableColumn<CustomerReportTableview, Integer> rCTotal = new TableColumn<CustomerReportTableview, Integer>();

    public int selector = 1;
    public int pageNum = 1;

    public TabPane tabPane;

    public Rectangle rUpcomingRect;

    Alert_Interface warningAlert = (t, h, c) -> {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(t);
        alert.setHeaderText(h);
        alert.setContentText(c);
        alert.showAndWait();
    };

    Locale currentRegion = Locale.getDefault();

    public String LANGUAGE = "en";

    /**
     * - Runs when the window opens, initializes all data for the program, including tableviews, buttons, and reports.
     * - Uses lambda warningAlert to create alerts more efficiently.
     * - Uses lambda data -> data.getValue.getAppointmentID() to give attributes to the cell value factory.
     */
    public void initialize() {
        //Refresh customers and appointments
        DBAppointments.getAllAppointments();
        AppointmentsTableview.generateAllAppointments();;
        DBCustomers.getAllDBCustomers();
        CustomersTableview.generateAllCustomers();

        //Set the display language
        if (Objects.equals(currentRegion.getLanguage(), "fr")) {
            LANGUAGE = "fr";
        }

        //Fill in Appointments tableview
        aTableView.setItems(AppointmentsTableview.allAppointments);
        appointment_id_column.setCellValueFactory(data -> data.getValue().getAppointmentID());
        title_column.setCellValueFactory(data -> data.getValue().getTitle());
        description_column.setCellValueFactory(data -> data.getValue().getDescription());
        location_column.setCellValueFactory(data -> data.getValue().getLocation());
        contact_column.setCellValueFactory(data -> data.getValue().getContactID());
        type_column.setCellValueFactory(data -> data.getValue().getType());
        start_column.setCellValueFactory(data -> data.getValue().getStart());
        end_column.setCellValueFactory(data -> data.getValue().getEnd());
        customer_id_column.setCellValueFactory(data -> data.getValue().getCustomerID());
        user_id_column.setCellValueFactory(data -> data.getValue().getUserID());

        //Set up Appointments Navigation
        aNextButton.setDisable(true);
        aPrevButton.setDisable(true);
        aAllRadio.setSelected(true);
        aMonthRadio.setSelected(false);
        aWeekRadio.setSelected(false);
        aRangeText.setText("Showing All Appointments");

        //Fill in Customers tableview
        cTableview.setItems(CustomersTableview.allCustomers);
        cCustomerID.setCellValueFactory(data -> data.getValue().getCustomerID());
        cName.setCellValueFactory(data -> data.getValue().getName());
        cAddress.setCellValueFactory(data -> data.getValue().getAddress());
        cPostalCode.setCellValueFactory(data -> data.getValue().getPostalCode());
        cPhone.setCellValueFactory(data -> data.getValue().getPhone());
        cDivisionID.setCellValueFactory(data -> data.getValue().getDivisionID());
        cCountry.setCellValueFactory(data -> data.getValue().getCountry());

        //Check if there is an upcoming appointment
        if (setHasUpcomingAppointment()) {
            warningAlert.alert("Notice", "Upcoming Appointment", "You have an appointment starting in the next 15 minutes.\nSee the reports tab for more information.");
            rUpcomingRect.setStyle("-fx-fill: #fdffe8");
        }
        else {
            warningAlert.alert("Notice", "No Upcoming Appointments", "You have no appointments within the next 15 minutes.");
        }

        //Fill in Schedule tableview combo
        rScheduleCombo.setItems(DBContacts.getAllNames());

        //Fill in TypeMonth tableview
        rTMTableview.setItems(TypeMonthTableview.typeMonthReport);
        rTMType.setCellValueFactory(data -> data.getValue().getType());
        rTMMonth.setCellValueFactory(data -> data.getValue().getMonth());
        rTMTotal.setCellValueFactory(data -> data.getValue().getTotal());

        //Fill in Division tableview
        DivisionTableview.generateReport();
        rDTableview.setItems(DivisionTableview.divisionReport);
        rDDivision.setCellValueFactory(data -> data.getValue().getDivision());
        rDTotal.setCellValueFactory(data -> data.getValue().getTotal());

        //Fill in Appointments by Customer tableview
        CustomerReportTableview.generateReport();
        rCTableview.setItems(CustomerReportTableview.customerReport);
        rCCustomer.setCellValueFactory(data -> data.getValue().getCustomer());
        rCTotal.setCellValueFactory(data -> data.getValue().getTotal());
    }

    /**
     * - Increments the pageNum value to change the date range being filtered into the future.
     */
    public void aNextButtonPressed() {
        //increment pageNum
        ++pageNum;

        //refresh tableview
        aTableView.setItems(filterAppointments(selector, pageNum));
    }

    /**
     * - Decrements the pageNum value to change the date range being filtered into the past.
     */
    public void aPrevButtonPressed() {
        //decrement pageNum
        --pageNum;

        //refresh tableview
        aTableView.setItems(filterAppointments(selector, pageNum));
    }

    /**
     * - Closes the current window and takes the user back to the login form.
     */
    public void aLogOutButtonPressed(ActionEvent actionEvent) throws IOException {
        Stage dashboard = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("EN_Login_View.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        dashboard.setTitle("Appointment Manager");
        dashboard.setScene(scene);
        dashboard.show();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * - Turns off date filtering on the appointments tab, displays all appointments.
     */
    public void aAllRadioPressed() {
        //disable other buttons
        aNextButton.setDisable(true);
        aPrevButton.setDisable(true);
        aAllRadio.setSelected(true);
        aMonthRadio.setSelected(false);
        aWeekRadio.setSelected(false);
        aRangeText.setText("Showing All Appointments");

        //show all appointments in tableview
        aTableView.setItems(AppointmentsTableview.allAppointments);
    }

    /**
     * - Turns on filtering by month on the appointments tab.
     */
    public void aMonthRadioPressed() {
        //set selector and pageNum
        selector = 1;
        pageNum = 1;

        //disable/enable other buttons
        aNextButton.setDisable(false);
        aPrevButton.setDisable(false);
        aAllRadio.setSelected(false);
        aMonthRadio.setSelected(true);
        aWeekRadio.setSelected(false);

        //show all appointments in tableview
        aTableView.setItems(filterAppointments(selector, pageNum));
    }

    /**
     * - Turns on filtering by week on the appointments tab.
     */
    public void aWeekRadioPressed() {
        //set selector and pageNum
        selector = 2;
        pageNum = 2;

        //disable/enable other buttons
        aNextButton.setDisable(false);
        aPrevButton.setDisable(false);
        aAllRadio.setSelected(false);
        aMonthRadio.setSelected(false);
        aWeekRadio.setSelected(true);

        //show all appointments in tableview
        aTableView.setItems(filterAppointments(selector, pageNum));
    }

    /**
     *
     * @param selector - The type of filtering to be performed (1 = MONTH, 2 = WEEK)
     * @param pageNum - The date range being displayed, pageNum = 1 displays the week or month the current date falls in.
     * @return - An observableList of all appointments falling within the chosen date range.
     */
    public ObservableList<AppointmentsTableview> filterAppointments(int selector, int pageNum) {
        ObservableList<AppointmentsTableview> filteredAppointments = FXCollections.observableArrayList();

        String start = "Not Found";
        String end = "Not Found";

        //If months is selected
        if (selector == 1) {
            for(AppointmentsTableview A : AppointmentsTableview.allAppointments) {

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                //Return a date on the first of the current month
                LocalDateTime startRange = LocalDateTime.parse(TimeHelper.getUTC(), dtf);
                startRange = startRange.minusDays(startRange.getDayOfMonth() - 1);

                //Return a date on the first of the upcoming month
                LocalDateTime endRange = startRange.plusMonths(1);

                //Increment start and end range based on current pageNum
                startRange = startRange.plusMonths(pageNum - 1);
                endRange = endRange.plusMonths(pageNum - 1);

                LocalDateTime appointmentStart = LocalDateTime.parse(A.getStart().getValue(), dtf);

                if ((appointmentStart.isAfter(startRange)) && (appointmentStart.isBefore(endRange))) {
                    filteredAppointments.add(A);
                }

                start = (startRange.getMonth() + " " + startRange.getDayOfMonth() + ", " + startRange.getYear());
                end = (endRange.getMonth() + " " + endRange.getDayOfMonth() + ", " + endRange.getYear());

            }
        }
        //if Weeks is selected
        else {
            for(AppointmentsTableview A : AppointmentsTableview.allAppointments) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                LocalDateTime startRange = LocalDateTime.parse(TimeHelper.getUTC(), dtf);

                while (startRange.getDayOfWeek() != DayOfWeek.SUNDAY) {
                    startRange = startRange.plusDays(-1);
                }

                LocalDateTime endRange = startRange.plusDays(7);

                //Make start and end Range at midnight
                startRange = startRange.plusHours(-startRange.getHour());
                startRange = startRange.plusMinutes(-startRange.getMinute());
                endRange = endRange.plusHours(-endRange.getHour());
                endRange = endRange.plusMinutes(-endRange.getMinute());

                //increment the start and end range based on the pageNum
                startRange = startRange.plusDays((pageNum - 1) * 7);
                endRange = endRange.plusDays((pageNum - 1) * 7);

                LocalDateTime appointmentStart = LocalDateTime.parse(A.getStart().getValue(), dtf);

                if ((appointmentStart.isAfter(startRange)) && (appointmentStart.isBefore(endRange))) {
                    filteredAppointments.add(A);
                }

                start = (startRange.getMonth() + " " + startRange.getDayOfMonth() + ", " + startRange.getYear());
                end = (endRange.getMonth() + " " + endRange.getDayOfMonth() + ", " + endRange.getYear());
            }
        }
        aRangeText.setText("Showing Appointments Between " + start + " and " + end);

        return filteredAppointments;
    }

    /**
     * - Opens the add appointment form.
     */
    public void aAddAppointmentPressed(ActionEvent actionEvent) throws IOException{
        //Open New Window
        Stage dashboard = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Create_Appointment_View.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        dashboard.setTitle("Appointment Manager - Create Appointment");
        dashboard.setScene(scene);
        dashboard.show();

        //Close Current Window
        Stage stage = (Stage) aAddAppointment.getParentPopup().getOwnerWindow();
        stage.close();
    }

    /**
     * - Opens the selected appointment in modify appointment form.
     * - Uses lambda warningAlert to create alerts more efficiently.
     */
    public void aModifyAppointmentPressed(ActionEvent actionEvent) throws IOException{
        //Check if an appointment is selected
        if (aTableView.getSelectionModel().getSelectedItem() == null) {
            warningAlert.alert("Warning", "No Appointment Selected", "Please select an appointment to modify.");
            return;
        }

        //Get the appointmentID of the selected appointment
        int selectedAppointmentID = aTableView.getSelectionModel().getSelectedItem().getAppointmentID().getValue();

        //set DBAppointments.selectedAppointment as the DBAppointment object that matches selectedAppointmentID
        DBAppointments.setSelectedAppointment(DBAppointments.getAppointment(selectedAppointmentID));

        //Open new Window
        Stage dashboard = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Modify_Appointment_View.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        dashboard.setTitle("Appointment Manager - Modify Appointment");
        dashboard.setScene(scene);
        dashboard.show();

        //Close current window
        Stage stage = (Stage) aModifyAppointment.getParentPopup().getOwnerWindow();
        stage.close();
    }

    /**
     * - Deletes the selected appointment.
     */
    public void aDeleteAppointmentPressed() {
        //Check if there is a selected appointment, if not, display a warning and exit the method.
        if (aTableView.getSelectionModel().getSelectedItem() == null) {
            warningAlert.alert("Warning", "No Appointment Selected", "Please select an appointment to delete.");
            return;
        }

        //Save the ID and Type of the selected appointment
        int selectedAppointmentID = aTableView.getSelectionModel().getSelectedItem().getAppointmentID().getValue();
        String selectedAppointmentType = aTableView.getSelectionModel().getSelectedItem().getType().getValue();

        //Confirm the deletion of the selected appointment, exit the method if user cancels
        Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDelete.setTitle("Delete Appointment");
        confirmDelete.setHeaderText("Are you sure you want to delete Appointment " + selectedAppointmentID + " - " + selectedAppointmentType + "?");
        confirmDelete.setContentText("This action cannot be reversed.");
        Optional<ButtonType> confirmDeleteResult = confirmDelete.showAndWait();
        if (confirmDeleteResult.isPresent() && confirmDeleteResult.get() == ButtonType.CANCEL) {
            return;
        }

        //Pass the selected appointment ID to the deleteAppointment method
        DBAppointments.deleteAppointment(selectedAppointmentID);

        //Refresh list of DBAppointments
        DBAppointments.getAllAppointments();

        //Refresh list of Appointments
        AppointmentsTableview.generateAllAppointments();

        //Refresh the tableview
        if (aAllRadio.isSelected()) {
            aTableView.setItems(AppointmentsTableview.allAppointments);
        }
        else {
            aTableView.setItems(filterAppointments(selector, pageNum));
        }

    }

    /**
     * - Opens the add customer form.
     */
    public void cAddCustomerPressed(ActionEvent actionEvent) throws IOException{
        //Open the all appointments window
        Stage dashboard = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Add_Customer_View.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        dashboard.setTitle("Appointment Manager - Add Customer");
        dashboard.setScene(scene);
        dashboard.show();

        //Close the current window
         Stage stage = (Stage) cAddCustomer.getParentPopup().getOwnerWindow();
         stage.close();
    }

    /**
     * - Opens the selected customer in modify customer form.
     * - Uses lambda warningAlert to create alerts more efficiently.
     */
    public void cUpdateCustomerPressed(ActionEvent actionEvent) throws IOException{
        //Check if a customer is selected
        if (cTableview.getSelectionModel().getSelectedItem() == null) {
            warningAlert.alert("Warning", "No Customer Selected", "Please select a customer to update.");
            return;
        }

        //Save the selected customer as the DBCustomers selected customer
        int customerID = cTableview.getSelectionModel().getSelectedItem().getCustomerID().getValue();

        for(DBCustomers C : DBCustomers.allDBCustomers) {
            if (C.getCustomerID() == customerID) {
                DBCustomers.selectedCustomer = C;
            }
        }

        //Open the all appointments window
        Stage dashboard = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Modify_Customer_View.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        dashboard.setTitle("Appointment Manager - Edit Customer");
        dashboard.setScene(scene);
        dashboard.show();

        //Close the current window
        Stage stage = (Stage) cUpdateCustomer.getParentPopup().getOwnerWindow();
        stage.close();
    }

    /**
     * - Deletes the selected customer.
     * - Uses lambda warningAlert to create alerts more efficiently.
     */
    public void cDeleteCustomerPressed(ActionEvent actionEvent) {
        //Make Sure a customer is selected
        if (cTableview.getSelectionModel().getSelectedItem() == null) {
            warningAlert.alert("Warning", "No Customer Selected", "Please select a customer to delete.");
            return;
        }

        //Get Customer Name and ID
        int selectedCustomerID = cTableview.getSelectionModel().getSelectedItem().getCustomerID().getValue();
        String selectedCustomerName = cTableview.getSelectionModel().getSelectedItem().getName().getValue();

        //Confirm the deletion
        Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDelete.setTitle("Delete Customer");
        confirmDelete.setHeaderText("Are you sure you want to delete Customer: " + selectedCustomerName + "?");
        confirmDelete.setContentText("This action cannot be reversed.");
        Optional<ButtonType> confirmDeleteResult = confirmDelete.showAndWait();
        if (confirmDeleteResult.isPresent() && confirmDeleteResult.get() == ButtonType.CANCEL) {
            System.out.println("Return Statement Reached ***Line 449");
            return;
        }
        System.out.println("Return Statement MISSED ***Line 452");
        //Remove the customer from the database
        DBCustomers.removeCustomer(selectedCustomerID);

        //Refresh the list of customers from the database
        DBCustomers.getAllDBCustomers();

        //Generate a new list of customers from DBCustomers
        CustomersTableview.generateAllCustomers();

        //Refresh Customer tableview
        cTableview.setItems(CustomersTableview.allCustomers);

        //Refresh Appointments Tableview
        aTableView.setItems(AppointmentsTableview.allAppointments);
    }

    /**
     * - Refreshes the customer table with a fresh connection to the database.
     */
    public void cRefreshPressed() {
        //Refresh appointments tab
        DBAppointments.getAllAppointments();
        AppointmentsTableview.generateAllAppointments();

        //Refresh customer tab
        DBCustomers.getAllDBCustomers();
        CustomersTableview.generateAllCustomers();
        cTableview.setItems(CustomersTableview.allCustomers);

        //Refresh reports tab
        CustomerReportTableview.generateReport();
        rCTableview.setItems(CustomerReportTableview.customerReport);
        TypeMonthTableview.generateReport();
        rTMTableview.setItems(TypeMonthTableview.typeMonthReport);
        DivisionTableview.generateReport();
        rDTableview.setItems(DivisionTableview.divisionReport);
    }

    /**
     *  - Checks if there is an upcoming appointment, then displays it in the upcoming appointments box
     *  - This was placed in its own method instead of initialize() so it can be called whenever the appointments change, as opposed to only when the window opens.
     */
    public boolean setHasUpcomingAppointment() {
        boolean hasUpcomingAppointment = false;

        //Check if there is an Appointment within 15 minutes
        for (DBAppointments A : DBAppointments.allDBAppointments) {

            //If the current appointment is within 15 minutes, and it belongs to the current user, set it as the upcoming appointment
            if ((TimeHelper.upcomingAppointment(A)) && (A.getUserID() == DBUsers.activeUser.getUserID())) {
                DBAppointments.upcomingAppointment = A;
                hasUpcomingAppointment = true;
            }
        }

        //if hasUpcomingAppointment == true, display details in upcoming appointments box
        if (hasUpcomingAppointment) {
            //Pull data from DBAppointments.upcomingAppointment
            rUpcomingID.setText("Appointment ID: " + DBAppointments.upcomingAppointment.getAppointmentID());

            //Extract the date and time from appointment start time
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime appointmentStart = LocalDateTime.parse(TimeHelper.getLocal(DBAppointments.upcomingAppointment.getStart()), dtf);
            String date = appointmentStart.getMonth() + " " + appointmentStart.getDayOfMonth() + ", " + appointmentStart.getYear();
            String time = appointmentStart.getHour() + ":" + appointmentStart.getMinute();

            rUpcomingDate.setText("Date: " + date);
            rUpcomingTime.setText("Time: " + time);
        }
        else {
            rUpcomingDate.setText("No Upcoming Appointment to Display.");
            rUpcomingTime.setText("");
            rUpcomingID.setText("");
        }
        return hasUpcomingAppointment;
    }

    /**
     * - Displays all appointments for the selected contact in the schedule table.
     */
    public void rScheduleComboPressed() {
        String contactName = rScheduleCombo.getSelectionModel().getSelectedItem().toString();

        //Fill in Schedule TableView
        rTableView.setItems(AppointmentsTableview.getAppointmentByContact(contactName));
        rAppointmentID.setCellValueFactory(data -> data.getValue().getAppointmentID());
        rTitle.setCellValueFactory(data -> data.getValue().getTitle());
        rDescription.setCellValueFactory(data -> data.getValue().getDescription());
        rType.setCellValueFactory(data -> data.getValue().getType());
        rStart.setCellValueFactory(data -> data.getValue().getStart());
        rEnd.setCellValueFactory(data -> data.getValue().getEnd());
        rCustomerID.setCellValueFactory(data -> data.getValue().getCustomerID());
    }
}
