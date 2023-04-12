package ru.skypro.diplom.DTO;



public class CommentDTO {

    private int pk;
    private String text;
    private String createdAt;
    private int author;
    private String authorImage;
    private String authorFirstName;

    public CommentDTO() {
    }

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

    public void setPk(int pk) {
        this.pk = pk;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public void setAuthorImage(String authorImage) {
        this.authorImage = authorImage;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "pk=" + pk +
                ", text='" + text + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", author=" + author +
                ", authorImage='" + authorImage + '\'' +
                ", authorFirstName='" + authorFirstName + '\'' +
                '}';
    }
}
