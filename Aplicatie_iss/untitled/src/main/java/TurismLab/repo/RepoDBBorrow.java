package TurismLab.repo;

import TurismLab.domain.Book;
import TurismLab.domain.Borrow;
import TurismLab.domain.User;

import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RepoDBBorrow {
    private JdbcUtils dbUtils;

    public RepoDBBorrow(JdbcUtils dbUtils) {
        this.dbUtils = dbUtils;
    }

    public Borrow save(Borrow borrow) {
        var con = dbUtils.getConnection();
        try (var preStmt = con.prepareStatement("INSERT INTO borrows (user_id, book_id, data_imprumut, data_restituire) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            preStmt.setLong(1, borrow.getUser().getId());
            preStmt.setLong(2, borrow.getBook().getId());
            preStmt.setTimestamp(3, Timestamp.valueOf(borrow.getDataImprumut()));
            preStmt.setTimestamp(4, Timestamp.valueOf(borrow.getDataRestituire()));
            try (var result = preStmt.getGeneratedKeys()) {
                if (result.next()) {
                    var id = result.getLong(1);
                    borrow.setId(id);
                }
                return borrow;
            }
        } catch (Exception e) {
            System.out.println("Error DB " + e);
        }
        return null;
    }

    public Borrow delete(Long id) {
        var con = dbUtils.getConnection();
        try (var preStmt = con.prepareStatement("DELETE FROM borrows WHERE id = ?")) {
            preStmt.setLong(1, id);
            preStmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error DB " + e);
        }
        return null;
    }

    public List<Borrow> findAllForUser(Long id){
        var con = dbUtils.getConnection();
        String queryString = "SELECT b.id,\n" +
                " u.id, u.cnp, u.nume, u.adresa, u.telefon\n" +
                " bk.id, bk.nume, bk.autor, bk.gen, bk.cantitate\n" +
                " from borrows as b\n" +
                "inner join users as u on b.user_id = u.id\n" +
                "inner join books as bk on b.book_id = bk.id\n" +
                "where u.id = ?";
        try (var preStmt = con.prepareStatement("SELECT * FROM borrows WHERE user_id = ?")) {
            preStmt.setLong(1, id);
            var rs = preStmt.executeQuery();
            List<Borrow> borrows = new ArrayList<>();
            while (rs.next()) {
                Long borrowId = rs.getLong(1);
                Long userId = rs.getLong(2);
                String cnp = rs.getString(3);
                String nume = rs.getString(4);
                String adresa = rs.getString(5);
                String telefon = rs.getString(6);
                Long bookId = rs.getLong(7);
                String bookNume = rs.getString(8);
                String bookAutor = rs.getString(9);
                String bookGen = rs.getString(10);
                int bookCantitate = rs.getInt(11);
                User user = new User(cnp, nume, adresa, telefon);
                user.setId(userId);
                Book book = new Book(bookNume, bookAutor, bookGen, bookCantitate);
                book.setId(bookId);
                Borrow borrow = new Borrow(user, book);
                borrow.setId(borrowId);
                borrows.add(borrow);
            }
            return borrows;
        } catch (Exception e) {
            System.out.println("Error DB " + e);
        }
        return null;
    }
}
