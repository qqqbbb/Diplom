package ru.skypro.diplom.DTO;


import java.util.Objects;

public class LoginReq {
    private String password;
    private String username;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "LoginReq{" +
                "password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

}
