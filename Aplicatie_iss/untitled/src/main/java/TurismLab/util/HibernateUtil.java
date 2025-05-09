package TurismLab.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import TurismLab.domain.Book;
import TurismLab.domain.Borrow;
import TurismLab.domain.User;

import java.util.Properties;

public class HibernateUtil {
    private static final Logger logger = LogManager.getLogger();
    private static SessionFactory sessionFactory;
    private Properties jdbcProps;

    public HibernateUtil(Properties props) {
        this.jdbcProps = props;
        System.out.println(jdbcProps.getProperty("turism.jdbc.url"));
        System.out.println(jdbcProps.getProperty("jdbc.user"));
        System.out.println(jdbcProps.getProperty("jdbc.pass"));
    }

    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                // Hibernate settings
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "org.postgresql.Driver");
                settings.put(Environment.URL, jdbcProps.getProperty("turism.jdbc.url"));
                settings.put(Environment.USER, jdbcProps.getProperty("jdbc.user"));
                settings.put(Environment.PASS, jdbcProps.getProperty("jdbc.pass"));
                settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
                settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.FORMAT_SQL, "true");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                settings.put(Environment.HBM2DDL_AUTO, "update");

                configuration.setProperties(settings);

                // Add entity classes
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Book.class);
                configuration.addAnnotatedClass(Borrow.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);

                logger.info("Hibernate SessionFactory created successfully");
            } catch (Exception e) {
                logger.error("SessionFactory creation failed", e);
                throw new ExceptionInInitializerError(e);
            }
        }
        return sessionFactory;
    }

    public void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}