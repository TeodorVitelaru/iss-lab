package TurismLab.repo;

import TurismLab.domain.User;

public class RepoDBUser {
    private JdbcUtils dbUtils;

    public RepoDBUser(JdbcUtils dbUtils) {
        this.dbUtils = dbUtils;
    }

    public User findById(Long id) {
        var con = dbUtils.getConnection();
        try (var preStmt = con.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            preStmt.setLong(1, id);
            var rs = preStmt.executeQuery();
            if (rs.next()) {
                var cnp = rs.getString("cnp");
                var nume = rs.getString("nume");
                var adresa = rs.getString("adresa");
                var telefon = rs.getString("telefon");
                User user = new User(cnp, nume, adresa, telefon);
                user.setId(id);
                return user;
            }
        } catch (Exception e) {
            System.out.println("Error DB " + e);
        }
        return null;
    }

    public User findByUsernameAndId(String nume, Long id){
        var con = dbUtils.getConnection();
        try (var preStmt = con.prepareStatement("SELECT * FROM users WHERE nume = ? AND id = ?")) {
            preStmt.setString(1, nume);
            preStmt.setLong(2, id);
            var rs = preStmt.executeQuery();
            if (rs.next()) {
                var cnp = rs.getString("cnp");
                var nume2 = rs.getString("nume");
                var adresa = rs.getString("adresa");
                var telefon = rs.getString("telefon");
                User user = new User(cnp, nume2, adresa, telefon);
                user.setId(id);
                return user;
            }
        } catch (Exception e) {
            System.out.println("Error DB " + e);
        }
        return null;
    }
}
