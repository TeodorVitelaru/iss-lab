package TurismLab.controller;

import TurismLab.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController {
    private Service service;

    @FXML
    private Button loginButton;
    @FXML
    private TextField numeTextField;
    @FXML
    private TextField codUnicTextField;

    public LoginController(Service service) {
        this.service = service;
    }

    public void handleLogin(ActionEvent actionEvent) {
        String nume = numeTextField.getText();
        String codUnic = codUnicTextField.getText();

        if (nume.isEmpty() || codUnic.isEmpty()) {
            System.out.println("Please fill in all fields.");
            return;
        }

        // Perform login logic here
        // For example, check if the user exists in the database

        System.out.println("Login successful for user: " + nume);
        //close the login window
        ((javafx.stage.Stage) loginButton.getScene().getWindow()).close();
    }
}
