package TurismLab.domain;

public class Book extends Entity<Long>{
    private Long id;
    private String nume;
    private String autor;
    private String gen;
    private int cantitate;

    public Book(String nume, String autor, String gen, int cantitate) {
        this.nume = nume;
        this.autor = autor;
        this.gen = gen;
        this.cantitate = cantitate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
