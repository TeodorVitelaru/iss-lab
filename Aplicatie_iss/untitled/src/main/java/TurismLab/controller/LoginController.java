package TurismLab.controller;

import TurismLab.Main;
import TurismLab.domain.User;
import TurismLab.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

    public LoginController() {
        // Constructor fără parametri necesar pentru JavaFX
    }

    public void setService(Service service) {
        this.service = service;
    }

    @FXML
    public void handleLogin(ActionEvent actionEvent) {
        String nume = numeTextField.getText();
        String codUnic = codUnicTextField.getText();

        if (nume.isEmpty() || codUnic.isEmpty()) {
            showAlert("Error", "Please fill in all fields", Alert.AlertType.ERROR);
            return;
        }

        try {
            // For this example, we'll assume codUnic is the user ID
            Long userId = Long.parseLong(codUnic);
            User user = service.findUserByUsernameAndId(nume, userId);

            if (user == null) {
                showAlert("Login Failed", "Invalid username or ID", Alert.AlertType.ERROR);
                return;
            }

            // Close the login window
            ((javafx.stage.Stage) loginButton.getScene().getWindow()).close();

            // Determine user type and open appropriate view
            // For example, if user ID is 1, it's an admin, otherwise it's a regular user
            if (userId == 1) {
                Main.openAdminView();
            } else {
                Main.openUserView(user);
            }

        } catch (NumberFormatException e) {
            showAlert("Error", "User ID must be a number", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
            System.err.println("Error during login: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}