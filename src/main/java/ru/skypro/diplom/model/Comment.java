package ru.skypro.diplom.model;

import ru.skypro.diplom.Exceptions.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "Comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime creationDate;
    private String text;
    @ManyToOne
    private User user;
    @ManyToOne
    private Ad ad;

    public Comment() {
    }

    public Comment(int id, LocalDateTime creationDate, String text, User user, Ad ad) {
        this.id = id;
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

    public int getId() {
        return id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getText() {
        return text;
    }

    public User getUser() {
        if (user == null)
            throw new UserNotFoundException();

        return user;
    }

    public Ad getAd() {
        if (ad == null)
            throw new AdNotFoundException();

        return ad;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }
}
