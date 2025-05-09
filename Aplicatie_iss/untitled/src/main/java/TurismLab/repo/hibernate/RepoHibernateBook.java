package TurismLab.repo.hibernate;

import TurismLab.domain.Book;
import TurismLab.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class RepoHibernateBook {
    private final HibernateUtil hibernateUtil;

    public RepoHibernateBook(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            Query<Book> query = session.createQuery("FROM Book", Book.class);
            books = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    public Book save(Book book) {
        Transaction transaction = null;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(book);
            transaction.commit();
            return book;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }

    public void update(Book book) {
        Transaction transaction = null;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(book);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Book findById(Long id) {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            return session.get(Book.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Book> searchByFilter(String titlu, String autor, String gen) {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            StringBuilder queryString = new StringBuilder("FROM Book WHERE 1=1");

            // Verificăm doar acele câmpuri care nu sunt null sau goale
            if (titlu != null && !titlu.isEmpty()) {
                queryString.append(" AND nume = :titlu");
            }
            if (autor != null && !autor.isEmpty()) {
                queryString.append(" AND autor = :autor");
            }
            if (gen != null && !gen.isEmpty()) {
                queryString.append(" AND gen = :gen");
            }

            Query<Book> query = session.createQuery(queryString.toString(), Book.class);

            // Setăm doar parametrii care sunt valabili
            if (titlu != null && !titlu.isEmpty()) {
                query.setParameter("titlu", titlu);
            }
            if (autor != null && !autor.isEmpty()) {
                query.setParameter("autor", autor);
            }
            if (gen != null && !gen.isEmpty()) {
                query.setParameter("gen", gen);
            }

            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}