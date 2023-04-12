package ru.skypro.diplom.DTO;

public class AdPreview {
    private int pk;
    private String title;
    private int author;
    private int price;
    private String image;

    public AdPreview() {
    }

    public AdPreview(int pk, String title, int author, int price, String image) {
        this.pk = pk;
        this.title = title;
        this.author = author;
        this.price = price;
        this.image = image;
    }

    public int getPk() {
        return pk;
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

    public void setPk(int pk) {
        this.pk = pk;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
