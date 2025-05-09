package TurismLab;

import TurismLab.controller.AdminController;
import TurismLab.controller.LoginController;
import TurismLab.controller.UserController;
import TurismLab.domain.User;
import TurismLab.repo.hibernate.RepoHibernateBook;
import TurismLab.repo.hibernate.RepoHibernateBorrow;
import TurismLab.repo.hibernate.RepoHibernateUser;
import TurismLab.service.Service;
import TurismLab.util.HibernateUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileReader;
import java.util.Properties;

public class Main extends Application {
    private static Service service;

    public static void main(String[] args) {
        try {
            // Load properties
            Properties props = new Properties();
            props.load(new FileReader("bd.config"));

            // Initialize Hibernate
            HibernateUtil hibernateUtil = new HibernateUtil(props);

            // Initialize repositories
            RepoHibernateBook repoBook = new RepoHibernateBook(hibernateUtil);
            RepoHibernateUser repoUser = new RepoHibernateUser(hibernateUtil);
            RepoHibernateBorrow repoBorrow = new RepoHibernateBorrow(hibernateUtil);

            // Initialize service
            service = new Service(repoBook, repoBorrow, repoUser);



            // Launch JavaFX application
            launch(args);

        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load login view
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
        Parent loginRoot = loginLoader.load();

        // After loading, get the controller and set the service
        LoginController loginController = loginLoader.getController();
        loginController.setService(service);  // Asigură-te că ai un setter pentru service

        // Set login scene
        Scene loginScene = new Scene(loginRoot);
        primaryStage.setTitle("Library Management System - Login");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    // Helper method to open the admin view
    public static void openAdminView() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/AdminView.fxml"));

            // Load the FXML view and set the controller explicitly in case it's not injected automatically
            Parent root = loader.load();

            // Inject the service into the controller after loading the FXML
            AdminController adminController = loader.getController();
            adminController.setService(service); // Method that you define in the AdminController to set service

            Stage stage = new Stage();
            stage.setTitle("Library Management System - Admin");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.err.println("Error loading admin view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to open the user view
    public static void openUserView(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/UserView.fxml"));

            // Load the FXML view and set the controller explicitly
            Parent root = loader.load();

            // Inject the service and set the current user
            UserController userController = loader.getController();
            userController.setService(service);  // Method that you define in the UserController to set service
            userController.setCurrentUser(user);  // Set the user

            Stage stage = new Stage();
            stage.setTitle("Library Management System - User");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.err.println("Error loading user view: " + e.getMessage());
            e.printStackTrace();
        }
    }
}