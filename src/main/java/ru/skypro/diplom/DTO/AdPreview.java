package ru.skypro.diplom.DTO;

public class AdPreview {
    private int id;
    private String title;
    private int author;
    private int price;
    private String image;

    public AdPreview(int id, String title, int author, int price, String image) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getAuthor() {
        return author;
    }

    public int getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
}
