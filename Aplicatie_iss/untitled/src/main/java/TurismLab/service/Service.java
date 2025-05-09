package TurismLab.service;

import TurismLab.domain.Book;
import TurismLab.domain.Borrow;
import TurismLab.domain.User;
import TurismLab.repo.hibernate.RepoHibernateBook;
import TurismLab.repo.hibernate.RepoHibernateBorrow;
import TurismLab.repo.hibernate.RepoHibernateUser;

import java.util.List;

public class Service {
    private RepoHibernateBook repoBook;
    private RepoHibernateBorrow repoBorrow;
    private RepoHibernateUser repoUser;

    public Service(RepoHibernateBook repoBook, RepoHibernateBorrow repoBorrow, RepoHibernateUser repoUser) {
        this.repoBook = repoBook;
        this.repoBorrow = repoBorrow;
        this.repoUser = repoUser;
    }

    public Book addBook(String nume, String autor, String gen, int cantitate) throws Exception {
        Book book = new Book(nume, autor, gen, cantitate);
        Book savedBook = repoBook.save(book);
        if (savedBook != null) {
            return savedBook;
        } else {
            throw new Exception("Failed to save book");
        }
    }

    public Borrow borrowBook(User user, Book book) throws Exception {
        if (user == null || book == null) {
            throw new Exception("User or Book not found");
        }
        if(book.getCantitate() <= 0) {
            throw new Exception("No copies available");
        }
        book.setCantitate(book.getCantitate() - 1);
        repoBook.update(book);
        var borrow = new Borrow(user, book);
        return repoBorrow.save(borrow);
    }

    public User findUserById(Long id) {
        return repoUser.findById(id);
    }

    public User findUserByUsernameAndId(String nume, Long id) {
        return repoUser.findByUsernameAndId(nume, id);
    }

    public void returnBook(Borrow borrow) throws Exception {
        if (borrow == null) {
            throw new Exception("Borrow not found");
        }
        Book book = borrow.getBook();
        book.setCantitate(book.getCantitate() + 1);
        repoBook.update(book);
        repoBorrow.delete(borrow.getId());
    }

    public List<Borrow> getAllBorrowsForUser(Long userId) {
        return repoBorrow.findAllForUser(userId);
    }

    public List<Book> getAllBooks() {
        return repoBook.findAll();
    }

    public List<Book> searchByFilter(String titlu, String autor, String gen) {
        return repoBook.searchByFilter(titlu, autor, gen);
    }
}