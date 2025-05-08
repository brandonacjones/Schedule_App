package com.project.schedule_app;

import DB_Model.DBUsers;
import Helper.Logger;
import Helper.TimeHelper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

/**
 * @author - Brandon Jones
 */
public class Login_Controller {

    public TextField username_field;
    public Text zoneID_text;
    public PasswordField password_field;
    public Button login_button;

    final String LANG = Locale.getDefault().getLanguage();

    /**
     * Display the user's zoneID when the window opens
     */
    public void initialize() {
        zoneID_text.setText(TimeZone.getDefault().getID().toString());
    }

    /**
     * - Compares user input against database users, if a match is found open the main window, otherwise display an error.
     */
    public void login_button_pressed(ActionEvent actionEvent) throws IOException {
        boolean userFound = false;
        ObservableList<DBUsers> userList = DBUsers.getAllUsers();
        for(int i = 0; i < DBUsers.getAllUsers().size(); ++i) {
            if (Objects.equals(username_field.getText(), userList.get(i).getName())) {
                if (Objects.equals(password_field.getText(), userList.get(i).getPassword())) {
                    //open dashboard, save userID and userName
                    System.out.println("Login Successful");
                    DBUsers.getActiveUser().setUserID(userList.get(i).getUserID());
                    DBUsers.getActiveUser().setName(userList.get(i).getName());
                    userFound = true;
                    Logger.writeToLog(TimeHelper.getUTC() + " | SUCCESS");

                    Stage dashboard = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("EN_Main_View.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    dashboard.setTitle("Appointment Manager");
                    dashboard.setScene(scene);
                    dashboard.show();

                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    stage.close();
                }
            }
        }
        if (!userFound && Objects.equals(LANG, "fr")) {
            System.out.println("La connexion a échoué");
            Logger.writeToLog(TimeHelper.getUTC() + " | FAIL");
            Alert invalidCredentials = new Alert(Alert.AlertType.WARNING);
            invalidCredentials.setTitle("LES INFORMATIONS D'IDENTIFICATION INVALIDES");
            invalidCredentials.setHeaderText("Nom d'utilisateur ou mot de passe incorrect");
            invalidCredentials.setContentText("Le pseudo ou mot de passe est incorect.");
            invalidCredentials.showAndWait();
        }
        else if (!userFound) {
            System.out.println("Login Failed");
            Logger.writeToLog(TimeHelper.getUTC() + " | FAIL");
            Alert invalidCredentials = new Alert(Alert.AlertType.WARNING);
            invalidCredentials.setTitle("Invalid Credentials");
            invalidCredentials.setHeaderText("Wrong Username or Password");
            invalidCredentials.setContentText("The username or password is incorrect.");
            invalidCredentials.showAndWait();
        }
    }
}
