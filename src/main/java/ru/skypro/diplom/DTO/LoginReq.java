package ru.skypro.diplom.DTO;


import java.util.Objects;

public class LoginReq {
    private String password;
    private String username;

    public LoginReq() {
    }

    public LoginReq(String password, String username) {
        this.password = password;
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "LoginReq{" +
                "password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

}
