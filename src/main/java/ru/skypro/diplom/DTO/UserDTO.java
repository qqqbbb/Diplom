package ru.skypro.diplom.DTO;

import ru.skypro.diplom.model.Avatar;


import java.time.LocalDate;

public class UserDTO {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String regDate;
    private String city;
    private String avatar;

    public UserDTO(int id, String firstName, String lastName, String email, String phone, String regDate, String city) {
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

    public String getRegDate() {
        return regDate;
    }

    public String getCity() {
        return city;
    }

    public String getAvatar() {
        return avatar;
    }
}
