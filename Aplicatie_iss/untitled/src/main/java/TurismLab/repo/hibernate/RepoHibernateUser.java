package TurismLab.repo.hibernate;

import TurismLab.domain.User;
import TurismLab.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class RepoHibernateUser {
    private final HibernateUtil hibernateUtil;

    public RepoHibernateUser(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    public User findById(Long id) {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User findByUsernameAndId(String nume, Long id) {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                    "FROM User WHERE nume = :nume AND id = :id", User.class);
            query.setParameter("nume", nume);
            query.setParameter("id", id);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User save(User user) {
        Transaction transaction = null;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            return user;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }

    public void update(User user) {
        Transaction transaction = null;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}