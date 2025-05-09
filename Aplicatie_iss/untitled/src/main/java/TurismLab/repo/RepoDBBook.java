package TurismLab.repo;

import TurismLab.domain.Book;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RepoDBBook {
    private JdbcUtils dbUtils;

    public RepoDBBook(JdbcUtils dbUtils) {
        this.dbUtils = dbUtils;
    }

    public List<Book> findAll() {
        Connection con = dbUtils.getConnection();
        try (var preStmt = con.prepareStatement("SELECT * FROM books")) {
            var rs = preStmt.executeQuery();
            List<Book> books = new ArrayList<>();
            try(var result = preStmt.executeQuery()){
                while(result.next()){
                    var id = result.getLong("id");
                    var title = result.getString("nume");
                    var author = result.getString("autor");
                    var gen = result.getString("gen");
                    var cantitate = result.getInt("cantitate");
                    Book book = new Book(title, author, gen, cantitate);
                    book.setId(id);
                    books.add(book);
                }
            }
            return books;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Book save(Book book) {
        Connection con = dbUtils.getConnection();
        try (var preStmt = con.prepareStatement("INSERT INTO books (nume, autor, cantitate) VALUES (?, ?, ?) RETURNING id", Statement.RETURN_GENERATED_KEYS)) {
            preStmt.setString(1, book.getNume());
            preStmt.setString(2, book.getAutor());
            preStmt.setInt(3, book.getCantitate());
            preStmt.executeUpdate();
            try (var result = preStmt.getGeneratedKeys()) {
                if (result.next()) {
                    var id = result.getLong(1);
                    book.setId(id);
                }
            }
            return book;
        } catch (Exception e) {
            System.out.println("Error DB " + e);
            return null;
        }
    }

    public void update(Book book){
        var con = dbUtils.getConnection();
        try (var preStmt = con.prepareStatement("UPDATE books SET nume = ?, autor = ?, cantitate = ? WHERE id = ?")) {
            preStmt.setInt(1, book.getCantitate());
            preStmt.setLong(2, book.getId());
            preStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }

    }

}
