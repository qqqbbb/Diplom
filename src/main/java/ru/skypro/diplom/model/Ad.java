package ru.skypro.diplom.model;

import ru.skypro.diplom.Exceptions.*;

import javax.persistence.*;
import java.util.Objects;

@Entity()
@Table(name = "ads")
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private int price;
    @ManyToOne
    private User user;
    private  byte[] image;

    public Ad() {
    }

    public Ad(String title, String description, int price, User user) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Ad{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", user id=" + user.getId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ad ad = (Ad) o;
        return getId() == ad.getId() && getUser().equals(ad.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser());
    }
}
