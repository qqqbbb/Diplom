package ru.skypro.diplom.DTO;



public class CommentDTO {

    private int pk;
    private String text;
    private String createdAt;
    private int author;
    private String authorImage;
    private String authorFirstName;

    public CommentDTO(int pk, String text, String createdAt, int author, String authorImage, String authorFirstName) {
        this.pk = pk;
        this.text = text;
        this.createdAt = createdAt;
        this.author = author;
        this.authorImage = authorImage;
        this.authorFirstName = authorFirstName;
    }

    public int getId() {
        return pk;
    }

    public String getText() {
        return text;
    }

    public String getCreationDate() {
        return createdAt;
    }

    public int getAuthorId() {
        return author;
    }

    public String getAuthorImage() {
        return authorImage;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }
}
