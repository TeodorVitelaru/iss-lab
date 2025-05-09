package TurismLab.service;

import TurismLab.domain.Book;
import TurismLab.domain.Borrow;
import TurismLab.domain.User;
import TurismLab.repo.RepoDBBook;
import TurismLab.repo.RepoDBBorrow;
import TurismLab.repo.RepoDBUser;

import java.util.ArrayList;
import java.util.List;

public class Service {
    private RepoDBBook repoDBBook;
    private RepoDBBorrow repoDBBorrow;
    private RepoDBUser repoDBUser;

    public Service(RepoDBBook repoDBBook, RepoDBBorrow repoDBBorrow, RepoDBUser repoDBUser) {
        this.repoDBBook = repoDBBook;
        this.repoDBBorrow = repoDBBorrow;
        this.repoDBUser = repoDBUser;
    }

    public Book addBook(String nume, String autor, String gen, int cantitate) throws Exception {
        Book book = new Book(nume, autor, gen, cantitate);
        Book savedBook = repoDBBook.save(book);
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
        repoDBBook.update(book);
        var borrow = new Borrow(user, book);
        return repoDBBorrow.save(borrow);
    }

    public User findUserById(Long id) {
        return repoDBUser.findById(id);
    }

    public User findUserByUsernameAndId(String nume, Long id) {
        return repoDBUser.findByUsernameAndId(nume, id);
    }

    public void returnBook(Borrow borrow) throws Exception {
        if (borrow == null) {
            throw new Exception("Borrow not found");
        }
        Book book = borrow.getBook();
        book.setCantitate(book.getCantitate() + 1);
        repoDBBook.update(book);
        repoDBBorrow.delete(borrow.getId());
    }

    public List<Borrow> getAllBorrowsForUser(Long userId) {
        return repoDBBorrow.findAllForUser(userId);
    }

    public List<Book> getAllBooks() {
        return repoDBBook.findAll();
    }

    public List<Book> searchByFilter(String titlu, String autor, String gen) {
        //TODO!
        List<Book> searchResults = new ArrayList<>();
        for (Book book : repoDBBook.findAll()) {
            if((titlu != null && book.getNume() == titlu) &&
                    (autor != null && book.getAutor() == autor) &&
                    (gen != null && book.getGen() == gen)) {
                searchResults.add(book);

            }
        }
        return null;
    }


}
