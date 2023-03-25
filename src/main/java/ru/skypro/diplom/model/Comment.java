package ru.skypro.diplom.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "Comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate creationDate;
    private String text;
    @OneToOne
    private User user;
    @OneToOne
    private Ad ad;

    public Comment() {
    }

    public Comment(LocalDate creationDate, String text, User user, Ad ad) {
        this.creationDate = creationDate;
        this.text = text;
        this.user = user;
        this.ad = ad;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", createdAt='" + creationDate + '\'' +
                ", text='" + text + '\'' +
                ", user id=" + user.getId() +
                ", ad id=" + ad.getId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id && creationDate.equals(comment.creationDate) && text.equals(comment.text) && user.equals(comment.user) && ad.equals(comment.ad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, text, user, ad);
    }
}
