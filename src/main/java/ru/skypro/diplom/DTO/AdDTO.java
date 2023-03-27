package ru.skypro.diplom.DTO;

public class AdDTO {
    private int id;
    private String title;
    private int authorId;
    private int price;
    private String image;

    public AdDTO(int id, String title, int authorId, int price) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getAuthorId() {
        return authorId;
    }

    public int getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
}
