package ru.skypro.diplom.DTO;

import ru.skypro.diplom.model.Image;

public class FullAd {
    private long id;
    private String title;
    private String authorFirstName;
    private String authorLastName;
    private String description;
    private String email;
    private String phone;
    private int price;
    private String image;

    public FullAd(long id, String title, String authorFirstName, String authorLastName, String description, String email, String phone, int price, String image) {
        this.id = id;
        this.title = title;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.description = description;
        this.email = email;
        this.phone = phone;
        this.price = price;
        this.image = image;
    }


    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public int getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
}
