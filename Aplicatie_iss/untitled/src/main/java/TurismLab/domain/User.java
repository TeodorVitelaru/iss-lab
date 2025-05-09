package TurismLab.domain;

import java.util.ArrayList;
import java.util.List;

public class User extends Entity<Long>{
    private Long id;
    private String cnp;
    private String nume;
    private String adresa;
    private String telefon;

    public User(String cnp, String nume, String adresa, String telefon) {
        this.cnp = cnp;
        this.nume = nume;
        this.adresa = adresa;
        this.telefon = telefon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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


}
