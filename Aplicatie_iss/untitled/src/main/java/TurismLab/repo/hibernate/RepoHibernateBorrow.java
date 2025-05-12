package TurismLab.repo.hibernate;

import TurismLab.domain.Borrow;
import TurismLab.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class RepoHibernateBorrow {
    private final HibernateUtil hibernateUtil;

    public RepoHibernateBorrow(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    public Borrow save(Borrow borrow) {
        Transaction transaction = null;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(borrow);
            transaction.commit();
            return borrow;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }

    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Borrow borrow = session.get(Borrow.class, id);
            if (borrow != null) {
                session.delete(borrow);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Borrow> findAllForUser(Long userId) {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            Query<Borrow> query = session.createQuery(
                    "FROM Borrow b JOIN FETCH b.user JOIN FETCH b.book WHERE b.user.id = :userId",
                    Borrow.class);
            query.setParameter("userId", userId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Borrow findById(Long id) {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            return session.get(Borrow.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Borrow findByBook(Long bookId) {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            Query<Borrow> query = session.createQuery("FROM Borrow WHERE book.id = :bookId", Borrow.class);
            query.setParameter("bookId", bookId);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Borrow> findActiveBorrowsForBook(Long bookId) {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            Query<Borrow> query = session.createQuery("FROM Borrow WHERE book.id = :bookId", Borrow.class);
            query.setParameter("bookId", bookId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}