package TurismLab;

import TurismLab.repo.hibernate.RepoHibernateBook;
import TurismLab.repo.hibernate.RepoHibernateBorrow;
import TurismLab.repo.hibernate.RepoHibernateUser;
import TurismLab.service.Service;
import TurismLab.util.HibernateUtil;

import java.io.FileReader;
import java.util.Properties;

public class Main {
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
            Service service = new Service(repoBook, repoBorrow, repoUser);

            // Initialize UI controllers/app
            // For example:
            // startJavaFXApp(service);

            System.out.println("Application started successfully!");

        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}