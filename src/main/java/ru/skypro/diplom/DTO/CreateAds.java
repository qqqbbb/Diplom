package ru.skypro.diplom.DTO;

public class CreateAds {

    private String title;
    private String description;
    private int price;

    public CreateAds() {
    }

    public CreateAds(String title, String description, int price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }
}
