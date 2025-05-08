package com.project.schedule_app;

import DB_Model.*;
import Database.JDBC;
import Helper.Logger;
import Tableviews.AppointmentsTableview;
import Tableviews.CustomersTableview;
import Tableviews.TypeMonthTableview;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Locale;

public class Main extends Application {

    @Override
    public void start (Stage login) throws IOException {

        final String LANG = Locale.getDefault().getLanguage();
        if (LANG.equals("fr")) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("FR_Login_View.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            login.setTitle("Gestionnaire de rendez-vous");
            login.setScene(scene);
            login.show();
        }
        else {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("EN_Login_View.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            login.setTitle("Appointment Manager");
            login.setScene(scene);
            login.show();
        }
    }

    public static void main(String[] args) throws IOException {
        JDBC.openConnection();
        Logger.createLog();
        DBFirstLevelDiv.getAllDivisions();
        DBAppointments.getAllAppointments();
        DBCustomers.getAllDBCustomers();
        AppointmentsTableview.generateAllAppointments();
        CustomersTableview.generateAllCustomers();

        Locale currentRegion = Locale.getDefault();
        System.out.println("Launching in " + currentRegion.getDisplayLanguage());
        TypeMonthTableview.generateReport();

        launch(args);

        JDBC.closeConnection();
    }
}

//FIXME all lambdas should be specifically mentioned in method declaration.