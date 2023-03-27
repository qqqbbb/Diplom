package ru.skypro.diplom.DTO;



public class CommentDTO {

    private int id;
    private String creationDate;
    private String text;
    private int userId;
    private int adId;

    public CommentDTO(int id, String creationDate, String text, int userId, int adId) {
        this.id = id;
        this.creationDate = creationDate;
        this.text = text;
        this.userId = userId;
        this.adId = adId;
    }

    public int getId() {
        return id;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getText() {
        return text;
    }

    public int getUserId() {
        return userId;
    }

    public int getAdId() {
        return adId;
    }
}
