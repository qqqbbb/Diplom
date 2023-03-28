package ru.skypro.diplom.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity()
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate regDate;
    private String city;
    @OneToOne
    private Avatar avatar;

    protected User() {
    }

    public User(int id, String firstName, String lastName, String email, String phone, LocalDate regDate, String city) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.regDate = regDate;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDate getRegDate() {
        return regDate;
    }

    public String getCity() {
        return city;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }
}
