package ru.skypro.diplom.DTO;


import java.time.LocalDateTime;

public class CommentDTO {

    private int pk;
    private String text;
    private long createdAt;
    private int author;
    private String authorImage;
    private String authorFirstName;

    public CommentDTO() {
    }

    public CommentDTO(int pk, String text, long createdAt, int author, String authorImage, String authorFirstName) {
        this.pk = pk;
        this.text = text;
        this.createdAt = createdAt;
        this.author = author;
        this.authorImage = authorImage;
        this.authorFirstName = authorFirstName;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(String authorImage) {
        this.authorImage = authorImage;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
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
