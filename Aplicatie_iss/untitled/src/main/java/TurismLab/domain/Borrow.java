package TurismLab.domain;


import TurismLab.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import javax.persistence.Entity;

@Entity
@Table(name = "borrows")
public class Borrow extends BaseEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "data_imprumut")
    private LocalDateTime dataImprumut;

    @Column(name = "data_restituire")
    private LocalDateTime dataRestituire;

    public Borrow() {
        // Required by Hibernate
    }

    public Borrow(User user, Book book) {
        this.user = user;
        this.book = book;
        this.dataImprumut = LocalDateTime.now();
        this.dataRestituire = dataImprumut.plusDays(30);
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