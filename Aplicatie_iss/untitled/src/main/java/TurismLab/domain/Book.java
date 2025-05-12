package TurismLab.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "books")
public class Book extends TurismLab.domain.BaseEntity<Long> {

    @Column(name = "nume")
    private String nume;

    @Column(name = "autor")
    private String autor;

    @Column(name = "gen")
    private String gen;

    @Column(name = "cantitate")
    private int cantitate;


    public Book() {
        // Required by Hibernate
    }

    public Book(String nume, String autor, String gen, int cantitate) {
        this.nume = nume;
        this.autor = autor;
        this.gen = gen;
        this.cantitate = cantitate;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getCantitate() {
        return cantitate;
    }

    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }

    public String getGen() {
        return gen;
    }

    public void setGen(String gen) {
        this.gen = gen;
    }

}