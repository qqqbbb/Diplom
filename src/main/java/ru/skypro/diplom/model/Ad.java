package ru.skypro.diplom.model;

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
    @OneToOne
    private User user;
    private String phone;
    private String email;
    @OneToOne
    private Image image;

    public Ad() {
    }

    public Ad(String title, String description, int price, User user, String phone, String email) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.user = user;
        this.phone = phone;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }

    public User getUser() {
        return user;
    }

    public Image getImage() {
        return image;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImage(Image image) {
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