package TurismLab.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends TurismLab.domain.BaseEntity<Long> {

    @Column(name = "cnp", unique = true)
    private String cnp;

    @Column(name = "nume")
    private String nume;

    @Column(name = "adresa")
    private String adresa;

    @Column(name = "telefon")
    private String telefon;

    @OneToMany(mappedBy = "user")
    private List<Borrow> borrows = new ArrayList<>();

    public User() {
        // Required by Hibernate
    }

    public User(String cnp, String nume, String adresa, String telefon) {
        this.cnp = cnp;
        this.nume = nume;
        this.adresa = adresa;
        this.telefon = telefon;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public List<Borrow> getBorrows() {
        return borrows;
    }

    public void setBorrows(List<Borrow> borrows) {
        this.borrows = borrows;
    }
}