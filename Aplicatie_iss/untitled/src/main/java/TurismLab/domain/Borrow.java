package TurismLab.domain;

import java.time.LocalDateTime;

public class Borrow extends Entity<Long>{
    private Long id;
    private User user;
    private Book book;
    private LocalDateTime dataImprumut;
    private LocalDateTime dataRestituire;

    public Borrow(User user, Book book) {
        this.user = user;
        this.book = book;
        this.dataImprumut = LocalDateTime.now();
        this.dataRestituire = dataImprumut.plusDays(30);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDateTime getDataImprumut() {
        return dataImprumut;
    }

    public void setDataImprumut(LocalDateTime dataImprumut) {
        this.dataImprumut = dataImprumut;
    }

    public LocalDateTime getDataRestituire() {
        return dataRestituire;
    }

    public void setDataRestituire(LocalDateTime dataRestituire) {
        this.dataRestituire = dataRestituire;
    }
}
